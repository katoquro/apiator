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

modulejs.define('hbs', ['utils'], function (utils) {

    function responseTyper(responseType) {
        var response = "";

        responseType.basedOn.forEach(function (value) {
            response = " " + responseTyper(value);
        });
        if (responseType.type) {
            var typeName = utils.getAfterLastDot(responseType.type);
            response = '<a href="' + utils.getPageLinkToType(responseType.type) + '" class="param__type-name">' + typeName + '</a>' + response;
        } else if (responseType.modelType) {
            response = '<span class="param__model-type">' + responseType.modelType + '</span>' + response;
        }
        return response;
    }

    Handlebars.registerHelper('responseTyper', responseTyper);

    Handlebars.registerHelper('toLowerCase', function (string) {
        return (string && typeof string === 'string') ? string.toLowerCase() : '';
    });

    Handlebars.registerHelper('getAfterLastDot', utils.getAfterLastDot);

    Handlebars.registerHelper('ifCond', function (v1, v2, options) {
        if (v1 === v2) {
            return options.fn(this);
        }
        return options.inverse(this);
    });

    // http://stackoverflow.com/questions/4810841/how-can-i-pretty-print-json-using-javascript
    Handlebars.registerHelper('highlightJSON', function (json) {
        if (typeof json != 'string') {
            json = JSON.stringify(json, undefined, 4);
        }

        json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
        return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
            var cls = 'json__value_number';
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
            return '<span class="' + cls + '">' + match + '</span>';
        }).replace(/(\r\n|\n|\r)/gm, '<br>');
    });

    Handlebars.registerHelper('getTargetMarkerOfEndpoint', utils.getTargetMarkerOfEndpoint);
    Handlebars.registerHelper('getPageLinkToEndpoint', utils.getPageLinkToEndpoint);
    Handlebars.registerHelper('getAbsoluteLinkToEndpoint', utils.getAbsoluteLinkToEndpoint);
    Handlebars.registerHelper('getTargetMarkerOfType', utils.getTargetMarkerOfType);
    Handlebars.registerHelper('getPageLinkToType', utils.getPageLinkToType);
    Handlebars.registerHelper('getAbsoluteLinkToType', utils.getAbsoluteLinkToType);

    Handlebars.registerHelper('hashToObject', function (options) {
        return options.hash
    });

    $("[type='text/x-handlebars-template']").each(function (i, template) {
        var $template = $(template);
        Handlebars.registerPartial($template.attr('id'), $template.html());
    });

    return {
        runMainRender: function () {
            var template = $('#main');
            var templateSrc = template.html();

            $('#doc-container').html(Handlebars.compile(templateSrc)(apiJson));
        }
    }
});
