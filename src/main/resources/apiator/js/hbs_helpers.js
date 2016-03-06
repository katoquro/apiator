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

modulejs.define('hbsHelpers', function () {

    var PANEL_MAPPER = {
        'GET': 'panel-info',
        'POST': 'panel-success',
        'DELETE': 'panel-danger',
        'PUT': 'panel-warning',
        'PATCH': 'panel-default',
        'OPTIONS': 'panel-default',
        'HEAD': 'panel-default',
        'DEFAULT': 'panel-default'
    };

    function responseTyper(responseType) {
        var response = "";

        responseType.basedOn.forEach(function (value) {
            response = " " + responseTyper(value);
        });
        if (responseType.type) {
            var shrinkByDots = Handlebars.helpers.shrinkByDots(responseType.type);
            response = '<span class="name object-link"><a href="#' + shrinkByDots + '">' + shrinkByDots + '</a></span>' + response;
        } else if (responseType.modelType) {
            response = '<span class="type">' + responseType.modelType + '</span>' + response;
        }
        return response;
    }

    Handlebars.registerHelper('responseTyper', responseTyper);
    function lower(string) {
        return string.toLowerCase();
    }

    $("[type='text/x-handlebars-template']").each(function (i, template) {
        var $template = $(template);
        Handlebars.registerPartial($template.attr('id'), $template.html());
    });

    Handlebars.registerHelper('lower', lower);

    Handlebars.registerHelper('panelStyle', function (method) {
        return PANEL_MAPPER[method] || PANEL_MAPPER.DEFAULT;
    });

    Handlebars.registerHelper('skipLeadSlash', function (string) {
        if (string.startsWith('/')) {
            return string.slice(1);
        }
        return string;
    });

    Handlebars.registerHelper('shrinkByDots', function (str) {
        return str.split('.').slice(-1);
    });

    Handlebars.registerHelper('json', function (data) {
        return JSON.stringify(data, null, "  ");
    });

    Handlebars.registerHelper('ifCond', function (v1, v2, options) {
        if (v1 === v2) {
            return options.fn(this);
        }
        return options.inverse(this);
    });

    function urler(data) {
        var hash = data.hash;
        return '#_' + lower(hash.method) + '_' + hash.apiPath + hash.path;
    }

    function copyUrler(data) {
        var prefix = location.origin + location.pathname + location.search;
        return prefix + urler(data);
    }

    function copyUrlerType(data) {
        var prefix = location.origin + location.pathname;
        return prefix + '#' + Handlebars.helpers.shrinkByDots(data.hash.type)
    }

    Handlebars.registerHelper('urler', urler);
    Handlebars.registerHelper('copyUrler', copyUrler);
    Handlebars.registerHelper('copyUrlerType', copyUrlerType);

});