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
package gradle

import com.ainrif.apiator.core.Apiator
import com.ainrif.apiator.core.ApiatorConfig
import com.ainrif.apiator.provider.jaxrs.JaxRsProvider
import com.ainrif.apiator.renderer.core.json.CoreJsonRenderer
import com.ainrif.apiator.renderer.plugin.jaxrs.JaxRsCompositePlugin

import javax.ws.rs.Path

class GenerateJsonForUiDev {
    static final String jaxrsPackage = 'com.ainrif.apiator.test.model.jaxrs.uidev'

    static void main(String[] args) {
        def path = "${args[0]}/stub.json"
        def config = new ApiatorConfig(
                basePackage: jaxrsPackage,
                apiClass: Path,
                provider: new JaxRsProvider(),
                renderer: new CoreJsonRenderer({
                    docletBasePackage = 'com.ainrif.apiator'
                    plugins << new JaxRsCompositePlugin()
                }))

        new File(path).write(new Apiator(config).render())
    }
}
