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
package com.ainrif.apiator.core.model.api

import com.ainrif.apiator.core.model.ModelType
import com.ainrif.apiator.core.model.ModelTypeRegister

import java.lang.reflect.*

class ApiType {

    //Inject
    static ModelTypeRegister modelTypeRegister

    Type type

    List<ApiType> flattenArgumentTypesCache
    Class<?> rawTypeCache
    ModelType modelTypeCache
    ApiType componentApiTypeCache

    ApiType(Type type) {
        this.type = type
    }

    boolean isGeneric() {
        type instanceof ParameterizedType
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

    Class<?> getRawType() {
        rawTypeCache ?: {
            if (generic) {
                rawTypeCache = type.asType(ParameterizedType).rawType.asType(Class)
            } else if (type instanceof TypeVariable) {
                def bounds = type.asType(TypeVariable).bounds
                if (bounds) {
                    //todo multiple bounds
                    rawTypeCache = new ApiType(bounds[0]).rawType
                } else {
                    rawTypeCache = Object //fallback case; need test
                }
            } else if (type instanceof WildcardType) {
                def bound = type.asType(WildcardType).upperBounds[0]
                rawTypeCache = new ApiType(bound).rawType
            } else if (type instanceof GenericArrayType) {
                rawTypeCache = type.class
            } else {
                rawTypeCache = type.asType(Class)
            }
        }()
    }

    ApiType getComponentApiType() {
        componentApiTypeCache ?: {
            if (array) {
                switch (type) {
                    case { it instanceof Class }:
                        return new ApiType(type.asType(Class).componentType)
                    case { it instanceof GenericArrayType }:
                        return new ApiType(type.asType(GenericArrayType).genericComponentType)
                }
            }

            throw new RuntimeException('TYPE IS NOT ARRAY')
        }()
    }

    List<ApiType> getActualTypeArguments() {
        if (generic) {
            return type.asType(ParameterizedType)
                    .actualTypeArguments
                    .collect { new ApiType(it) }
        }

        throw new RuntimeException('TYPE IS NOT GENERIC')
    }

    ModelType getModelType() {
        modelTypeCache ?: {
            if (!modelTypeRegister) throw new RuntimeException('Model Type Register was not injected')
            modelTypeCache = modelTypeRegister.getTypeByClass(rawType)
        }()
    }

    /**
     * Don't rely to the item order. Bounded Generic Types are resolved by class constraints
     *
     * @return list which represents all generic args
     */
    List<ApiType> flattenArgumentTypes() {
        flattenArgumentTypesCache ?: {
            flattenArgumentTypesCache = generic ? _flattenArgumentTypes(actualTypeArguments) : []
        }()
    }

    private static List<ApiType> _flattenArgumentTypes(List<ApiType> apiTypes) {
        def result = []

        apiTypes.each {
            if (it.generic) {
                result += _flattenArgumentTypes(it.actualTypeArguments)
            } else if (it.array) {
                result += _flattenArgumentTypes([it.componentApiType])
            }
            result << it
        }

        result.reverse()
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        ApiType apiType = (ApiType) o
        if (rawType != apiType.rawType) return false

        return flattenArgumentTypes().collect { it.rawType } == apiType.flattenArgumentTypes().collect { it.rawType }
    }

    int hashCode() {
        return Objects.hash(rawType, flattenArgumentTypes())
    }

    @Override
    public String toString() {
        "ApiType{${type.typeName}}"
    }
}
