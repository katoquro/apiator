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

Handlebars.registerHelper('panelStyle', function (method) {
    return PANNEL_MAPPER[method] || PANNEL_MAPPER.DEFAULT;
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

$('#doc-container')
    .html(Handlebars.compile(templateSrc)(apiJson));

$('body').scrollspy({
    target: '.complementary',
    offset: 40
});

var adjustSidebar = function () {
    $('#sidebar').outerHeight($(window).height() - 50);
};
adjustSidebar();
$(window).resize(adjustSidebar);

document.title = new Date();

//fuzzy search
var fuseDictionary = [];
$.each(apiJson.apiContexts, function () {
    var _apiPath = this.apiPath;
    var _title = this.title;
    $.each(this.apiEndpoints, function () {
        var entry = {
            fullPath: _apiPath + this.path,
            method: this.method,
            hash: String(stringHashCode(_title)).concat('-', stringHashCode(this.name))
        };
        fuseDictionary.push(entry)
    })
});

var fuseOptions = {
    keys: ['fullPath'],
    caseSensitive: true // keys to search in
};

var fuse = new Fuse(fuseDictionary, fuseOptions);

$('html').click(function (event) {
    if (!$(event.target).is('#fuzzy-suggest, #fuzzy-input')) {
        $('#fuzzy-suggest').hide();
    }
});

$('#fuzzy-suggest').on('click', 'li', function () {
    $('#fuzzy-suggest').hide();
    $('#fuzzy-input').val('');
});

$('#fuzzy-input').on('keyup click', function () {
    var that = $(this);
    if (2 > that.val().length) return;

    var hits = fuse.search(that.val()).slice(0, 10);
    var suggestItems = hits.map(function (hit) {
        return $('<li role="presentation">' +
            '<a role="menuitem" tabindex="-1" href="#' + hit.hash + '">' +
            '@' + hit.method + ' ' + hit.fullPath +
            '</a>' +
            '</li>')
    });

    var suggestMenu = $('#fuzzy-suggest');
    suggestMenu.children()
        .remove()
        .end()
        .append(suggestItems)
        .show();
})