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

Handlebars.registerHelper('shrinkByDots', function (str) {
    var splitName = str.split(".");
    var result = '';
    for (var i = 0; i < splitName.length - 1; i++) {
        result += splitName[i][0] + '.';
    }
    result += splitName[splitName.length - 1];
    return result
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