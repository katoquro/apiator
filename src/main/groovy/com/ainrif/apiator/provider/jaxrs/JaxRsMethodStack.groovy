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

import javax.ws.rs.*
import java.lang.reflect.Method

class JaxRsMethodStack extends MethodStack {

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
        def result = []
        def annotations = getParametersAnnotationsLists()

        result += filterParametersAnnotationsLists(annotations, PathParam).collect {
            def parameter = this.last().parameters[it.key.index]
            new ApiEndpointParam(
                    index: it.key.index,
                    name: parameter.name,
                    type: new ApiType(parameter.parameterizedType),
                    httpParamType: ApiEndpointParamType.PATH
            )
        }

        result += filterParametersAnnotationsLists(annotations, QueryParam).collect {
            def parameter = this.last().parameters[it.key.index]
            new ApiEndpointParam(
                    index: it.key.index,
                    name: parameter.name,
                    type: new ApiType(parameter.parameterizedType),
                    httpParamType: ApiEndpointParamType.QUERY
            )
        }

        result += filterParametersAnnotationsLists(annotations, null).collect {
            def parameter = this.last().parameters[it.key.index]
            new ApiEndpointParam(
                    index: it.key.index,
                    name: parameter.name,
                    type: new ApiType(parameter.parameterizedType),
                    httpParamType: ApiEndpointParamType.BODY
            )
        }

        result
    }
}
