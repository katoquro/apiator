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

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.lang.reflect.WildcardType

class ApiType {

    //Inject
    static ModelTypeRegister modelTypeRegister

    Type type
    List<ApiType> flattenArgumentTypesCache
    Class<?> rawTypeCache
    ModelType modelTypeCache

    ApiType(Type type) {
        this.type = type
    }

    boolean isGeneric() {
        type instanceof ParameterizedType
    }

    boolean isArray() {
        type instanceof Class ?
                type.asType(Class).isArray() :
                false
    }

    Class<?> getRawType() {
        rawTypeCache ?: {
            if (generic) {
                rawTypeCache = type.asType(ParameterizedType).rawType.asType(Class)
            } else if (type instanceof TypeVariable) {
                def bounds = type.asType(TypeVariable).bounds
                if (bounds) {
                    rawTypeCache = bounds[0].asType(Class)
                } else {
                    rawTypeCache = Object //fallback case; need test
                }
            } else if (type instanceof WildcardType) {
                rawTypeCache = type.asType(WildcardType).upperBounds[0].asType(Class)
            } else {
                rawTypeCache = type.asType(Class)
            }
        }()
    }

    Class<?> getArrayType() {
        if (array) {
            return type.asType(Class).componentType
        }

        throw new RuntimeException('TYPE IS NOT ARRAY')
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
