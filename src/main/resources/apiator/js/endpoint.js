modulejs.define('endpoint', function () {

  function run() {
    var $endpointHeader = $('.endpoint__header');

    $endpointHeader.find('.header__view').on('click', function(e) {
      $(this).siblings('.header__view').removeClass('header__view_active');
      $(this).addClass('header__view_active');

      $(this).parents('.panel__endpoint').attr('data-type', $(this).data('type'));
    });
  }

  return {
    run: run
  }
});
