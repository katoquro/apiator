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

const WHITESPACE_REGEXP = /\s/g;

export default function Searcher(_options) {
    const options = {
        hitsCount: 10,
        ..._options,
    };

    let dataSet = [];

    this.search = function(pattern, indexType) {
        pattern = normalizePattern(pattern);

        return dataSet
            .map(item => ({
                item,
                score: match(pattern, item.index[indexType]),
            }))
            .filter(it => it.score > 0)
            .sort((a, b) => b - a)
            .slice(0, options.hitsCount)
            .map(item => item.item);
    };

    this.addToDataSet = function(_array) {
        dataSet = [...dataSet, ..._array];

        return this;
    };

    function normalizePattern(pattern) {
        return pattern.replace(WHITESPACE_REGEXP, '');
    }

    function match(pattern, str) {
        if (!str) {
            return -1;
        }

        let score = 0;

        let pi = 0;

        let si = 0;

        let prev_si = -1;

        while (pi < pattern.length && si < str.length) {
            if (pattern[pi].toLowerCase() === str[si].toLowerCase()) {
                // chars distance
                score += 1 / (si - prev_si + 1);

                // the same letter case
                if (pattern[pi] === str[si]) {
                    score += 0.5;
                }

                prev_si = si;
                pi++;
            }

            si++;
        }

        return pi === pattern.length ? score : -1;
    }
}
