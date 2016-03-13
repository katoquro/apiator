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
    function toTitleCase(str) {
        return str.replace(/\w+/g, function (txt) {
            return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
        });
    }

    function toIndex(str) {
        return toTitleCase(str.replace(/\W/g, ' ').replace(/\s+/g, ' '))
    }

    var fuseDataSet = _
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
                    method: it.endpoint.method + '-' + it.endpoint.method.toLocaleLowerCase(),
                    apiPath: toIndex(it.apiPath),
                    path: toIndex(it.endpoint.path)
                },
                method: it.endpoint.method,
                apiPath: it.apiPath,
                path: it.endpoint.path
            }
        })
        .value();

    var fuseOptions = {
        keys: [{
            name: 'index.method',
            weight: 0.50
        }, {
            name: 'index.apiPath',
            weight: 0.30
        }, {
            name: 'index.path',
            weight: 0.19
        }],
        caseSensitive: true,
        verbose: false
    };

    var fuse = new Fuse(fuseDataSet, fuseOptions);

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

        var hits = fuse.search(that.val()).slice(0, 10);
        var suggestItems = hits.map(function (hit) {
            return $(fuzzyTemplate({hash: hit}));
        });

        var suggestMenu = $('#fuzzy-suggest');
        suggestMenu.children()
            .remove()
            .end()
            .append(suggestItems)
            .show();
    });
});