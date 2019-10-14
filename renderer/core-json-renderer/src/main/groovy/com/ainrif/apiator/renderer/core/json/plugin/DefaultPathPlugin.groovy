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

package com.ainrif.apiator.renderer.core.json.plugin

import com.ainrif.apiator.renderer.plugin.spi.path.PathPlugin

class DefaultPathPlugin implements PathPlugin {
    @Override
    String transform(String path) {
        return normalizePath(path)
    }

    static String normalizePath(String path) {
        def result = []

        for (def it in path.split('/')) {
            if (!it || it == '.') {
                continue
            }

            if (it != '..') {
                result.add(it)
            } else if (result.size()) {
                result.removeLast()
            }
        }

        return '/' + result.join('/')
    }
}
