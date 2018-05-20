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

modulejs.define('searcher', [], function () {
    function Searcher(_options) {
        var WHITESPACE_REGEXP = /\s/g;

        var options = _.extend({
            hitsCount: 10
        }, _options);

        var dataSet = [];

        this.search = function (pattern, indexType) {
            pattern = normalizePattern(pattern);
            return _
                .chain(dataSet)
                .map(function (item) {
                    return {item: item, score: match(pattern, item.index[indexType])};
                })
                .filter(function (it) {
                    return 0 < it.score
                })
                .orderBy(['score'], ['desc'])
                .slice(0, options.hitsCount)
                .map('item')
                .value()
        };

        this.addToDataSet = function (_array) {
            dataSet = _.concat(dataSet, _array);
            return this;
        };

        function normalizePattern(pattern) {
            return pattern.replace(WHITESPACE_REGEXP, '')
        }

        function match(pattern, str) {
            if (!str) {
                return -1;
            }

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

    return Searcher;
});
