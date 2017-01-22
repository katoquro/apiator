modulejs.define('menu', function () {

    function run() {
        var $menu = $('.menu');

        $menu.find('.menu__pic').on('click', function (e) {
            e.stopPropagation();
            $menu.toggleClass('menu_active');
        });

        $(window).click(function(e) {
            $menu.removeClass('menu_active');
        });
    }

    return {
        run: run
    }
});
