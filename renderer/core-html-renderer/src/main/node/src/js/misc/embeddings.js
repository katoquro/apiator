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

import $ from 'jquery';
import fontAwesomeBase64 from 'font-awesome/fonts/fontawesome-webfont.woff';
import faviconBase64 from '/bw_favicon.png';

function font(fi) {
    const html = `
<style>    
    @font-face {
        font-family: 'FontAwesome';
    
        src: url('data:application/font-woff;charset=utf-8;base64,${fi}') format('woff');
    
        font-weight: normal;
        font-style: normal;
    }
</style>`;

    $('head').append(html);
}

function favicon(icon) {
    const html = `<link rel="icon" type="image/png" href="data:image/png;base64,${icon}">`;

    $('head').append(html);
}

function development() {
    if (process.env.NODE_ENV === 'development') {
        window.windowForDemo = () => {
            window.open(document.location, '', 'width=1100, height=800');
        };
    }
}

export default function embed() {
    font(fontAwesomeBase64);
    favicon(faviconBase64);
    development();
}
