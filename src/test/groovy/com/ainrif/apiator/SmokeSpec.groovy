/*
 * Copyright 2014-2016 Ainrif <support@ainrif.com>
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
import com.ainrif.apiator.modeltype.jaxrs.JaxRsModelTypeResolver
import com.ainrif.apiator.provider.jaxrs.JaxRsProvider
import com.ainrif.apiator.renderer.core.html.CoreHtmlRenderer
import com.ainrif.apiator.renderer.core.json.CoreJsonRenderer
import groovy.json.JsonSlurper
import spock.lang.Specification

import java.nio.file.Paths

import static org.apache.commons.lang3.StringUtils.deleteWhitespace

//todo split by renderers
class SmokeSpec extends Specification {
    static final String smokeJson = SmokeSpec.classLoader.getResource('jaxrs-smoke.json').text
    static
    final String sourcePath = Paths.get(System.getProperty('user.dir'), 'test', 'jax-rs-model-test', 'src', 'main', 'java').toString()
    static final String jaxrsPackage = 'com.ainrif.apiator.test.model.jaxrs.smoke'
    static final def resolvers = [new JaxRsModelTypeResolver()]

    ApiatorConfig getConfigWithJsonRenderer() {
        return new ApiatorConfig(
                provider: new JaxRsProvider(),
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

    def "Smoke test; auto-detection of source paths"() {
        when:
        def config = configWithJsonRenderer
        config.renderer = new CoreJsonRenderer()
        def actual = new Apiator(config).render()

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
        Apiator apiator = new Apiator(
                new ApiatorConfig(
                        basePackage: jaxrsPackage,
                        provider: new JaxRsProvider(),
                        renderer: new CoreHtmlRenderer(),
                        modelTypeResolvers: resolvers))

        when:
        def actual = apiator.render()

        then:
        actual.size() > 0
    }
}