/**
 * SLMLP - Project Management App
 *
 */
(function () {
    angular.module('slmlp', [
        'ui.router',                    // Routing
        'ui.bootstrap',                 // Bootstrap
        'ngSanitize',
        'LocalStorageModule', // AngularJs LocalStorage,
        'pascalprecht.translate',
        'ngCookies',
        'ui.slimscroll',
        'ui.select2',
        'dirPagination', //Pagination
        'NgSwitchery',
        'gridshore.c3js.chart',
        'ui.sortable',
        'ngAnimate',
        'toastr',
        'gridster',
        'ngFileUpload',
        'frapontillo.bootstrap-duallistbox',
        'tagged.directives.infiniteScroll',
        'ngTagsInput'
    ])
})();
