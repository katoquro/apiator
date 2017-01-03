/*
 * Copyright 2014-2016 Ainrif <support@ainrif.com>
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

modulejs.define('utils', function () {

    /**
     * returns substring after last dot, example:
     * a.b.c -> c
     * a -> a
     * @param {string} dotSeparatedString
     * @returns {string}
     */
    this.getAfterLastDot = function (dotSeparatedString) {
        if (dotSeparatedString && typeof dotSeparatedString === 'string') {
            return dotSeparatedString.split('.').slice(-1)[0];
        }

        return '';
    };

    /**
     * Data type for endpoint link generator
     * @typedef {Object} EndpointData
     * @property {string} method - http method
     * @property {string} apiPath - path to api context
     * @property {string} path - relative path from apiPath
     */

    /**
     * @param {EndpointData} data
     * @returns {string}
     */
    this.getTargetMarkerOfEndpoint = function (data) {
        return '_' + data.method.toLowerCase() + '_' + data.apiPath + data.path;
    };

    /**
     * @param {EndpointData} data
     * @returns {string}
     */
    this.getPageLinkToEndpoint = function (data) {
        return '#' + getTargetMarkerOfEndpoint(data)
    };

    /**
     * @param {EndpointData} data
     * @returns {string}
     */
    this.getAbsoluteLinkToEndpoint = function (data) {
        var prefix = location.origin + location.pathname + location.search;
        return prefix + getPageLinkToEndpoint(data);
    };

    /**
     * @param {string} type - full type name
     * @return {string}
     */
    this.getTargetMarkerOfType = getAfterLastDot;

    /**
     * @param {string} type - full type name
     * @return {string}
     */
    this.getPageLinkToType = function (type) {
        return '#' + getTargetMarkerOfType(type)
    };

    /**
     * @param {string} type - full type name
     * @return {string}
     */
    this.getAbsoluteLinkToType = function (type) {
        var prefix = location.origin + location.pathname + location.search;
        return prefix + getPageLinkToType(type);
    };

    return this;
});