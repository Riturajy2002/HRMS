var leaveManagementApp = angular.module('leaveManagementApp', [
    'ui.bootstrap',
    'ui.select',
    'ngRoute'
]);

leaveManagementApp.config(['$routeProvider', '$locationProvider', '$httpProvider', function ($routeProvider, $locationProvider, $httpProvider) {
    
    
    // Add the interceptor to the $httpProvider
    $httpProvider.interceptors.push('myHttpResponseInterceptor');

    $routeProvider
        .when('/login', {
            templateUrl: 'views/login.template.html',
            controller: 'LoginController',
            controllerAs: '$ctrl'
        })
        .when('/leave-request', {
            templateUrl: 'views/leaveRequestForm.html',
            controller: 'LeaveformController',
            controllerAs: '$ctrl'
        })
        .when('/admin-dashboard', {
            templateUrl: 'views/adminDashboard.template.html',
            controller: 'AdminDashboardController',
            controllerAs: '$ctrl'
        })
        .when('/super-admin', {
            templateUrl: 'views/superAdminDashboard.html',
            controller: 'SuperAdminController',
            controllerAs: '$ctrl'
        })
        .otherwise({
            redirectTo: '/login'
        });
}]);

leaveManagementApp.run(function ($rootScope, $location) {
    // Any run-time logic you need
});


leaveManagementApp.factory('myHttpResponseInterceptor', function($q, $location, $window, $rootScope) {
    var service = {
        // Run this function before making requests 
        'request': function(config) {
            if ($window.sessionStorage.userId && $window.sessionStorage.token) {
                config.headers['user-name'] = $window.sessionStorage.userId;
                config.headers['auth-token'] = $window.sessionStorage.token;
                config.headers['group'] = $window.sessionStorage.group;
            } else if ($location.$$path != '/login' && $location.$$path != '/logout') {
                $location.path("/login");
                return $q.reject(config);
            }
            return config;
        },

        'response': function(response) {
            return response;
        },
        'responseError': function(response) {
            if (response.status == 401) {
                delete $window.sessionStorage.token;
                delete $window.sessionStorage.userId;
                setTimeout(function() {
                    $location.path("/login");
                }, 100);
            }
            return $q.reject(response);
        }
    };

    return service;
});

// Define the mainCtrl
leaveManagementApp.controller("mainCtrl", function($scope, $rootScope) {
    // Your main controller logic here
});
