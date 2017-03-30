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
