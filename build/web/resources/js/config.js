jQuery(document).ready(function() {

	// panel filter
    $('.panel-filter .title').click(function() {
        $('.page-table').toggleClass('toggle-filter');
    });

    // menu toggle
    $('#btn-toggle-menu').click(function() {
        $('#main').toggleClass('toggle-layout');
    });

    //arrange blocks in a circle
    $('#rotator-wrapper .inner').repeat().each($).addClass('active',$).wait(6250).removeClass('active').all();
    $('.satelite').repeat().each($).addClass('active',$).wait(6250).removeClass('active').all();

    // sticky menu

    $("#menu").stick_in_parent({
    	offset_top: 25,
    });

    // unit selector

    $('#select-unit .toggle').click(function(){
    	$('#select-unit').toggleClass('active');
    });

});
