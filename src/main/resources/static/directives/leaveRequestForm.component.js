angular.module('leaveManagementApp').directive('leaveRequestFormComponent', function() {
	return {
		templateUrl: 'views/leaveRequestForm.html',
		controller: 'LeaveformController',
		controllerAs: '$ctrl'
	};
});
