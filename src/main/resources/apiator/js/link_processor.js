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
    /**
     * @param {string} hash
     */
    function navigateSidebarMenu(hash) {
        $('.sidebar a[href="' + hash + '"]').each(function (i, item) {
            markSidebarActive(item);
            scrollToSidebarItem(item);
        })
    }

    function markSidebarActive(_anchor) {
        var anchor = $(_anchor);
        $('.side-container.active').removeClass('active');

        if (anchor.is('.sidebar-title')) {
            anchor.next().addClass('active');
        } else {
            $('.side-container li.active').removeClass('active');
            anchor.closest('li').addClass('active');
            anchor.closest('.side-container').addClass('active');
        }
    }

    const SIDEBAR_SCROLL_BOTTOM_THRESHOLD = 30;

    function scrollToSidebarItem(_anchor) {
        var anchor = $(_anchor);

        var activeItem = $('.side-container li.active');
        var activeSideContainer = anchor.closest('.side-container');

        var itemHeight = activeItem.height();
        var containerHeight = activeSideContainer.height() - SIDEBAR_SCROLL_BOTTOM_THRESHOLD;

        var scrollTo = activeItem.prevAll().length * itemHeight;
        var currentScroll = activeSideContainer.scrollTop();

        if (!(currentScroll < scrollTo && scrollTo < (currentScroll + containerHeight))) {
            activeSideContainer.scrollTop(scrollTo);
        }
    }

    /**
     * @param {Event} event
     */
    function clickLinkCallback(event) {
        var href = $(event.currentTarget).attr('href');

        navigateSidebarMenu(href);
    }


    return {
        navigateSidebarMenu: navigateSidebarMenu,
        attachCallbacks: function () {
            var clipboard = new Clipboard('[data-clipboard-text]');
            clipboard.on('success', function (event) {
                var url = event.text;
                var urlHash = url.substring(url.indexOf('#'));

                if ($('li.active>a').attr('href') === urlHash) {
                    return;
                }

                navigateSidebarMenu(urlHash);
            });

            $('.main a[href^="#"], .sidebar a[href^="#"]').on('click', clickLinkCallback);

            $('.raw-view-switch').on('click', function () {
                $(this).parent().next().slideToggle();
            });

        }
    }
});