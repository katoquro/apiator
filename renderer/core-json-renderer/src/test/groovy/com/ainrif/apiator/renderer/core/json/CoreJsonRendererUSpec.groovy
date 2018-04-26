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

package com.ainrif.apiator.renderer.core.json

import com.ainrif.apiator.renderer.plugin.spi.CompositePlugin
import com.ainrif.apiator.renderer.plugin.spi.CoreJsonRendererPlugin
import com.ainrif.apiator.renderer.plugin.spi.PropertyPlugin
import spock.lang.Specification

class CoreJsonRendererUSpec extends Specification {

    def "composite plugins can be nested ans should be flatten properly"() {
        given:
        List<CoreJsonRendererPlugin> plugins = [
                { type -> } as PropertyPlugin,
                { [{ [{ type -> } as PropertyPlugin] } as CompositePlugin] } as CompositePlugin,
        ]

        when:
        def actual = CoreJsonRenderer.flattenCompositePlugins(plugins)

        then:
        actual.size() == 2
        actual.each { it instanceof PropertyPlugin }
    }
}
