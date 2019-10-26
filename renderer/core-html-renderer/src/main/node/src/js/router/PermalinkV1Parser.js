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

const PERMALINK_VERSION = 1;

/**
 * Ex.:
 * #1/e/_post_/entities/new
 *
 * @param {string} _hash ready to parse hash
 * @constructor
 */
export class PermalinkV1Parser {
    constructor(_hash) {
        this.hash = _hash;
        this.source = this.hash;
        this.endpoint = false;
        this.model = false;
        this.pageUri = '';

        if (this.source.startsWith('#')) {
            this.source = this.source.substr(1);
        }

        if (this.source.startsWith('/')) {
            this.source = this.source.substr(1);
        }

        this.source = this.source.substr(`${PERMALINK_VERSION}`.length);

        if (this.source.startsWith('/e/')) {
            this.endpoint = true;
        } else if (this.source.startsWith('/m/')) {
            this.model = true;
        } else {
            throw Error('Unsupported Permalink format');
        }

        this.pageUri = this.source.substr(3);
    }

    version() {
        return PERMALINK_VERSION;
    }

    getPageLink() {
        return this.hash;
    }

    isEndpointLink() {
        return this.endpoint;
    }

    isModelLink() {
        return this.model;
    }

    getPageUri() {
        return this.pageUri;
    }
}
