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
var template = $('#app');
var templateSrc = template.html();
template.remove();

function stringHashCode(str) {
    var hash = 0;
    if (str.length == 0) return hash;
    for (var i = 0; i < str.length; i++) {
        var char = str.charCodeAt(i);
        hash = ((hash << 5) - hash) + char;
        hash = hash & hash;
    }
    return hash;
}

Handlebars.registerHelper('hashCode', stringHashCode);

var PANNEL_MAPPER = {
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

Handlebars.registerHelper('templater', function (selector, data) {
    var tpl = $('#' + selector);
    return Handlebars.compile(tpl.html())(data.hash.data);
});

Handlebars.registerHelper('lower', lower);

Handlebars.registerHelper('panelStyle', function (method) {
    return PANNEL_MAPPER[method] || PANNEL_MAPPER.DEFAULT;
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

$('#doc-container')
    .html(Handlebars.compile(templateSrc)(apiJson));

//document.title = new Date().toTimeString();

//fuzzy search
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

new Clipboard('[data-clipboard-text]');

$('[data-clipboard-text]').click(function (e) {
    var $this = $(this);
    $this.addClass('copying');
    window.setTimeout($this.removeClass.bind($this, 'copying'), 250);
    var hashUrl = this.getAttribute('data-clipboard-text');
    var hash = hashUrl.substring(hashUrl.indexOf('#'));
    if ($('li.active>a').attr('href') === hash) {
        return;
    }
    var item = _.find(anchorsMap, {id: hash});
    if (item) {
        activateAnchor(item.anchor, hash);
        scrollAnchor(item.anchor, hash);
    }
});

var clickAnchorEvent = function (ev) {
    var anchor = $(ev.currentTarget);

    if (anchor.is('.main *')) {
        anchor = $('.sidebar a[href=' + $(ev.currentTarget).attr('href') + ']')
    }

    var id = anchor.attr('href');

    activateAnchor(anchor, id);
};

var activateAnchor = function (_anchor, id) {
    $('.side-container.active').removeClass('active');
    var anchor = $(_anchor);

    if (anchor.is('.sidebar-title')) {
        anchor.next().addClass('active');
    } else {
        $('.side-container li.active').removeClass('active');
        anchor.closest('li').addClass('active');
        anchor.closest('.side-container').addClass('active');
    }
};

window.scrollAnchor = function (anchor, id) {
    anchor = $(anchor);

    var e = $(".side-container .active");
    var ep = (e.position().top);
    var eh = e.height();

    var c = anchor.parents('.side-container').length ? anchor.parents('.side-container') : anchor.next();
    var activeItem = c.find('li.active');

    var scrollTo = activeItem.prevAll().length * eh;
    c.scrollTop(scrollTo);
};

(function () {
    var anchors = document.querySelectorAll(".nav li a");
    anchors = _.toArray(anchors);
    var anchorsMap = anchors.map(function (anchor, index, collection) {
        var id = anchor.getAttribute('href');
        return {anchor, id};
    });

    var bindedClick = clickAnchorEvent.bind(anchorsMap);

    window.anchorsMap = anchorsMap;

    anchors.map(function (anchor) {
        anchor.addEventListener('click', bindedClick);
    });

    $('.sidebar-title').on('click', clickAnchorEvent);
    $('.main a[href^="#"]').on('click', clickAnchorEvent);

    setTimeout(function () {
        var href = window.location.hash;
        if (!href) {
            return;
        }
        var node = _.find(anchorsMap, {id: href});
        if (node) {
            activateAnchor(node.anchor, href);
            scrollAnchor(node.anchor, href);
        }
    }, 300);
})();

$('.raw-view-switch').on('click', function () {
    $(this).parent().next().slideToggle();
});