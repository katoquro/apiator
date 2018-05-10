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
import com.ainrif.apiator.core.spi.Renderer
import com.ainrif.apiator.doclet.ApiatorDoclet
import com.ainrif.apiator.doclet.model.JavaDocInfo
import com.ainrif.apiator.renderer.core.json.javadoc.JavaDocInfoIndexer
import com.ainrif.apiator.renderer.core.json.plugin.DefaultCompositePlugin
import com.ainrif.apiator.renderer.core.json.view.ApiSchemeView
import com.ainrif.apiator.renderer.plugin.spi.*
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.google.common.annotations.VisibleForTesting
import groovy.json.JsonSlurper
import groovy.transform.Memoized
import groovy.transform.PackageScope
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static java.util.Collections.singletonList

class CoreJsonRenderer implements Renderer {
    private static final Logger logger = LoggerFactory.getLogger(CoreJsonRenderer)

    @Delegate
    Config config

    static PluginsConfig pluginsConfig

    static class Config {
        /**
         * Path to sources separated with colons (:) or semicolon for Windows OS (;)
         *
         * By default renderer tries to detect if auto config is enabled
         */
        String sourcePath
        /**
         * Given package is used to reduce amount of classes processed by doclet
         */
        String docletBasePackage = '.'
        /**
         * List of plugins to customise renderer output <br>
         * Custom plugins can be add to the end of list or instead of defaults <br>
         * For plugins which relay on order like {@link ModelTypePlugin} the last added plugins will be processed first
         */
        List<CoreJsonRendererPlugin> plugins = [new DefaultCompositePlugin()]
        /**
         * Enable auto configuration features
         */
        boolean autoConfig = true
    }

    static class PluginsConfig {
        PropertyPlugin propertyPlugin
        List<ModelTypePlugin> modelTypePlugins = []
    }

    CoreJsonRenderer(@DelegatesTo(Config) Closure configurator) {
        this.config = new Config()
        this.config.with configurator
        init()
    }

    CoreJsonRenderer(Config config) {
        this.config = config
        init()
    }

    CoreJsonRenderer() {
        this.config = new Config()
        init()
    }

    protected init() {
        pluginsConfig = new PluginsConfig()

        def plugins = flattenCompositePlugins(config.plugins)
        plugins.each {
            switch (it) {
                case PropertyPlugin:
                    pluginsConfig.propertyPlugin = it
                    break
                case ModelTypePlugin:
                    pluginsConfig.modelTypePlugins << it
                    break
                default:
                    throw new RuntimeException("Unsupported Plugin type: ${it.class.name}")
            }
        }

        // check user defined plugins first
        pluginsConfig.modelTypePlugins.reverse(true)
    }

    static List<CoreJsonRendererPlugin> flattenCompositePlugins(List<CoreJsonRendererPlugin> plugins) {
        return plugins.collectMany {
            if (it instanceof CompositePlugin) {
                return flattenCompositePlugins(it.plugins)
            } else {
                return singletonList(it)
            }
        }
    }

    @Memoized
    static ModelType getTypeByClass(Class<?> type) {
        internalTypeByClass(type)
    }

    @PackageScope
    @VisibleForTesting
    static ModelType internalTypeByClass(Class<?> type) {
        for (ModelTypePlugin mtr : pluginsConfig.modelTypePlugins) {
            def resolved = mtr.resolve(type)
            if (resolved) return resolved
        }

        return ModelType.OBJECT
    }

    @Override
    String render(ApiScheme scheme) {
        logger.debug('auto configuration: {}', autoConfig)
        if (autoConfig) {
            if (!sourcePath) {
                sourcePath = new SourcePathDetector(scheme).detect()
            }
        }

        def javaDocInfo = null
        if (sourcePath) {
            // TODO katoquro: 22/04/2018 support classpath search when all paths are wrong
            sourcePath.split(SourcePathDetector.OS_PATH_DELIMITER)
                    .findAll { !new File(it).exists() }
                    .each { logger.warn("There are no source path like {}", it) }
            try {
                getClass().forName('com.sun.javadoc.Doclet')

                def result = ApiatorDoclet.runDoclet(sourcePath, docletBasePackage, null)
                if (0 != result.code) System.exit(result.code)

                def filePath = result.outputFile
                javaDocInfo = new JsonSlurper().parse(new File(filePath)) as JavaDocInfo

            } catch (ClassNotFoundException ignore) {
                logger.info("JavaDoc Spi was not found. tools.jar may be missing at classpath")
            }
        }

        def apiScheme = new ApiSchemeView(scheme, new JavaDocInfoIndexer(javaDocInfo.classes))

        def mapper = new ObjectMapper()
        mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
        mapper.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)

        mapper.serializationInclusion = JsonInclude.Include.NON_NULL

        return mapper.writeValueAsString(apiScheme)
    }
}
