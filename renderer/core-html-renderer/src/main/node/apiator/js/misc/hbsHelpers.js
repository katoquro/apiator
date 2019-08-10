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


import Handlebars from 'handlebars/runtime';
import $ from 'jquery'
import _ from 'lodash'

import * as utils from '/apiator/js/misc/utils';

import endpointHbs from '/apiator/hbs/card/endpoint.hbs'
import enumHbs from '/apiator/hbs/card/enum.hbs'
import typeHbs from '/apiator/hbs/card/type.hbs'

import suggestItemHbs from '/apiator/hbs/header/search/suggest-item.hbs'

import faviconHbs from '/apiator/hbs/misc/favicon.hbs'
import modalHbs from '/apiator/hbs/misc/modal.hbs'

import mainHbs from '/apiator/hbs/main.hbs'
import sidebarHbs from '/apiator/hbs/sidebar.hbs'
import contentHbs from '/apiator/hbs/content.hbs'

/**
 * @param {Apiator.EndpointType|Apiator.BasicType} type
 */
function renderTemplateTypes(type) {
    let result = "";
    if (type.type) {
        result += '<a data-link="' + utils.getPageLinkToType(type.type) + '" class="type-view__model">'
            + utils.getAfterLastDot(type.type) + '</a>'
    } else {
        result += '<div class="type-view__modeltype">'
            + type.modelType + '</div>'
    }
    if (type.templateName) {
        // todo process template name
    }
    if (!_.isEmpty(type.basedOn)) {
        result += ' of (';
        _.each(type.basedOn, function (it) {
            result += renderTemplateTypes(it);
            result += ', '
        });
        result = result.slice(0, -2);
        result += ')';
    }

    return result;
}

Handlebars.registerHelper('renderTemplateTypes', renderTemplateTypes);

Handlebars.registerHelper('toLowerCase', string => (string && typeof string === 'string') ? string.toLowerCase() : '');

Handlebars.registerHelper('getAfterLastDot', utils.getAfterLastDot);

Handlebars.registerHelper('ifCond', function (v1, v2, options) {
    if (v1 === v2) {
        return options.fn(this);
    }
    return options.inverse(this);
});

// http://stackoverflow.com/questions/4810841/how-can-i-pretty-print-json-using-javascript
Handlebars.registerHelper('highlightJSON', json => {
    if (typeof json !== 'string') {
        json = JSON.stringify(json, undefined, 4);
    }

    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
        let cls = 'json__value_number';
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

Handlebars.registerHelper('getIdForTargetMarkerOfEndpoint', utils.getIdForTargetMarkerOfEndpoint);
Handlebars.registerHelper('getPageLinkToEndpoint', utils.getPageLinkToEndpoint);
Handlebars.registerHelper('getAbsoluteLinkToEndpoint', utils.getAbsoluteLinkToEndpoint);
Handlebars.registerHelper('getIdForTargetMarkerOfModel', utils.getIdForTargetMarkerOfModel);
Handlebars.registerHelper('getPageLinkToType', utils.getPageLinkToType);
Handlebars.registerHelper('getAbsoluteLinkToType', utils.getAbsoluteLinkToType);
Handlebars.registerHelper('splitCamelCase', utils.splitCamelCase);

Handlebars.registerHelper('hashToObject', options => options.hash);

Handlebars.registerPartial('endpoint', endpointHbs);
Handlebars.registerPartial('enum', enumHbs);
Handlebars.registerPartial('type', typeHbs);

Handlebars.registerPartial('suggest-item', suggestItemHbs);

Handlebars.registerPartial('favicon', faviconHbs);
Handlebars.registerPartial('modal', modalHbs);

Handlebars.registerPartial('sidebar', sidebarHbs);
Handlebars.registerPartial('content', contentHbs);

/**
 *
 * @param {Apiator.ApiScheme} apiJson
 */
export default function render(apiJson) {
    $('#doc-container').html(mainHbs(apiJson));
}
