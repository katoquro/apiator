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
package com.ainrif.apiator.provider.jaxrs


import com.ainrif.apiator.core.model.api.ApiEndpointParam
import com.ainrif.apiator.core.model.api.ApiEndpointParamType
import com.ainrif.apiator.core.model.api.ApiType
import com.ainrif.apiator.core.reflection.MethodStack
import com.ainrif.apiator.core.reflection.RUtils
import org.springframework.core.annotation.AnnotationUtils

import javax.ws.rs.*
import javax.ws.rs.core.Context
import java.lang.annotation.Annotation
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.util.function.Predicate

import static java.util.Collections.emptyList
import static java.util.Collections.singletonList

class JaxRsMethodStack extends MethodStack {

    private final static def SIMPLE_PARAM_ANNOTATIONS = [PathParam, FormParam, QueryParam, HeaderParam, CookieParam]
    private final static Predicate<Field> testFieldAnnotations = { field ->
        SIMPLE_PARAM_ANNOTATIONS.any { field.isAnnotationPresent(it) }
    }

    private JaxRsContextStack context

    JaxRsMethodStack(Collection<? extends Method> collection, JaxRsContextStack context) {
        super(collection)
        this.context = context
    }

    @Override
    String getName() {
        return this.last().name
    }

    @Override
    String getPath() {
        return AnnotationUtils.findAnnotation(this.last(), Path)?.value() ?: '/'
    }

    @Override
    String getMethod() {
        return AnnotationUtils.findAnnotation(this.last(), HttpMethod)?.value() ?: 'GET'
    }

    //todo tests for annotated body params
    @Override
    List<ApiEndpointParam> getParams() {
        def methodParams = this.last().parameters

        return getParametersAnnotationsLists().collectMany { index, annList ->
            def reversedAnnList = annList.reverse()

            def found = reversedAnnList.find { annotation -> Context.isAssignableFrom(annotation.annotationType()) }
            if (found) {
                return emptyList()
            }

            // explicitly annotated params
            found = reversedAnnList.find { annotation ->
                SIMPLE_PARAM_ANNOTATIONS.any { it.isAssignableFrom(annotation.annotationType()) }
            }
            if (found) {
                def result = new ApiEndpointParam(
                        index: index,
                        name: found.value(),
                        type: new ApiType(methodParams[index].parameterizedType),
                        httpParamType: httpParamTypeFor(found.annotationType()),
                        defaultValue: reversedAnnList.find {
                            DefaultValue.isAssignableFrom(it.annotationType())
                        }?.value()
                )

                return singletonList(result)
            }

            // complex BeanParams
            found = reversedAnnList.find { annotation -> BeanParam.isAssignableFrom(annotation.annotationType()) }
            if (found) {
                return RUtils.getAllFields(methodParams[index].type, testFieldAnnotations).collect {
                    def annotation = it.annotations.find { SIMPLE_PARAM_ANNOTATIONS.contains(it.annotationType()) }
                    new ApiEndpointParam(
                            index: -1,
                            name: annotation.value(),
                            type: new ApiType(it.genericType),
                            httpParamType: httpParamTypeFor(annotation.annotationType()),
                            defaultValue: AnnotationUtils.getAnnotation(it, DefaultValue)?.value()
                    )
                }
            }

            // implicit BODY param (not annotated with jax-rs param annotations)
            def result = new ApiEndpointParam(
                    index: index,
                    name: null,
                    type: new ApiType(methodParams[index].parameterizedType),
                    httpParamType: ApiEndpointParamType.BODY,
                    defaultValue: null
            )

            return singletonList(result)
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
            default: throw new RuntimeException('UNSUPPORTED HTTP ENDPOINT PARAM')
        }

        result
    }
}
