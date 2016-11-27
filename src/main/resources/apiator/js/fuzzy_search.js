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

modulejs.define('fuzzySearch', ['hbs'], function (hbs) {

    function Searcher(rawDataSet, options) {
        var WHITESPACE_REGEXP = /\s/g;

        this.options = _.extend({
            hitsCount: 10
        }, options);

        this.dataSet = _
            .chain(rawDataSet)
            .flatMap(function (context) {
                var apiPath = context.apiPath;
                return _.map(context.apiEndpoints, function (endpoint) {
                    return {apiPath: apiPath, endpoint: endpoint}
                });
            })
            .map(function (it) {
                return {
                    index: {
                        path: it.apiPath + it.endpoint.path
                    },
                    payload: {
                        method: it.endpoint.method,
                        apiPath: it.apiPath,
                        path: it.endpoint.path
                    }
                }
            })
            .value();

        this.search = function (pattern) {
            pattern = normalizePattern(pattern);
            return _
                .chain(this.dataSet)
                .map(function (item) {
                    return {item: item, score: match(pattern, item.index.path)};
                })
                .filter(function (it) {
                    return 0 < it.score
                })
                .orderBy(['score'], ['desc'])
                .slice(0, this.options.hitsCount)
                .map('item')
                .value()
        };

        function normalizePattern(pattern) {
            return pattern.replace(WHITESPACE_REGEXP, '')
        }

        function match(pattern, str) {
            var score = 0;
            var pi = 0;
            var si = 0;
            var prev_si = -1;

            while (pi < pattern.length && si < str.length) {
                if (pattern[pi].toLowerCase() === str[si].toLowerCase()) {
                    // chars distance
                    score += 1 / (si - prev_si + 1);

                    // same case
                    if (pattern[pi] === str[si]) {
                        score += 0.5;
                    }

                    prev_si = si;
                    pi++;
                }

                si++;
            }

            return (pi === pattern.length) ? score : -1
        }
    }

    var searcher = new Searcher(apiJson.apiContexts, {});

    var fuzzyTemplate = Handlebars.compile($("#fuzzy-response").html());

    $('html').click(function (event) {
        if (!$(event.target).is('#fuzzy-suggest, #fuzzy-input')) {
            $('#fuzzy-suggest').hide();
        }
    });

    $('#fuzzy-suggest').on('click', 'li', function () {
        $('#fuzzy-suggest').hide();
    });

    $('#fuzzy-input').on('keyup click', function () {
        var that = $(this);
        if (2 > that.val().length) return;

        var hits = searcher.search(that.val());

        var suggestContent;
        if (_.isEmpty(hits)) {
            suggestContent = $('<li style="margin: 3px 10px">There are no items for such params ¯\\_(ツ)_/¯</li>')
        } else {
            suggestContent = hits.map(function (hit) {
                return $(fuzzyTemplate({hash: hit.payload}));
            });
        }

        var suggestMenu = $('#fuzzy-suggest');
        suggestMenu.children()
            .remove()
            .end()
            .append(suggestContent)
            .show();
    });
});