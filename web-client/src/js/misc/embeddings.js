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

import favicon from '../../../favicon.png';

function development() {
    if (process.env.NODE_ENV === 'development') {
        window.windowForDemo = () => {
            window.open(document.location, '', 'width=1100, height=800');
        };
    }
}

function insertFavicon(icon) {
    const html = `<link rel="icon" type="image/png" href="data:image/png;base64,${icon}">`;
    const element = document.createElement('div');

    element.innerHTML = html;

    document.getElementsByTagName('head')[0].append(element.children[0]);
}

export default function embed() {
    insertFavicon(favicon);
    development();
}
