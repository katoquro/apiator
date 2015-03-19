/*
 * Copyright 2014-2015 Ainrif <ainrif@outlook.com>
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
package com.ainrif.apiator.provider.jaxrs

import com.ainrif.apiator.core.model.api.*
import com.ainrif.apiator.core.reflection.MethodStack
import com.ainrif.apiator.core.reflection.ParamSignature
import com.ainrif.apiator.core.reflection.RUtils

import javax.ws.rs.*
import java.lang.annotation.Annotation
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.function.Predicate

class JaxRsMethodStack extends MethodStack {

    private final static def paramAnnotations = [FormParam, QueryParam, HeaderParam, CookieParam, FormParam];
    private final static Predicate<Field> testFieldAnnotations = { field ->
        paramAnnotations.any { field.isAnnotationPresent(it) }
    }

    private JaxRsContextStack context;

    JaxRsMethodStack(Collection<? extends Method> collection, JaxRsContextStack context) {
        super(collection)
        this.context = context
    }

    @Override
    String getTitle() {
        this.last().name
    }

    @Override
    String getPath() {
        def path = (getAnnotationList(Path) ?: null)?.last()?.value()
        if (!path) {
            path = (context.getAnnotationList(Path) ?: null)?.last()?.value()
        }

        path ?: { throw new RuntimeException("PATH") }.call();
    }

    @Override
    ApiEndpointMethod getMethod() {
        def method = 'GET'

        this.each { Method javaMethod ->
            def annotation = [POST, GET, PUT, DELETE, OPTIONS, HEAD].find { javaMethod.isAnnotationPresent(it) }

            if (annotation) {
                method = annotation.getAnnotation(HttpMethod).value()
            } else if (javaMethod.isAnnotationPresent(HttpMethod)) {
                method = javaMethod.getAnnotation(HttpMethod).value()
            } else {
                def annotationOpt = javaMethod.getAnnotations().find {
                    it.annotationType().isAnnotationPresent(HttpMethod)
                }

                if (annotationOpt) {
                    method = annotationOpt.annotationType().getAnnotation(HttpMethod).value()
                }
            }
        }

        ApiEndpointMethod.valueOf(method);
    }

    @Override
    ApiEndpointReturnType getReturnType() {
        new ApiEndpointReturnType(
                type: new ApiType(this.last().genericReturnType),
        )
    }

    @Override
    List<ApiEndpointParam> getParams() {
        def annotations = getParametersAnnotationsLists()
        def result = []

        result += paramAnnotations.collect { processParamAnnotation(annotations, it) }.flatten()
        result += processParamAnnotation(annotations, null)

        filterParametersAnnotationsLists(annotations, BeanParam).collect {
            def parameter = this.last().parameters[it.key.index]

            result += RUtils.getAllFields(parameter.type, testFieldAnnotations).collect {
                def annotation = it.annotations.find { paramAnnotations.contains(it.annotationType()) }
                new ApiEndpointParam(
                        index: -1,
                        name: annotation.value(),
                        type: new ApiType(it.genericType),
                        httpParamType: httpParamTypeFor(annotation.annotationType())
                )
            }
        }

        result
    }

    private List<ApiEndpointParam> processParamAnnotation(Map<ParamSignature, List<? extends Annotation>> annotations,
                                                          Class<? extends Annotation> filterAnnotation) {
        filterParametersAnnotationsLists(annotations, filterAnnotation).collect {
            def parameter = this.last().parameters[it.key.index]
            new ApiEndpointParam(
                    index: it.key.index,
                    name: it.value ? it.value.last().value() : null,
                    type: new ApiType(parameter.parameterizedType),
                    httpParamType: httpParamTypeFor(filterAnnotation)
            )
        }
    }

    private static ApiEndpointParamType httpParamTypeFor(Class<? extends Annotation> annotation) {
        def result
        switch (annotation) {
            case PathParam:
                result = ApiEndpointParamType.PATH
                break
            case QueryParam:
                result = ApiEndpointParamType.QUERY
                break
            case HeaderParam:
                result = ApiEndpointParamType.HEADER
                break
            case CookieParam:
                result = ApiEndpointParamType.COOKIE
                break
            case FormParam:
                result = ApiEndpointParamType.FORM
                break
            case null:
                result = ApiEndpointParamType.BODY
                break
            default: throw new RuntimeException('UNSUPPORTED HTTP ENDPOINT PARAM')
        }

        result
    }
}
