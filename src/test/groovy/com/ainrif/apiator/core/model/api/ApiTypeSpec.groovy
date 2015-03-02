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

import spock.lang.Specification
import spock.lang.Unroll

import static org.hamcrest.Matchers.containsInAnyOrder
import static org.hamcrest.Matchers.equalTo
import static spock.util.matcher.HamcrestSupport.that

class ApiTypeSpec extends Specification {

    @Unroll
    def "getRawType"() {
        given:
        def input = new ApiType(ModelDto.getDeclaredField(inputType).genericType)

        expect:
        that input.rawType, equalTo(expected)

        where:
        inputType                 | expected
        'objectField'             | Object
        'stringField'             | String
        'listField'               | List
        'arrayField'              | String[]
        'collectionField'         | Collection
        'typeVariableType'        | Object
        'typeVariableBoundedType' | Collection
    }

    @Unroll
    def "_flattenArgumentTypes"() {
        given:
        def input = [new ApiType(ModelDto2.getDeclaredField(inputType).genericType)]
        def expected = expectedTypes.collect { equalTo(new ApiType(it)) }

        expect:
        that ApiType._flattenArgumentTypes(input), containsInAnyOrder(expected)

        where:
        inputType              | expectedTypes
        'objectField'          | [Object]
        'listGStringField'     | [List, String]
        'listGSetGStringField' | [List, Set, String]
        'listGTVField'         | [List, Object]
        'listGTVBField'        | [List, Collection]
        'listGSetGTVBField'    | [List, Set, Collection]
        'ListGStringArray'     | [List, String[]]
    }
}
