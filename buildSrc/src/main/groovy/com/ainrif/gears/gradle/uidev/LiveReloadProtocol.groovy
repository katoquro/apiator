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

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

public class LiveReloadProtocol {

    public static String hello() {
        def root = new JsonBuilder()
        root {
            command 'hello'
            protocols(['http://livereload.com/protocols/official-7'])
            serverName 'gears-gradle-uiDev'
        }

        root.toString()
    }

    public static String alert(String msg) throws Exception {
        def root = new JsonBuilder()
        root {
            command 'alert'
            message msg
        }

        root.toString()
    }

    public static String reload(String filePath) throws Exception {
        def root = new JsonBuilder()
        root {
            command 'reload'
            path filePath
            liveCSS true
        }

        root.toString()
    }

    public static boolean isHello(String data) throws Exception {
        def json = new JsonSlurper().parseText(data)

        'hello' == json.command
    }

}