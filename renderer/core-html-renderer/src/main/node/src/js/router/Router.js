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

import { PermalinkV1Parser } from './PermalinkV1Parser';
import { ZeroParser } from './ZeroParser';

export class Router {
    constructor() {
        this.initialNavigate = true;
    }

    __hackToScrollToHashOnPageLoad() {
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
    __hashParserFactory(urlHash) {
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
    __mainPageRouter(parsed) {
        if (!parsed.version()) {
            this.__fallbackMainPageRouter(parsed.getPageLink());
        }
    }

    __fallbackMainPageRouter(hash) {
        location.hash = '';
    }

    /**
     * @param {string} urlHash like  "#1/e/_get_/books/{isbn}"
     */
    navigate(urlHash) {
        if (this.initialNavigate) {
            this.__hackToScrollToHashOnPageLoad();
            this.initialNavigate = false;
        }

        const parser = this.__hashParserFactory(urlHash);

        this.__mainPageRouter(parser);
    }
}

export const router = new Router();
