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

package com.ainrif.apiator.gradle

import com.ainrif.apiator.core.Apiator
import com.ainrif.apiator.core.ApiatorConfig
import com.ainrif.apiator.core.ApiatorInternalApi
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.*

class ApiatorGradleTask extends DefaultTask {
    @Internal Project configuredProject
    @Internal ApiatorConfig config

    // TODO katoquro: 01/07/2018 use reflection to get fields and append simple names of their classes to dump
    @Input String configDump
    @InputDirectory File classesDir

    @OutputFile File renderOutput

    @TaskAction
    void action() {
        def sourcesUrls = configuredProject.sourceSets['main']
                .asType(SourceSet)
                .output.classesDirs.files
                .collect { it.toURI().toURL() }

        def runtimeCpUrls = configuredProject.configurations['runtimeClasspath']
                .files
                .collect { it.toURI().toURL() }

        def extraUrls = (sourcesUrls + runtimeCpUrls) as URL[]

        def apiator = new Apiator(config)
        ApiatorInternalApi.setExtraClassPath(apiator, extraUrls)

        renderOutput.write(apiator.render())
    }
}