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

package com.ainrif.apiator.renderer.core.json.view

import com.ainrif.apiator.core.model.api.ApiType
import com.ainrif.apiator.renderer.core.json.test.model.M_CustomGeneric
import spock.lang.Specification
import spock.lang.Unroll

class ApiSchemeViewSpec extends Specification {
    @Unroll
    def "collectApiTypesFromGenerics; #inputType"() {
        given:
        def input = new ApiType(M_CustomGeneric.getDeclaredField(inputType).genericType)

        when:
        def actual = ApiSchemeView.collectApiTypesFromGenerics.call(input)

        then:
        actual.size() == expected

        where:
        inputType               | expected
        'nonGeneric'            | 1
        'explicitGeneric'       | 2
        'explicitCustomGeneric' | 2
        'onlyClass'             | 1
    }
}
