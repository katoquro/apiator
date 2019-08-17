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

import com.ainrif.apiator.gradle.internal.ApiatorInternalRunner
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.plugins.GroovyPlugin
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.internal.cleanup.BuildOutputCleanupRegistry

import static GradleUtils.hint

class ApiatorGradlePlugin implements Plugin<Project> {
    @Override
    void apply(Project target) {
        target.getPluginManager().apply(JavaBasePlugin)

        def javaPluginConvention = target.getConvention().getPlugin(JavaPluginConvention)

        SourceSet apiatorSourceSet = javaPluginConvention.sourceSets.create('apiator')

        // TODO katoquro: 17/08/19 #plugin try ti reduce to java plugin
        target.getPluginManager().apply(GroovyPlugin)

        ProjectInternal javaProject = javaPluginConvention.project
        SourceSet mainSourceSet = javaPluginConvention.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)

        apiatorSourceSet.compileClasspath =
                javaProject.objects.fileCollection().from(mainSourceSet.output, javaProject.configurations.getByName('apiatorCompileClasspath'))
        apiatorSourceSet.runtimeClasspath =
                javaProject.objects.fileCollection().from(apiatorSourceSet.output, mainSourceSet.output, javaProject.configurations.getByName('apiatorRuntimeClasspath'))

        // TODO katoquro: 17/08/19 #plugin freeze version
        target.configurations.getByName('apiatorCompile').dependencies.with {
            add(target.dependencies.create("com.ainrif.apiator:utils-apiator-gradle-plugin:+"))
            add(target.dependencies.create("org.slf4j:jcl-over-slf4j:1.7.26"))
            add(target.dependencies.create("org.slf4j:log4j-over-slf4j:1.7.26"))

            // TODO katoquro: 17/08/19 #plugin configure logging provider by excluding rest of them
            add(target.dependencies.create("ch.qos.logback:logback-classic:1.2.3"))
        }

        target.configurations
                .findAll { it.name.startsWith('apiator') }
                .each { it.extendsFrom(target.configurations.getByName((it.name - 'apiator').uncapitalize())) }

        // TODO katoquro: 17/08/19 #plugin exclude other loggers here
        target.configurations.all { Configuration c ->
            c.exclude(group: 'commons-logging')
            c.exclude(group: 'org.apache.logging.log4j')
            c.exclude(group: 'org.slf4j', module: 'slf4j-log4j12')
        }

        target.getServices().get(BuildOutputCleanupRegistry).registerOutputs(apiatorSourceSet.getOutput())

        target.tasks.create('apiator', ApiatorGradleTask, hint(ApiatorGradleTask) {
            group = 'documentation'
            description = 'Generates documentation by scanning given classpath'
            dependsOn = ['classes']

            classesDir = apiatorSourceSet.allSource.asFileTree
            renderOutput = new File(target.buildDir, 'apiator')
            renderOutput.mkdir()

            classpath = apiatorSourceSet.runtimeClasspath
            main = ApiatorInternalRunner.canonicalName
            doFirst {
                args = [renderOutput.absolutePath, runnerClass]
            }
        })
    }
}
