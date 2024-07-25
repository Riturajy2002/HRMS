angular.module('leaveManagementApp')
	.directive('adminDashboardcomponent', function() {
		return {
			templateUrl: 'views/adminDashBoard.template.html',
			controller: 'AdminDashboardController',
			controllerAs: '$ctrl'
		};
	});
 