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

const PERMALINK_VERSION = 1;

/**
 * returns substring after last dot, example:
 * a.b.c -> c
 * a -> a
 * @param {string} dotSeparatedString
 * @returns {string}
 */
const getAfterLastDot = function(dotSeparatedString) {
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
const getIdForTargetMarkerOfEndpoint = function(data) {
    return (
        `${PERMALINK_VERSION}/e/` +
        `_${data.method.toLowerCase()}_${data.apiPath}${data.path}`
    );
};

/**
 * @param {EndpointData} data
 * @returns {string}
 */
const getPageLinkToEndpoint = function(data) {
    return `#${getIdForTargetMarkerOfEndpoint(data)}`;
};

/**
 * @param {EndpointData} data
 * @returns {string}
 */
const getAbsoluteLinkToEndpoint = function(data) {
    const prefix = location.origin + location.pathname + location.search;

    return prefix + getPageLinkToEndpoint(data);
};

/**
 * generates id value for page navigation to model
 * @param {string} type - full type name
 * @returns {string} value for {@code id} attribute
 */
const getIdForTargetMarkerOfModel = function(type) {
    return `${PERMALINK_VERSION}/m/${getAfterLastDot(type)}`;
};

/**
 * @param {string} type - full type name
 * @return {string}
 */
const getPageLinkToType = function(type) {
    return `#${getIdForTargetMarkerOfModel(type)}`;
};

/**
 * @param {string} type - full type name
 * @return {string}
 */
const getAbsoluteLinkToType = type => {
    const prefix = location.origin + location.pathname + location.search;

    return prefix + getPageLinkToType(type);
};

/**
 * Split Camel Case string into separate words and keep case of letters
 *
 * @param {string} string
 * @return {string}
 */
const splitCamelCase = function(string) {
    return string.replace(/[A-Z]/g, function(letter, index) {
        if (index !== 0) {
            return ` ${letter}`;
        }

        return letter;
    });
};

const renderTemplate = type => {
    let result = '';

    if (type.type) {
        result += `<a data-link="${getPageLinkToType(
            type.type
        )}" class="type-view__model">${getAfterLastDot(type.type)}</a>`;
    } else {
        result += `<div class="type-view__modeltype">${type.modelType}</div>`;
    }

    if (type.basedOn.length) {
        result += ' of (';

        type.basedOn.forEach(it => {
            result += renderTemplate(it);
            result += ', ';
        });

        result = result.slice(0, -2);
        result += ')';
    }

    return result;
};

const highlightJSON = json => {
    if (typeof json !== 'string') {
        json = JSON.stringify(json, undefined, 4);
    }

    json = json
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;');

    return json
        .replace(
            /("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g,
            function(match) {
                let cls = 'json__value_number';

                if (/^"/.test(match)) {
                    if (/:$/.test(match)) {
                        cls = 'json__key';
                    } else {
                        cls = 'json__value_string';
                    }
                } else if (/true|false/.test(match)) {
                    cls = 'json__value_boolean';
                } else if (/null/.test(match)) {
                    cls = 'null';
                }

                return `<span class="${cls}">${match}</span>`;
            }
        )
        .replace(/(\r\n|\n|\r)/gm, '<br>');
};

export {
    getAfterLastDot,
    getIdForTargetMarkerOfEndpoint,
    getPageLinkToEndpoint,
    getAbsoluteLinkToEndpoint,
    getIdForTargetMarkerOfModel,
    getPageLinkToType,
    getAbsoluteLinkToType,
    splitCamelCase,
    renderTemplate,
    highlightJSON,
};
