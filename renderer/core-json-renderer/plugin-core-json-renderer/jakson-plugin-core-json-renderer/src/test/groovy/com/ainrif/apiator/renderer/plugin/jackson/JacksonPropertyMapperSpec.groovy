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

package com.ainrif.apiator.renderer.plugin.jackson

import com.ainrif.apiator.core.model.api.ApiField
import com.ainrif.apiator.renderer.plugin.jackson.test.model.M_JacksonProperties
import spock.lang.Specification

class JacksonPropertyMapperSpec extends Specification {
    def "retrieve public property with getter or setter stay fully accessible"() {
        given:
        def mapper = new JacksonPropertyMapper()
        def expectedNames = ['plainPublicField',
                             'jackson_public_field',
                             'jackson_getter_public_field',
                             'jackson_setter_public_field',
                             'jackson_getter_private_field',
                             'jackson_setter_private_field',
                             'jackson_asymmetric_property']

        when:
        def actual = mapper.mapProperties(M_JacksonProperties)

        then:
        actual.size() == expectedNames.size()
        actual*.name.containsAll(expectedNames)
        Map<String, ApiField> fields = actual.collectEntries { [(it.name): it] }
        fields['jackson_asymmetric_property'].writable
        !fields['jackson_asymmetric_property'].readable
    }
}
