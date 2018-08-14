/**
 * Datepicker service.
 *
 * Rayhar - Date picker widget service.
 *
 * @author - Jamseer N
 */

var dateService = function() {

    // Generic wrapper around modal opening
    getDateOptions = function() {
        return {
                dateFormat: 'dd-MM-yyyy',
                maxDate: new Date(2020, 5, 22),
//                minDate: new Date(),
                startingDay: 1
            }
    };

    return {
        getDateOptions : getDateOptions
    };
};
angular.module('appfiss').factory('dateService', dateService);
