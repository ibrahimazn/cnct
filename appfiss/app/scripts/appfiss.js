/**
 * AppFiss - Serverless ML Management App
 * 1.0.0
 *
 * Custom scripts
 */

$(document).ready(function () {


    // Full height of sidebar
    function fix_height() {
        var heightWithoutNavbar = $("body > #wrapper").height() - 61;
        $(".sidebar-panel").css("min-height", heightWithoutNavbar + "px");

        var navbarHeight = $('nav.navbar-default').height();
        var wrapperHeight = $('#page-wrapper').height();

        if(navbarHeight > wrapperHeight){
            $('#page-wrapper').css("min-height", navbarHeight + "px");
        }

        if(navbarHeight < wrapperHeight){
            $('#page-wrapper').css("min-height", $(window).height()  + "px");
        }

        if ($('body').hasClass('fixed-nav')) {
            if (navbarHeight > wrapperHeight) {
                $('#page-wrapper').css("min-height", navbarHeight + "px");
            } else {
                $('#page-wrapper').css("min-height", $(window).height() - 60 + "px");
            }
        }

    }

    $(window).bind("load resize scroll", function() {
        if(!$("body").hasClass('body-small')) {
            fix_height();
        }
    });

    // Move right sidebar top after scroll
    $(window).scroll(function(){
        if ($(window).scrollTop() > 0 && !$('body').hasClass('fixed-nav') ) {
            $('#right-sidebar').addClass('sidebar-top');
        } else {
            $('#right-sidebar').removeClass('sidebar-top');
        }
    });

    setTimeout(function(){
        fix_height();
    });

});


// Minimalize menu when screen is less than 768px
$(window).bind("load resize", function () {
    
    if ($(document).width() < 1025) {
       
        $('body').addClass('body-small')
    } else {
        $('body').removeClass('body-small')
    }
});