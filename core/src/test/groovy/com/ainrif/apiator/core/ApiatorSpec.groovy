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
package com.ainrif.apiator.core

import com.ainrif.apiator.test.model.m06.M06_Api
import spock.lang.Specification

class ApiatorSpec extends Specification {
    def "scanForApi"() {
        given:
        def config = new ApiatorConfig(
                basePackage: 'com.ainrif.apiator.test.model.m06',
                apiClass: M06_Api)
        def apiator = new Apiator(config)

        when:
        def actual = apiator.scanForApi()

        then:
        actual.size() == 2
        actual.collect { it.simpleName }
                .containsAll('M06_Annotated', 'M06_AnnotatedViaInheritance')
    }
}
