/*
 * Copyright 2014-2018 Ainrif <support@ainrif.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ainrif.apiator.provider.micronaut

import com.ainrif.apiator.core.model.api.ApiEndpointParam
import com.ainrif.apiator.core.model.api.ApiEndpointParamType
import com.ainrif.apiator.core.model.api.ApiType
import com.ainrif.apiator.core.reflection.MethodStack
import com.ainrif.apiator.core.reflection.RUtils
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpParameters
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.*
import io.micronaut.http.cookie.Cookies
import io.micronaut.http.uri.UriMatchTemplate
import io.micronaut.http.uri.UriMatchVariable
import net.bytebuddy.ByteBuddy
import net.bytebuddy.description.modifier.Visibility
import org.springframework.core.annotation.AnnotationUtils

import java.lang.annotation.Annotation
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.Type

class MicronautMethodStack extends MethodStack {
    private static COMPILE_WITH_PARAMS_MSG = 'Please check method signature and annotations or try to compile with ' +
            '`-parameters` flag for java code or ' +
            '`--parameters` flag for groovy code (since Gradle 6.1.0) '

    List<Class<? extends Annotation>> ENDPOINT_PATH_ANNOTATIONS = [Post,
                                                                   Get,
                                                                   Put,
                                                                   Delete,
                                                                   Options,
                                                                   Head,
                                                                   Patch,
                                                                   Trace]

    private MicronautContextStack context

    protected MicronautMethodStack(Collection<? extends Method> collection, MicronautContextStack context) {
        super(collection)
        this.context = context
    }

    @Override
    String getName() {
        return this.last().name
    }

    @Override
    String getPath() {
        Annotation pathOpt = ENDPOINT_PATH_ANNOTATIONS.findResult {
            AnnotationUtils.findAnnotation(this.last(), it)
        }

        def value = pathOpt.value() as String
        def uri = pathOpt.uri() as String

        return (value.length() <=> uri.length()) >= 0 ? value : uri
    }

    @Override
    String getMethod() {
        Annotation methodOpt = ENDPOINT_PATH_ANNOTATIONS.findResult {
            AnnotationUtils.findAnnotation(this.last(), it)
        }

        return methodOpt.annotationType().simpleName.toUpperCase()
    }

    /**
     * https://docs.micronaut.io/latest/guide/index.html#_variables_resolution
     */
    @Override
    List<ApiEndpointParam> getParams() {
        String rawPath = this.path
        def querySignIndex = rawPath.indexOf('?')

        def pathPart = rawPath
        def queryPart = ''
        if (-1 != querySignIndex) {
            if (0 != querySignIndex && '{' == rawPath[querySignIndex - 1]) {
                querySignIndex--
            }

            pathPart = rawPath.substring(0, querySignIndex)
            queryPart = rawPath.substring(querySignIndex)
        }

        def pathTmpl = UriMatchTemplate.of(pathPart)
        def queryTmpl = UriMatchTemplate.of(queryPart)

        def paramAnnotations = getParametersAnnotationsLists()
        def methodParams = this.last().parameters
        List<ApiEndpointParam> result = []

        pathTmpl.variables.each { var ->
            int paramIndex = findIndexOfParam(paramAnnotations, var)

            result << new ApiEndpointParam(
                    index: paramIndex,
                    name: var.name,
                    type: new ApiType(methodParams[paramIndex].parameterizedType),
                    httpParamType: ApiEndpointParamType.PATH,
                    annotations: paramAnnotations[paramIndex]
            )

            paramAnnotations.remove(paramIndex)
        }

        queryTmpl.variables.each { var ->
            int paramIndex = findIndexOfParam(paramAnnotations, var)

            if (var.exploded) {
                def explodedFields = RUtils.getAllDeclaredDynamicFields(methodParams[paramIndex].type).collect {
                    def nameAnnotation = paramAnnotations[paramIndex].find { annotation ->
                        QueryValue.isAssignableFrom(annotation.annotationType())
                    }
                    new ApiEndpointParam(
                            index: -1,
                            name: nameAnnotation?.value() ?: it.name,
                            type: new ApiType(it.genericType),
                            httpParamType: ApiEndpointParamType.QUERY,
                            annotations: (it.annotations as List<? extends Annotation>) + paramAnnotations[paramIndex]
                    )
                }

                result.addAll(explodedFields)
            } else {
                def nameAnnotation = paramAnnotations[paramIndex].find { annotation ->
                    QueryValue.isAssignableFrom(annotation.annotationType())
                }
                result << new ApiEndpointParam(
                        index: paramIndex,
                        name: nameAnnotation?.value() ?: var.name,
                        type: new ApiType(methodParams[paramIndex].parameterizedType),
                        httpParamType: ApiEndpointParamType.QUERY,
                        annotations: paramAnnotations[paramIndex]
                )
            }

            paramAnnotations.remove(paramIndex)
        }

        Map<Integer, List<String>> parametersNameLists = getParametersNameLists()

        Map<String, Type> expandedBodyParams = [:]

        List<ApiEndpointParam> remainingParamTypes = paramAnnotations
                .findAll { index, annList ->
                    def paramType = methodParams[index].type
                    def isMicronautSystemType = [HttpRequest, HttpHeaders, HttpParameters, Cookies].any {
                        it.isAssignableFrom(paramType)
                    }

                    return !isMicronautSystemType
                }
                .collect { index, annList ->
                    def param = methodParams[index]

                    def found = annList.find { [QueryValue, CookieValue, Header, Part].contains(it.annotationType()) }
                    if (found) {
                        return new ApiEndpointParam(
                                index: index,
                                name: found.value() ?:
                                        !parametersNameLists[index].empty ?
                                                parametersNameLists[index].find() :
                                                null,
                                type: new ApiType(param.parameterizedType),
                                httpParamType: httpParamTypeFor(found.annotationType()),
                                annotations: annList
                        )
                    }

                    def bodyAnnotations = annList.findAll { Body.isAssignableFrom(it.annotationType()) }
                    def foundBody = bodyAnnotations.find { it.asType(Body).value().empty }
                    if (foundBody) {
                        return new ApiEndpointParam(
                                index: index,
                                name: null,
                                type: new ApiType(param.parameterizedType),
                                httpParamType: ApiEndpointParamType.BODY,
                                annotations: annList
                        )
                    }

                    // https://docs.micronaut.io/latest/guide/index.html#_variables_resolution
                    def bodyParamAnnotation = bodyAnnotations.find { !it.asType(Body).value().empty }
                    String bodyParamName = bodyParamAnnotation?.value() ?:
                            parametersNameLists[index].empty ?
                                    null :
                                    parametersNameLists[index].find()

                    if (bodyParamName) {
                        expandedBodyParams[bodyParamName] = param.parameterizedType
                        return null
                    }

                    throw new RuntimeException('No corresponding params type were found for method params with ' +
                            "index(${index}) annotated with path(${this.path}) in class(${context.name}). " +
                            COMPILE_WITH_PARAMS_MSG
                    )
                }
                .findAll()

        result.addAll(remainingParamTypes)

        if (expandedBodyParams) {
            def typeName = 'adhoc_type.' + context.apiType.rawType.simpleName + getName().capitalize()

            def genClass = new ByteBuddy()
                    .makeInterface()
                    .merge(Visibility.PUBLIC)
                    .name(typeName)

            for (def pair : expandedBodyParams) {
                String name = pair.key
                Type type = pair.value
                genClass = genClass.defineMethod('set' + name.capitalize(), Void.TYPE, Modifier.PUBLIC)
                        .withParameters(type)
                        .withoutCode()
            }

            def generated = genClass.make()
                    .load(getClass().getClassLoader())
                    .getLoaded()

            result.add(new ApiEndpointParam(
                    index: -1,
                    name: null,
                    type: new ApiType(generated),
                    httpParamType: ApiEndpointParamType.BODY))
        }

        return result
    }

    private int findIndexOfParam(Map<Integer, List<? extends Annotation>> annotations,
                                 UriMatchVariable var) {
        for (m in this.reverse(false)) {
            def methodParams = m.parameters
            def result = methodParams.findIndexOf { it.namePresent && it.name == var.name }

            if (-1 != result) {
                return result
            }
        }

        Integer foundIdxFromAnnotations = annotations.findResult { idx, annList ->
            def found = annList.find { ann -> [QueryValue, PathVariable].contains(ann.annotationType()) }
            if (found && found.asType(QueryValue).value() == var.name) {
                return idx
            }

            return null
        }

        if (null == foundIdxFromAnnotations) {
            throw new RuntimeException('No corresponding query or path params were found for ' +
                    "variable(${var.name})' in path(${this.path}) of method(${this.last().name}) " +
                    COMPILE_WITH_PARAMS_MSG)
        }

        return foundIdxFromAnnotations
    }

    private static ApiEndpointParamType httpParamTypeFor(Class<? extends Annotation> annotation) {
        def result
        switch (annotation) {
            case QueryValue:
                result = ApiEndpointParamType.QUERY
                break
            case Header:
                result = ApiEndpointParamType.HEADER
                break
            case CookieValue:
                result = ApiEndpointParamType.COOKIE
                break
            case Part:
                result = ApiEndpointParamType.FORM
                break
            default: throw new RuntimeException('UNSUPPORTED HTTP ENDPOINT PARAM')
        }

        return result
    }
}
