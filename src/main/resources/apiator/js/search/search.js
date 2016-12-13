/*
 * Copyright 2014-2016 Ainrif <ainrif@outlook.com>
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

modulejs.define('search', ['hbs', 'searcher', 'search_box'], function (hbs, Searcher) {

    var endpointsDataSet = _
        .chain(apiJson.apiContexts)
        .flatMap(function (context) {
            var apiPath = context.apiPath;
            return _.map(context.apiEndpoints, function (endpoint) {
                return {apiPath: apiPath, endpoint: endpoint}
            });
        })
        .map(function (it) {
            return {
                index: {
                    endpoint: it.apiPath + it.endpoint.path
                },
                payload: {
                    showAs: 'endpoint',
                    method: it.endpoint.method,
                    apiPath: it.apiPath,
                    path: it.endpoint.path
                }
            }
        })
        .value();

    var modelDataSet = _
        .chain(apiJson.usedApiTypes)
        .map(function (it) {
            return {
                index: {
                    model: it.type,
                    type: it.type
                },
                payload: {
                    showAs: 'model',
                    type: it.type,
                    simpleName: _.last(_.split(_.last(_.split(it.type, '.')), '$'))
                }
            }
        })
        .value();

    var enumDataSet = _
        .chain(apiJson.usedEnumerations)
        .map(function (it) {
            var typeNames = _.split(_.last(_.split(it.type, '.')), '$');
            return {
                index: {
                    enum: it.type
                },
                payload: {
                    showAs: 'enum',
                    type: it.type,
                    simpleName: _.last(typeNames),
                    enclosingType: 2 == typeNames.length ? typeNames[0] : null
                }
            }
        })
        .value();

    var searcher = new Searcher(apiJson, {})
        .addToDataSet(endpointsDataSet)
        .addToDataSet(modelDataSet)
        .addToDataSet(enumDataSet);

    var fuzzyTemplate = Handlebars.compile($("#fuzzy-response").html());

    $('#fuzzy-input').search_box({
        searchFunc: function (input) {
            return searcher.search(input, 'endpoint')
        },
        renderSuggestFunc: function (item) {
            return fuzzyTemplate({hash: item.payload})
        }
    });
});