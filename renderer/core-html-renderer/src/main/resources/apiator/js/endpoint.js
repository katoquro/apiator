modulejs.define('endpoint', function () {

    function run() {
        var $endpointHeader = $('.panel_endpoint .panel__header');

        $endpointHeader.find('.panel__header-view').on('click', function (e) {
            $(this).siblings('.panel__header-view').removeClass('panel__header-view_active');
            $(this).addClass('panel__header-view_active');

            $(this).parents('.endpoint').attr('data-type', $(this).data('type'));
        });
    }

    return {
        run: run
    }
});
