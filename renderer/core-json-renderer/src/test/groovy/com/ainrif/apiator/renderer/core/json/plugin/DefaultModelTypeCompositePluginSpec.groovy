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

package com.ainrif.apiator.renderer.core.json.plugin

import com.ainrif.apiator.renderer.core.json.plugin.modeltype.CoreJavaModelTypePlugin
import com.ainrif.apiator.renderer.core.json.plugin.modeltype.DefaultModelTypeCompositePlugin
import com.ainrif.apiator.renderer.plugin.spi.modeltype.ModelTypePlugin
import io.github.classgraph.ClassGraph
import spock.lang.Specification

class DefaultModelTypeCompositePluginSpec extends Specification {
    def "default init should include all core resolvers"() {
        given:
        def corePackage = CoreJavaModelTypePlugin.package.name

        def corePlugins = new ClassGraph()
                .enableClassInfo()
                .whitelistPackages(corePackage)
                .scan()
                .withCloseable {
                    it.getClassesImplementing(ModelTypePlugin.name)
                }

        expect:
        new DefaultModelTypeCompositePlugin().plugins.size() == corePlugins.size()
    }
}
