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

modulejs.define('search', ['utils', 'hbs', 'searcher', 'search_box'], function (utils, hbs, Searcher) {

    var endpointDataSet = _
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

    var bangDataSet = _
        .chain([
            ['endpoint', 'Search trough url addresses'],
            ['model', 'Search trough model & enum names']])
        .map(function (it) {
            return {
                index: {
                    bang: '!' + it[0]
                },
                payload: {
                    showAs: 'bang',
                    bang: '!' + it[0],
                    name: it[0],
                    description: it[1]
                }
            }
        })
        .value();

    var searcher = new Searcher({})
        .addToDataSet(endpointDataSet)
        .addToDataSet(modelDataSet)
        .addToDataSet(enumDataSet)
        .addToDataSet(bangDataSet);

    var fuzzyTemplate = Handlebars.compile($("#suggest-item").html());

    $('#fuzzy-input').search_box({
        searchFunc: function (input) {
            var query = _.trimStart(input);

            var pattern = query;
            var indexType = 'endpoint';
            if (query.startsWith('!')) {
                var rawBang = query.match(/^!\w*/g);
                if (_.isEmpty(rawBang)) {
                    return [];
                }
                var bang = rawBang[0];

                if (!/\s/.test(query)) {
                    return searcher.search(bang, 'bang');
                } else {
                    indexType = bang.substring(1);
                    pattern = query.replace(bang, '')
                }
            }

            return searcher.search(pattern, indexType);
        },
        renderSuggestFunc: function (item) {
            return fuzzyTemplate(item.payload)
        },
        onChangeFunc: function (changeEvent, box) {
            box.clearSuggest();
            var payload = changeEvent.item.payload;
            if ('bang' === payload.showAs) {
                box.getInput().val(payload.bang + ' ');
            } else if (changeEvent.key) {
                var navigateTo;
                switch (payload.showAs) {
                    case 'endpoint':
                        navigateTo = utils.getPageLinkToEndpoint(payload);
                        break;
                    case 'model':
                        navigateTo = utils.getPageLinkToType(payload.type);
                        break;
                    default:
                        throw new Error('Not supported payload type')
                }

                location.hash = navigateTo;
            }
        }
    });
});