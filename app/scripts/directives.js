/**
 * SLMLP - Project Management App
 *
 */


/**
 * pageTitle - Directive for set Page title - mata title
 */
function pageTitle($rootScope, $timeout) {
    return {
        link: function (scope, element) {
            var listener = function (event, toState, toParams, fromState, fromParams) {
                // Default title - load on Dashboard 1
                var title = 'SLMLP - Project Management App';
                // Create your own title pattern
                if (toState.data && toState.data.pageTitle)
                    title = 'SLMLP | ' + toState.data.pageTitle;
                $timeout(function () {
                    element.text(title);
                });
            };
            $rootScope.$on('$stateChangeStart', listener);
        }
    }
}
;

/**
 * sideNavigation - Directive for run metsiMenu on sidebar navigation
 */
function sideNavigation($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element) {
            // Call the metsiMenu plugin and plug it to sidebar navigation
            $timeout(function () {
                element.metisMenu();

            });

            // Colapse menu in mobile mode after click on element
            var menuElement = $('#side-menu a:not([href$="\\#"])');
            menuElement.click(function () {
                if ($(window).width() < 769) {
                    $("body").toggleClass("mini-navbar");
                }
            });

            // Enable initial fixed sidebar
            if ($("body").hasClass('fixed-sidebar')) {
                var sidebar = element.parent();
                sidebar.slimScroll({
                    height: '100%',
                    railOpacity: 0.9
                });
            }
        }
    };
}
;

/**
 * iboxTools - Directive for iBox tools elements in right corner of ibox
 */
function iboxTools($timeout) {
    return {
        restrict: 'A',
        scope: true,
        templateUrl: 'views/common/ibox_tools.html',
        controller: function ($scope, $element) {
            // Function for collapse ibox
            $scope.showhide = function () {
                var ibox = $element.closest('div.ibox');
                var icon = $element.find('i:first');
                var content = ibox.children('.ibox-content');
                content.slideToggle(200);
                // Toggle icon from up to down
                icon.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
                ibox.toggleClass('').toggleClass('border-bottom');
                $timeout(function () {
                    ibox.resize();
                    ibox.find('[id^=map-]').resize();
                }, 50);
            },
                    // Function for close ibox
                    $scope.closebox = function () {
                        var ibox = $element.closest('div.ibox');
                        ibox.remove();
                    }
        }
    };
}
;

/**
 * minimalizaSidebar - Directive for minimalize sidebar
 */
function minimalizaSidebar($timeout) {
    return {
        restrict: 'A',
        template: '<a class="navbar-minimalize minimalize-styl-2 btn btn-primary" href="" ng-click="minimalize()"><i class="fa fa-bars"></i></a>',
        controller: function ($scope, $element) {
            $scope.minimalize = function () {
                $("body").toggleClass("mini-navbar");
                if (!$('body').hasClass('mini-navbar') || $('body').hasClass('body-small')) {
                    // Hide menu in order to smoothly turn on when maximize menu
                    $('#side-menu').hide();
                    // For smoothly turn on menu
                    setTimeout(
                            function () {
                                $('#side-menu').fadeIn(400);
                            }, 200);
                } else if ($('body').hasClass('fixed-sidebar')) {
                    $('#side-menu').hide();
                    setTimeout(
                            function () {
                                $('#side-menu').fadeIn(400);
                            }, 100);
                } else {
                    // Remove all inline style from jquery fadeIn function to reset menu state
                    $('#side-menu').removeAttr('style');
                }
            }
        }
    };
}
;

/**
 * iboxTools with full screen - Directive for iBox tools elements in right corner of ibox with full screen option
 */
function iboxToolsFullScreen($timeout) {
    return {
        restrict: 'A',
        scope: true,
        templateUrl: 'views/common/ibox_tools_full_screen.html',
        controller: function ($scope, $element) {
            // Function for collapse ibox
            $scope.showhide = function () {
                var ibox = $element.closest('div.ibox');
                var icon = $element.find('i:first');
                var content = ibox.children('.ibox-content');
                content.slideToggle(200);
                // Toggle icon from up to down
                icon.toggleClass('fa-chevron-up').toggleClass('fa-chevron-down');
                ibox.toggleClass('').toggleClass('border-bottom');
                $timeout(function () {
                    ibox.resize();
                    ibox.find('[id^=map-]').resize();
                }, 50);
            };
            // Function for close ibox
            $scope.closebox = function () {
                var ibox = $element.closest('div.ibox');
                ibox.remove();
            };
            // Function for full screen
            $scope.fullscreen = function () {
                var ibox = $element.closest('div.ibox');
                var button = $element.find('i.fa-expand');
                $('body').toggleClass('fullscreen-ibox-mode');
                button.toggleClass('fa-expand').toggleClass('fa-compress');
                ibox.toggleClass('fullscreen');
                setTimeout(function () {
                    $(window).trigger('resize');
                }, 100);
            }
        }
    };
}
// Fullscreen ibox function
function expandScreen() {
    return {
        restrict: 'A',
        link: function (scope, element) {
            element.bind("click", function () {
                var ibox = $(element).closest('div.ibox');
                var button = $(element).find('i');
                $('body').toggleClass('fullscreen-ibox-mode');
                button.toggleClass('fa-expand').toggleClass('fa-compress');
                ibox.toggleClass('fullscreen');
                setTimeout(function () {
                    $(window).trigger('resize');
                }, 100);
            });
        }
    }
}

function paginationContent() {
    return {
        restrict: 'E',
        link: function (scope, element, attrs) {
        },
        templateUrl: "views/common/pagination-content.html",
    };
}

function resizable() {
    return {
        restrict: 'A',
        scope: {
            callback: '&onResize'
        },
        link: function postLink(scope, elem, attrs) {
            elem.resizable({
                grid: 110
            });
            elem.on('resize', function (evt, ui) {

                scope.$apply(function () {
                    if (scope.callback) {
                        scope.callback({$evt: evt, $ui: ui});
                    }
                })
            });
        }
    };
}

;

function fileModel($parse) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            element.bind('change', function () {
                scope.$apply(function () {
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
}
;


function inputDropdown1() {
    var templateString = '<div class="input-dropdown">'
            + '<input type="text"'
            + 'name="{{inputName}}"'
            + 'placeholder="{{inputPlaceholder}}"'
            + 'autocomplete="off"'
            + 'ng-model="inputValue"'
            + 'ng-required="false"'
            + 'ng-change="inputChange()"'
            + 'ng-focus="inputFocus()"'
            + 'ng-blur="inputBlur($event)"'
            + 'input-dropdown-validator>'
            + '<ul ng-show="dropdownVisible">'
            + '<li ng-repeat="item in dropdownItems"'
            + 'ng-click="selectItem(item)"'
            + 'ng-mouseenter="setActive($index)"'
            + 'ng-mousedown="dropdownPressed()"'
            + 'ng-class="{\'active\': activeItemIndex === $index}"'
            + '>'
            + '<span ng-if="item.name">{{item.name}}</span>'
            + '<span ng-if="!item.name">{{item}}</span>'
            + '</li>' + '</ul>' + '</div>';

    return {
        restrict: 'E',
        scope: {
            defaultDropdownItems: '=',
            selectedItem: '=',
            allowCustomInput: '=',
            inputRequired: '=',
            inputName: '@',
            inputPlaceholder: '@',
            filterListMethod: '&',
            itemSelectedMethod: '&'
        },
        template: templateString,
        controller: function ($scope) {
            this.getSelectedItem = function () {
                return $scope.selectedItem;
            };
            this.isRequired = function () {
                return $scope.inputRequired;
            };
            this.customInputAllowed = function () {
                return $scope.allowCustomInput;
            };
            this.getInput = function () {
                return $scope.inputValue;
            };
        },
        link: function (scope, element) {
            var pressedDropdown = false;
            console.log("element", element.find('input').isolateScope())
            var inputScope = element.find('input').isolateScope();
            scope.activeItemIndex = 0;
            scope.inputValue = '';
            scope.dropdownVisible = false;
            scope.dropdownItems = scope.defaultDropdownItems || [];
            scope.$watch('dropdownItems', function (newValue,
                    oldValue) {
                if (!angular.equals(newValue, oldValue)) {
                    if (scope.allowCustomInput) {
                        scope.setInputActive();
                    } else {
                        scope.setActive(0);
                    }
                }
            });
            scope.$watch('selectedItem', function (newValue, oldValue) {
                inputScope.updateInputValidity();
                if (!angular.equals(newValue,
                        oldValue)) {
                    if (newValue) {
                        if (typeof newValue === 'string') {
                            scope.inputValue = newValue;
                        } else {
                            scope.inputValue = newValue.name;
                        }
                    } else {
                        var promise = scope.filterListMethod({
                            userInput: newValue
                        });
                    }
                }
            });
            scope.setInputActive = function () {
                scope.setActive(-1);
            };
            scope.setActive = function (itemIndex) {
                scope.activeItemIndex = itemIndex;
            };
            scope.inputChange = function () {
                //scope.selectedItem = null;
                if (scope.inputValue == "") {
                    hideDropdown();
                } else {
                    showDropdown();
                }
                if (!scope.inputValue) {
                    scope.dropdownItems = scope.defaultDropdownItems
                            || [];
                    return;
                } else if (scope.allowCustomInput) {
                    inputScope.updateInputValidity();
                }

                if (scope.filterListMethod) {
                    var promise = scope.filterListMethod({
                        userInput: scope.inputValue
                    });
                    if (promise) {
                        promise
                                .then(function (dropdownItems) {
                                    scope.dropdownItems = dropdownItems;
                                });
                    }
                }
            };
            scope.inputFocus = function () {
                if (scope.allowCustomInput) {
                    scope.setInputActive();
                } else {
                    scope.setActive(0);
                }
                scope.dropdownItems = scope.defaultDropdownItems;
                //showDropdown();
            };
            scope.inputBlur = function (event) {
                if (pressedDropdown) {
                    pressedDropdown = false;
                    return;
                }
                hideDropdown();
            };
            scope.dropdownPressed = function () {
                pressedDropdown = true;
            };
            scope.selectItem = function (item) {
                scope.selectedItem = item;
                hideDropdown();
                scope.dropdownItems = [item];
                if (scope.itemSelectedMethod) {
                    scope.itemSelectedMethod({
                        item: item
                    });
                }
            };
            var showDropdown = function () {
                scope.dropdownVisible = true;
            };
            var hideDropdown = function () {
                scope.dropdownVisible = false;
            };
            var selectPreviousItem = function () {
                var prevIndex = scope.activeItemIndex - 1;
                if (prevIndex >= 0) {
                    scope.setActive(prevIndex);
                } else if (scope.allowCustomInput) {
                    scope.setInputActive();
                }
            };
            var selectNextItem = function () {
                var nextIndex = scope.activeItemIndex + 1;
                if (nextIndex < scope.dropdownItems.length) {
                    scope.setActive(nextIndex);
                }
            };
            var selectActiveItem = function () {
                if (scope.activeItemIndex >= 0
                        && scope.activeItemIndex < scope.dropdownItems.length) {
                    scope
                            .selectItem(scope.dropdownItems[scope.activeItemIndex]);
                } else if (scope.allowCustomInput
                        && scope.activeItemIndex === -1) {
                }
            };

            element.bind("keydown keypress", function (event) {
                switch (event.which) {
                    case 38: // up
                        scope.$apply(selectPreviousItem);
                        break;
                    case 40: // down
                        scope.$apply(selectNextItem);
                        break;
                    case 13: // return
                        if (scope.dropdownVisible
                                && scope.dropdownItems
                                && scope.dropdownItems.length > 0
                                && scope.activeItemIndex !== -1) {
                            // only preventDefault when there is a
                            // list so that we can submit form with
                            // return key after a selection is made
                            event.preventDefault();
                            scope.$apply(selectActiveItem);
                        }
                        break;
                }
            });
        }
    }
}

function validNumber() {
    return {
        require: '?ngModel',
        link: function (scope, element, attrs, ngModelCtrl) {
            if (!ngModelCtrl) {
                return;
            }

            ngModelCtrl.$parsers.push(function (val) {
                if (angular.isUndefined(val)) {
                    var val = '';
                }
                var clean = val.replace(/[^0-9]/g, '');

                if (parseInt(attrs.ngMin) == 500 || parseInt(attrs.ngMin) == 512) {
                    if (clean < parseInt(attrs.ngMin)) {
                        clean = clean.substring(0, clean.length);
                    }
                } else {
                    if (clean < parseInt(attrs.ngMin)) {
                        clean = clean.substring(1, clean.length);
                    }
                }

                if (clean > parseInt(attrs.ngMax)) {
                    clean = clean.substring(0, clean.length - 1);
                }

                if (val !== clean) {
                    ngModelCtrl.$setViewValue(clean);
                    ngModelCtrl.$render();
                }
                return clean;
            });

            element.bind('keypress', function (event) {
                if (event.keyCode === 32) {
                    event.preventDefault();
                }
            });
        }
    };
}
;

//LOADERS
function getDashboardSmallLoaderImage() {
    return {
        restrict: 'E',
        link: function (scope, element, attrs) {
        },
        templateUrl: "views/common/loaders/dashboard-small-loader.html",
    }
};

function getAncodeLoaderLg() {
    return {
        restrict: 'E',
        link: function (scope, element, attrs) {
        },
        templateUrl: "views/common/loaders/loader-lg.html",
    }
};

function getAncodeLoaderMd() {
    return {
        restrict: 'E',
        link: function (scope, element, attrs) {
        },
        templateUrl: "views/common/loaders/loader-md.html",
    }
};

function getAncodeLoaderSm() {
    return {
        restrict: 'E',
        link: function (scope, element, attrs) {
        },
        templateUrl: "views/common/loaders/loader-sm.html",
    }
};

function getAncodeLoaderXs() {
    return {
        restrict: 'E',
        link: function (scope, element, attrs) {
        },
        templateUrl: "views/common/loaders/loader-xs.html",
    }
};

function getAncodeLoaderFullOverlay() {
    return {
        restrict: 'E',
        link: function (scope, element, attrs) {
        },
        templateUrl: "views/common/loaders/loader-full-overlay.html",
    }
};

function getAncodeLoaderContainerOverlay() {
    return {
        restrict: 'E',
        link: function (scope, element, attrs) {
        },
        templateUrl: "views/common/loaders/loader-container-overlay.html",
    }
};

function fileReader() {
    return {
        scope: {
            fileReader: "="
        },
        link: function (scope, element) {
            $(element).on('change', function (changeEvent) {
                var files = changeEvent.target.files;
                if (files.length) {
                    var r = new FileReader();
                    r.onload = function (e) {
                        var contents = e.target.result;
                        scope.$apply(function () {
                            scope.fileReader = contents;
                        });
                    }
                }
            }
            )
        }
    }
}

/**
 *
 * Pass all functions into module
 */
angular
        .module('slmlp')
        .directive('pageTitle', pageTitle)
        .directive('sideNavigation', sideNavigation)
        .directive('iboxTools', iboxTools)
        .directive('minimalizaSidebar', minimalizaSidebar)
        .directive('iboxToolsFullScreen', iboxToolsFullScreen)
        .directive('paginationContent', paginationContent)
        .directive('resizable', resizable)
        .directive('fileModel', fileModel)
        .directive('inputDropdown1', inputDropdown1)
        .directive('expandScreen', expandScreen)
        .directive('validNumber', validNumber)
        .directive('fileReader', fileReader)
        .directive('getDashboardSmallLoaderImage', getDashboardSmallLoaderImage)
        .directive('getAncodeLoaderLg', getAncodeLoaderLg)
        .directive('getAncodeLoaderMd', getAncodeLoaderMd)
        .directive('getAncodeLoaderSm', getAncodeLoaderSm)
        .directive('getAncodeLoaderXs', getAncodeLoaderXs)
        .directive('getAncodeLoaderContainerOverlay', getAncodeLoaderContainerOverlay)
        .directive('getAncodeLoaderFullOverlay', getAncodeLoaderFullOverlay)
        .directive('angularFilemanager', ['$parse', 'fileManagerConfig', 'slmlpService', function ($parse, fileManagerConfig, slmlpService) {
                return {
                    restrict: 'EA',
                    templateUrl: fileManagerConfig.tplPath + '/main.html'
                };
            }])
        .directive('angularFilemanagerData', ['$parse', '$timeout', 'fileManagerConfig', 'slmlpService', function ($parse, $timeout, fileManagerConfig, slmlpService) {
                    return {
                        restrict: 'EA',
                        bindToController: true,
                        templateUrl: function (scope, element, attr) {
                            return fileManagerConfig.tplPath + '/dataset-main.html';

                        },
                        link: function(scope, element, iAttrs, controller) {
                            element.bind('onload', function() {
                                scope.$apply();
                                console.log(scope.fileNavigator);
                                console.log(scope.fileNavigator.fileList.length)
                            });


                        }
                    };
        }])
        .directive('ngFile', ['$parse', function ($parse) {
                return {
                    restrict: 'A',
                    link: function (scope, element, attrs) {
                        var model = $parse(attrs.ngFile);
                        var modelSetter = model.assign;

                        element.bind('change', function () {
                            scope.$apply(function () {
                                modelSetter(scope, element[0].files);
                            });
                        });
                    }
                };
            }])
        .directive('customOnChange', function () {
            return {
                restrict: 'A',
                link: function (scope, element, attrs) {
                    var onChangeFunc = scope.$eval(attrs.customOnChange);
                    element.bind('change', scope.$eval(attrs.customOnChange));

                }
            };
        })
        .directive('focusOn', function () {
            return function (scope, elem, attr) {
                scope.$on(attr.focusOn, function (e) {
                    elem[0].focus();
                });
            };
        })
        .directive('ngRightClick', ['$parse', function ($parse) {
                return function (scope, element, attrs) {
                    var fn = $parse(attrs.ngRightClick);
                    element.bind('contextmenu', function (event) {
                        scope.$apply(function () {
                            event.preventDefault();
                            fn(scope, {$event: event});
                        });
                    });
                };
            }])
            .directive('noHtml', function($filter){
              return function(scope, element, attrs){
                var html = attrs.data;
                var text = angular.element("<div>").html(html).text();
                // post filter
                var filter = attrs.postFilter;
                var result = $filter(filter)(text);

                // apending html
                element.html(result);
              };
            });
