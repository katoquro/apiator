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
package com.ainrif.apiator

import com.ainrif.apiator.writer.core.CoreHtmlRenderer

class UiDevGenerator {
    public static void main(String[] args) {
        def htmlPath = "${args[0]}/site/api.html"
        def jsonStubPath = "${args[0]}/resources/stub.json"

        def json = new File(jsonStubPath).text

        new CoreHtmlRenderer(htmlPath).renderTemplate(json)
    }
}

