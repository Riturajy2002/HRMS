angular.module('leaveManagementApp')
.directive('superAdminComponent', function() {
    return {
        templateUrl: 'views/superAdminDashboard.html',
        controller: 'SuperAdminController',
        controllerAs: '$ctrl'
    };
});
