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

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

import java.nio.file.FileSystems
import java.nio.file.Path
import java.time.LocalTime

class UiDevTask extends DefaultTask {
    private static final int liveReloadPort = 35729

    String liveReloadRoot

    List<String> taskTriggerRoots
    String taskName
    boolean coldStart = true

    @TaskAction
    void action() {
        Map<Path, WatchServer.EventCallback> watchServerListeners = [:]

        if (taskTriggerRoots) {
            if (!taskName) {
                throw new RuntimeException("'taskName' is not defined")
            }

            def taskRunner = new GradleTaskExecutor(project)
            if (coldStart) {
                taskRunner.execute(taskName)
            }

            taskTriggerRoots
                    .collect { FileSystems.default.getPath(it) }
                    .each { watchServerListeners.put(it, { taskRunner.execute(taskName) }) }
        }

        def liveReloadPath = FileSystems.default.getPath(liveReloadRoot)
        def webServer = new LiveReloadServer(liveReloadPort, liveReloadPath)

        watchServerListeners.put(liveReloadPath, {
            logger.lifecycle "[${LocalTime.now()}] resource update : ${liveReloadPath}"

            String msg = LiveReloadProtocol.reload(liveReloadPath.toString())
            LiveReloadWebSocketHandler.broadcast(msg)
        })

        new WatchServer(watchServerListeners).start()

        logger.lifecycle "Start LiveReload Server for ${liveReloadRoot} at http://localhost:${liveReloadPort}/"
        webServer.start()
    }
}
