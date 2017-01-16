modulejs.define('sidebar', function () {

    function run() {
        var $sidebar = $('.sidebar'),
            $apiContext = $sidebar.find('.api');

        $apiContext.on('click', function (e) {
            $(this).find('.api__toggle').toggleClass('api__toggle_active');
            $(this).toggleClass('api_active');
        });

        var $groupTitle = $('.group__title');

        $groupTitle.on('click', function (e) {
            $(this).siblings('.group__content').removeClass('group__content_active');
            $(this).next().addClass('group__content_active');
        })
    }

    return {
        run: run
    }
});
