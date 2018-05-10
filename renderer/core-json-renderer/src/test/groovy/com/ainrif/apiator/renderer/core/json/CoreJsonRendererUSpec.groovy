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

import com.ainrif.apiator.renderer.core.json.plugin.DefaultModelTypeCompositePlugin
import com.ainrif.apiator.renderer.core.json.plugin.modeltype.CustomUnresolvedType
import com.ainrif.apiator.renderer.plugin.spi.*
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

    def "model type is resolved according to given plugins"() {
        setup:
        CoreJsonRenderer.pluginsConfig = new CoreJsonRenderer.PluginsConfig(
                modelTypePlugins: new DefaultModelTypeCompositePlugin().plugins as List<ModelTypePlugin>
        )

        expect:
        CoreJsonRenderer.internalTypeByClass(Object) == ModelType.ANY
        CoreJsonRenderer.internalTypeByClass(CustomUnresolvedType) == ModelType.OBJECT

        cleanup:
        CoreJsonRenderer.pluginsConfig = null
    }

    def "model type is resolved according to given plugins; with custom plugins"() {
        setup:
        def customPluginList = [
                { CustomUnresolvedType.isAssignableFrom(it) ? ModelType.VOID : null } as ModelTypePlugin
        ]

        CoreJsonRenderer.pluginsConfig = new CoreJsonRenderer.PluginsConfig(
                modelTypePlugins: customPluginList + (new DefaultModelTypeCompositePlugin().plugins as List<ModelTypePlugin>)
        )

        expect:
        CoreJsonRenderer.internalTypeByClass(Object) == ModelType.ANY
        CoreJsonRenderer.internalTypeByClass(CustomUnresolvedType) == ModelType.VOID

        cleanup:
        CoreJsonRenderer.pluginsConfig = null
    }
}
