/**
 * Utility functions will be present here.
 *
 * Wolf - Cloud Management Portal Copyright 2016 Assistanz.com
 *
 * @author - Jamseer N
 */
angular.module('appfiss').factory('utilService', utilService);

function utilService($state, appConfig, localStorageService) {
    
    
    var util = {};
    
    util.getSortingOrderBySortBy = function(sortBy) {
        var sort = appConfig.sort;
        if (sort.column == sortBy) {
            sort.descending = !sort.descending;
        } else {
            sort.column = sortBy;
            sort.descending = false;
        }
        var sortOrder = '-';
        if (!sort.descending) {
            sortOrder = '+';

        }
        sort.sortOrder = sortOrder;
        return sort;
    };
    
    return util;
}
