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
package com.ainrif.apiator.provider.jaxrs

import com.ainrif.apiator.test.model.jaxrs.Service
import com.ainrif.apiator.test.model.jaxrs.ServiceImpl
import spock.lang.Specification

import static org.hamcrest.Matchers.containsInAnyOrder
import static org.hamcrest.Matchers.hasSize
import static spock.util.matcher.HamcrestSupport.expect

class JaxRsProviderSpec extends Specification {
    def "getApiEndpoints"() {
        given:
        def contextStack = new JaxRsContextStack([Service, ServiceImpl])
        def provider = new JaxRsProvider()
        and:
        String[] expected = ['postDtoObject',
                             'getStringDtoInImpl',
                             'putStringDto',
                             'getAll',
                             'getAllFlatten',
                             'getDtoTypeValue',
                             'getDtoWildcardType',
                             'doVoidMethod',
                             'getByteArray',
                             'setStatus',
                             'getStream']

        when:
        def actual = provider.getMethodStacks(contextStack)

        then:
        expect actual, hasSize(expected.size())
        expect actual.collect { it.name }, containsInAnyOrder(expected)
    }
}
