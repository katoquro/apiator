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

modulejs.define('utils', function () {

    const PERMALINK_VERSION = 1;

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
     * generates id value for page navigation to endpoint
     * @param {EndpointData} data
     * @returns {string} value for {@code id} attribute
     */
    this.getIdForTargetMarkerOfEndpoint = function (data) {
        return PERMALINK_VERSION + '/e/' + '_' + data.method.toLowerCase() + '_' + data.apiPath + data.path;
    };

    /**
     * @param {EndpointData} data
     * @returns {string}
     */
    this.getPageLinkToEndpoint = function (data) {
        return '#' + getIdForTargetMarkerOfEndpoint(data)
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
     * generates id value for page navigation to model
     * @param {string} type - full type name
     * @returns {string} value for {@code id} attribute
     */
    this.getIdForTargetMarkerOfModel = function (type) {
        return PERMALINK_VERSION + '/m/' + getAfterLastDot(type);
    };

    /**
     * @param {string} type - full type name
     * @return {string}
     */
    this.getPageLinkToType = function (type) {
        return '#' + getIdForTargetMarkerOfModel(type)
    };

    /**
     * @param {string} type - full type name
     * @return {string}
     */
    this.getAbsoluteLinkToType = function (type) {
        var prefix = location.origin + location.pathname + location.search;
        return prefix + getPageLinkToType(type);
    };


    /**
     * Split Camel Case string into separate words and keep case of letters
     *
     * @param {string} string
     * @return {string}
     */
    this.splitCamelCase = function (string) {
        return string.replace(/[A-Z]/g, function (letter, index) {
            if (0 != index) {
                return ' ' + letter;
            }
            return letter;
        });
    };

    return this;
});