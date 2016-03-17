/*
 * Copyright 2014-2016 Ainrif <ainrif@outlook.com>
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

import java.beans.Introspector
import java.lang.annotation.Annotation
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.function.Predicate

import static com.ainrif.apiator.core.model.ModelType.ENUMERATION
import static com.ainrif.apiator.core.model.ModelType.OBJECT

/**
 * List of method overrides from parent (interface/superclass) to child (implementation)
 */
abstract class MethodStack extends ArrayList<Method> {

    protected MethodStack(Collection<? extends Method> collection) {
        super(collection)
    }

    abstract String getName()

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
     * @return [ < param index > : [inherited annotations] ]
     */
    public Map<Integer, List<? extends Annotation>> getParametersAnnotationsLists() {
        Map<Integer, List<? extends Annotation>> result = new HashMap<>().withDefault { [] }
        this.each {
            it.parameterAnnotations.eachWithIndex { Annotation[] entry, int i ->
                result[i] += entry as List
            }
        }

        result
    }

    //todo cache
    private Set<ApiType> collectAllUsedTypes() {
        Set<ApiType> types = [] // result

        // all types used in params and return type form input data to start BFS collecting
        def nextLookup = (params.collect { it.type } << returnType.type)
        while (nextLookup) {
            def typesToLookup = nextLookup
                    .collect(collectApiTypesFromGenerics).flatten()
                    .collect(mapArraysToItsTypeApiType).toSet()
                    .findAll(testTypeIsNotPrimitive)

            def typesFromFields = typesToLookup
                    .findAll(testTypeIsCustomModelType)
                    .collect(collectApiTypesFromFields).flatten().toSet()
                    .collect(collectApiTypesFromGetters).flatten().toSet()
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
        type.generic ? type.flattenArgumentTypes() << type : [type]
    }

    protected static Closure<List<ApiType>> collectApiTypesFromFields = { ApiType type ->
        RUtils.getAllFields(findFirstNotArrayType(type), testFieldIsPublicAndNotStatic)
                .collect { new ApiType(it.genericType) } << type
    }

    protected static Closure<List<ApiType>> collectApiTypesFromGetters = { ApiType type ->
        def rawType = findFirstNotArrayType(type)

        if (!rawType.interface && !rawType.primitive && testTypeIsCustomModelType.call(new ApiType(rawType))) {
            def beanInfo = rawType.enum ?
                    Introspector.getBeanInfo(rawType, Enum) :
                    Introspector.getBeanInfo(rawType, Object)

            def typesFromGetters = beanInfo.propertyDescriptors
                    .findAll { it.readMethod }
                    .collect { it.readMethod.genericReturnType }
                    .collect { new ApiType(it) }

            typesFromGetters << type

            return typesFromGetters
        } else {
            return [type]
        }
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
        ModelType.notPrimitiveTypes.any { it == type.modelType } && Object != type.rawType && Enum != type.rawType
    }

    protected static Closure<Boolean> testTypeIsCustomModelType = { ApiType type ->
        ModelType.customModelTypes.any { it == type.modelType } && Object != type.rawType && Enum != type.rawType
    }

    protected static Predicate<Field> testFieldIsPublicAndNotStatic = {
        Modifier.isPublic(it.modifiers) && !Modifier.isStatic(it.modifiers)
    }
}
