modulejs.define('sidebar', function () {

  function run() {
    var $sidebar = $('.sidebar'),
        $toggler = $sidebar.find('.api__toggler');

    $toggler.on('click', function(e) {
      $(this).toggleClass('api__toggler_active');
      $(this).parent().toggleClass('api_active');
    });


    var $groupTitle = $('.group__title');

    $groupTitle.on('click', function(e) {
      $(this).siblings('.group__content').removeClass('group__content_active');
      $(this).next().addClass('group__content_active');
    })
  }

  return {
    run: run
  }
});
