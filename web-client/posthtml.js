/*
 * Copyright 2014-2019 Ainrif <support@ainrif.com>
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

'use strict';

const fs = require('fs');
const posthtml = require('posthtml');

let html = fs.readFileSync('./dist/index.html');

posthtml([
    require('posthtml-inline-assets')({
        errors: 'throw',
        root: './dist',
        cwd: './dist',
        transforms: {
            script: {
                transform(node, data) {
                    delete node.attrs.src;

                    if ('apiator-json-stub' === node.attrs.id) {
                        delete node.attrs.id;
                        node.content = ['<!--apiatorJson placeholder-->'];
                    } else {
                        node.content = [data.buffer.toString('utf8')];
                    }
                },
            },
        },
    }),
])
    .process(html)
    .then(result =>
        fs.writeFileSync(
            '../renderer/core-html-renderer/build/resources/main/apiator.min.html',
            result.html
        )
    );
