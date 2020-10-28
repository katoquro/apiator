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

import apiator.ApiatorGradleRunner
import com.ainrif.apiator.gradle.internal.ApiatorInternalRunner
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.internal.cleanup.BuildOutputCleanupRegistry

import static GradleUtils.hint
import static com.ainrif.apiator.gradle.GradleUtils.assertArg

class ApiatorGradlePlugin implements Plugin<Project> {

    private static List<String> apiatorModules = ['core',
                                                  'api',

                                                  'utils-apiator-gradle-plugin',

                                                  'jax-rs-provider',
                                                  'micronaut-provider',

                                                  'core-html-renderer',
                                                  'core-json-renderer',

                                                  'spi-plugin-core-json-renderer',
                                                  'jakson-plugin-core-json-renderer',
                                                  'jax-rs-plugin-core-json-renderer',
                                                  'micronaut-plugin-core-json-renderer']

    @Override
    void apply(Project prj) {
        prj.getPluginManager().apply(JavaPlugin)

        def javaPluginConvention = prj.getConvention().getPlugin(JavaPluginConvention)

        javaPluginConvention.sourceSets.register('apiator').configure({ apiatorSourceSet ->
            prj.configurations
                    .findAll { it.name.startsWith('apiator') }
                    .each { it.extendsFrom(prj.configurations.getByName((it.name - 'apiator').uncapitalize())) }

            configureDependencyConstraints(prj)
            configureClasspath(prj, javaPluginConvention, apiatorSourceSet)

            prj.getServices().get(BuildOutputCleanupRegistry).registerOutputs(apiatorSourceSet.getOutput())

            prj.tasks.register('apiator', ApiatorGradleTask, hint(ApiatorGradleTask) {
                group = 'documentation'
                description = 'Generates documentation by scanning given classpath'
                dependsOn = ['classes']

                classesDir = apiatorSourceSet.allSource.asFileTree
                renderOutput = new File(prj.buildDir, 'apiator')
                renderOutput.mkdir()

                classpath = apiatorSourceSet.runtimeClasspath
                main = ApiatorInternalRunner.canonicalName

                systemProperty('org.slf4j.simpleLogger.logFile', 'System.out')
                systemProperty('org.slf4j.simpleLogger.defaultLogLevel', 'info')

                doFirst {
                    assertArg(runnerClass && !runnerClass.isBlank(), "'runnerClass' must be set for apiator task")

                    args = [renderOutput.absolutePath, runnerClass]
                }
            })
        })
    }

    private Object configureDependencyConstraints(Project prj) {
        def apiatorVersion = ApiatorGradleRunner.package.implementationVersion

        if (!apiatorVersion) {
            prj.logger.lifecycle("DEV mode detected. Local Maven Repo was added. Version changed to {}", prj.version)
            prj.repositories.mavenLocal()
            apiatorVersion = prj.version
        }

        prj.repositories.mavenCentral()
        prj.dependencies.constraints.add('implementation', "com.ainrif.apiator:api:${apiatorVersion}")

        prj.configurations.maybeCreate('apiatorImplementation').with {
            def apiatorVersionConstraints = apiatorModules.collect {
                prj.dependencies.constraints.create(group: 'com.ainrif.apiator', name: it, version: apiatorVersion)
            }

            dependencyConstraints.addAll(apiatorVersionConstraints)

            dependencies.with {
                add(prj.dependencies.create("com.ainrif.apiator:utils-apiator-gradle-plugin"))
                add(prj.dependencies.create("org.slf4j:jcl-over-slf4j:1.7.26"))
                add(prj.dependencies.create("org.slf4j:log4j-over-slf4j:1.7.26"))
                add(prj.dependencies.create("org.slf4j:slf4j-simple:1.7.26"))
            }

            exclude(group: 'commons-logging')
            exclude(group: 'ch.qos.logback')
            exclude(group: 'org.apache.logging.log4j')
            exclude(group: 'org.slf4j', module: 'slf4j-log4j12')
        }
    }

    private void configureClasspath(Project prj, JavaPluginConvention javaPluginConvention, SourceSet apiatorSourceSet) {
        SourceSet mainSourceSet = javaPluginConvention.sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME)

        def apiatorCompile = prj.configurations.maybeCreate('apiatorCompileClasspath')
        def apiatorRuntime = prj.configurations.maybeCreate('apiatorRuntimeClasspath')

        apiatorSourceSet.compileClasspath = prj.objects.fileCollection().from(mainSourceSet.output, apiatorCompile)
        apiatorSourceSet.runtimeClasspath = prj.objects.fileCollection().from(mainSourceSet.output, apiatorSourceSet.output, apiatorRuntime)
    }
}
