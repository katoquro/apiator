/*
 * Copyright 2014-2017 Ainrif <support@ainrif.com>
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

modulejs.define('link_processor', [], function () {
    function runCopyBtnProcessor() {
        var clipboard = new Clipboard('.copy-btn');
        clipboard.on('success', function (event) {
            // todo navigation to item in sidebar
            // todo tooltip about coping
        });
        clipboard.on('error', function (event) {
            // todo show input with message 'Press Ctrl+C' (OS related)
        });
    }

    return {
        run: function () {
            // todo navigation to item in sidebar
            runCopyBtnProcessor();
        }
    }
});
