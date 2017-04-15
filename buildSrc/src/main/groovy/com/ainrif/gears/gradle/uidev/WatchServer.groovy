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
package com.ainrif.gears.gradle.uidev

import org.gradle.api.logging.Logging

import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import java.time.LocalTime
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

import static com.sun.nio.file.SensitivityWatchEventModifier.HIGH
import static java.nio.file.Files.isDirectory
import static java.nio.file.LinkOption.NOFOLLOW_LINKS
import static java.nio.file.StandardWatchEventKinds.*

class WatchServer {
    static final def logger = Logging.getLogger(WatchServer)

    static final def WATCHED_EVENTS = [ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY] as WatchEvent.Kind<Path>[]

    private final AtomicBoolean running = new AtomicBoolean(false)
    private final ExecutorService threadPool = Executors.newSingleThreadExecutor()
    private final WatchService watchService

    private Map<WatchKey, WatchUnit> watched = new HashMap<>()

    WatchServer(Map<Path, EventCallback> listeners) {
        this.watchService = FileSystems.default.newWatchService()

        listeners.each(this.&registerNestedTree)

        logger.lifecycle "${listeners.size()} file watch listener${listeners.size() > 1 ? 's were' : ' was'} configured"
    }

    void registerNestedTree(Path root, EventCallback callback) {
        def watchedCount = watched.size()

        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                def watchKey = dir.register(watchService, WATCHED_EVENTS, HIGH)

                def watchUnit = new WatchUnit(
                        path: dir,
                        watchKey: watchKey,
                        callback: callback)

                watched.put(watchKey, watchUnit)

                return FileVisitResult.CONTINUE
            }
        })

        logger.debug("added ${watched.size() - watchedCount} new keys after scan")
    }

    void start() throws Exception {
        if (!running.compareAndSet(false, true)) {
            logger.warn "WatchServer is already running"
            return
        }

        try {
            threadPool.submit({
                logger.info "Running Watch Server thread"
                try {
                    while (running.get()) {
                        WatchKey key = watchService.take()

                        WatchEvent<Path> event = key.pollEvents()
                                .find { Path == it.kind().type() }

                        def wUnit = watched[key]
                        if (event) {
                            def kind = event.kind()
                            def eventPath = wUnit.path.resolve(event.context())

                            logger.lifecycle "[${LocalTime.now()}] File watch notification for : ${eventPath}"

                            if (isDirectory(eventPath, NOFOLLOW_LINKS)) {
                                if (ENTRY_CREATE == kind) {
                                    registerNestedTree(eventPath, wUnit.callback)
                                }
                            } else {
                                wUnit.callback.call(kind)
                            }
                        }

                        boolean valid = key.reset()
                        if (!valid) {
                            logger.warn "File watch root was removed : ${wUnit.path}"
                            logger.info "Evict ${wUnit.path} from Watched"
                            watched.remove(key)
                        }
                    }
                } catch (ClosedWatchServiceException e) {
                    logger.info "Access to Stopped Watch Server"
                }
            } as Runnable)
        } finally {
            threadPool.shutdown()
        }
    }

    void stop() throws Exception {
        running.set(false) // todo think about lock for auto-restart

        logger.info "Stopping Watch Server"
        watchService.close()
    }

    static interface EventCallback {
        void call(WatchEvent.Kind<Path> event)
    }

    static class WatchUnit {
        Path path
        WatchKey watchKey
        EventCallback callback
    }
}
