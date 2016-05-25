/*
 * Copyright 2014-2016 Ainrif <ainrif@outlook.com>
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
import com.ainrif.apiator.modeltype.JaxRsModelTypeResolver
import com.ainrif.apiator.renderer.core.CoreHtmlRenderer
import com.ainrif.apiator.renderer.core.CoreJsonRenderer
import groovy.json.JsonSlurper
import spock.lang.Specification

import java.nio.file.Paths

import static org.apache.commons.lang3.StringUtils.deleteWhitespace
import static org.hamcrest.Matchers.greaterThan
import static spock.util.matcher.HamcrestSupport.expect

class SmokeSpec extends Specification {
    static final String smokeJson = SmokeSpec.classLoader.getResource('smoke1.json').text
    static final String sourcePath = Paths.get(System.getProperty('user.dir'), 'src', 'test', 'java').toString()
    static final String jaxrsPackage = 'com.ainrif.apiator.test.model.jaxrs.smoke'
    static final def resolvers = [new JaxRsModelTypeResolver()]

    ApiatorConfig getConfigWithJsonRenderer() {
        return new ApiatorConfig(
                renderer: new CoreJsonRenderer(sourcePath: sourcePath),
                basePackage: jaxrsPackage,
                modelTypeResolvers: resolvers)
    }

    def "Smoke test; jax-rs"() {
        when:
        def actual = new Apiator(configWithJsonRenderer).render()

        then:
        new JsonSlurper().parseText(actual) == new JsonSlurper().parseText(smokeJson)
    }

    def "Smoke test; should produce the same result each time"() {
        given:
        def apiator1 = new Apiator(configWithJsonRenderer)
        def apiator2 = new Apiator(configWithJsonRenderer)
        def apiator3 = new Apiator(configWithJsonRenderer)

        when:
        def render1 = apiator1.render()
        def render2 = apiator2.render()
        def render3 = apiator3.render()

        then:
        render1 == render2
        render2 == render3
        deleteWhitespace(render3) == deleteWhitespace(smokeJson)
    }

    def "Smoke test; Core HTML Renderer"() {
        given:
        Apiator apiator = new Apiator(new ApiatorConfig(
                basePackage: jaxrsPackage, renderer: new CoreHtmlRenderer(), modelTypeResolvers: resolvers))

        when:
        def actual = apiator.render()

        then:
        expect actual.size(), greaterThan(0)
    }
}