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

modulejs.define('fuzzySearch', ['hbsHelpers'], function (helpers) {
    var fuseDictionary = {};
    var urlBanger = _.compose(_.flatten, _.map);
    fuseDictionary.url = urlBanger(apiJson.apiContexts, mapApiContexts);

    function mapApiContexts(apiContext) {
        return apiContext.apiEndpoints.map(mapApiEndpoints, apiContext);
    }

    function mapApiEndpoints(apiEndpoint) {
        var miniApiEndpoint = _.pick(apiEndpoint, [
            "method",
            "name",
            "params",
            "path"
        ]);
        miniApiEndpoint.path = this.apiPath + miniApiEndpoint.path;
        return miniApiEndpoint;
    }

    var fuseOptions = {
        keys: ['name', 'path', 'method'],
        caseSensitive: true // keys to search in
    };

    var fuse = new Fuse(fuseDictionary.url, fuseOptions);

    $('html').click(function (event) {
        if (!$(event.target).is('#fuzzy-suggest, #fuzzy-input')) {
            $('#fuzzy-suggest').hide();
        }
    });

    $('#fuzzy-suggest').on('click', 'li', function () {
        $('#fuzzy-suggest').hide();
    });

    var fuzzyTemplate = Handlebars.compile($("#fuzzy-response").html());

    $('#fuzzy-input').on('keyup click', function () {
        var that = $(this);
        if (2 > that.val().length) return;

        var hits = fuse.search(that.val()).slice(0, 10);
        var suggestItems = hits.map(function (hit) {
            return $(fuzzyTemplate({
                    hit: hit,
                    hash: {
                        apiPath: '',
                        method: hit.method,
                        path: hit.path
                    }
                })
            );
        });

        var suggestMenu = $('#fuzzy-suggest');
        suggestMenu.children()
            .remove()
            .end()
            .append(suggestItems)
            .show();
    });
});