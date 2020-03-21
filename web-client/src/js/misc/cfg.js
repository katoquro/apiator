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

/**
 * @typedef {Object} LocalStoredInfo
 * @property {string} version
 */

const apiatorInfo = apiatorJson.apiatorInfo;

/**
 * @method
 * @return {LocalStoredInfo}
 */
const read = function() {
    return JSON.parse(localStorage.getItem('apiatorCfg') || '{}');
};

/**
 * @method
 * @param {!LocalStoredInfo} info
 */
const save = function(info) {
    if (!info) throw Error('info cannot be empty');
    localStorage.setItem('apiatorCfg', JSON.stringify(info));
};

/**
 * @method
 * @return {boolean} true if stored and apiator version are different
 */
export function isUpdated() {
    return apiatorInfo.version != read().version;
}

export function markUpdated() {
    const info = read();

    info.version = apiatorInfo.version;
    save(info);
}
