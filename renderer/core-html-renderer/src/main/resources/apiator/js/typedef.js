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

/**
 * @namespace Apiator
 */

/**
 * @typedef {Object} Apiator.ApiScheme
 *
 * @property {Apiator.ApiatorInfo} apiatorInfo -
 * @property {Apiator.ClientApiInfo} clientApiInfo -
 * @property {Apiator.ApiContext[]} apiContexts -
 * @property {Apiator.ApiType[]} usedApiTypes -
 * @property {Apiator.ApiEnumerations[]} usedEnumerations -
 */

/**
 * @typedef {Object} Apiator.ApiatorInfo
 *
 * @property {string} artifactId -
 * @property {string} groupId -
 * @property {string} version -
 */

/**
 * @typedef {Object} Apiator.ClientApiInfo
 *
 * @property {string} basePath -
 * @property {string} version -
 */

/**
 * @typedef {Object} Apiator.ApiContext
 *
 * @property {Apiator.ApiEndpoint[]} apiEndpoints -
 * @property {string} apiPath -
 * @property {string} description -
 * @property {string} name -
 */

/**
 * @typedef {Object} Apiator.ApiType
 * @augments Apiator.BasicType
 *
 * @property {Apiator.ApiTypeField[]} fields -
 */

/**
 * @typedef {Object} Apiator.BasicType
 *
 * @property {string} type -
 * @property {Apiator.ModelType} modelType -
 */

/**
 * @typedef {Object} Apiator.ApiTypeField
 * @augments Apiator.BasicType
 *
 * @property {Apiator.ApiTypeField} basedOn -
 * @property {string} description -
 * @property {string} name -
 * @property {boolean} readable -
 * @property {boolean} writable -
 */

/**
 * @typedef {Object} Apiator.ApiEnumerations
 * @augments Apiator.BasicType
 *
 * @property {string[]} values -
 */

/**
 * @typedef {Object} Apiator.ApiEndpoint
 *
 * @property {string} method -
 * @property {string} name -
 * @property {string} path -
 * @property {Apiator.ApiEndpointParam[]} params -
 * @property {Apiator.EndpointType} returnType -
 */

/**
 * @typedef {Object} Apiator.ApiEndpointParam
 *
 * @property {string} name -
 * @property {string} description -
 * @property {number} index -
 * @property {string} defaultValue -
 * @property {Apiator.ApiEndpointParamType} httpParamType -
 */

/**
 * @typedef {Object} Apiator.EndpointType
 * @augments Apiator.BasicType
 *
 * @property {Apiator.EndpointType[]} basedOn -
 * @property {string} templateName -
 */

/**
 * @enum {string}
 * @memberOf Apiator
 * @readonly
 */
const ModelType = Object.freeze({
    ANY: 'ANY',
    OBJECT: 'OBJECT',
    ENUMERATION: 'ENUMERATION',
    DICTIONARY: 'DICTIONARY',
    ARRAY: 'ARRAY',
    SET: 'SET',
    VOID: 'VOID',
    BOOLEAN: 'BOOLEAN',
    BYTE: 'BYTE',
    INTEGER: 'INTEGER',
    LONG: 'LONG',
    FLOAT: 'FLOAT',
    DOUBLE: 'DOUBLE',
    STRING: 'STRING',
    DATE: 'DATE',
    BINARY: 'BINARY',
    SYSTEM: 'SYSTEM'
});

/**
 * @enum {string}
 * @memberOf Apiator
 * @readonly
 */
const ApiEndpointParamType = Object.freeze({
    PATH: 'PATH',
    QUERY: 'QUERY',
    HEADER: 'HEADER',
    COOKIE: 'COOKIE',
    FORM: 'FORM',
    BODY: 'BODY'
});
