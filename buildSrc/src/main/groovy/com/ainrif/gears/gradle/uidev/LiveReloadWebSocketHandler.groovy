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

import org.eclipse.jetty.util.ConcurrentArrayQueue
import org.eclipse.jetty.websocket.api.Session
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage
import org.eclipse.jetty.websocket.api.annotations.WebSocket
import org.eclipse.jetty.websocket.server.WebSocketHandler
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory
import org.gradle.api.logging.Logging

class LiveReloadWebSocketHandler extends WebSocketHandler {
    static final def logger = Logging.getLogger(LiveReloadWebSocketHandler)

    private static final Queue<LiveReloadWebSocket> sockets = new ConcurrentArrayQueue<>()

    public static void broadcast(String msg) throws Exception {
        sockets.each { socket ->
            try {
                socket.session.remote.sendString(msg)
            } catch (IOException e) {
                sockets.remove(socket)
                logger.error('', e)
            }
        }
    }

    @Override
    void configure(WebSocketServletFactory factory) {
        factory.register(LiveReloadWebSocket)
    }

    @WebSocket
    public static class LiveReloadWebSocket {
        protected Session session

        @OnWebSocketConnect
        public void onConnect(Session session) {
            if (!session.upgradeRequest.requestURI.path.endsWith('livereload')) {
                session.close()
            }
            this.session = session
            sockets.add(this)
        }

        @OnWebSocketClose
        public void onClose(int code, String message) {
            sockets.remove(this)
        }

        @OnWebSocketMessage
        public void onMessage(final String data) {
            try {
                if (LiveReloadProtocol.isHello(data)) {
                    session.remote.sendString(LiveReloadProtocol.hello())
                }
            } catch (Exception e) {
                logger.error('', e)
            }
        }
    }
}