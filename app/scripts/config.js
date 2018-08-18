/**
 * SLMLP - Project Management App
 *
 */
function config($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/index/dashboard/user");

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

            .state('projects', {
                abstract: true,
                url: "/projects",
                templateUrl: "views/common/content.html",
            })

            .state('projects.list', {
                url: "/list",
                templateUrl: "views/projects/list.html",
                data: {pageTitle: 'Projects List'}
            })
             .state('projects.status', {
                url: "/projects-status",
                templateUrl: "views/projects/projects-status.html",
                data: {pageTitle: 'Projects Status'}
            })
             .state('projects.resource-usage', {
                url: "/resource-usage",
                templateUrl: "views/projects/resource-usage.html",
                data: {pageTitle: 'Resource Usage'}
            })

            .state('projects.grid', {
                url: "/grid-view",
                templateUrl: "views/projects/grid.html",
                data: {pageTitle: 'Projects Grid'}
            })

            .state('projects.create', {
                url: "/create",
                templateUrl: "views/projects/create/create.html",
                data: {pageTitle: 'Create Projects'}
            })

            .state('projects.edit', {
                url: "edit/:view/:id",
                templateUrl: "views/projects/edit/edit.html",
                data: {pageTitle: 'Edit Project'}
            })

            .state('projects.details', {
                url: "/details",
                templateUrl: "views/projects/project-details/details.html",
                data: {pageTitle: 'Details'}
            })

            .state('projects.data-transformation', {
                url: "/data-transformation",
                templateUrl: "views/projects/data-transformation/data-transformation.html",
                data: {pageTitle: 'Data Transformation'}
            })

            .state('projects.modeling', {
                url: "/modeling",
                templateUrl: "views/projects/modeling/modeling.html",
                data: {pageTitle: 'Modeling'}
            })

            .state('projects.scoring', {
                url: "/scoring",
                templateUrl: "views/projects/scoring/scoring.html",
                data: {pageTitle: 'Scoring'}
            })

            .state('projects.explore-analyze', {
                url: "/files/dataset/explore-analyze",
                templateUrl: "views/projects/project-details/dataset/explore-analyze/explore-analyze.html",
                data: {pageTitle: 'Explore And Analyze'}
            })

            .state('department', {
                abstract: true,
                url: "/department",
                templateUrl: "views/common/content.html",
            })

            .state('department.list', {
                url: "/list",
                templateUrl: "views/departments/list.html",
                data: {pageTitle: 'Departments List'}
            })

            .state('user-accounts', {
                abstract: true,
                url: "/user-accounts",
                templateUrl: "views/common/content.html",
            })

            .state('user-accounts.list', {
                url: "/list",
                templateUrl: "views/user-accounts/list.html",
                data: {pageTitle: 'User Account List'}
            })

            .state('roles', {
                abstract: true,
                url: "/roles",
                templateUrl: "views/common/content.html",
            })

            .state('roles.list', {
                url: "/list",
                templateUrl: "views/roles/list.html",
                data: {pageTitle: 'Roles List'}
            })

            .state('roles.create', {
                url: "/create",
                templateUrl: "views/roles/create.html",
                data: {pageTitle: 'Create'}
            })

            .state('project-details', {
                abstract: true,
                url: "/project-details",
                templateUrl: "views/common/project-details.html",
            })

//            .state('project-details.summary', {
//                url: "/summary",
//                templateUrl: "views/projects/project-details/summary.html",
//                data: {pageTitle: 'Project Summary'}
//            })

            .state('project-details.summary', {
                url: "/summary/:id",
                templateUrl: "views/projects/project-details/summary.html",
                data: {pageTitle: 'Project Summary'}
            })

            .state('dataset', {
                abstract: true,
                url: "/project-details/dataset",
                templateUrl: "views/common/project-details.html",
            })

            .state('dataset.list', {
                url: "/list",
                templateUrl: "views/projects/project-details/dataset/list.html",
                data: {pageTitle: 'Dataset List View'}
            })

            .state('dataset.datasetConfigList', {
                url: "/datasetConfigList",
                templateUrl: "views/projects/project-details/dataset/datasetConfigList.html",
                data: {pageTitle: 'DatasetCnfig List View'}
            })

            .state('file-manager.explore-csv-to-table', {
                url: "/explore-csv-to-table",
                templateUrl: "views/projects/project-details/file-manager/explore-csv-to-table.html",
                data: {pageTitle: 'Explored Data List View'}
            })

            .state('dataset.lists', {
                url: "/list/:id",
                templateUrl: "views/projects/project-details/dataset/list.html",
                data: {pageTitle: 'Dataset List View'}
            })

            .state('dataset.view', {
                url: "/view",
                templateUrl: "views/projects/project-details/dataset/view-dataset.html",
                data: {pageTitle: 'View Dataset'}
            })
            .state('dataset.status', {
                url: "/status",
                templateUrl: "views/projects/project-details/dataset/status.html",
                data: {pageTitle: 'Status'}
            })

            .state('dataset.explore', {
                url: "/explore",
                templateUrl: "views/projects/project-details/dataset/explore-analyze/explore-data.html",
                data: {pageTitle: 'Explore Dataset'}
            })

            .state('file-manager', {
                abstract: true,
                url: "/project-details/file-manager",
                templateUrl: "views/common/project-details.html",
            })

            .state('file-manager.list', {
                url: "/list",
                templateUrl: "views/projects/project-details/file-manager/list.html",
                data: {pageTitle: 'File Manager'}
            })
             .state('file-manager.status', {
                url: "/modified-list",
                templateUrl: "views/projects/project-details/file-manager/status.html",
                data: {pageTitle: 'Status'}
            })


            .state('model', {
                abstract: true,
                url: "/project-details/model",
                templateUrl: "views/common/project-details.html",
            })

            .state('model.list', {
                url: "/list",
                templateUrl: "views/projects/project-details/model/list.html",
                data: {pageTitle: 'Model List'}
            })
             .state('model.files-list-new-model', {
                url: "/model-files-list",
                templateUrl: "views/projects/project-details/model/create/files-list-new-model.html",
                data: {pageTitle: 'Files List'}
            })

            .state('model.create', {
                url: "/create/automatic",
                templateUrl: "views/projects/project-details/model/create.html",
                data: {pageTitle: 'Create Model Automatic'}
            })

            .state('model.import', {
                url: "/import/model",
                templateUrl: "views/projects/project-details/model/import.html",
                data: {pageTitle: 'Create Model Automatic'}
            })

            .state('model.details', {
                url: "/details",
                templateUrl: "views/projects/project-details/model/details/details.html",
                data: {pageTitle: 'Model Details'}
            })

            .state('model.details-predict', {
                url: "/details/predicted-data",
                templateUrl: "views/projects/project-details/model/details/dataset/predicted-data.html",
                data: {pageTitle: 'Predicted Data'}
            })

            .state('model.train', {
                url: "/train",
                templateUrl: "views/projects/project-details/model/train/train.html",
                data: {pageTitle: 'Model - New Train'}
            })
            .state('model.files-list', {
                url: "/files-list",
                templateUrl: "views/projects/project-details/model/train/files-list.html",
                data: {pageTitle: 'Model - Files List'}
            })

            .state('analyze', {
                abstract: true,
                url: "/project-details/analyze",
                templateUrl: "views/common/project-details.html",
            })

            .state('analyze.list', {
                url: "/list",
                templateUrl: "views/projects/project-details/analyze/list.html",
                data: {pageTitle: 'Analyze'}
            })

            .state('analyze.details', {
                url: "/details",
                templateUrl: "views/projects/project-details/analyze/details.html",
                data: {
                    pageTitle: 'Analyze Details',
                    bodyClasses: 'mini-navbar'
                }
            })

            .state('launchers', {
                abstract: true,
                url: "/project-details/launchers",
                templateUrl: "views/common/project-details.html",
            })

            .state('launchers.list', {
                url: "/list",
                templateUrl: "views/projects/project-details/launchers/list.html",
                data: {pageTitle: 'Launchers'}
            })

            .state('launchers.list-container-progress', {
                url: "/list/:containerId",
                templateUrl: "views/projects/project-details/launchers/list.html",
                data: {pageTitle: 'Launchers'}
            })

            .state('scheduled-deployments', {
                abstract: true,
                url: "/project-details/scheduled-deployments",
                templateUrl: "views/common/project-details.html",
            })

            .state('scheduled-deployments.list', {
                url: "/list",
                templateUrl: "views/projects/project-details/scheduled-deployments/list.html",
                data: {pageTitle: 'Scheduled Deployments'}
            })

            .state('scoring', {
                abstract: true,
                url: "/project-details/scoring",
                templateUrl: "views/common/project-details.html",
            })

            .state('scoring.list', {
                url: "/list",
                templateUrl: "views/projects/project-details/scoring/list.html",
                data: {pageTitle: 'Scoring'}
            })

            .state('scoring.details', {
                url: "/:id1/details",
                templateUrl: "views/projects/project-details/scoring/details.html",
                data: {pageTitle: 'Scoring Details'}
            })

            .state('deployments', {
                abstract: true,
                url: "/project-details/deployments",
                templateUrl: "views/common/project-details.html",
            })

            .state('deployments.create', {
                url: "/project-details/deployment/create",
                templateUrl: "views/projects/project-details/deployments/create.html",
                data: {pageTitle: 'Create Deployment'}
            })

            .state('deployments.list', {
                url: "/list",
                templateUrl: "views/projects/project-details/deployments/list.html",
                data: {pageTitle: 'Deployments'}
            })

            .state('deployments.scheduled-deployments', {
                url: "/scheduled-deployments",
                templateUrl: "views/projects/project-details/deployments/scheduled-deployments/list.html",
                data: {pageTitle: 'Scheduled Deployments'}
            })

            .state('deployments.sd-details', {
                url: "/scheduled-deployments/details",
                templateUrl: "views/projects/project-details/deployments/scheduled-deployments/details/sd-details.html",
                data: {pageTitle: 'Scheduled Deployments Details'}
            })

            .state('deployments.dashboard', {
                url: "/dashboard",
                templateUrl: "views/projects/project-details/deployments/dashboard/list.html",
                data: {pageTitle: 'Dashboard'}
            })

            .state('deployments.new-dashboard', {
                url: "/dashboard/create",
                templateUrl: "views/projects/project-details/deployments/dashboard/create.html",
                data: {
                    pageTitle: 'Create Dashboard',
                    bodyClasses: 'mini-navbar'
                }
            })
            .state('deployments.project-dashboard', {
                url: "/dashboard/project-dashboard/:deploymentId",
                templateUrl: "views/projects/project-details/deployments/dashboard/project-dashboard.html",
                data: {
                    pageTitle: 'Project Dashboard',
                    bodyClasses: 'mini-navbar'
                }
            })
            .state('logs', {
                abstract: true,
                url: "/project-details/logs",
                templateUrl: "views/common/project-details.html",
            })

            .state('logs.log', {
                url: "/log",
                templateUrl: "views/projects/project-details/logs.html",
                data: {pageTitle: 'Logs'}
            })
}
angular
        .module('slmlp')
        .config(config)
        .filter('newlines', function () {
            return function(text) {
              return text.replace(/\n/g, '<br/>');
            };
        })
        .run(function ($rootScope, $state, slmlpService) {
            $rootScope.selectedPath = "";
            $rootScope.$state = $state;
             slmlpService.webSocket.connect(slmlpService.appConfig.SOCKET_URL + 'socket/ws');
        });
