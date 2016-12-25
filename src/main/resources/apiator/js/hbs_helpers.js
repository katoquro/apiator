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

modulejs.define('hbs', function () {
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
            response = '<a href="#' + shrinkByDots + '" class="param__type-name">' + shrinkByDots + '</a>' + response;
        } else if (responseType.modelType) {
            response = '<span class="param__model-type">' + responseType.modelType + '</span>' + response;
        }
        return response;
    }

    Handlebars.registerHelper('responseTyper', responseTyper);

    function lower(string) {
        return string.toLowerCase();
    }

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
        if (!str) {return;}
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

    // http://stackoverflow.com/questions/4810841/how-can-i-pretty-print-json-using-javascript
    Handlebars.registerHelper('highlightJSON', function(json) {
      if (typeof json != 'string') {
         json = JSON.stringify(json, undefined, 2);
      }

      json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
      return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
        var cls = 'number';
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'key';
            } else {
                cls = 'string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/null/.test(match)) {
            cls = 'null';
        }
        return '<span class="' + cls + '">' + match + '</span>';
      });
    });

    function urler(data) {
        // console.log(data)
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

    $("[type='text/x-handlebars-template']").each(function (i, template) {
        var $template = $(template);
        Handlebars.registerPartial($template.attr('id'), $template.html());
    });

    return {
        render: function () {
            var template = $('#main');
            var templateSrc = template.html();

            $('#doc-container').html(Handlebars.compile(templateSrc)(apiJson));
        },
        urlerGeneral: function (payload) {
            switch (payload.showAs) {
                case 'endpoint':
                    return urler({hash: payload});
                case 'model':
                    return Handlebars.helpers.shrinkByDots(payload.type); // todo refactor helpers after redesign
                default:
                    throw new Error('Not supported payload type')
            }
        }
    }
});
