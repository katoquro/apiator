/*
 * Copyright 2014-2019 Ainrif <support@ainrif.com>
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
package gradle

import com.ainrif.apiator.core.Apiator
import com.ainrif.apiator.core.ApiatorConfig
import com.ainrif.apiator.core.DocletConfig
import com.ainrif.apiator.provider.jaxrs.JaxRsProvider
import com.ainrif.apiator.renderer.core.json.CoreJsonRenderer
import com.ainrif.apiator.renderer.plugin.jaxrs.JaxRsCompositePlugin
import groovy.json.JsonOutput
import groovy.transform.CompileDynamic

import javax.ws.rs.Path
import java.time.LocalDate

@CompileDynamic
class GenerateJsonForUiDev {
    static final String JAXRS_PACKAGE = 'com.ainrif.apiator.test.model.jaxrs.uidev'
    static final String PRETTIER_IGNORE = '// prettier-ignore'
    static final String JS_PREFIX = 'module.exports = '
    static final String COPYRIGHT = """
        /*
         * Copyright 2014-${LocalDate.now().year} Ainrif <support@ainrif.com>
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
        """.stripIndent().trim()

    static void main(String[] args) {
        def path = "${args[0]}/web-client/apiator-data/dev/index.js"
        def config = new ApiatorConfig(
                basePackage: JAXRS_PACKAGE,
                apiClass: Path,
                docletConfig: new DocletConfig(
                        includeBasePackage: 'com.ainrif.apiator'
                ),
                provider: new JaxRsProvider(),
                renderer: new CoreJsonRenderer({
                    plugins << new JaxRsCompositePlugin()
                }))

        def apiator = new Apiator(config)
        apiator.@info.version = '0.0.0-dev_version'
        new File(path)
                .write(COPYRIGHT +
                        System.lineSeparator() * 2 +
                        PRETTIER_IGNORE + System.lineSeparator() +
                        JS_PREFIX +
                        JsonOutput.prettyPrint(apiator.render()) +
                        ';' +
                        System.lineSeparator()
                )
    }
}
