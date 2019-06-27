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

const PERMALINK_VERSION = 0;

/**
 * Empty parses for fallback cases
 *
 * @param {string} _hash ready to parse hash
 * @constructor
 */
export class ZeroParser {
    constructor(_hash) {
        this.hash = _hash
    }

    version() {
        return PERMALINK_VERSION;
    };

    getPageLink() {
        return this.hash;
    };
}
