/**
 * WOLF - Responsive Admin Theme
 *
 */
function config($translateProvider) {

    var hostName = window.location.origin;
    var pathName = window.location.pathname;
    var values = pathName.split("/");
    var languageFileUrl = hostName + "/" + values[1] + "/app/i18n/";
    $translateProvider.useStaticFilesLoader({
        'prefix': languageFileUrl + 'locale-',
        'suffix': '.json'
    });

    $translateProvider.preferredLanguage('en');
    $translateProvider.useSanitizeValueStrategy('sanitizeParameters');
    $translateProvider.useLocalStorage();

}

angular
    .module('appfiss')
    .config(config)
