/**
 * APPFiss - Serverless ML Platform Management App
 *
 */
function config($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/main/login");

    $stateProvider

            .state('common', {
                abstract: true,
                url: "/common",
                templateUrl: "views/common/blank.html",
            })

            .state('common.login', {
                url: "/login",
                templateUrl: "views/login.html",
                data: {pageTitle: 'Login'}
            })

            .state('common.register', {
                url: "/register",
                templateUrl: "views/register.html",
                data: {pageTitle: 'Register'}
            })

            .state('index', {
                abstract: true,
                url: "/index",
                templateUrl: "views/common/content.html",
            })
            .state('app', {
                abstract: true,
                url: "/main",
                templateUrl: "views/common/blank.html",
            })
            
            .state('app.login', {
                url: "/login",
                templateUrl: "views/login.html",
            })

            .state('index.user-dashboard', {
                url: "/dashboard/user",
                templateUrl: "views/user-dashboard.html",
                data: {pageTitle: 'User Dashboard'}
            })

            .state('user', {
                abstract: true,
                url: "/user",
                templateUrl: "views/common/content.html",
            })

            .state('user.profile', {
                url: "/profile",
                templateUrl: "views/user-profile.html",
                data: {pageTitle: 'User Profile'}
            })

            .state('user.activities', {
                url: "/activities",
                templateUrl: "views/activities.html",
                data: {pageTitle: 'User Activities'}
            })


            .state('user-accounts', {
                abstract: true,
                url: "/user-accounts",
                templateUrl: "views/common/content.html",
            })

            .state('user-accounts.list', {
                url: "/list",
                templateUrl: "views/user-accounts/users.html",
                data: {pageTitle: 'User Account List'}
            })
            
            .state('user-accounts.dashboard', {
                url: "/view/:id",
                templateUrl: "views/user-accounts/users-dashboard.html",
                data: {pageTitle: 'User Account List'}
            })
            
            .state('user-accounts.edit', {
                url: "/edit/:id",
                templateUrl: "views/user-accounts/edit-users.html",
                data: {pageTitle: 'User Account List'}
            })

            .state('configuration', {
                abstract: true,
                url: "/configuration",
                templateUrl: "views/common/content.html"
            })

            .state('configuration.general', {
                url: "/general",
                templateUrl: "views/configuration/general.html",
                data: {pageTitle: 'General Configurations'},
                ncyBreadcrumb: {
                    label: 'General', // angular-breadcrumb's configuration
                    parent: 'configuration',
                }
            })

            .state('configuration.host', {
                url: "/host",
                templateUrl: "views/configuration/host/host_list.html",
                data: {pageTitle: 'Cloudstack Configurations'}
            })
                        

            .state('configuration.general.themesetting', {
                url: "/themesettings",
                templateUrl: "views/configuration/general/theme-settings.html",
                data: {pageTitle: 'Theme Settings'}
            })
           
            .state('configuration.general.staffs', {
                url: "/staffs",
                templateUrl: "views/configuration/general/staffs.html",
                data: {pageTitle: 'Staffs'}
            })
            .state('configuration.general.addstaffs', {
                url: "/staffs/add",
                templateUrl: "views/configuration/general/add-staffs.html",
                data: {pageTitle: 'Add Staffs', specialClass: 'gray-bg'},
            })

            .state('configuration.general.editstaffs', {
                url: "/staffs/edit/:id",
                templateUrl: "views/configuration/general/edit-staffs.html",
                data: {pageTitle: 'Edit Staffs', specialClass: 'gray-bg'}
            })

           .state('configuration.host.list', {
                url: "/list",
                templateUrl: "views/configuration/host/host_list.html",
                data: {pageTitle: 'host list', specialClass: 'gray-bg'},
                
            })
          
            .state('file-manager', {
                abstract: true,
                url: "/file-manager",
                templateUrl: "views/common/content.html",
            })

            .state('file-manager.list', {
                url: "/list",
                templateUrl: "views/file-manager/list.html",
                data: {pageTitle: 'File Manager'}
            })
             .state('file-manager.status', {
                url: "/modified-list",
                templateUrl: "views/file-manager/status.html",
                data: {pageTitle: 'Status'}
            })


            .state('platform', {
                abstract: true,
                url: "/machine/learning/platform",
                templateUrl: "views/common/content.html",
            })

            .state('platform.list', {
                url: "/list",
                templateUrl: "views/platforms/list.html",
                data: {pageTitle: 'Launchers'}
            })

            .state('platform.list-container-progress', {
                url: "/list/:containerId",
                templateUrl: "views/platforms/list.html",
                data: {pageTitle: 'Launchers'}
            })

         
            .state('activity', {
                abstract: true,
                url: "/activity",
                templateUrl: "views/common/content.html",
            })

            .state('activity.log', {
                url: "/log",
                templateUrl: "views/activities.html",
                data: {pageTitle: 'Adufit Logs'}
            })
}
angular
        .module('appfiss')
        .config(config)
        .filter('newlines', function () {
            return function(text) {
              return text.replace(/\n/g, '<br/>');
            };
        })
        .run(function ($rootScope, $state, appfissService) {
            $rootScope.selectedPath = "";
            $rootScope.$state = $state;
             appfissService.webSocket.connect(appfissService.appConfig.SOCKET_URL + 'container/container-ws');
        });
