/*
 * Copyright 2014-2015 Ainrif <ainrif@outlook.com>
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
package com.ainrif.gears.gradle.uidev

import org.gradle.api.Project
import org.gradle.api.logging.Logging
import org.gradle.tooling.*

public class GradleTaskExecutor {
    static final def logger = Logging.getLogger(GradleTaskExecutor)

    private final ProjectConnection connection

    public GradleTaskExecutor(Project project) {
        //todo connection from project
        connection = GradleConnector.newConnector()
                .useInstallation(project.gradle.gradleHomeDir)
                .forProjectDirectory(project.projectDir)
                .connect()
    }

    public void execute(String task, ResultHandler<Void> callback = null) {
        BuildLauncher launcher = connection
                .newBuild()
                .withArguments('-q')
                .setStandardOutput(System.out)
                .forTasks([task] as String[])

        try {
            callback ? launcher.run(callback) : launcher.run()
        } catch (BuildException e) {
            logger.error("Error on task execution", e)
        }
    }
}