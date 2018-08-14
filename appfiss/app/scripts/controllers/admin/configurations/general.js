function generalConfigurationCtrl($scope, appfissService, toastr) {
    $scope.shouldBeOpen = true;
    $scope.sshkeyForm = {
        submitted: false
    };
    $scope.general = {
    }

    $scope.generalConfigUpdatedObject = {
        findByUpdatedDate: function () {
            var hasResult = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_GET, appfissService.appConfig.APP_URL + "generalsettings/updatedDate" + "?lang=en");
            if (!angular.isUndefined(hasResult)) {
                hasResult.then(function (result) {
                    $scope.configUpdatedDateList = result;
                    angular.forEach($scope.configUpdatedDateList, function (obj, key) {
                        if (obj.name === 'DATE_FORMAT') {
                            if (obj.dateFormat === "DDMMYYYY") {
                                $scope.defaultDateFormat = "dd/MM/yyyy";
                            } else if (obj.dateFormat === "MMDDYYYY") {
                                $scope.defaultDateFormat = "MM/dd/yyyy";
                            } else if (obj.dateFormat === "YYYYDDMM") {
                                $scope.defaultDateFormat = "yyyy/dd/MM";
                            } else if (obj.dateFormat === "YYYYMMDD") {
                                $scope.defaultDateFormat = "yyyy/MM/dd";
                            }
                        }
                    });
                });
            }
        }
    }
    $scope.generalConfigUpdatedObject.findByUpdatedDate();
}
;

function notificationConfigurationCtrl(uploadFile, $scope, $stateParams, $cookies, $translate, appfissService, toastr, localStorageService) {
    $scope.global = appfissService.appConfig;
    $scope.config = {};

    $scope.formElements = {};
    $scope.serviceConfiguration = {};
    $scope.formSubmitted = false;
    $scope.displayDate = function () {
        var date = new Date();
        $scope.date = $filter('date')(new Date(), 'yyyy-MM-dd');
        $scope.time = $filter('date')(new Date(), 'HH:mm');
    }
    $scope.eventsTemplateList = [];
    $scope.alertConfiguration = {};
    $scope.serviceList = [];
    $scope.configElements = {
        category: $stateParams.category,
        oneItemSelected: {},
        selectedAll: {}
    };
    $scope.files = [];
    $scope.test = 0;
    // Service List
    $scope.listService = function () {
        var hasService = appfissService.promiseAjax.httpRequestZabbix(appfissService.appConfig.HTTP_GET, appfissService.appConfig.ZABBIX_APP_URL + "api/notification/service/list?status=ALL");
        hasService.then(function (result) {
            $scope.serviceConfigurationList = result;
        });
    };
    $scope.listService();

    $scope.listActiveService = function () {
        var hasService = appfissService.promiseAjax.httpRequestZabbix(appfissService.appConfig.HTTP_GET, appfissService.appConfig.ZABBIX_APP_URL + "api/notification/service/list?status=ENABLED");
        hasService.then(function (result) {
            $scope.serviceList = result;
        });
    };

    $scope.eventLists = function () {
        var hasEvent = appfissService.promiseAjax.httpRequestZabbix(appfissService.appConfig.HTTP_GET, appfissService.appConfig.ZABBIX_APP_URL + "api/notification/event/list");
        hasEvent.then(function (result) {  // this is only run after $http completes0
            $scope.eventList = result;
        });
    };
    $scope.eventLists();

    $scope.emailEventList = function (eventName, language) {
        var hasEventList = appfissService.promiseAjax.httpRequestZabbix(appfissService.appConfig.HTTP_GET, appfissService.appConfig.ZABBIX_APP_URL + "api/notification/event/listbyevent?eventName=" + eventName.eventName);
        hasEventList.then(function (result) {
            $scope.eventsList = result;
        });
        $scope.config.subject = "";
        var hasEventTestList = appfissService.promiseAjax.httpRequestZabbix(appfissService.appConfig.HTTP_GET, appfissService.appConfig.ZABBIX_APP_URL + "api/notification/email/listbyeventname?eventName=" + eventName.eventName + "&language=" + language);
        hasEventTestList.then(function (result) {
            $scope.eventsTemplateList = result;
            console.log(result);
            if (result.length > 0) {
                $scope.config.subject = $scope.eventsTemplateList[0].subject;
            }
        });
    };


    $scope.shouldBeOpen = true;
    $scope.serviceNameList = [
        "Zabbix",
        "Bacula",
        "Hardware Monitoring"
    ];
    $scope.alertTypeList = [
        "EMAIL"
    ];
    $scope.defaultLanguageList = [
        "English",
        "Portuguese"
    ];
    $scope.eventsList = [
        "CAPACITY",
        "SUSPENDED",
        "SYSTEM_ERROR"
    ];
    $scope.statusList = [
        "ENABLED",
        "DISABLED"
    ];

    $scope.serviceChange = function (service) {
        $scope.formSubmitted = false;
        if (service != undefined && $scope.alertConfiguration.confType != undefined) {
            $scope.list($scope.alertConfiguration.confType, service);
        } else if (service == undefined) {
            $scope.alertConfiguration.service = service;
            $scope.alertConfiguration.confType = undefined;
        } else if (service != undefined && $scope.alertConfiguration.confType == undefined) {
            $scope.list("EMAIL", service);
        }
    };

    $scope.alertTypeChange = function (type) {
        $scope.formSubmitted = false;
        if (type != undefined && $scope.alertConfiguration.service != undefined) {
            $scope.list(type, $scope.alertConfiguration.service);
        } else if (type == undefined) {
            $scope.alertConfiguration.confType = type;
        }
    };

    $scope.list = function (type, service) {
        var hasEmail = {};
        hasEmail = appfissService.promiseAjax.httpRequestZabbix(appfissService.appConfig.HTTP_GET, appfissService.appConfig.ZABBIX_APP_URL + "api/notification/alert-configuration/list?alertType=" + type + "&serviceId=" + service.serviceId);
        hasEmail.then(function (result) {
            if (result.length > 0) {
                $scope.alertConfiguration = result[0];
                angular.forEach($scope.serviceConfigurationList, function (value, key) {
                    if (value.serviceId === $scope.alertConfiguration.serviceId) {
                        $scope.alertConfiguration.service = value;
                    }
                });
            } else {
                $scope.alertConfiguration = {};
                $scope.alertConfiguration.service = service;
                $scope.alertConfiguration.confType = type;
            }
        });

    };

    $scope.notificationConfigObject = {

        notificationDiscovery: function (size) {
            appfissService.dialogService.openDialog("app/views/configuration/notification/notification-discovery.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.ok = function (key) {
                                $scope.showLoader = true;
                                var hasService = appfissService.promiseAjax.httpRequestZabbix(appfissService.appConfig.HTTP_GET, appfissService.appConfig.ZABBIX_APP_URL + "api/notification/discovery?key=" + key);
                                hasService.then(function (result) {
                                    $scope.showLoader = false;
                                    toastr.pop({type: "success", body: "Sync done for " + key, showCloseButton: true});
                                }).catch(function (result) {
                                    $scope.showLoader = false;
                                    if (result.data.globalError != null) {
                                        var msg = result.data.globalError[0];
                                        toastr.pop({type: "error", body: msg, showCloseButton: true});
                                    }
                                });
                            };

                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
                        }]);
        },
        notificationType: function (size) {
            $scope.listActiveService();
            appfissService.dialogService.openDialog("app/views/configuration/notification/notification-type.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.ok = function (service) {
                                var hasType = {};
                                if (service != undefined) {
                                    hasType = appfissService.promiseAjax.httpRequestZabbix(appfissService.appConfig.HTTP_GET, appfissService.appConfig.ZABBIX_APP_URL + "api/notification/type/list?serviceId=" + service.serviceId);
                                    hasType.then(function (result) {
                                        $scope.notificationType = result;
                                    });
                                } else {
                                    $scope.notificationType = {};
                                }

                            };

                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
                        }]);
        },
        alertsConfig: function (size) {
            $scope.alertConfiguration = {};
            appfissService.dialogService.openDialog("app/views/configuration/notification/alerts.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.saveAlertConfig = function (form, email) {
                                $scope.formSubmitted = true;
                                if (form.$valid) {
                                    $scope.showLoader = true;
                                    var email = angular.copy($scope.alertConfiguration);
                                    if (email.confType == 'EMAIL') {
                                        email.alertTypeId = 1;
                                    } else if (email.confType == 'SMS') {
                                        email.alertTypeId = 2;
                                    } else if (email.confType == 'WECHAT') {
                                        email.alertTypeId = 3;
                                    } else if (email.confType == 'WEBHOOK') {
                                        email.alertTypeId = 4;
                                    }
                                    email.serviceId = email.service.serviceId;
                                    var hasEmail = appfissService.promiseAjax.httpRequestZabbix(appfissService.appConfig.HTTP_POST, appfissService.appConfig.ZABBIX_APP_URL + "api/notification/alert/configuration", email);
                                    hasEmail.then(function (result) {
                                        $scope.showLoader = false;
                                        $scope.list(result.confType, email.service);
                                        var displayMessage = $translate.instant("add.success");
                                        toastr.pop({type: "success", body: displayMessage, showCloseButton: true});
                                        $scope.cancel();
                                    }).catch(function (result) {
                                        $scope.showLoader = false;
                                        if (!angular.isUndefined(result.data)) {
                                            if (result.data.fieldErrors != null) {
                                                $scope.showLoader = false;
                                                angular.forEach(result.data.fieldErrors, function (errorMessage, key) {
                                                    $scope.emailConfigForm[key].$invalid = true;
                                                    $scope.emailConfigForm[key].errorMessage = errorMessage;
                                                });
                                            }
                                            if (result.data.globalError != null) {
                                                var msg = result.data.globalError[0];
                                                toastr.pop({type: "error", body: msg, showCloseButton: true});
                                            }

                                        }
                                    });
                                }
                            };

                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                        }]);
        },
        emailTemplateConfig: function (size) {
            $scope.config = {};
            appfissService.dialogService.openDialog("app/views/configuration/notification/email-template.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.ok = function () {
                                $uibModalInstance.close();
                            };

                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                            $scope.checkfile = function (file, form, emails, file1) {
                                var arrayTest = [file, file1];
                                if ((angular.isUndefined(file) || angular.isUndefined(file1)) && !angular.isUndefined($scope.eventsTemplateList[0])) {
                                    if (emails.subject && emails.eventName != null) {
                                        if (file != null) {
                                            emails.englishLanguage = "ENGLISH";
                                        }
                                        if (file1 != null) {
                                            emails.otherLanguage = "PORTUGUESE";
                                        }
                                        emails.recipientType = "USER";
                                        emails.eventName = emails.eventName.eventName;
                                        uploadFile.upload(arrayTest, emails, appfissService.promiseAjax.httpTokenRequest, appfissService.appConfig, $cookies, localStorageService, appfissService.appConfig.ZABBIX_APP_URL + "api/notification/uploadFile");
                                        var displayMessage = $translate.instant("add.success");
                                        toastr.pop({type: "success", body: displayMessage, showCloseButton: true});
                                        $scope.cancel();
                                    }
                                } else if (!angular.isUndefined(file) || !angular.isUndefined(file1)) {
                                    if ((!angular.isUndefined(file) && file.type != "text/html") || (!angular.isUndefined(file1) && file1.type != "text/html")) {
                                        var displayMessage = $translate.instant("please.upload.html");
                                        toastr.pop({type: "success", body: displayMessage, showCloseButton: true});
                                        return false;
                                    } else {
                                        if (emails.subject && emails.eventName != null) {
                                            if (file != null) {
                                                emails.englishLanguage = "ENGLISH";
                                            }
                                            if (file1 != null) {
                                                emails.otherLanguage = "PORTUGUESE";
                                            }
                                            emails.recipientType = "USER";
                                            emails.eventName = emails.eventName.eventName;
                                            uploadFile.upload(arrayTest, emails, appfissService.promiseAjax.httpTokenRequest, appfissService.appConfig, $cookies, localStorageService, appfissService.appConfig.ZABBIX_APP_URL + "api/notification/uploadFile");
                                            var displayMessage = $translate.instant("add.success");
                                            toastr.pop({type: "success", body: displayMessage, showCloseButton: true});
                                            $scope.cancel();
                                        }
                                    }
                                }
                            };

                            $scope.saveEmailTemplate = function (form, emails, file, file1) {
                                $scope.formSubmitted = true;
                                $scope.checkfile(file, form, emails, file1);
                            };
                        }]);
        },
        addService: function (size, service) {
            if (service != null) {
                $scope.serviceConfiguration = service;
            }
            appfissService.dialogService.openDialog("app/views/configuration/notification/add-service.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.ok = function () {
                                $uibModalInstance.close();
                            };

                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };
                            $scope.saveService = function (form, serviceConfiguration) {
                                $scope.formSubmitted = true;
                                if (form.$valid) {
                                    $scope.showLoader = true;
                                    var service = angular.copy($scope.serviceConfiguration);
                                    var hasService = {}
                                    if (service.id == null) {
                                        hasService = appfissService.promiseAjax.httpRequestZabbix(appfissService.appConfig.HTTP_POST, appfissService.appConfig.ZABBIX_APP_URL + "api/notification/service/add", service);
                                    } else {
                                        hasService = appfissService.promiseAjax.httpRequestZabbix(appfissService.appConfig.HTTP_PUT, appfissService.appConfig.ZABBIX_APP_URL + "api/notification/service/update", service);
                                    }
                                    hasService.then(function (result) {
                                        $scope.showLoader = false;
                                        var displayMessage = "";
                                        if (service.id == null) {
                                            displayMessage = displayMessage + $translate.instant("add.success");
                                        } else {
                                            displayMessage = displayMessage + $translate.instant("update.success");
                                        }
                                        $scope.serviceConfiguration = {};
                                        delete $scope.serviceConfiguration;
                                        $scope.formSubmitted = false;
                                        $scope.cancel();
                                        $scope.listService();
                                        toastr.pop({type: "success", body: displayMessage, showCloseButton: true});
                                    }).catch(function (result) {
                                        $scope.showLoader = false;
                                        if (!angular.isUndefined(result.data)) {
                                            if (result.data.fieldErrors != null) {
                                                $scope.showLoader = false;
                                                angular.forEach(result.data.fieldErrors, function (errorMessage, key) {
                                                    $scope.serviceConfigForm[key].$invalid = true;
                                                    $scope.serviceConfigForm[key].errorMessage = errorMessage;
                                                });
                                                if (result.data.globalError != null) {
                                                    var msg = result.data.globalError[0];
                                                    toastr.pop({type: "error", body: msg, showCloseButton: true});
                                                }
                                                $scope.cancel();
                                            }
                                        }
                                    });
                                }
                            };
                        }]);
        }
    }
    //$scope.notificationConfigObject.notificationDiscovery();
}
;

function emailConfigurationCtrl($scope, $translate, appfissService, toastr, localStorageService) {
    $scope.emailConfigForm = {
        submitted: false
    };
    $scope.emailConfig = {};
    $scope.emailConfigObject = {
        list: [],
        languageList: [],
        findAllLanguage: function () {
            var hasResult = appfissService.crudService.listAll("languages/list");
            hasResult.then(function (result) {
                $scope.emailConfigObject.languageList = result;
                angular.forEach($scope.emailConfigObject.languageList, function (obj, key) {
                    $scope.emailConfig.emailLanguage = obj;

                });
            });
        },
        findByDefaultLanguage: function () {
            var hasResult = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_GET, appfissService.appConfig.APP_URL + "languages/default" + "?lang=" + localStorageService.get('adminDefaultLanguage').languageCode);
            if (!angular.isUndefined(hasResult)) {
                hasResult.then(function (result) {
                    $scope.emailConfig.emailLanguageId = result.id;
                    $scope.emailConfig.emailLanguage = result;
                    $scope.emailConfigObject.findByLanguage(result.id);
                });
            }
        },
        findByLanguage: function (languageId) {
            var emailLanguage = angular.copy($scope.emailConfig.emailLanguage);
            if (!angular.isUndefined(languageId)) {
                var hasResult = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_GET, appfissService.appConfig.APP_URL + "email-configuration/findbylanguage?languageid=" + languageId +
                        "&lang=" + localStorageService.get('adminDefaultLanguage').languageCode + "&sortBy=-id");
                hasResult.then(function (result) {
                    if (result == "") {
                        $scope.emailConfig = {};
                    } else {
                        $scope.emailConfig = result;
                    }
                    angular.forEach($scope.emailConfigObject.languageList, function (obj, key) {
                        if (obj.id == emailLanguage.id)
                            $scope.emailConfig.emailLanguage = obj;

                    });
                });
            }
        },
        open: function (size) {
            $scope.emailConfigObject.findAllLanguage();
            $scope.emailConfigObject.findByDefaultLanguage();
            appfissService.dialogService.openDialog("app/views/configuration/general/email-config.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.ok = function () {
                                $uibModalInstance.close();
                            };

                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                            $scope.changeDetails = function (languageId) {
                                $scope.emailConfigObject.findByLanguage(languageId);
                            }

                            $scope.save = function (emailConfigObject) {
                                var emailConfig = angular.copy(emailConfigObject);
                                if (!angular.isUndefined(emailConfig.emailLanguage)) {
                                    emailConfig.emailLanguageId = emailConfig.emailLanguage.id;
                                    delete emailConfig.emailLanguage;
                                }
                                $scope.emailConfigForm.submitted = true;
                                if ($scope.emailConfigForm.$valid) {
                                    $scope.showLoader = true;
                                    var hasResult = appfissService.crudService.add("email-configuration", emailConfig);
                                    hasResult.then(function (result) {  // this is only run after $http completes
                                        $scope.emailConfigForm.submitted = false;
                                        $scope.showLoader = false;
                                        $uibModalInstance.dismiss('cancel');
                                        $scope.emailConfigObject.findByDefaultLanguage();
                                        toastr.pop({
                                            type: 'success',
                                            body: $translate.instant('updated-successfully'),
                                            showCloseButton: true

                                        });
                                        $scope.generalConfigUpdatedObject.findByUpdatedDate();
                                    }).catch(function (result) {
                                        $scope.showLoader = false;
                                        toastr.pop({
                                            type: 'error',
                                            body: 'Something went wrong.',
                                            showCloseButton: true
                                        });
                                    });
                                }
                            }
                        }]);
        }
    }
}
;

function languageCtrl($scope, appfissService, toastr, localStorageService, $translate, $state) {
    $scope.languageForm = {
        submitted: false
    };

    $scope.languageObject = {
        list: [],
        findAll: function () {
            var hasResult = appfissService.crudService.listAll("languages/list")
            hasResult.then(function (result) {
                $scope.languageObject.list = result;
                angular.forEach($scope.languageObject.list, function (obj, key) {
                    if (obj.isDefault) {
                        $scope.defaultLanguage = obj;
                        $scope.defaultLanguageOld = obj;
                    }
                });
            });
        },
        open: function (size) {
            $scope.languageObject.findAll();
            appfissService.dialogService.openDialog("app/views/configuration/general/language-selection.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.ok = function () {
                                $uibModalInstance.close();
                            };

                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                            $scope.save = function () {
                                $scope.languageForm.submitted = true;
                                if ($scope.languageForm.$valid) {
                                    $scope.showLoader = true;
                                    var language = $scope.defaultLanguage;
                                    language.isDefault = true;
                                    var hasResult = appfissService.crudService.update("languages", language);
                                    hasResult.then(function (result) {
                                        $scope.showLoader = false;
                                        $translate.use(language.languageCode);
                                        localStorageService.set('adminLanguage', language.languageCode);
                                        toastr.pop({
                                            type: 'success',
                                            body: $translate.instant('updated-successfully'),
                                            showCloseButton: true

                                        });
                                        setTimeout(function () {
                                            $state.reload();
                                        }, 1000);

                                        $scope.languageForm.submitted = false;
                                        $uibModalInstance.dismiss('cancel');
                                        $scope.languageObject.findAll();
                                    }).catch(function (result) {
                                        $scope.showLoader = false;
                                        toastr.pop({
                                            type: 'error',
                                            body: 'Something went wrong.',
                                            showCloseButton: true
                                        });
                                    });
                                }
                            }
                        }]);
        }
    }

}
;

function dateFormatCtrl($scope, appfissService, toastr, $state) {
    $scope.dateFormatConfigForm = {
        submitted: false
    };
    $scope.dateFormatObject = {
        list: [],
        findAll: function () {
            var hasResult = appfissService.crudService.listAll("generalsettings/list")
            hasResult.then(function (result) {
                if (!angular.isUndefined(result[0])) {
                    $scope.dateFormat = result[0];
                    if (result[0].format === "DDMMYYYY") {
                        result[0].format = "DD/MM/YYYY";
                    } else if (result[0].format === "MMDDYYYY") {
                        result[0].format = "MM/DD/YYYY";
                    } else if (result[0].format === "YYYYMMDD") {
                        result[0].format = "YYYY/MM/DD";
                    } else if (result[0].format === "YYYYDDMM") {
                        result[0].format = "YYYY/DD/MM";
                    }
                    $scope.dateFormat.format = result[0].format;
                }
            });
        },
        open: function (size) {
            $scope.dateFormatObject.findAll();
            appfissService.dialogService.openDialog("app/views/configuration/general/date-format.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.ok = function () {
                                $uibModalInstance.close();
                            };
                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                                $state.reload();
                            };

                            $scope.saveDateFormat = function (dateFormatObject) {
                                var dateFormat = angular.copy(dateFormatObject);
                                $scope.dateFormatConfigForm.submitted = true;
                                if ($scope.dateFormatConfigForm.$valid) {
                                    $scope.showLoader = true;

                                    if (dateFormat.format === "DD/MM/YYYY") {
                                        dateFormat.format = "DDMMYYYY";
                                    } else if (dateFormat.format === "MM/DD/YYYY") {
                                        dateFormat.format = "MMDDYYYY";
                                    } else if (dateFormat.format === "YYYY/MM/DD") {
                                        dateFormat.format = "YYYYMMDD";
                                    } else if (dateFormat.format === "YYYY/DD/MM") {
                                        dateFormat.format = "YYYYDDMM";
                                    }
                                    var hasServer = appfissService.crudService.add("generalsettings", dateFormat)
                                    hasServer.then(function (result) {  // this is only run after $http completes
                                        $scope.dateFormatConfigForm.submitted = false;
                                        $scope.showLoader = false;

                                        toastr.pop({
                                            type: 'success',
                                            body: 'Updated successfully.',
                                            showCloseButton: true

                                        });
                                        $uibModalInstance.dismiss('cancel');
                                        setTimeout(function () {
                                            $state.reload();
                                        }, 1000);
                                    }).catch(function (result) {
                                        $scope.showLoader = false;
                                        if (!angular.isUndefined(result.data)) {
                                            if (result.data.globalError !== '' && !angular.isUndefined(result.data.globalError)) {
                                                var msg = result.data.globalError[0];
                                                toastr.pop({
                                                    type: 'error',
                                                    body: msg,
                                                    showCloseButton: true
                                                });
                                            }
                                        }
                                    });

                                }
                            }
                        }]);

            $scope.dateFormatObject = {
                dateFormatList: {
                    "0": "DD/MM/YYYY",
                    "1": "MM/DD/YYYY",
                    "2": "YYYY/MM/DD",
                    "3": "YYYY/DD/MM"
                }
            };

        }
    }

}
;

function organizationCtrl($scope, $translate, appfissService, localStorageService, toastr) {
    $scope.organizationForm = {
        submitted: false
    };
    $scope.organization = {};
    $scope.orgResult = {};
    $scope.organizationObject = {
        list: [],
        languageList: [],
        findAllLanguage: function () {
            var hasResult = appfissService.crudService.listAll("languages/list");
            hasResult.then(function (result) {
                $scope.organizationObject.languageList = result;
            });
        },
        findByDefaultLanguage: function () {
            var hasResult = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_GET, appfissService.appConfig.APP_URL + "languages/default" + "?lang=" + localStorageService.get('adminDefaultLanguage').languageCode);
            hasResult.then(function (result) {

                $scope.organization.emailLanguageId = result.id;
                $scope.organization.emailLanguage = result;
                $scope.orgResult = result;
                $scope.organizationObject.findByLanguage(result.id);

            });

        },
        findByLanguage: function (languageId) {
            var emailLanguage = angular.copy($scope.organization.emailLanguage);
            if (!angular.isUndefined(languageId)) {
                var hasResult = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_GET, appfissService.appConfig.APP_URL + "organization/findbylanguage?languageid=" + languageId +
                        "&lang=" + localStorageService.get('adminDefaultLanguage').languageCode + "&sortBy=-id");
                hasResult.then(function (result) {
                    if (result == "") {
                        $scope.organization = {};
                    } else {
                        $scope.organization = result;
                    }
                    angular.forEach($scope.organizationObject.languageList, function (obj, key) {
                        if (obj.id == emailLanguage.id)
                            $scope.organization.emailLanguage = obj;

                    });
                });
            }
        },
        open: function (size) {
            $scope.organizationObject.findByDefaultLanguage();
            appfissService.dialogService.openDialog("app/views/configuration/general/organization.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                            $scope.ok = function () {
                                $uibModalInstance.close();
                            };

                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                            $scope.changeDetails = function (languageId) {
                                $scope.organizationObject.findByLanguage(languageId);
                            }

                            $scope.saveOrganization = function (organizationObject) {
                                var organization = angular.copy(organizationObject);
                                if (!angular.isUndefined(organization.emailLanguage)) {
                                    organization.emailLanguageId = organization.emailLanguage.id;
                                    organization.languageCode = organization.emailLanguage.languageCode;
                                    delete organization.emailLanguage;
                                }
                                $scope.organizationForm.submitted = true;
                                if ($scope.organizationForm.$valid) {
                                    $scope.showLoader = true;
                                    var hasResult = appfissService.crudService.add("organization", organization);
                                    hasResult.then(function (result) {  // this is only run after $http completes
                                        $scope.organizationForm.submitted = false;
                                        $scope.showLoader = false;
                                        $uibModalInstance.dismiss('cancel');
                                        $scope.organizationObject.findByDefaultLanguage();

                                        toastr.pop({
                                            type: 'success',
                                            body: $translate.instant('updated-successfully'),
                                            showCloseButton: true

                                        });
                                        $scope.generalConfigUpdatedObject.findByUpdatedDate();
                                    }).catch(function (result) {
                                        $scope.showLoader = false;
                                        if (!angular.isUndefined(result.data)) {
                                            if (result.data.globalError !== '' && !angular.isUndefined(result.data.globalError)) {
                                                var msg = result.data.globalError[0];
                                                toastr.pop({
                                                    type: 'error',
                                                    body: msg,
                                                    showCloseButton: true
                                                });
                                            }
                                        }
                                    });
                                }


                            }
                        }]);
        }
    }
    $scope.organizationObject.findAllLanguage();

}
;

function loginSecurityCtrl($scope, appfissService, $translate, toastr) {
    $scope.loginSecurityForm = {
        submitted: false
    };
    $scope.loginSecurityObject = {
        list: [],
        findAll: function () {
            var hasResult = appfissService.crudService.listAll("loginsecurity/list");
            hasResult.then(function (result) {
                $scope.loginsecurity = result[0];
                $scope.loginSecurityObject.list = result;

            }).catch(function (result) {
                if (!angular.isUndefined(result.data)) {
                    if (result.data.globalError !== '' && !angular.isUndefined(result.data.globalError)) {
                        var msg = result.data.globalError[0];
                        toastr.pop({
                            type: 'error',
                            body: msg,
                            showCloseButton: true
                        });
                    }
                }
            });
        },
        open: function (size) {
            $scope.loginSecurityObject.findAll();
            appfissService.dialogService.openDialog("app/views/configuration/general/login-security.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {

                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                            $scope.saveloginSecurity = function (loginSecurityObject) {
                                var loginsecurity = angular.copy(loginSecurityObject);
                                $scope.loginSecurityForm.submitted = true;
                                if ($scope.loginSecurityForm.$valid) {
                                    $scope.showLoader = true;
                                    var hasResult = appfissService.crudService.add("loginsecurity", loginsecurity)
                                    hasResult.then(function (result) {  // this is only run after $http completes
                                        $scope.loginSecurityForm.submitted = false;
                                        $scope.showLoader = false;
                                        $uibModalInstance.dismiss('cancel');
                                        $scope.loginSecurityObject.findAll();
                                        toastr.pop({
                                            type: 'success',
                                            body: $translate.instant('updated-successfully'),
                                            showCloseButton: true

                                        });
                                        $scope.generalConfigUpdatedObject.findByUpdatedDate();
                                    }).catch(function (result) {
                                        $scope.showLoader = false;
                                        toastr.pop({
                                            type: 'error',
                                            body: $translate.instant('went-wrong'),
                                            showCloseButton: true
                                        });
                                    });
                                }
                            }

                        }]);

        }
    }

}
;

function helpDeskCtrl($scope, appfissService, $translate, toastr) {
    $scope.helpDeskForm = {
        submitted: false
    };
    $scope.helpDeskObject = {
        list: [],
        findAll: function () {
            var hasResult = appfissService.promiseAjax.httpRequest(appfissService.appConfig.HTTP_GET,
                    "helpdesk/list")
            hasResult.then(function (result) {
                $scope.helpdesk = result[0];
                $scope.helpDeskObject.list = result;
            }).catch(function (result) {
                if (!angular.isUndefined(result.data)) {
                    if (result.data.globalError !== '' && !angular.isUndefined(result.data.globalError)) {
                        var msg = result.data.globalError[0];
                        toastr.pop({
                            type: 'error',
                            body: msg,
                            showCloseButton: true
                        });
                    }
                }
            });
        },
        open: function (size) {
            $scope.helpDeskObject.findAll();
            appfissService.dialogService.openDialog("app/views/configuration/general/helpdesk-setting.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {

                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                            $scope.savehelpDesk = function (helpDeskObject) {
                                var helpdesk = angular.copy(helpDeskObject);
                                $scope.helpdeskForm.submitted = true;
                                if ($scope.helpdeskForm.$valid) {
                                    $scope.showLoader = true;
                                    var hasResult = appfissService.crudService.add("helpdesk", helpdesk)
                                    hasResult.then(function (result) {  // this is only run after $http completes
                                        $scope.helpdeskForm.submitted = false;
                                        $scope.showLoader = false;
                                        $uibModalInstance.dismiss('cancel');
                                        $scope.helpDeskObject.findAll();
                                        toastr.pop({
                                            type: 'success',
                                            body: $translate.instant('updated-successfully'),
                                            showCloseButton: true
                                        });
                                        $scope.generalConfigUpdatedObject.findByUpdatedDate();
                                    }).catch(function (result) {
                                        $scope.showLoader = false;
                                        toastr.pop({
                                            type: 'error',
                                            body: $translate.instant('went-wrong'),
                                            showCloseButton: true
                                        });
                                    });
                                }
                            }
                        }]);
        }
    }
}
;

function emailTemplateCtrl($scope, $http, appfissService, localStorageService, $translate, toastr) {
    $scope.emailTemplateForm = {
        submitted: false
    };
    $scope.emailTemplateObject = {
        list: [],
        findAllLiterals: function () {
            var hasResult = appfissService.crudService.listAll("literals/list")
            hasResult.then(function (result) {
                $scope.eventsList = result;
            }).catch(function (result) {
                if (!angular.isUndefined(result.data)) {
                    if (result.data.globalError !== '' && !angular.isUndefined(result.data.globalError)) {
                        var msg = result.data.globalError[0];
                        toastr.pop({
                            type: 'error',
                            body: msg,
                            showCloseButton: true
                        });
                    }
                }
            });
        },
        findAllLanguage: function () {
            var hasResult = appfissService.crudService.listAll("languages/list");
            hasResult.then(function (result) {
                $scope.emailTemplateObject.list = result;
                angular.forEach($scope.emailTemplateObject.list, function (obj, key) {
                    $scope.emailTemplateObject.emailLanguage = obj;
                });
            });
        },
        open: function (size) {
            $scope.emailTemplateObject.findAllLiterals();
            $scope.emailTemplateObject.findAllLanguage();
            appfissService.dialogService.openDialog("app/views/configuration/general/email-template.html", size, $scope,
                    ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {

                            $scope.cancel = function () {
                                $uibModalInstance.dismiss('cancel');
                            };

                            $scope.selectLanguage = function (languageId) {
                                emailLanguageId = languageId;
                                delete  $scope.emailTemplate.subject;
                                delete  $scope.emailTemplate.fileName;
                            }

                            $scope.emailEventList = function (event) {

                                var hasEventList = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_GET, appfissService.appConfig.APP_URL + "literals/listbyevent?eventName=" + event.eventName +
                                        "&lang=" + localStorageService.get('adminDefaultLanguage').languageCode + "&sortBy=-id");
                                hasEventList.then(function (result) {
                                    $scope.eventsBasedList = result;
                                });
                                var hasEventTestList = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_GET, appfissService.appConfig.APP_URL + "emails/listbyeventname?eventName=" + event.eventName + "&emailLanguageId=" + emailLanguageId +
                                        "&lang=" + localStorageService.get('adminDefaultLanguage').languageCode + "&sortBy=-id");
                                hasEventTestList.then(function (result) {
                                    if (result === "") {
                                        delete  $scope.emailTemplate.subject;
                                        delete  $scope.emailTemplate.fileName;
                                    } else {
                                        $scope.emailTemplate.subject = result.subject;
                                        $scope.emailTemplate.fileName = result.fileName;
                                    }
                                });
                            };

                            $scope.saveEmailTemplate = function (emailTemplates, file) {
                                $scope.emailTemplateForm.submitted = true;
                                if ($scope.emailTemplateForm.$valid) {
                                    $scope.showLoader = true;
                                    if (angular.isUndefined(file) && angular.isUndefined($scope.emailTemplate.fileName)) {
                                        $scope.showLoader = false;
                                        toastr.pop({
                                            type: 'error',
                                            body: $translate.instant('upload-file'),
                                            showCloseButton: true
                                        });
                                    } else {
                                        emailTemplates.emailLanguageId = emailTemplates.emailLanguage.id;
                                        emailTemplates.eventName = emailTemplates.eventName.eventName;
                                        var url = "emails/uploadFile";
                                        if (!angular.isUndefined(file)) {
                                            $scope.showLoader = false;
                                            if ((file.type !== "text/html") || (!angular.isUndefined(file) && (file.type !== "text/html"))) {
                                                toastr.pop({
                                                    type: 'error',
                                                    body: $translate.instant('upload-html-file'),
                                                    showCloseButton: true
                                                });

                                            } else {
                                                var fd = new FormData();
                                                if (!angular.isUndefined(file)) {
                                                    fd.append("file", file);
                                                }
                                                fd.append("eventName", emailTemplates.eventName);
                                                fd.append("subject", emailTemplates.subject);
                                                fd.append("languageId", emailTemplates.emailLanguageId);
                                                appfissService.promiseAjax.httpFileuploadRequest(fd, url, emailTemplates, localStorageService)
                                                $scope.showLoader = false;
                                                toastr.pop({
                                                    type: 'success',
                                                    body: $translate.instant('updated-successfully'),
                                                    showCloseButton: true
                                                });
                                                $uibModalInstance.close();
                                                $scope.generalConfigUpdatedObject.findByUpdatedDate();
                                            }
                                        } else {
                                            var fd = new FormData();
                                            if (!angular.isUndefined(file)) {
                                                fd.append("file", file);
                                            }
                                            fd.append("eventName", emailTemplates.eventName);
                                            fd.append("subject", emailTemplates.subject);
                                            fd.append("languageId", emailTemplates.emailLanguageId);
                                            appfissService.promiseAjax.httpFileuploadRequest(fd, url, emailTemplates, localStorageService)
                                            $scope.showLoader = false;
                                            toastr.pop({
                                                type: 'success',
                                                body: $translate.instant('updated-successfully'),
                                                showCloseButton: true
                                            });
                                            $uibModalInstance.close();
                                            $scope.generalConfigUpdatedObject.findByUpdatedDate();
                                        }
                                    }
                                }
                            }

                        }]);
        }
    }

}
;

function themeSettingsCtrl($scope, $window, appfissService, toastr, $translate, localStorageService, $state) {
    $scope.shouldBeOpen = true;
    $scope.themeSettingsList = {};
    $scope.themeSettingsForm = {
        submitted: false,
        languageList: []
    };
    $scope.themeSettings = {
        headers: [{id: 0}],
        logoImage: '',
        favIconImage: '',
        headerTitle: '',
        welcomeContent: '',
        welcomeContentUser: '',
        footerContent: '',
        termsCondition: '',
        maintenanceMessage: '',
        isbroadCast: 0,

    };


    $scope.summernoteThemeOpttion = {
        toolbar: [
            ['style', ['bold', 'italic', 'underline', 'clear']],
            ['color', ['color']],
            ['font', ['strikethrough', 'superscript', 'subscript']],
            ['fontsize', ['fontsize']],
            ['misc', ['fullscreen', 'undo', 'redo', 'help']]
        ]

    };

    $scope.themeSettingsObject = {
        findAllLanguage: function (languageId) {
            var hasResult = appfissService.crudService.listAll("languages/list");
            hasResult.then(function (result) {
                $scope.themeSettingsForm.languageList = result;

                if (!angular.isUndefined(languageId)) {
                    angular.forEach($scope.themeSettingsForm.languageList, function (obj, key) {
                        if (obj.id === languageId) {
                            $scope.themeSettings.language = obj;
                            $scope.themeSettings.logoImage = appfissService.appConfig.RESOURCE_BASE_UI_URL + 'theme_logo-' + obj.languageCode + '.jpg';
                            $scope.themeSettings.favIconImage = appfissService.appConfig.RESOURCE_BASE_UI_URL + 'favicon-' + obj.languageCode + '.ico';
                        }
                    });
                }
            });
        },
        findThemeSettingsByLanguage: function (language) {
            var languageId = appfissService.localStorageService.get("adminDefaultLanguage").id;
            if (!angular.isUndefined(language)) {
                languageId = language.id;
            }
            var hasResult = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_GET, appfissService.appConfig.APP_URL + "themesettings/findbylanguage?languageId=" + languageId +
                    "&lang=" + localStorageService.get('adminDefaultLanguage').languageCode + "&sortBy=-id");
            hasResult.then(function (result) {
                if (result == "" || result == null) {
                    $scope.themeSettings.headers = [{id: 0}];
                    $scope.themeSettings.languageId = 0;
                } else {
                    $scope.themeSettings = result;
                    if (result.headers.length === 0) {
                        $scope.themeSettings.headers = [{id: 0}];
                    }
                    $scope.themeSettings.logoImage = appfissService.appConfig.RESOURCE_BASE_UI_URL + result.logoImgFile;
                    $scope.themeSettings.favIconImage = appfissService.appConfig.RESOURCE_BASE_UI_URL + result.favIcon;
                }

                $scope.themeSettingsObject.findAllLanguage(languageId);
            });
        },
        savethemeSettings: function (themeSettings) {
            var saveMethod = appfissService.appConfig.HTTP_POST;
            var saveUrl = "themesettings/update";
            if (themeSettings.id > 0) {
                saveMethod = appfissService.appConfig.HTTP_PUT;
            }
            $scope.themeSettingsForm.submitted = true;
            if ($scope.themeSettingsForm.$valid) {
                $scope.showLoader = true;
                var formData = new FormData();
                $scope.themeSettings.languageId = $scope.themeSettings.language.id;
                var themeSettingsFormData = {}
                angular.forEach($scope.themeSettings, function (obj, key) {
                    if (!angular.isUndefined(obj)) {
                        if (key == "headers") {
                            formData.append(key, JSON.stringify(obj));
                        } else {
                            formData.append(key, obj);
                        }
                    } else {
                        formData.append(key, "");
                    }
                });
                var hasResult = appfissService.promiseAjax.httpFileuploadRequest(formData, saveUrl, themeSettings, localStorageService)
                hasResult.then(function (result) {
                    $scope.themeSettingsForm.submitted = false;
                    $scope.showLoader = false;
                    toastr.pop({
                        type: 'success',
                        body: $translate.instant('updated-successfully'),
                        showCloseButton: true

                    });
                    $state.go("configuration.general");
                }).catch(function (result) {
                    if (!angular.isUndefined(result.data)) {
                        $scope.showLoader = false;
                        if (result.data.globalError !== '' && !angular.isUndefined(result.data.globalError)) {
                            var msg = result.data.globalError[0];
                            toastr.pop({
                                type: 'error',
                                body: msg,
                                showCloseButton: true
                            });
                        }
                    }
                });
            }
        },
        addHeaderLink: function () {
            var newItemNo = $scope.themeSettings.headers.length + 1;
            $scope.themeSettings.headers.push({id: 0});
        },
        removeHeaderLink: function () {
            var lastItem = $scope.themeSettings.headers.length - 1;
            if (lastItem > 0) {
                $scope.themeSettings.headers.splice(lastItem);
            }
        }
    };
    $scope.themeSettingsObject.findThemeSettingsByLanguage();

}
;

function staffCtrl($scope, $translate, $state, $stateParams, appfissService, $rootScope, toastr, localStorageService) {
    $scope.shouldBeOpen = true;
    $scope.isWhmcsEnabled = localStorageService.get("whmcsEnabled");
    $scope.clientForm = {
        submitted: false
    };

    $scope.clientObject = {
        loginAsUser: function () {
        }
    };

    $scope.global = appfissService.crudService.globalConfig;
    $scope.paginationObject = {};
    $scope.paginationObject.sortOrder = '+';
    appfissService.appConfig.sort.sortBy = 'id';
    var sort = appfissService.utilService.getSortingOrderBySortBy("id");
    $scope.sort = sort;
    $scope.sort = sort;
    appfissService.appConfig.sort.sortOrder = sort.sortOrder;
    $scope.quickSearch = null;
    $scope.totalCounts = null;
    $scope.state = null;

    $scope.getSupperAdmin = function () {
        var hasResult = appfissService.crudService.listAll("users/superAdmin");
        hasResult.then(function (result) {
            $scope.superAdmin = result;
        });
    };
    $scope.getSupperAdmin();

    $scope.changeSort = function (sortBy, pageNumber, sort) {
        var sort = appfissService.utilService.getSortingOrderBySortBy(sortBy);
        $scope.sort = sort;

        sort.sortOrder = (sort.sortOrder === "+") ? "-" : "+";
        $scope.sort = sort;
        appfissService.appConfig.sort.sortOrder = sort.sortOrder;
        appfissService.appConfig.sort.sortBy = sortBy;
        $scope.showLoader = true;

        var limit = (angular.isUndefined($scope.paginationObject.limit)) ? appfissService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
        var hasClientList = {};
        if ($scope.quickSearch === null && $scope.state === null) {
            hasClientList = appfissService.crudService.list("users/adminUserList", appfissService.appConfig.paginationHeaders(pageNumber, limit), {"limit": limit});
        } else {
            $scope.filter = "&searchText=" + $scope.quickSearch;
            hasClientList = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_GET, appfissService.appConfig.APP_URL + "users/adminUserListbySearchText" + "?lang=" + appfissService.localStorageService.cookie.get('language') + encodeURI($scope.filter) + "&sortBy=" + appfissService.appConfig.sort.sortOrder + appfissService.appConfig.sort.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {"limit": limit});
        }
        hasClientList.then(function (result) { // this is only run after $http
            $scope.clientList = result;

            // For pagination
            $scope.paginationObject.currentPage = pageNumber;
            $scope.paginationObject.totalItems = result.totalItems;
            $scope.showLoader = false;
        });
    };

    $scope.searchList = function (quickSearch) {
        $scope.quickSearch = quickSearch;
        $scope.list(1);
    };

    $scope.list = function (pageNumber) {
        $scope.showLoader = true;
        var limit = (angular.isUndefined($scope.paginationObject.limit)) ? appfissService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
        var hasactionServer = {};
        if ($scope.quickSearch === null) {
            hasactionServer = appfissService.crudService.listAllWithPagination("users/adminUserList", appfissService.appConfig.paginationHeaders(pageNumber, limit), {
                "limit": limit});
        } else {
            $scope.filter = "&searchText=" + $scope.quickSearch;
            hasactionServer = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_GET, appfissService.appConfig.APP_URL + "users/adminUserListbySearchText" + "?lang=" + appfissService.localStorageService.cookie.get('language') + encodeURI($scope.filter) + "&sortBy=" + appfissService.appConfig.sort.sortOrder + appfissService.appConfig.sort.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {"limit": limit});
        }
        hasactionServer.then(function (result) {  // this is only run after $http completes
            $scope.clientList = result;
            // For pagination
            $scope.totalCounts = result.totalItems;
            $scope.paginationObject.limit = limit;
            $scope.paginationObject.currentPage = pageNumber;
            $scope.paginationObject.totalItems = result.totalItems;
            $scope.paginationObject.pagingMethod = $scope.list;
            $scope.showLoader = false;
        });
    };
    $scope.list(1);

    $scope.getRoleList = function () {
        var hasResult = appfissService.crudService.listAll("role/getRoleByDomain");
        hasResult.then(function (result) {
            $scope.rootRoleList = result;
        });
    };

    if ($stateParams.id > 0) {
        var hasResult = appfissService.crudService.listAll("role/getRoleByDomain");
        hasResult.then(function (result) {
            $scope.rootRoleList = result;
            var hasServer = appfissService.crudService.read("users", $stateParams.id);
            hasServer.then(function (result) {
                $scope.user = result;
                angular.forEach($scope.rootRoleList, function (item, key) {
                    if ($scope.user.roleId === item.id) {
                        $scope.user.role = item;
                    }
                });
            });
        });
    } else {
        $scope.getRoleList();
    }

    $scope.addStaff = function (user, confirmPassword) {
        $scope.profileForm.submitted = true;
        if ($scope.profileForm.$valid) {
            $scope.showLoader = true;
            user.syncFlag = false;
            user.status = "ENABLED";
            user.transActionType = "ADMIN_INSERT";
            user.isActive = true;
            user.roleId = user.role.id;
            var regularExpression = /^(?=.*\d)(?=.*[!@#$%^&*])(?=.*[A-Z]).{8,}$/;
            if (!regularExpression.test(user.password)) {
                $scope.passwordMsg = true;
                $scope.showLoader = false;
            } else {
                if (user.password !== confirmPassword) {
                    $scope.profileForm['confirmPassword'].$invalid = true;
                    $scope.captchaValidation = false;
                    $scope.showLoader = false;
                } else {
                    var hasResult = appfissService.crudService.add("users", user);
                    hasResult.then(function (result) {  // this is only run after $http completes
                        $scope.formSubmitted = false;
                        $scope.showLoader = false;
                        toastr.pop({
                            type: 'success',
                            body: 'Successfully Added.',
                            showCloseButton: true
                        });
                        $state.go('configuration.general.staffs');
                    }).catch(function (result) {
                        if (!angular.isUndefined(result) && result.data !== null) {
                            if (result.data.globalError[0] !== '' && !angular.isUndefined(result.data.globalError[0])) {
                                var msg = result.data.globalError[0];
                                $scope.showLoader = false;
                            }
                        } else if (!angular.isUndefined(result) && result.data === null && result.status === -1) {
                            var msg = $translate.instant('server.not.connected');
                        }
                    });
                }
            }
        }
    };

    $scope.updateStaff = function (user) {
        $scope.profileForm.submitted = true;
        if ($scope.profileForm.$valid) {
            $scope.showLoader = true;
            user.syncFlag = false;
            user.status = "ENABLED";
            user.transActionType = "UPDATE";
            user.isActive = true;
            user.roleId = user.role.id;
            var hasResult = appfissService.crudService.update("users", user);
            hasResult.then(function (result) {  // this is only run after $http completes
                $scope.formSubmitted = false;
                $scope.showLoader = false;
                toastr.pop({
                    type: 'success',
                    body: 'Successfully Updated.',
                    showCloseButton: true
                });
                $state.go('configuration.general.staffs');
            }).catch(function (result) {
                if (!angular.isUndefined(result) && result.data !== null) {
                    if (result.data.globalError[0] !== '' && !angular.isUndefined(result.data.globalError[0])) {
                        var msg = result.data.globalError[0];
                        $scope.showLoader = false;
                    }
                } else if (!angular.isUndefined(result) && result.data === null && result.status === -1) {
                    var msg = $translate.instant('server.not.connected');
                }
            });
        }
    };

    $scope.deleteClient = function (user) {
        appfissService.dialogService.openDialog("app/views/common/confirm-delete.html", 'sm', $scope,
                ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                        $scope.deleteObject = user;
                        $scope.ok = function (deleteObject) {
                            $scope.showLoader = true;
                            var hasResult = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_POST,
                                    appfissService.appConfig.APP_URL + "users/delete" + "?lang=" + localStorageService.cookie.get('language'), {}, user);
                            hasResult.then(function (result) {  // this is only run after $http completes
                                $scope.showLoader = false;
                                $uibModalInstance.dismiss('cancel');
                                toastr.pop({
                                    type: 'success',
                                    body: 'Successfully Deleted.',
                                    showCloseButton: true
                                });
                                if (user.id === localStorageService.get('adminId')) {
                                    setTimeout(function () {
                                        appfissService.utilService.logout();
                                    }, 2000);
                                } else {
                                    $scope.list(1);
                                }
                            }).catch(function (result) {
                                $scope.showLoader = false;
                                if (!angular.isUndefined(result.data)) {
                                    if (result.data.globalError !== '' && !angular.isUndefined(result.data.globalError)) {
                                        var msg = result.data.globalError[0];
                                        toastr.pop({
                                            type: 'error',
                                            body: msg,
                                            showCloseButton: true
                                        });
                                    }
                                }
                            });
                        };
                        $scope.cancel = function () {
                            $uibModalInstance.dismiss('cancel');
                        };
                    }]);
    };

}
;

function roleListCtrl($scope, appfissService, $filter, toastr, $state) {
    $scope.shouldBeOpen = true;
    $scope.global = appfissService.appConfig;
    $scope.sort = appfissService.appConfig.sort;
    $scope.changeSorting = appfissService.utilService.changeSorting;
    $scope.roleCreate = false;
    $scope.displayDate = function () {
        $scope.date = $filter('date')(new Date(), 'yyyy-MM-dd');
        $scope.time = $filter('date')(new Date(), 'HH:mm');
    };
    $scope.role = {};
    $scope.roles = {};
    $scope.domainrolesList = {};
    $scope.domainObject = {};
    $scope.permissionGroup = {};
    $scope.rolePermission = {};
    $scope.permissionList = [];
    $scope.permissionLength = 0;
    $scope.checkAll = false;
    $scope.accordionClass = "";

    $scope.checkAllPermissions = function (permissions, checkAll, actionFrom) {
        if (actionFrom === 'CHECK_ALL' && (angular.isUndefined($scope.domainObject.id) || angular.isUndefined($scope.rolePermission)
                || angular.isUndefined($scope.rolePermission.type))) {
            $scope.checkAll = false;
            $scope.rolesPrivillegesForm.submitted = true;
            $scope.rolesPrivillegesForm.rolePermission.$invalid = true;
        } else {
            var unchecked = false;
            for (var i = 0; i < permissions.length; i++) {
                if (!$scope.permissionGroup[permissions[i].module] || angular.isUndefined($scope.permissionGroup[permissions[i].module])) {
                    unchecked = true;
                    break;
                }
            }
            for (var i = 0; i < permissions.length; i++) {
                if (unchecked && checkAll) {
                    $scope.permissionGroup[permissions[i].module] = true;
                    $scope.permissionList[permissions[i].id] = true;
                    if (isNaN($scope.permissionGroupCount[permissions[i].module])) {
                        $scope.permissionGroupCount[permissions[i].module] = 0;
                    }
                    $scope.permissionGroupCount[permissions[i].module]++;
                } else {
                    $scope.permissionGroupCount = [];
                    $scope.permissionGroup[permissions[i].module] = false;
                    $scope.permissionList[permissions[i].id] = false;
                }
            }
        }
    };

    $scope.checkAlls = function (permissionModule, key) {
        if (angular.isUndefined($scope.domainObject.id) || angular.isUndefined($scope.rolePermission)
                || angular.isUndefined($scope.rolePermission.type)) {
            $scope.permissionGroup[key] = false;
            $scope.rolesPrivillegesForm.submitted = true;
            $scope.rolesPrivillegesForm.rolePermission.$invalid = true;
        } else {
            if ($scope.permissionGroup[key]) {
                $scope.permissionGroup[key] = true;
            } else {
                $scope.permissionGroup[key] = false;
            }
            angular.forEach(permissionModule, function (permission, permissionKey) {
                $scope.permissionList[permission.id] = $scope.permissionGroup[key];
            });
        }
    };

    $scope.checkOne = function (permission, permissionModule) {
        if (angular.isUndefined($scope.domainObject.id) || angular.isUndefined($scope.rolePermission)
                || angular.isUndefined($scope.rolePermission.type)) {
            $scope.permissionList[permission.id] = false;
            $scope.rolesPrivillegesForm.submitted = true;
            $scope.rolesPrivillegesForm.rolePermission.$invalid = true;
        } else {
            var i = 0;
            if (!$scope.permissionList[permission.id]) {
                $scope.permissionList[permission.id] = false;
            } else {
                $scope.permissionList[permission.id] = true;
            }
            angular.forEach(permissionModule, function (object) {
                if ($scope.permissionList[object.id]) {
                    i++;
                }
            });
            if (i === permissionModule.length) {
                $scope.permissionGroup[permissionModule[0].module] = true;
            } else {
                $scope.permissionGroup[permissionModule[0].module] = false;
            }
        }
    };

    // Load permission
    function listPermissions() {
        $scope.permissions = {};
        $scope.showLoader = true;
        var hasPermissions = appfissService.promiseAjax.httpTokenRequest(appfissService.crudService.globalConfig.HTTP_GET, appfissService.crudService.globalConfig.APP_URL + "permissions/list");
        hasPermissions.then(function (result) { // this is only run after
            $scope.permissions = result;
            console.log($scope.permissions);
            $scope.permissionLength = $scope.permissions.length;
            $scope.showLoader = false;
        });
    }
    ;
    listPermissions();

    $scope.getDomains = function () {
        var hasDomainList = appfissService.crudService.listAll("domains/list");
        hasDomainList.then(function (result) { // this is only run after $http
            $scope.domainList = result;
            angular.forEach($scope.domainList, function (item, key) {
                if (item.name === 'ROOT') {
                    $scope.selectDomain(item);
                }
            });
        });
    };
    $scope.getDomains();

    $scope.selectDomain = function (domain) {
        if (angular.isUndefined(domain)) {
            $scope.domainObject = {};
        } else {
            $scope.domainObject = domain;
            $scope.getRoleByDomain("CREATE", domain.id);
        }
    };

    $scope.getRoleByDomain = function (type, domainId) {
        $scope.role = {};
        $scope.rolePermission = {};
        $scope.checkAllPermissions($scope.permissions, false, 'DOMAIN');
        $scope.checkAll = false;
        $scope.showLoader = true;
        var hasRoles = appfissService.crudService.listAll("role/listroles");
        hasRoles.then(function (result) {
            $scope.showLoader = false;
            $scope.domainrolesList = [];
            angular.forEach(result, function (item, key) {
                if (domainId === item.domainId) {
                    item.type = "Existing Role";
                    $scope.domainrolesList.push(item);
                    if (type === 'CREATE') {
                        if (item.name === $scope.roles.name) {
                            $scope.selectRole(item);
                        }
                    }
                    $scope.roleCreate = true;
                }
            });
            var data = {'name': 'Add role', 'type': 'Create New Role'};
            $scope.domainrolesList.push(data);
        });
    };

    $scope.selectRole = function (menuItem) {
        if (!angular.isUndefined(menuItem) && menuItem !== null && menuItem.type === 'Create New Role') {
            $scope.role = {};
            $scope.checkAllPermissions($scope.permissions, false, 'ROLE');
            $scope.checkAll = false;
        } else if (!angular.isUndefined(menuItem) && menuItem !== null) {
            $scope.roleEdit(menuItem.id);
        } else {
            $scope.role = {};
            $scope.checkAllPermissions($scope.permissions, false, 'ROLE');
            $scope.checkAll = false;
        }
    };

    // Create a new role to our application
    $scope.roleEdit = function (roleId) {
        $scope.permissionList = [];
        var hasRole = appfissService.crudService.read("role", roleId);
        hasRole.then(function (result) {
            $scope.role = result;
            $scope.domainObject = result.domain;
            $scope.accountId = result.accountId;

            $scope.permissionGroupCount = [];
            angular.forEach($scope.role.permissionList, function (permission, key) {
                $scope.permissionList[permission.id] = true;
                if (isNaN($scope.permissionGroupCount[permission.module])) {
                    $scope.permissionGroupCount[permission.module] = 0;
                }
                $scope.permissionGroupCount[permission.module]++;
            });

            var totalPermission = $scope.role.permissionList.length;
            if ($scope.role.permissionList.length === 0) {
                for (var i = 0; i < $scope.permissions.length; i++) {
                    $scope.permissionGroup[$scope.permissions[i].module] = false;
                    $scope.permissionList[$scope.permissions[i].id] = false;
                }
            } else if ($scope.role.permissionList.length !== $scope.permissionLength && $scope.role.permissionList.length !== 0) {
                totalPermission = 0;
                angular.forEach($scope.role.permissionList, function (permission, key) {
                    for (var i = 0; i < $scope.permissions.length; i++) {
                        if (permission.actionKey === $scope.permissions[i].actionKey) {
                            $scope.permissionList[$scope.permissions[i].id] = true;
                            totalPermission++;
                        }
                    }
                });
            }

            $scope.checkAll = false;
            if (totalPermission === $scope.permissionLength) {
                $scope.checkAll = true;
            }
        });
    };

    $scope.createUpdateRolePermission = function (role) {
        $scope.rolesPrivillegesForm.submitted = true;
        $scope.rolesPrivillegesForm.name.errorMessage = "";
        if (angular.isUndefined($scope.rolePermission)
                || (!angular.isUndefined($scope.rolePermission) && angular.isUndefined($scope.rolePermission.type))) {
            $scope.rolesPrivillegesForm.rolePermission.$invalid = true;
            $scope.rolesPrivillegesForm.$valid = false;
        }
        if ($scope.rolesPrivillegesForm.$valid) {
            $scope.roles = {};
            if ($scope.rolePermission.type === 'Create New Role') {
                $scope.roles.name = role.name;
                $scope.roles.description = role.description;
                $scope.roles.domainId = $scope.domainObject.id;
                var roles = angular.copy($scope.roles);
                roles.permissionList = [];
                angular.forEach($scope.permissions, function (permissions, key) {
                    if ($scope.permissionList[permissions.id]) {
                        roles.permissionList.push(permissions);
                    }
                });
                $scope.showLoader = true;
                var hasServer = appfissService.crudService.add("role/createRole", roles);
                hasServer.then(function (result) {
                    $scope.showLoader = false;
                    $scope.rolesPrivillegesForm.submitted = false;
                    toastr.pop({
                        type: 'success',
                        body: 'Role created successfully',
                        showCloseButton: true
                    });
                    setTimeout(function () {
                        $state.reload();
                    }, 1000);
                }).catch(function (result) {
                    $scope.showLoader = false;
                    $scope.rolesPrivillegesForm.name.errorMessage = "";
                    if (!angular.isUndefined(result) && result.data !== null) {
                        angular.forEach(result.data.fieldErrors, function (errorMessage, key) {
                            $scope.rolesPrivillegesForm[key].$invalid = true;
                            $scope.rolesPrivillegesForm[key].errorMessage = errorMessage;
                        });
                    }
                });
            } else {
                var roles = angular.copy(role);
                roles.permissionList = [];
                angular.forEach($scope.permissions, function (permissions, key) {
                    if ($scope.permissionList[permissions.id]) {
                        roles.permissionList.push(permissions);
                    }
                });
                var hasAssignedPermissions = appfissService.crudService.add("role/updateRole", roles);
                hasAssignedPermissions.then(function (result) { // this is only run after $http
                    $scope.showLoader = false;
                    $scope.rolesPrivillegesForm.submitted = false;
                    toastr.pop({
                        type: 'success',
                        body: 'Role updated successfully',
                        showCloseButton: true
                    });
                    $scope.roleEdit(result.id);
                }).catch(function (result) {
                    $scope.showLoader = false;
                    if (!angular.isUndefined(result) && result.data !== null) {
                        angular.forEach(result.data.fieldErrors, function (errorMessage, key) {
                            $scope.rolesPrivillegesForm[key].$invalid = true;
                            $scope.rolesPrivillegesForm[key].errorMessage = errorMessage;
                        });
                    }
                });
            }
        }
    };

    $scope.roleDelete = function (size, role) {
        appfissService.dialogService.openDialog("app/views/common/confirm-delete.html", size, $scope,
                ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                        $scope.deleteObject = role;
                        $scope.ok = function (deleteObject) {
                            $scope.showLoader = true;
                            role.isActive = false;
                            var hasRole = appfissService.crudService.softDelete("role", role);
                            hasRole.then(function (result) {  // this is only run after $http completes
                                $scope.showLoader = false;
                                $uibModalInstance.dismiss('cancel');
                                toastr.pop({
                                    type: 'success',
                                    body: 'Role deleted successfully',
                                    showCloseButton: true
                                });
                                setTimeout(function () {
                                    $state.reload();
                                }, 1000);
                            }).catch(function (result) {
                                $scope.showLoader = false;
                                $uibModalInstance.dismiss('cancel');
                                if (!angular.isUndefined(result.data)) {
                                    if (result.data.globalError !== '' && !angular.isUndefined(result.data.globalError)) {
                                        var msg = "This role assigned to an admin. So you can't delete";
                                        toastr.pop({type: 'error', body: msg, showCloseButton: true});
                                    }
                                }
                            });
                        };
                        $scope.cancel = function () {
                            $uibModalInstance.dismiss('cancel');
                        };
                    }]);

    };

}
;

function maintenanceEventCtrl($scope, $translate, $state, appfissService, toastr, localStorageService, $filter) {
    $scope.shouldBeOpen = true;
    $scope.maintenanceEventForm = {
        submitted: false
    };

    $scope.formElements = {
        emailTypeList: {
            "0": "MAIl_IMMEDIATELY",
            "1": "MAIl_DAY_BEFORE"
        }
    };

    $scope.global = appfissService.crudService.globalConfig;
    $scope.paginationObject = {};
    $scope.paginationObject.sortOrder = '+';
    appfissService.appConfig.sort.sortBy = 'id';
    var sort = appfissService.utilService.getSortingOrderBySortBy("id");
    $scope.sort = sort;
    $scope.sort = sort;
    appfissService.appConfig.sort.sortOrder = sort.sortOrder;
    $scope.quickSearch = null;
    $scope.totalCounts = null;
    $scope.state = null;

    $scope.open = function ($event, currentDateField) {
        $scope.eventEndMinDate = new Date($scope.event.start);
//        $event.preventDefault();
//        $event.stopPropagation();
        $scope.event[currentDateField] = true;
    };

    $scope.event = {};

    $scope.changeSort = function (sortBy, pageNumber, sort) {
        var sort = appfissService.utilService.getSortingOrderBySortBy(sortBy);
        $scope.sort = sort;

        sort.sortOrder = (sort.sortOrder === "+") ? "-" : "+";
        $scope.sort = sort;
        appfissService.appConfig.sort.sortOrder = sort.sortOrder;
        appfissService.appConfig.sort.sortBy = sortBy;
        $scope.showLoader = true;

        var limit = (angular.isUndefined($scope.paginationObject.limit)) ? appfissService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
        var hasEventsList = {};
        if ($scope.quickSearch === null && $scope.state === null) {
            hasEventsList = appfissService.crudService.list("maintenanceEvents/activeEventList", appfissService.appConfig.paginationHeaders(pageNumber, limit), {"limit": limit});
        } else {
            $scope.filter = "&searchText=" + $scope.quickSearch;
            hasEventsList = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_GET, appfissService.appConfig.APP_URL + "maintenanceEvents/activeEventListbySearchText" + "?lang=" + appfissService.localStorageService.cookie.get('language') + encodeURI($scope.filter) + "&sortBy=" + appfissService.appConfig.sort.sortOrder + appfissService.appConfig.sort.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {"limit": limit});
        }
        hasEventsList.then(function (result) { // this is only run after $http
            $scope.eventsList = result;

            // For pagination
            $scope.paginationObject.currentPage = pageNumber;
            $scope.paginationObject.totalItems = result.totalItems;
            $scope.showLoader = false;
        });
    };

    $scope.searchList = function (quickSearch) {
        $scope.quickSearch = quickSearch;
        $scope.maintenanceEventList(1);
    };

    $scope.maintenanceEventList = function (pageNumber) {
        $scope.showLoader = true;
        var limit = (angular.isUndefined($scope.paginationObject.limit)) ? appfissService.appConfig.CONTENT_LIMIT : $scope.paginationObject.limit;
        var hasactionServer = {};
        if ($scope.quickSearch === null) {
            hasactionServer = appfissService.crudService.list("maintenanceEvents/activeEventList", appfissService.appConfig.paginationHeaders(pageNumber, limit), {"limit": limit});
        } else {
            $scope.filter = "&searchText=" + $scope.quickSearch;
            hasactionServer = appfissService.promiseAjax.httpTokenRequest(appfissService.appConfig.HTTP_GET, appfissService.appConfig.APP_URL + "maintenanceEvents/activeEventListbySearchText" + "?lang=" + appfissService.localStorageService.cookie.get('language') + encodeURI($scope.filter) + "&sortBy=" + appfissService.appConfig.sort.sortOrder + appfissService.appConfig.sort.sortBy + "&limit=" + limit, $scope.global.paginationHeaders(pageNumber, limit), {"limit": limit});

        }
        hasactionServer.then(function (result) {  // this is only run after $http completes
            $scope.eventsList = result;
            // For pagination
            $scope.totalCounts = result.totalItems;
            $scope.paginationObject.limit = limit;
            $scope.paginationObject.currentPage = pageNumber;
            $scope.paginationObject.totalItems = result.totalItems;
            $scope.paginationObject.pagingMethod = $scope.list;
            $scope.showLoader = false;
        });
    };
    $scope.maintenanceEventList(1);

    $scope.addEvent = function () {
        $scope.event = {};
        appfissService.dialogService.openDialog("app/views/configuration/general/add-maintenance-event.html", 'md', $scope,
                ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                        $scope.saveEvent = function (event) {
                            $scope.event = event;
                            $scope.maintenanceEventForm.submitted = true;
                            if ($scope.maintenanceEventForm.$valid) {
                                event.startDate = $filter('date')(event.start, 'yyyy-MM-dd');
                                event.endDate = $filter('date')(event.end, 'yyyy-MM-dd');
                                if (!angular.isUndefined(event.fromTime) && event.fromTime !== null) {
                                    event.startTime = event.fromTime.format('HH:mm');
                                }
                                if (!angular.isUndefined(event.toTime) && event.toTime !== null) {
                                    event.endTime = event.toTime.format('HH:mm');
                                }
                                $scope.showLoader = true;
                                var hasResult = appfissService.crudService.add("maintenanceEvents", event);
                                hasResult.then(function (result) {  // this is only run after $http completes
                                    $scope.formSubmitted = false;
                                    $scope.showLoader = false;
                                    $uibModalInstance.dismiss('cancel');
                                    $scope.maintenanceEventList(1);
                                    toastr.pop({
                                        type: 'success',
                                        body: 'Successfully Added.',
                                        showCloseButton: true
                                    });
                                }).catch(function (result) {
                                    if (!angular.isUndefined(result) && result.data !== null) {
                                        if (result.data.globalError[0] !== '' && !angular.isUndefined(result.data.globalError[0])) {
                                            var msg = result.data.globalError[0];
                                            $scope.showLoader = false;
                                        }
                                    } else if (!angular.isUndefined(result) && result.data === null && result.status === -1) {
                                        var msg = $translate.instant('server.not.connected');
                                    }
                                });
                            }
                        };
                        $scope.cancel = function () {
                            $uibModalInstance.dismiss('cancel');
                        };
                    }]);
    };

    $scope.editEvent = function (event) {
        appfissService.dialogService.openDialog("app/views/configuration/general/edit-maintenance-event.html", 'md', $scope,
                ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                        $scope.event.start = event.start * 1000;
                        $scope.event.end = event.end * 1000;
                        $scope.event.description = event.description;
                        $scope.event.title = event.title;
                        $scope.event.emailType = event.emailType;
                        $scope.event.id = event.id;
                        $scope.event.version = event.version;
                        $scope.event.createdDateTime = event.createdDateTime;
                        if (!angular.isUndefined(event.fromTime) && event.fromTime !== null) {
                            $scope.event.fromTime = moment($filter('date')(event.fromTime * 1000, 'yyyy-MM-dd') + ' ' + $filter('date')(event.fromTime * 1000, 'HH:mm'));
                        }
                        if (!angular.isUndefined(event.toTime) && event.toTime !== null) {
                            $scope.event.toTime = moment($filter('date')(event.toTime * 1000, 'yyyy-MM-dd') + ' ' + $filter('date')(event.toTime * 1000, 'HH:mm'));
                        }

                        $scope.updateevent = function (maintenanceEventForm, event) {
                            $scope.maintenanceEventForm.submitted = true;
                            if ($scope.maintenanceEventForm.$valid) {
                                $scope.showLoader = true;
                                event.start = new Date(event.start);
                                event.end = new Date(event.end);
                                event.startDate = $filter('date')(event.start, 'yyyy-MM-dd');
                                event.endDate = $filter('date')(event.end, 'yyyy-MM-dd');
                                if (!angular.isUndefined(event.fromTime) && event.fromTime !== null) {
                                    event.startTime = event.fromTime.format('HH:mm');
                                }
                                if (!angular.isUndefined(event.toTime) && event.toTime !== null) {
                                    event.endTime = event.toTime.format('HH:mm');
                                }
                                var hasResult = appfissService.crudService.update("maintenanceEvents", event);
                                hasResult.then(function (result) {  // this is only run after $http completes
                                    $scope.formSubmitted = false;
                                    $scope.showLoader = false;
                                    $uibModalInstance.dismiss('cancel');
                                    $scope.maintenanceEventList(1);
                                    toastr.pop({
                                        type: 'success',
                                        body: 'Successfully Updated.',
                                        showCloseButton: true
                                    });
                                }).catch(function (result) {
                                    if (!angular.isUndefined(result) && result.data !== null) {
                                        if (result.data.globalError[0] !== '' && !angular.isUndefined(result.data.globalError[0])) {
                                            var msg = result.data.globalError[0];
                                            $scope.showLoader = false;
                                        }
                                    } else if (!angular.isUndefined(result) && result.data === null && result.status === -1) {
                                        var msg = $translate.instant('server.not.connected');
                                    }
                                });
                            }
                        };
                        $scope.cancel = function () {
                            $uibModalInstance.dismiss('cancel');
                        };
                    }]);
    };

    $scope.deleteEvent = function (event) {
        appfissService.dialogService.openDialog("app/views/common/confirm-delete.html", 'sm', $scope,
                ['$scope', '$uibModalInstance', '$rootScope', function ($scope, $uibModalInstance, $rootScope) {
                        $scope.deleteObject = event;
                        $scope.ok = function (deleteObject) {
                            $scope.showLoader = true;
                            var hasResult = appfissService.crudService.softDelete("maintenanceEvents", event);
                            hasResult.then(function (result) {  // this is only run after $http completes
                                $scope.showLoader = false;
                                $uibModalInstance.dismiss('cancel');
                                $scope.maintenanceEventList(1);
                                toastr.pop({
                                    type: 'success',
                                    body: 'Successfully Deleted.',
                                    showCloseButton: true
                                });
                            }).catch(function (result) {
                                $scope.showLoader = false;
                                if (!angular.isUndefined(result.data)) {
                                    if (result.data.globalError !== '' && !angular.isUndefined(result.data.globalError)) {
                                        var msg = result.data.globalError[0];
                                        toastr.pop({
                                            type: 'error',
                                            body: msg,
                                            showCloseButton: true
                                        });
                                    }
                                }
                            });
                        };
                        $scope.cancel = function () {
                            $uibModalInstance.dismiss('cancel');
                        };
                    }]);
    };
}
;


function uploadFile($http) {
    var upload = function (files, emails, httpTokenRequest, globalConfig, $cookies, localStorageService, url) {
        var global = globalConfig;
        var userName = angular.copy(localStorageService.get('adminEncryptMail'));
        var userPass = angular.copy(localStorageService.get('adminEncryptPass'));
        var fd = new FormData();
        /**angular.forEach(files,function(file){
         if(!angular.isUndefined(file)) {
         fd.append('file',file);
         }
         
         });**/
        var i = 0;
        angular.forEach(files, function (file) {
            if (!angular.isUndefined(file)) {
                fd.append("file", file);
            }
            if (!angular.isUndefined(file) && i == 0) {
                fd.append("hasEnglish", true);
            } else {
                fd.append("hasEnglish", false);
            }
            i++;
        });
        fd.append("eventName", emails.eventName);
        fd.append("subject", emails.subject);
        fd.append('englishLanguage', emails.englishLanguage);
        fd.append('otherLanguage', emails.otherLanguage);
        fd.append("recipientType", emails.recipientType);
        return $http.post(url, fd, {
            transformRequest: angular.identity,
            headers: {
                'Content-Type': undefined,
                'apiKey': global.sessionValues.loginToken,
                'X-Auth-Username': userName,
                'X-Auth-Password': userPass
            }
        });

    }
    return {upload: upload};
}
;
angular
        .module('appfiss')
        .controller('generalConfigurationCtrl', generalConfigurationCtrl)
        .controller('notificationConfigurationCtrl', notificationConfigurationCtrl)
        .controller('emailConfigurationCtrl', emailConfigurationCtrl)
        .controller('dateFormatCtrl', dateFormatCtrl)
        .controller('languageCtrl', languageCtrl)
        .controller('organizationCtrl', organizationCtrl)
        .controller('loginSecurityCtrl', loginSecurityCtrl)
        .controller('helpDeskCtrl', helpDeskCtrl)
        .controller('emailTemplateCtrl', emailTemplateCtrl)
        .controller('themeSettingsCtrl', themeSettingsCtrl)
        .controller('staffCtrl', staffCtrl)
        .controller('roleListCtrl', roleListCtrl)
        .controller('maintenanceEventCtrl', maintenanceEventCtrl)
        .factory('uploadFile', uploadFile);
