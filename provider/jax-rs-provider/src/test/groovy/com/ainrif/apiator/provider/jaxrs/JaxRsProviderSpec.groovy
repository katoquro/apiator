/*
 * Copyright 2014-2017 Ainrif <support@ainrif.com>
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

import com.ainrif.apiator.test.model.jaxrs.smoke.JaxRsSpecCheckService
import com.ainrif.apiator.test.model.jaxrs.smoke.JaxRsSpecCheckServiceImpl
import spock.lang.Specification

class JaxRsProviderSpec extends Specification {
    def "getApiEndpoints"() {
        given:
        def contextStack = new JaxRsContextStack([JaxRsSpecCheckService, JaxRsSpecCheckServiceImpl])
        def provider = new JaxRsProvider()
        and:
        String[] expected = ['supportCustomAnnotationsSubset',
                             'pathParam',
                             'getDtoFromImpl',
                             'pathFromImpl',
                             'implOnlyEndpoint',
                             'implicitHttpMethodDeclaration']

        when:
        def actual = provider.getMethodStacks(contextStack)

        then:
        actual.size() == expected.size()
        actual.collect { it.name }.containsAll(expected)
    }
}
