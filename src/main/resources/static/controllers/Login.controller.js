angular.module('leaveManagementApp')
    .controller('LoginController', ['$http', '$window', '$scope', '$rootScope', '$location', '$uibModal', function($http, $window, $scope, $rootScope, $location, $uibModal) {
        var $ctrl = this;
        $ctrl.userId = '';
        $ctrl.password = '';
        $ctrl.errorMessage = '';

        $ctrl.login = function() {
            if (!$ctrl.userId || !$ctrl.password) {
                $ctrl.errorMessage = 'Please enter both User ID and Password';
                return;
            } else if ($ctrl.password.length < 8) {
                $ctrl.errorMessage = 'Password must be at least 8 characters long';
                return;
            } else {
                $ctrl.errorMessage = '';
            }

           var username = CryptoJS.AES.encrypt($ctrl.userId, "2fs6828r61oo68rr0su3eurf4serfu675eerf5oeoesr60s17fo5u5u418us5372" ).toString()
           var password = CryptoJS.AES.encrypt($ctrl.password, "2fs6828r61oo68rr0su3eurf4serfu675eerf5oeoesr60s17fo5u5u418us5372" ).toString()
         	
         	var credentials = {"userId" : username, "password" : password};

            $http.post('/api/login', credentials)
                .then(function(response) {
                    var data = response.data;
                    if (data && data.authToken) {
                          // Store session data in sessionStorage
                        $window.sessionStorage.token = data.authToken;
                        $window.sessionStorage.id = data.id;
                        $window.sessionStorage.userId = data.userId;
                        $window.sessionStorage.username = data.userName;
                        $window.sessionStorage.report_manager = data.reportManager;
                        $window.sessionStorage.designation = data.designation;
                        $window.sessionStorage.email_id = data.email_id;
                        $window.sessionStorage.roles = data.roles;
                        $window.sessionStorage.contactNo = data.contactNo;
                        $window.sessionStorage.profilePicUrl = data.profilePicUrl;
                        $rootScope.currentUser = data.userName; 
                        $rootScope.token = data.authToken;
                        
                       
                       
                        var roles = data.roles.map(function(role) {
                            return role.toLowerCase();
                        });
                        if (roles.includes('user')) {
                            $location.path('/leave-request');
                        } 
                    } else {
                        $ctrl.errorMessage = 'Invalid username or password';
                    }
                })
                .catch(function(error) {
                    $ctrl.errorMessage = 'Login failed. Please check your credentials and try again.';
                    console.error('Login error:', error);
                });
        };

    }]);
