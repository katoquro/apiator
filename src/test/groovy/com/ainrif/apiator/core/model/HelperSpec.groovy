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
package com.ainrif.apiator.core.model

import com.ainrif.apiator.test.model.m05.M05_ConcreteInterface
import com.ainrif.apiator.test.model.m05.M05_ConcreteType
import spock.lang.Specification

import static org.hamcrest.Matchers.containsInAnyOrder
import static spock.util.matcher.HamcrestSupport.expect

class HelperSpec extends Specification {
    def "getPropertiesTypes"() {
        given:
        def expectedNames = ['fieldPublic',
                             'publicBeanPropGetSet',
                             'publicBeanPropOnlyGet',
                             'publicBeanPropOnlySet'].toArray()

        when:
        def actual = Helper.getPropertiesTypes(M05_ConcreteType)

        then:
        actual.size() == 4
        expect actual.keySet(), containsInAnyOrder(expectedNames)
    }

    def "getPropertiesTypes; w/ interface"() {
        given:
        def expectedNames = ['interfaceOnlyGet'].toArray()

        when:
        def actual = Helper.getPropertiesTypes(M05_ConcreteInterface)

        then:
        actual.size() == 1
        expect actual.keySet(), containsInAnyOrder(expectedNames)
    }
}
