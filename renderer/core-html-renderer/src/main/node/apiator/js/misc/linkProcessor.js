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

import $ from 'jquery';
import _ from 'lodash';
import Clipboard from 'clipboard';

function enableCopyBtns() {
    const clipboard = new Clipboard('.copy-btn');
    clipboard.on('success', event => {});
    clipboard.on('error', function (event) {
        // todo show input with message 'Press Ctrl+C' (OS related)
    });
}

function enableDataLinks() {
    $(document).on('click', '[data-link]', event => {
        location.hash = $(event.currentTarget).attr('data-link');
    });

    $(window).on("hashchange", event => {
        const newHash = _.last(event.originalEvent.newURL.split('#'));
        const pageLinkTarget = document.querySelector(`[data-id='${newHash}']`);
        if (pageLinkTarget) {
            const content = $(".content");
            const target = $(pageLinkTarget);

            const heightElementAfterTarget = target.next().outerHeight();
            const heightElementAndTarget = target.outerHeight() + heightElementAfterTarget;

            const targetTop = pageLinkTarget.offsetTop;
            const elementBottom = targetTop + heightElementAndTarget;

            const viewportTop = content.scrollTop();
            const viewportBottom = viewportTop + $(window).height();

            const isWholeVisible = targetTop > viewportTop && elementBottom < viewportBottom;

            if (!isWholeVisible) {
                content.animate({
                    scrollTop: targetTop
                }, 700, 'swing');
            }
        }
    });
}


export function run() {
    enableCopyBtns();
    enableDataLinks();
}
