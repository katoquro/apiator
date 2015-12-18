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
    var prefix = location.origin + location.pathname;
    return prefix + urler(data);
}

Handlebars.registerHelper('urler', urler);
Handlebars.registerHelper('copyUrler', copyUrler);

$('#doc-container')
    .html(Handlebars.compile(templateSrc)(apiJson));

document.title = new Date();

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

var fuzzyTemplate = Handlebars.compile($("#fuzzy-response-template").html());

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
    $this = $(this);
    $this.addClass('copying');
    window.setTimeout($this.removeClass.bind($this, 'copying'), 250);
    var hashUrl = this.getAttribute('data-clipboard-text');
    var hash = hashUrl.substring(hashUrl.indexOf('#'))
    if ($('li.active>a').attr('href') === hash) {
        return;
    }
    var item = _.find(anchorsMap, {id: hash});
    activateAnchor(item.anchor, hash);
    scrollAnchor(item.anchor, hash);
});

var clickAnchorEvent = function (ev) {
    var anchor = $(ev.target).parent('a'),
        id = anchor.attr('href');

    activateAnchor(anchor, id);
};

var deactivateAnchors = function () {
    var activeItems = _.toArray(document.querySelectorAll('.side-container a.active,.side-container li.active'));
    activeItems.forEach(function (item) {
        if (item.classList)
            item.classList.remove('active');
        else
            item.className = item.className.replace(new RegExp('(^|\\b)' + 'active' + '(\\b|$)', 'gi'), ' ');
    })
};

var activateAnchor = function (anchor, id) {
    deactivateAnchors()
    anchor = $(anchor);
    var c = anchor.parents('.side-container').length ? anchor.parents('.side-container') : anchor.next();
    anchor.parent().addClass("active");
    c.addClass('active');
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
    var anchors = document.querySelectorAll(".nav a");
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