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
package com.ainrif.apiator

import com.ainrif.apiator.core.Apiator
import com.ainrif.apiator.core.ApiatorConfig
import com.ainrif.apiator.writer.core.CoreHtmlRenderer
import spock.lang.Specification

import static org.hamcrest.Matchers.greaterThan
import static org.hamcrest.Matchers.notNullValue
import static spock.util.matcher.HamcrestSupport.expect

class SmokeSpec extends Specification {

    def "Smoke test"() {
        given:
        Apiator apiator = new Apiator(new ApiatorConfig())

        when:
        def actual = apiator.scheme
        println apiator.render()

        then:
        expect actual, notNullValue()
    }

    def "Smoke test; should produce the same result each time"() {
        given:
        def apiator1 = new Apiator(new ApiatorConfig())
        def apiator2 = new Apiator(new ApiatorConfig())
        def apiator3 = new Apiator(new ApiatorConfig())

        when:
        def render1 = apiator1.render()
        def render2 = apiator2.render()
        def render3 = apiator3.render()

        then:
        render1 == render2
        render2 == render3
    }

    def "Smoke test; Core HTML Renderer"() {
        given:
        Apiator apiator = new Apiator(new ApiatorConfig(renderer: new CoreHtmlRenderer()))

        when:
        def actual = apiator.render()

        then:
        expect actual.size(), greaterThan(0)
    }
}