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
package com.ainrif.apiator.core.model.api

import groovy.transform.Memoized

import java.lang.reflect.*

class ApiType {

    protected Type type

    ApiType(Type type) {
        this.type = type
    }

    boolean isGeneric() {
        return type instanceof ParameterizedType ||
                (type instanceof Class && type.typeParameters)
    }

    boolean isActuallyParametrised() {
        return type instanceof ParameterizedType
    }

    boolean isArray() {
        switch (type) {
            case { it instanceof Class }:
                return type.asType(Class).isArray()
            case { it instanceof GenericArrayType }:
                return true
            default:
                return false
        }
    }

    boolean isTemplate() {
        type instanceof TypeVariable
    }

    @Memoized
    Class<?> getRawType() {
        def result
        if (actuallyParametrised) {
            result = type.asType(ParameterizedType).rawType as Class
        } else if (type instanceof TypeVariable) {
            def bounds = type.bounds
            if (bounds) {
                // TODO katoquro: 09/05/2018 #generic-bound multiple bounds
                result = new ApiType(bounds[0]).rawType
            } else {
                // TODO katoquro: 09/05/2018 #generic-bound fallback case; need tests
                result = Object
            }
        } else if (type instanceof WildcardType) {
            def bound = type.upperBounds[0]
            result = new ApiType(bound).rawType
        } else if (type instanceof GenericArrayType) {
            result = type.class
        } else {
            result = type as Class
        }

        return result
    }

    @Memoized
    ApiType getComponentApiType() {
        if (array) {
            switch (type) {
                case { it instanceof Class }:
                    return new ApiType(type.asType(Class).componentType)
                case { it instanceof GenericArrayType }:
                    return new ApiType(type.asType(GenericArrayType).genericComponentType)
            }
        }

        throw new RuntimeException('TYPE IS NOT ARRAY')
    }

    String getTemplateName() {
        if (template) {
            return type.asType(TypeVariable).name
        }

        throw new RuntimeException('TYPE IS NOT TEMPLATE')
    }

    /**
     * List<Integer> -> [Integer]
     * List -> []
     *
     * @return list of generic types actually present on current type
     */
    @Memoized
    List<ApiType> getActualParameters() {
        if (actuallyParametrised) {
            return type.asType(ParameterizedType)
                    .actualTypeArguments
                    .collect { new ApiType(it) }
        }

        throw new RuntimeException('TYPE IS NOT ACTUALLY PARAMETRISED')
    }

    /**
     * @return list of templates which can be parametrised
     */
    @Memoized
    List<ApiType> getGenericParameters() {
        if (generic) {
            return rawType
                    .typeParameters
                    .collect { new ApiType(it) }
        }

        throw new RuntimeException('TYPE IS NOT GENERIC')
    }

    /**
     * Don't rely to the item order. Bounded Generic Types are resolved by class constraints
     * <br>
     * input: <br>
     * {@code List < Map < String , Integer > >}  <br>
     * result: <br>
     * set of {@code [List , Map, Integer, String]}
     *
     *
     * @return list which represents all generic args including generic type
     */
    @Memoized
    List<ApiType> flattenArgumentTypes() {
        return actuallyParametrised ? _flattenArgumentTypes(actualParameters) : []
    }

    private static List<ApiType> _flattenArgumentTypes(List<ApiType> apiTypes) {
        List<ApiType> result = []

        apiTypes.each {
            if (it.actuallyParametrised) {
                result += _flattenArgumentTypes(it.actualParameters)
            } else if (it.array) {
                result += _flattenArgumentTypes([it.componentApiType])
            }
            result << it
        }

        return result.reverse()
    }

    @Override
    boolean equals(o) {
        if (!equalsIgnoreActualParams(o)) return false

        return flattenArgumentTypes().collect { it.rawType } ==
                o.asType(ApiType).flattenArgumentTypes().collect { it.rawType }
    }

    @Override
    int hashCode() {
        return Objects.hash(rawType, flattenArgumentTypes().collect { it.rawType })
    }

    @Override
    String toString() {
        "ApiType{${type.typeName}}"
    }

    boolean equalsIgnoreActualParams(Object o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        if (rawType != o.asType(ApiType).rawType) return false

        return true
    }
}
