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
package apiator

import com.ainrif.apiator.core.ApiatorConfig
import com.ainrif.apiator.gradle.ApiatorGradleTask
import org.gradle.api.Project

import static com.ainrif.apiator.gradle.GroovyUtils.hint

class ApiatorGradle {
    /**
     * Applies plugin to given project (module) and delegate to default Apiator config
     *
     * @param project
     * @param configClosure
     */
    static void configureApiator(Project project, @DelegatesTo(ApiatorConfig) Closure configClosure) {
        project.configure(project, hint(Project) {
            apply plugin: 'java'
            apply plugin: 'com.ainrif.apiator'

            configure(getTasksByName('apiator', false), hint(ApiatorGradleTask) {
                def apiatorConfig = new ApiatorConfig()

                def closure = configClosure.clone() as Closure
                closure.delegate = apiatorConfig
                closure.resolveStrategy = Closure.DELEGATE_FIRST

                closure.call()

                configuredProject = project
                config = apiatorConfig

                configDump = createConfigDump(config)
                classesDir = file(configuredProject.buildDir.absolutePath + '/classes')

                renderOutput = file(configuredProject.buildDir.absolutePath + '/apiator-render-output')
            })
        })
    }

    /**
     * Rough check than config wasn't changed dramatically
     * @param config
     * @return string representation of key config properties
     */
    private static String createConfigDump(ApiatorConfig config) {
        new StringBuilder('apiator-config-dump')
                .append(config.basePackage)
                .append(config.provider.class.canonicalName)
                .append(config.renderer.class.canonicalName)
                .append(config.apiClass.canonicalName)
                .append(config.docletConfig.enabled)
    }
}
