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

package com.ainrif.apiator.renderer.plugin.micronaut

import spock.lang.Specification

class MicronautPathPluginSpec extends Specification {
    def plugin = new MicronautPathPlugin()

    def "path must contain only part before query params"() {
        expect:
        plugin.transform(uri) == expectedPath

        where:
        uri                   || expectedPath
        '/books/{id}'         || '/books/{id}'
        '/books?max'          || '/books'
        '/books{?max,offset}' || '/books'
    }
}
