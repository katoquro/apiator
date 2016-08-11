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
package com.ainrif.apiator.renderer.core.json

import com.ainrif.apiator.api.Renderer
import com.ainrif.apiator.core.model.api.ApiScheme
import com.ainrif.apiator.doclet.ApiatorDoclet
import com.ainrif.apiator.doclet.model.JavaDocInfo
import com.ainrif.apiator.renderer.core.json.javadoc.JavaDocInfoIndexer
import com.ainrif.apiator.renderer.core.json.view.ApiSchemeView
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import groovy.json.JsonSlurper
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CoreJsonRenderer implements Renderer {
    private static final Logger logger = LoggerFactory.getLogger(CoreJsonRenderer)

    @Delegate final Config config

    public static class Config {
        String sourcePath
        String basePackage
        boolean autoConfig = true
    }

    CoreJsonRenderer(@DelegatesTo(Config) Closure configurator) {
        this()
        this.config.with configurator
    }

    CoreJsonRenderer(Config config) {
        this.config = config
    }

    CoreJsonRenderer() {
        this.config = new Config()
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
            try {
                getClass().forName('com.sun.javadoc.Doclet')

                def result = ApiatorDoclet.runDoclet(sourcePath, basePackage, null)
                if (0 != result.code) System.exit(result.code)

                def filePath = result.outputFile
                javaDocInfo = new JsonSlurper().parse(new File(filePath)) as JavaDocInfo


            } catch (ClassNotFoundException e) {
                logger.info("JavaDoc Spi was not found. tools.jar may be missing at classpath")
            }
        }

        def apiScheme = new ApiSchemeView(scheme, new JavaDocInfoIndexer(javaDocInfo.classes))

        def mapper = new ObjectMapper()
        mapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
        mapper.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)

        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        mapper.serializationInclusion = JsonInclude.Include.NON_NULL

        mapper.writeValueAsString(apiScheme)
    }
}
