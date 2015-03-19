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
package com.ainrif.apiator.core.reflection

import com.ainrif.apiator.core.model.ModelType
import com.ainrif.apiator.core.model.api.ApiEndpointMethod
import com.ainrif.apiator.core.model.api.ApiEndpointParam
import com.ainrif.apiator.core.model.api.ApiEndpointReturnType
import com.ainrif.apiator.core.model.api.ApiType

import javax.annotation.Nullable
import java.lang.annotation.Annotation
import java.lang.reflect.Method

/**
 * List of method overrides from parent (interface/superclass) to child (implementation)
 */
abstract class MethodStack extends ArrayList<Method> {

    protected MethodStack(Collection<? extends Method> collection) {
        super(collection)
    }

    abstract String getTitle()

    abstract String getPath()

    abstract ApiEndpointMethod getMethod()

    abstract ApiEndpointReturnType getReturnType()

    abstract List<ApiEndpointParam> getParams()

    /**
     * collects java types from params and return values
     *
     * @return
     */
    public Set<ApiType> getUsedApiTypes() {
        (params.collect { it.type } << returnType.type)
                .collect { it.generic ? it.flattenArgumentTypes() : it }
                .flatten()
                .collect { ApiType type -> type.array ? new ApiType(type.arrayType) : type }
                .findAll { ApiType type -> ModelType.OBJECT == type.modelType }
                .findAll { ApiType type -> Object.class != type.rawType }
                .toSet()
    }

    /**
     * collects annotations from method hierarchy tree
     *
     * @param annotationClass
     * @return
     */
    public <T extends Annotation> List<T> getAnnotationList(Class<T> annotationClass) {
        this.findAll { it.isAnnotationPresent(annotationClass) }
                .collect { it.getAnnotation(annotationClass) }
    }

    /**
     * collects annotations from method parameters from hierarchy tree
     *
     * @return [ < param signature > : [inherited annotations] ]
     */
    public Map<ParamSignature, List<? extends Annotation>> getParametersAnnotationsLists() {
        Map<ParamSignature, List<? extends Annotation>> result = new HashMap<>().withDefault { [] }
        def paramTypes = this.last().parameterTypes
        this.each {
            it.parameterAnnotations.eachWithIndex { Annotation[] entry, int i ->
                result[new ParamSignature(i, paramTypes[i])] += entry as List
            }
        }

        result
    }

    static <T extends Annotation> Map<ParamSignature, List<? extends Annotation>> filterParametersAnnotationsLists(
            Map<ParamSignature, List<? extends Annotation>> annotations, @Nullable Class<T> type) {
        def result;
        if (type) {
            result = annotations
            // all annotated params
                    .findAll { it.value.any { type.isAssignableFrom(it.class) } }
            // only annotated stack items
                    .collectEntries { k, v -> [k, v.findAll { type.isAssignableFrom(it.class) }] }
        } else {
            // find all not annotated params
            result = annotations.findAll { it.value.empty }
        }

        result
    }

}
