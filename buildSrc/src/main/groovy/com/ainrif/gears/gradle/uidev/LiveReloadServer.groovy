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

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.ResourceHandler
import org.eclipse.jetty.util.resource.Resource
import org.gradle.api.logging.Logging

import java.nio.file.Path

class LiveReloadServer {
    static final def logger = Logging.getLogger(LiveReloadServer)

    private final int port
    private final Path resourceRoot
    private Server jettyServer

    LiveReloadServer(int port, Path resourceRoot) {
        this.port = port
        this.resourceRoot = resourceRoot
    }

    private void init() throws Exception {
        def resHandler = new ResourceHandler() {
            @Override
            Resource getResource(String path) throws MalformedURLException {
                if ('/livereload.js' == path) {
                    return Resource.newResource(LiveReloadServer.class.getResource(path))
                }

                return super.getResource(path)
            }
        }
        resHandler.resourceBase = resourceRoot.toString()
        resHandler.directoriesListed = true
        resHandler.welcomeFiles = ['index.html'] as String[]

        def wsHandler = new LiveReloadWebSocketHandler()
        wsHandler.handler = resHandler

        jettyServer = new Server(port)
        jettyServer.handler = wsHandler
    }

    void start() throws Exception {
        this.init()

        try {
            jettyServer.start()

            jettyServer.join()
        } catch (Throwable t) {
            logger.error('', t)
        } finally {
            jettyServer.stop()
        }
    }
}
