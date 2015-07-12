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

import org.gradle.api.logging.Logging

import java.nio.file.*
import java.time.LocalTime
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

import static com.sun.nio.file.SensitivityWatchEventModifier.HIGH
import static java.nio.file.StandardWatchEventKinds.*

class WatchServer {
    static final def WATCHED_EVENTS = [ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY] as WatchEvent.Kind<Path>[]

    def logger = Logging.getLogger(getClass())

    private final AtomicBoolean running = new AtomicBoolean(false)
    private final ExecutorService threadPool

    private Map<WatchKey, WatchUnit> watched

    public WatchServer(Map<Path, EventCallback> listeners) {
        this.threadPool = Executors.newFixedThreadPool(listeners.size())

        this.watched = listeners.collectEntries { path, callback ->
            def watchService = path.fileSystem.newWatchService()
            def watchKey = path.register(watchService, WATCHED_EVENTS, HIGH)

            def watchUnit = new WatchUnit(
                    path: path,
                    watchKey: watchKey,
                    watchService: watchService,
                    callback: callback)

            [watchKey, watchUnit]
        }

        logger.lifecycle "${listeners.size()} file watch listener${listeners.size() > 1 ? 's were' : ' was'} configured"
    }

    void start() throws Exception {
        if (!running.compareAndSet(false, true)) {
            logger.warn "WatchServer is already running"
            return;
        }

        try {
            watched.each {
                def wUnit = it.value
                threadPool.submit({
                    logger.info "Running file watch for ${wUnit.path}"
                    try {
                        while (running.get()) {
                            WatchKey key = wUnit.watchService.take()

                            WatchEvent<Path> event = key.pollEvents()
                                    .find { Path == it.kind().type() }

                            if (event) {
                                logger.lifecycle "[${LocalTime.now()}] File watch notification for ${event.context()}"
                                wUnit.callback.call(event.kind())
                            }

                            boolean valid = key.reset()
                            if (!valid) {
                                logger.warn "File watch root was removed ${wUnit.path}"
                            }
                        }
                    } catch (ClosedWatchServiceException e) {
                        logger.info "File watch stopped for ${wUnit.path}"
                    }
                } as Runnable)
            }
        } finally {
            threadPool.shutdown()
        }
    }

    void stop() throws Exception {
        running.set(false) // todo think about lock fow auto-restart
        watched.each { it.value.watchService.close() }
    }

    static interface EventCallback {
        void call(WatchEvent.Kind<Path> event)
    }

    static class WatchUnit {
        Path path
        WatchKey watchKey
        WatchService watchService
        EventCallback callback
    }
}
