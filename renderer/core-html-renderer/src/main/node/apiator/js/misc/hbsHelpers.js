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

import faviconHbs from '/apiator/hbs/misc/favicon.hbs'
import modalHbs from '/apiator/hbs/misc/modal.hbs'

import mainHbs from '/apiator/hbs/main.hbs'

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

Handlebars.registerHelper('getIdForTargetMarkerOfEndpoint', utils.getIdForTargetMarkerOfEndpoint);
Handlebars.registerHelper('getPageLinkToEndpoint', utils.getPageLinkToEndpoint);
Handlebars.registerHelper('getAbsoluteLinkToEndpoint', utils.getAbsoluteLinkToEndpoint);
Handlebars.registerHelper('getIdForTargetMarkerOfModel', utils.getIdForTargetMarkerOfModel);
Handlebars.registerHelper('getPageLinkToType', utils.getPageLinkToType);
Handlebars.registerHelper('getAbsoluteLinkToType', utils.getAbsoluteLinkToType);
Handlebars.registerHelper('splitCamelCase', utils.splitCamelCase);

Handlebars.registerHelper('hashToObject', options => options.hash);

Handlebars.registerPartial('favicon', faviconHbs);
Handlebars.registerPartial('modal', modalHbs);

/**
 *
 * @param {Apiator.ApiScheme} apiJson
 */
export default function render(apiJson) {
    $('#doc-container').html(mainHbs(apiJson));
}
