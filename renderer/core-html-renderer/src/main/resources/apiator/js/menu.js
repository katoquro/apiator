/*
 * Copyright 2014-2017 Ainrif <support@ainrif.com>
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

modulejs.define('menu', ['cfg', 'modal'], function (cfg, modal) {

    function run() {
        var $menu = $('.menu');

        var menuPic = $menu.find('.menu__pic');
        menuPic.on('click', function (e) {
            e.stopPropagation();
            $menu.toggleClass('menu_active');
        });

        $(window).click(function (e) {
            $menu.removeClass('menu_active');
        });

        if (cfg.isUpdated()) {
            menuPic.addClass('menu__pic_notification');
            $('.js__menu-item-gift').addClass('menu__items-item_notification');
        }

        $('.js__menu-item-gift').on('click', function () {
            $(this)
                .removeClass('menu__items-item_notification')
                .closest('.header__menu')
                .find('.menu__pic')
                .removeClass('menu__pic_notification');

            modal.showModal();

            cfg.markUpdated();
        })
    }

    return {
        run: run
    }
});
