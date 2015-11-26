var template = $('#template');
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
        response = '<span class="name object-link"><a href="#">' + Handlebars.helpers.shrinkByDots(responseType.type) + '</a></span>' + response;
    } else if (responseType.modelType) {
        response = '<span class="type">' + responseType.modelType + '</span>' + response;
    }
    return response;
}

Handlebars.registerHelper('responseTyper', responseTyper);
function lower(string) {
    return string.toLowerCase();
}
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

$.fn.scrollspy.Constructor.prototype.refresh = function () {
    var that = this;
    var offsetMethod = 'offset';
    var offsetBase = 0;

    this.offsets = [];
    this.targets = [];
    this.scrollHeight = this.getScrollHeight();

    if (!$.isWindow(this.$scrollElement[0])) {
        offsetMethod = 'position';
        offsetBase = this.$scrollElement.scrollTop();
    }

    this.$body
        .find(this.selector)
        .map(function () {
            var $el = $(this);
            var href = $el.data('target') || $el.attr('href');
            var $href = /^#./.test(href) && $(document.getElementById(href.slice(1)));

            return ($href
                && $href.length
                && $href.is(':visible')
                && [[$href[offsetMethod]().top + offsetBase, href]]) || null
        })
        .sort(function (a, b) {
            return a[0] - b[0]
        })
        .each(function () {
            that.offsets.push(this[0]);
            that.targets.push(this[1]);
        })
};

var adjustSidebar = function () {
    $('#sidebar').outerHeight($(window).height() - 50);
};
adjustSidebar();
$(window).resize(adjustSidebar);

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


$('.endpoints li').on('activate.bs.scrollspy', function (e) {
    e.stopPropagation();
    e.preventDefault();
    var $this = $(this),
        position = $this.position(),
        sidebar = $('li.endpoints');
    if ($this.find('.active').length) {
        return;
    }
    sidebar.animate({scrollTop: $this.offsetParent().position().top + position.top}, 0)
});

setTimeout(function () {
    $('body').scrollspy({
        target: '.complementary',
        offset: 40
    });
}, 2000);

new Clipboard('[data-clipboard-text]');