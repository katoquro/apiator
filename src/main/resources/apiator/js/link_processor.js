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

modulejs.define('link_processor', ['sidebar'], function (sidebar) {
    function enableCopyBtns() {
        const clipboard = new Clipboard('.copy-btn');
        clipboard.on('success', function (event) {
            // todo tooltip about copying
        });
        clipboard.on('error', function (event) {
            // todo show input with message 'Press Ctrl+C' (OS related)
        });

    }

    function enableDataLinks() {
        $('[data-link]').on('click', function () {
            location.hash = $(this).attr('data-link');
        })
    }

    function restoreState() {
        hackToScrollToHash();

        const hash = location.hash;
        let parsed;
        if (/#?1\//.test(hash)) {
            parsed = new PermalinkV1Parser(hash);
        }

        mainPageRouter(hash, parsed);
        sidebarRouter(hash, parsed);
    }

    function hackToScrollToHash() {
        if (location.hash) {
            const hash = location.hash;
            location.hash = '';
            location.hash = hash;
        }
    }

    /**
     * restores Main page state by url
     * @param {string} hash
     * @param {PermalinkV1Parser} parsed
     */
    function mainPageRouter(hash, parsed) {
        if (!parsed) {
            fallbackMainPageRouter(hash)
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
            console.log(parts);
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
     * @param {string} hash
     * @param {PermalinkV1Parser} parsed
     */
    function sidebarRouter(hash, parsed) {
        if (!hash) {
            $('.api .api__toggle')
                .first()
                .closest('.api')
                .addClass('api_active')
        }

        if (!parsed) {
            // no-op
        } else if (1 == parsed.version) {
            if (parsed.isEndpointLink()) {
                sidebar.openGroupTitle($('.js_sidebar-title-endpoints'));
            }
            if (parsed.isModelLink()) {
                sidebar.openGroupTitle($('.js_sidebar-title-model'));
            }

            console.log('.sidebar span[data-link="' + parsed.getPageLink() + '"]');

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
     * @param {string} _pageLink ready to parse hash
     * @constructor
     */
    function PermalinkV1Parser(_pageLink) {
        const PERMALINK_VERSION = 1;

        let
            pageLink = _pageLink,
            source = _pageLink,
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
            return pageLink;
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

    return {
        run: function () {
            restoreState();
            enableCopyBtns();
            enableDataLinks();
        }
    }
});
