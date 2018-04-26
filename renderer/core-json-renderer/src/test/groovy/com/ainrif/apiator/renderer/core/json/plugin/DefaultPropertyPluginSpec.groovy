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

package com.ainrif.apiator.renderer.core.json.plugin

import com.ainrif.apiator.renderer.core.json.test.model.M_ConcreteInterface
import com.ainrif.apiator.renderer.core.json.test.model.M_ConcreteType
import com.ainrif.apiator.renderer.core.json.test.model.M_PropertiesWithClash
import spock.lang.Specification

class DefaultPropertyPluginSpec extends Specification {
    def "retrieve properties from pojo"() {
        given:
        def mapper = new DefaultPropertyPlugin()
        def expectedNames = ['fieldPublic',
                             'publicBeanPropGetSet',
                             'publicBeanPropOnlyGet',
                             'publicBeanPropOnlySet']

        when:
        def actual = mapper.collectProperties(M_ConcreteType)

        then:
        actual.size() == 4
        actual*.name.containsAll(expectedNames)
    }

    def "retrieve properties from interface"() {
        given:
        def mapper = new DefaultPropertyPlugin()
        def expectedNames = ['interfaceOnlyGet']

        when:
        def actual = mapper.collectProperties(M_ConcreteInterface)

        then:
        actual.size() == 1
        actual*.name.containsAll(expectedNames)
    }

    def "retrieve public property with getter or setter stay fully accessible"() {
        given:
        def mapper = new DefaultPropertyPlugin()
        def expectedName = 'publicFieldWithOnlySet'

        when:
        def actual = mapper.collectProperties(M_PropertiesWithClash)

        then:
        actual.size() == 1
        def field = actual.first()
        field.name == expectedName
        field.readable
        field.writable
    }
}
