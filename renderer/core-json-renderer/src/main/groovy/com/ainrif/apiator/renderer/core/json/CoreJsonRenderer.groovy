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

import com.ainrif.apiator.core.model.api.ApiScheme
import com.ainrif.apiator.core.model.api.ApiType
import com.ainrif.apiator.core.spi.Renderer
import com.ainrif.apiator.renderer.core.json.plugin.DefaultCompositePlugin
import com.ainrif.apiator.renderer.core.json.view.ApiSchemeView
import com.ainrif.apiator.renderer.plugin.spi.CompositePlugin
import com.ainrif.apiator.renderer.plugin.spi.CoreJsonRendererPlugin
import com.ainrif.apiator.renderer.plugin.spi.modeltype.ModelType
import com.ainrif.apiator.renderer.plugin.spi.modeltype.ModelTypePlugin
import com.ainrif.apiator.renderer.plugin.spi.param.ParamPlugin
import com.ainrif.apiator.renderer.plugin.spi.path.PathPlugin
import com.ainrif.apiator.renderer.plugin.spi.property.PropertyPlugin
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import groovy.transform.Memoized
import groovy.transform.PackageScope

import static java.util.Collections.singletonList

class CoreJsonRenderer implements Renderer {
    @Delegate
    Config config

    static PluginsConfig pluginsConfig

    static class Config {
        /**
         * List of plugins to customise renderer output <br>
         * Custom plugins can be add to the end of list or instead of defaults <br>
         * For plugins which relay on order like {@link com.ainrif.apiator.renderer.plugin.spi.modeltype.ModelTypePlugin} the last added plugins will be processed first
         */
        List<CoreJsonRendererPlugin> plugins = new ArrayList<>([new DefaultCompositePlugin()])
    }

    static class PluginsConfig {
        PropertyPlugin propertyPlugin
        ParamPlugin paramPlugin
        PathPlugin pathPlugin
        List<ModelTypePlugin> modelTypePlugins = []
    }

    CoreJsonRenderer(@DelegatesTo(value = Config, strategy = Closure.DELEGATE_FIRST) Closure configurator) {
        this.config = new Config()
        this.config.tap configurator
        init()
    }

    CoreJsonRenderer(Config config) {
        this.config = config
        init()
    }

    protected init() {
        pluginsConfig = new PluginsConfig()

        def plugins = flattenCompositePlugins(config.plugins)
        plugins.each {
            switch (it) {
                case ParamPlugin:
                    pluginsConfig.paramPlugin = it as ParamPlugin
                    break
                case PropertyPlugin:
                    pluginsConfig.propertyPlugin = it as PropertyPlugin
                    break
                case PathPlugin:
                    pluginsConfig.pathPlugin = it as PathPlugin
                    break
                case ModelTypePlugin:
                    pluginsConfig.modelTypePlugins << (it as ModelTypePlugin)
                    break
                default:
                    throw new RuntimeException("Unsupported Plugin type: ${it.class.name}")
            }
        }

        // check user defined plugins first
        pluginsConfig.modelTypePlugins.reverse(true)
    }

    static List<? extends CoreJsonRendererPlugin> flattenCompositePlugins(List<? super CoreJsonRendererPlugin> plugins) {
        return plugins.collectMany {
            if (it instanceof CompositePlugin) {
                return flattenCompositePlugins(it.plugins)
            } else {
                return singletonList(it as CoreJsonRendererPlugin)
            }
        }
    }

    @Memoized
    static ModelType getTypeByClass(ApiType type) {
        return internalTypeByClass(type.rawType)
    }

    // VisibleForTesting
    @PackageScope
    static ModelType internalTypeByClass(Class<?> type) {
        for (ModelTypePlugin mtr : pluginsConfig.modelTypePlugins) {
            def resolved = mtr.resolve(type)
            if (resolved) return resolved
        }

        return ModelType.OBJECT
    }

    @Override
    String render(ApiScheme scheme) {
        def apiScheme = new ApiSchemeView(scheme, scheme.docletIndex)

        def mapper = new ObjectMapper()
        mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
        mapper.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)

        mapper.serializationInclusion = JsonInclude.Include.NON_NULL

        return mapper.writeValueAsString(apiScheme)
    }
}
