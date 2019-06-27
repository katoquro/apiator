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

import * as cfg from '/apiator/js/misc/cfg.js'
import * as modal from '/apiator/js/misc/modal.js'
import $ from 'jquery'

import iconBase64 from '/bw_favicon.png';

export function run() {
    const $menu = $('.menu');

    const menuPic = $menu.find('.menu__pic');
    menuPic.css('background-image', `url(data:image/png;base64,${iconBase64})`);

    menuPic.on('click', function (e) {
        e.stopPropagation();
        $menu.toggleClass('menu_active');
    });

    $(window).click(function (e) {
        $menu.removeClass('menu_active');
    });

    if (cfg.isUpdated()) {
        menuPic.addClass('menu__pic_notification');
        $('.js__menu-item-whats-new').addClass('menu__items-item_notification');
    }

    $('.js__menu-item-whats-new').on('click', event => {
        $(event.currentTarget)
            .removeClass('menu__items-item_notification')
            .closest('.menu')
            .find('.menu__pic')
            .removeClass('menu__pic_notification');

        cfg.markUpdated();
    });

    $('.js__menu-item-thanks').on('click', modal.showModal);
}

