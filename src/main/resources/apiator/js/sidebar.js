modulejs.define('sidebar', function () {

    function run() {
        var $sidebar = $('.sidebar');

        var $apiTitleEndpoints = $sidebar.find('.js_sidebar-endpoints .api__title');

        $apiTitleEndpoints.on('click', function (e) {
            var $title = $(this);
            var $apiMenu = $title.closest('.api');

            $apiMenu.toggleClass('api_active');

            if ($apiMenu.hasClass('api_active')) {
                location.hash = $title.data('link')
            }
        });

        var $apiToggleEndpoints = $sidebar.find('.js_sidebar-endpoints .api__toggle');

        $apiToggleEndpoints.on('click', function (e) {
            $(this)
                .closest('.api')
                .toggleClass('api_active');
        });

        var $apiTitleModel = $sidebar.find('.js_sidebar-model .api__title');

        $apiTitleModel.on('click', function (e) {
            location.hash = $(this).data('link')
        });

        var $groupTitle = $('.group__title');

        $groupTitle.on('click', function (e) {
            openGroupTitle($(this))
        })
    }

    /**
     * @param {jQuery} $groupTitle
     */
    function openGroupTitle($groupTitle) {
        $groupTitle.siblings('.group__content').removeClass('group__content_active');
        $groupTitle.next().addClass('group__content_active');
    }

    return {
        run: run,
        openGroupTitle: openGroupTitle
    }
});
