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

import java.lang.annotation.Annotation
import java.lang.reflect.Method

import static com.ainrif.apiator.core.model.ModelType.ENUMERATION
import static com.ainrif.apiator.core.model.ModelType.OBJECT

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
     */
    public Set<ApiType> getUsedApiTypes() {
        collectAllUsedTypes()
                .findAll { OBJECT == it.modelType }
    }

    /**
     * collects java Enums from params and return values
     */
    public Set<ApiType> getUsedEnumerations() {
        collectAllUsedTypes()
                .findAll { ENUMERATION == it.modelType }
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

    protected
    static <T extends Annotation> Map<ParamSignature, List<? extends Annotation>> filterParametersAnnotationsLists(
            Map<ParamSignature, List<? extends Annotation>> annotations, Class<T> type) {
        def result = annotations
        // select all stacks which contain annotated param
                .findAll { it.value.any { type.isAssignableFrom(it.class) } }
        // select only annotated items from stack
                .collectEntries { k, v -> [k, v.findAll { type.isAssignableFrom(it.class) }] }

        result
    }

    protected
    static <T extends Annotation> Map<ParamSignature, List<? extends Annotation>> filterOutParametersAnnotationsLists(
            Map<ParamSignature, List<? extends Annotation>> annotations, List<Class<T>> types) {
        annotations
        // select all stacks which don't contain annotated param
                .findAll { !it.value.any { at -> types.any { it.isAssignableFrom(at.class) } } }
    }

    private Set<ApiType> collectAllUsedTypes() {
        Set<ApiType> types = []

        def nextLookup = (params.collect { it.type } << returnType.type)
        while (nextLookup) {
            def typesToLookup = nextLookup
                    .collect(collectApiTypesFromGenerics).flatten()
                    .collect(mapArraysToItsTypeApiType)
                    .findAll(testTypeIsNotPrimitive)

            def typesFromFields = typesToLookup
                    .findAll(testTypeIsModelObject)
                    .collect(collectApiTypesFromFields).flatten()
                    .collect(mapArraysToItsTypeApiType)
                    .findAll(testTypeIsNotPrimitive)
                    .minus(typesToLookup)

            types += typesToLookup
            types += typesFromFields

            nextLookup = typesFromFields.findAll { OBJECT == it.modelType || it.generic }
        }

        types
    }

    protected static Closure<List<ApiType>> collectApiTypesFromGenerics = { ApiType type ->
        type.generic ? type.flattenArgumentTypes() : [type]
    }

    protected static Closure<List<ApiType>> collectApiTypesFromFields = { ApiType type ->
        RUtils.getAllFields(findFirstNotArrayType(type))
                .collect { new ApiType(it.genericType) }
    }

    protected static Class<?> findFirstNotArrayType(ApiType type) {
        def targetType = type.array ? type.componentApiType.rawType : type.rawType
        while (targetType.array) {
            targetType = targetType.componentType
        }

        targetType
    }

    protected static Closure<ApiType> mapArraysToItsTypeApiType = { ApiType type ->
        type.array ? new ApiType(findFirstNotArrayType(type)) : type
    }

    protected static Closure<Boolean> testTypeIsNotPrimitive = { ApiType type ->
        ModelType.notPrimitiveTypes.any { it == type.modelType } && Object.class != type.rawType
    }

    protected static Closure<Boolean> testTypeIsModelObject = { ApiType type ->
        ModelType.modelObjectTypes.any { it == type.modelType } && Object.class != type.rawType
    }
}
