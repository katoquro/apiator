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

import _ from 'lodash';
import $ from 'jquery';
import { PermalinkV1Parser } from './PermalinkV1Parser';
import { ZeroParser } from './ZeroParser';

export class Router {
    constructor() {
        this.initialNavigate = true;
    }

    static __hackToScrollToHashOnPageLoad() {
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
     * @param {string} urlHash
     * @return {ZeroParser|PermalinkV1Parser}
     */
    static __hashParserFactory(urlHash) {
        let parsed;

        if (/#?1\//.test(urlHash)) {
            parsed = new PermalinkV1Parser(urlHash);
        } else {
            parsed = new ZeroParser(urlHash);
        }

        return parsed;
    }

    /**
     * restores Main page state by url
     * @param {PermalinkV1Parser} parsed
     */
    static __mainPageRouter(parsed) {
        if (!parsed.version()) {
            Router.__fallbackMainPageRouter(parsed.getPageLink());
        } else {
            // no-op
        }
    }

    static __fallbackMainPageRouter(hash) {
        if (hash.startsWith('#')) {
            hash = hash.substr(1);
        }
        let parts = hash.split('/');

        let result = '';

        for (let i = parts.length - 1; i > 0; i--) {
            const idSuffix = `[id$="${_.join(parts, '/')}"]`;

            if ($(idSuffix).length > 0) {
                result = $(idSuffix)
                    .eq(0)
                    .attr('id');
            }
            parts = _.slice(parts, i);
        }

        if (!result) {
            console.warn(`Unexpected permalink: ${hash}`);
        } else {
            location.hash = result;
        }
    }

    /**
     * restores Sidebar state by url
     * @param {PermalinkV1Parser} parsed
     */
    static __sidebarRouter(parsed) {
        if (!parsed.getPageLink()) {
            $('.api .api__toggle')
                .first()
                .closest('.api')
                .addClass('api_active');
        }

        if (!parsed.version()) {
            // no-op
        } else if (parsed.version() === 1) {
            if (parsed.isEndpointLink()) {
                sidebar.openGroupTitle($('.js_sidebar-title-endpoints'));
            }
            if (parsed.isModelLink()) {
                sidebar.openGroupTitle($('.js_sidebar-title-model'));
            }

            $(`.sidebar span[data-link="${parsed.getPageLink()}"]`)
                .closest('.api')
                .toggleClass('api_active')
                .get(0)
                .scrollIntoView(true);
        }
    }

    /**
     * @param {string} urlHash like  "#1/e/_get_/books/{isbn}"
     */
    navigate(urlHash) {
        if (this.initialNavigate) {
            Router.__hackToScrollToHashOnPageLoad();
            this.initialNavigate = false;
        }

        const parser = Router.__hashParserFactory(urlHash);

        Router.__mainPageRouter(parser);
        // Router.__sidebarRouter(parser);
    }
}

export const router = new Router();
