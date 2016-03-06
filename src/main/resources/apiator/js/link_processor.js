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

modulejs.define('linkProcessor', function () {
    new Clipboard('[data-clipboard-text]');

    $('[data-clipboard-text]').on('click', function (e) {
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

    $('.raw-view-switch').on('click', function () {
        $(this).parent().next().slideToggle();
    });
});