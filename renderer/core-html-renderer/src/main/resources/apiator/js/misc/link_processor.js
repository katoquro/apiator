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

modulejs.define('link_processor', ['sidebar', 'utils'], function (sidebar, utils) {
    function enableCopyBtns() {
        const clipboard = new Clipboard('.copy-btn');
        clipboard.on('success', event => {
            location.hash = utils.parseLink(event.text).hash;
        });
        clipboard.on('error', function (event) {
            // todo show input with message 'Press Ctrl+C' (OS related)
        });
    }

    function enableDataLinks() {
        $(document).on('click', '[data-link]', function () {
            location.hash = $(this).attr('data-link');
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

    function restoreState() {
        hackToScrollToHashOnPageLoad();

        mainPageRouter(hashParserFactory());
        sidebarRouter(hashParserFactory());
    }

    function hackToScrollToHashOnPageLoad() {
        if (location.hash) {
            const hash = location.hash;
            location.hash = '';
            location.hash = hash;
        }
    }

    /**
     * Creates one of permalink parser from hash
     * Can be called multiple times because one of router can have fallback which set the new hash and it's OK
     * if rest routers will use new state
     *
     * @return {ZeroParser|PermalinkV1Parser}
     */
    function hashParserFactory() {
        const hash = location.hash;
        let parsed;
        if (/#?1\//.test(hash)) {
            parsed = new PermalinkV1Parser(hash);
        } else {
            parsed = new ZeroParser(hash);
        }
        return parsed;
    }

    /**
     * restores Main page state by url
     * @param {PermalinkV1Parser} parsed
     */
    function mainPageRouter(parsed) {
        if (!parsed.version()) {
            fallbackMainPageRouter(parsed.getPageLink())
        } else {
            //no-op
        }
    }

    function fallbackMainPageRouter(hash) {
        if (hash.startsWith('#')) {
            hash = hash.substr(1);
        }
        let parts = hash.split('/');
        let result = '';

        for (let i = parts.length - 1; i > 0; i--) {
            const idSuffix = `[id$="${_.join(parts, '/')}"]`;
            if (0 < $(idSuffix).length) {
                result = $(idSuffix).eq(0)
                    .attr('id');
            }
            parts = _.slice(parts, i);
        }

        if (!result) {
            console.warn(`Unexpected permalink: ${hash}`)
        } else {
            location.hash = result;
        }
    }

    /**
     * restores Sidebar state by url
     * @param {PermalinkV1Parser} parsed
     */
    function sidebarRouter(parsed) {
        if (!parsed.getPageLink()) {
            $('.api .api__toggle')
                .first()
                .closest('.api')
                .addClass('api_active')
        }

        if (!parsed.version()) {
            // no-op
        } else if (1 === parsed.version()) {
            if (parsed.isEndpointLink()) {
                sidebar.openGroupTitle($('.js_sidebar-title-endpoints'));
            }
            if (parsed.isModelLink()) {
                sidebar.openGroupTitle($('.js_sidebar-title-model'));
            }

            $('.sidebar span[data-link="' + parsed.getPageLink() + '"]')
                .closest('.api')
                .toggleClass('api_active')
                .get(0)
                .scrollIntoView(true);
        }
    }

    /**
     * Ex.:
     * #1/e/_post_/entities/new
     *
     * @param {string} _hash ready to parse hash
     * @constructor
     */
    function PermalinkV1Parser(_hash) {
        const PERMALINK_VERSION = 1;
        const hash = _hash;

        let
            source = hash,
            endpoint = false,
            model = false,
            pageUri = '';

        if (source.startsWith('#')) {
            source = source.substr(1);
        }

        if (source.startsWith('/')) {
            source = source.substr(1);
        }

        source = source.substr(`${PERMALINK_VERSION}`.length);

        if (source.startsWith('/e/')) {
            endpoint = true;
        } else if (source.startsWith('/m/')) {
            model = true;
        } else {
            throw Error('Unsupported Permalink format')
        }

        pageUri = source.substr(3);

        this.version = function () {
            return PERMALINK_VERSION;
        };

        this.getPageLink = function () {
            return hash;
        };

        this.isEndpointLink = function () {
            return endpoint;
        };

        this.isModelLink = function () {
            return model;
        };

        this.getPageUri = function () {
            return pageUri;
        };
    }

    /**
     * Empty parses for fallback cases
     *
     * @param {string} _hash ready to parse hash
     * @constructor
     */
    function ZeroParser(_hash) {
        const PERMALINK_VERSION = 0;
        const hash = _hash;

        this.version = function () {
            return PERMALINK_VERSION;
        };

        this.getPageLink = function () {
            return hash;
        };
    }

    return {
        run: function () {
            restoreState();
            enableCopyBtns();
            enableDataLinks();
        }
    }
});
