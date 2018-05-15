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
package com.ainrif.apiator.test.smoke

import com.ainrif.apiator.core.Apiator
import com.ainrif.apiator.core.ApiatorConfig
import com.ainrif.apiator.provider.jaxrs.JaxRsProvider
import com.ainrif.apiator.renderer.core.html.CoreHtmlRenderer
import com.ainrif.apiator.renderer.plugin.jaxrs.JaxRsCompositePlugin
import spock.lang.Specification

class CoreHtmlRendererSmokeSpec extends Specification {
    def "Smoke test; Core HTML Renderer"() {
        given:
        Apiator apiator = new Apiator(
                new ApiatorConfig(
                        basePackage: 'com.ainrif.apiator.test.model.jaxrs.smoke',
                        provider: new JaxRsProvider(),
                        renderer: new CoreHtmlRenderer({
                            plugins << new JaxRsCompositePlugin()
                        }),
                ))

        when:
        def actual = apiator.render()

        then:
        actual.size() > 0
    }
}