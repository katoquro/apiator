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

import Clipboard from 'clipboard';

function enableCopyBtns() {
    const clipboard = new Clipboard('.copy-btn');

    clipboard.on('error', function(event) {
        // todo show input with message 'Press Ctrl+C' (OS related)
    });
}

function enableDataLinks() {
    document.addEventListener('click', event => {
        if (event.target.getAttribute('data-link')) {
            location.hash = event.target.getAttribute('data-link');
        }
    });

    window.addEventListener('hashchange', event => {
        const content = document.getElementsByClassName('content')[0];
        const newHash = event.newURL.split('#').pop();
        const pageLinkTarget = document.querySelector(`[data-id='${newHash}']`);

        if (pageLinkTarget) {
            const heightElementAfterTarget =
                pageLinkTarget.nextElementSibling.offsetHeight;
            const heightElementAndTarget =
                heightElementAfterTarget + pageLinkTarget.offsetHeight;

            const targetTop = pageLinkTarget.offsetTop;
            const elementBottom = targetTop + heightElementAndTarget;

            const viewportTop = content.scrollTop;
            const viewportBottom = viewportTop + window.outerHeight;

            const isWholeVisible =
                targetTop > viewportTop && elementBottom < viewportBottom;

            if (!isWholeVisible) {
                content.scrollTo({
                    top: targetTop,
                    behavior: 'smooth',
                });
            }
        }
    });
}

export function run() {
    enableCopyBtns();
    enableDataLinks();
}
