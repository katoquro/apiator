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
import spock.lang.Specification
import spock.lang.Unroll

import java.lang.reflect.ParameterizedType

import static org.hamcrest.Matchers.containsInAnyOrder
import static org.hamcrest.Matchers.equalTo
import static spock.util.matcher.HamcrestSupport.that

@Unroll
class ApiTypeSpec extends Specification {

    def "isGeneric; #inputType"() {
        given:
        def input = new ApiType(ModelDto1.getDeclaredField(inputType).genericType)

        expect:
        input.generic == expected

        where:
        inputType                 | expected
        'intPrimitiveField'       | false
        'objectField'             | false
        'enumField'               | false
        'stringField'             | false
        'setField'                | true
        'arrayField'              | false
        'iterableField'           | true
        'typeVariableType'        | false
        'typeVariableBoundedType' | false
    }

    def "isArray; #inputType"() {
        given:
        def input = new ApiType(ModelDto1.getDeclaredField(inputType).genericType)

        expect:
        input.array == expected

        where:
        inputType                 | expected
        'intPrimitiveField'       | false
        'objectField'             | false
        'enumField'               | false
        'stringField'             | false
        'setField'                | false
        'arrayField'              | true
        'iterableField'           | false
        'typeVariableType'        | false
        'typeVariableBoundedType' | false
    }

    def "getRawType; #inputType"() {
        given:
        def input = new ApiType(ModelDto1.getDeclaredField(inputType).genericType)

        expect:
        input.rawType == expected

        where:
        inputType                 | expected
        'intPrimitiveField'       | int
        'objectField'             | Object
        'enumField'               | ModelEnum
        'stringField'             | String
        'setField'                | Set
        'arrayField'              | String[]
        'iterableField'           | Iterable
        'typeVariableType'        | Object
        'typeVariableBoundedType' | Collection
    }

    def "getRawType; w/ wildcard type #inputType"() {
        given:
        def wildcardType = ModelDto1.getDeclaredField(inputType)
                .genericType.asType(ParameterizedType)
                .actualTypeArguments[0]
        def input = new ApiType(wildcardType)

        expect:
        input.rawType == expected

        where:
        inputType       | expected
        'iterableField' | Object
    }

    def "getArrayType"() {
        given:
        def input_array = new ApiType(ModelDto1.getDeclaredField('arrayField').genericType)
        def input_collection = new ApiType(ModelDto1.getDeclaredField('iterableField').genericType)

        expect:
        input_array.arrayType == String

        when:
        input_collection.arrayType

        then:
        thrown(RuntimeException)
    }

    def "getActualTypeArguments; #inputType"() {
        given:
        def input = new ApiType(ModelDto2.getDeclaredField(inputType).genericType)
        def expected = [new ApiType(ModelDto1.getDeclaredField(expectedType).genericType)]

        expect:
        input.actualTypeArguments == expected

        where:
        inputType                | expectedType
        'listGEnumField'         | 'enumField'
        'listGStringField'       | 'stringField'
        'listGSetGStringField'   | 'setField'
        'listGStringArray'       | 'arrayField'
        'listGIterableGTVBField' | 'iterableField'
        'listGTVField'           | 'typeVariableType'
        'listGTVBField'          | 'typeVariableBoundedType'
    }

    def "getActualTypeArguments; several generics"() {
        given:
        def input = new ApiType(ModelDto2.getDeclaredField('mapGSetGStringAndGTVBField').genericType)
        def expected = [new ApiType(ModelDto1.getDeclaredField('setField').genericType),
                        new ApiType(ModelDto1.getDeclaredField('typeVariableBoundedType').genericType)]

        expect:
        input.actualTypeArguments == expected
    }

    def "getActualTypeArguments; not generic type"() {
        given:
        def input = new ApiType(ModelDto2.getDeclaredField('objectField').genericType)

        when:
        input.actualTypeArguments

        then:
        def ex = thrown(RuntimeException)
    }

    def "_flattenArgumentTypes; #inputType"() {
        given:
        def input = [new ApiType(ModelDto2.getDeclaredField(inputType).genericType)]
        def expected = expectedTypes.collect { equalTo(new ApiType(it)) }

        expect:
        that ApiType._flattenArgumentTypes(input), containsInAnyOrder(expected)

        where:
        inputType                    | expectedTypes
        'objectField'                | [Object]
        'listGEnumField'             | [List, ModelEnum]
        'listGStringField'           | [List, String]
        'listGSetGStringField'       | [List, Set, String]
        'listGTVField'               | [List, Object]
        'listGTVBField'              | [List, Collection]
        'listGIterableGTVBField'     | [List, Iterable, Collection]
        'listGStringArray'           | [List, String[]]
        'mapGSetGStringAndGTVBField' | [Map, Set, String, Collection]
    }

    def "flattenArgumentTypes; w/o generic type"() {
        expect:
        new ApiType(String).flattenArgumentTypes() == []
    }

    def "getModelType; check if register was injected"() {
        setup:
        ApiType.modelTypeRegister = null

        when:
        new ApiType(String).getModelType()

        then:
        thrown(RuntimeException)

        when:
        ApiType.modelTypeRegister = new ModelTypeRegister()
        def actual = new ApiType(String).getModelType()

        then:
        actual == ModelType.STRING

        cleanup:
        ApiType.modelTypeRegister = null
    }
}
