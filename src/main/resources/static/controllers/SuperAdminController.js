angular.module('leaveManagementApp')
	.controller('SuperAdminController', ['$http', '$scope', '$window', '$rootScope', 'AuthService', '$location','$sce',
		function($http, $scope, $window, $rootScope, AuthService, $location, $sce) {


			// Initialize User Data object
			$scope.userData = {
				id: '',
				contactNo: '',
				emailId: '',
				password: '',
				designation: '',
				username: '',
				report_manager: '',
				profilePicUrl: '',
				token: ''
			};

			// Function to fetch user data from local storage and initialize $scope.userData
			function initializeUserData() {
				$scope.userData.id = $window.sessionStorage.id || '';
				$scope.userData.userId = $window.sessionStorage.userId || '';
				$scope.userData.token = $window.sessionStorage.token || '';
				$scope.userData.contactNo = $window.sessionStorage.contactNo || '';
				$scope.userData.email_id = $window.sessionStorage.email_id || '';
				$scope.userData.designation = $window.sessionStorage.designation || '';
				$scope.userData.username = $window.sessionStorage.username || '';
				$scope.userData.report_manager = $window.sessionStorage.report_manager || '';
				$scope.userData.profilePicUrl = $window.sessionStorage.profilePicUrl || '';
			}

			// Call the initialization function on controller load
			initializeUserData();

			// For getting the profile pic by the Admin.
			$http.get('http://localhost:8080/api/profilePic/' + $scope.userData.id, {
				responseType: 'arraybuffer',
				headers: {
					'auth-token': $scope.userData.token
				}
			}).then(function(response) {
				if (response.data && response.data.byteLength > 0) {
					var arrayBufferView = new Uint8Array(response.data);
					var blob = new Blob([arrayBufferView], { type: response.headers('Content-Type') });
					var urlCreator = window.URL || window.webkitURL;
					var imageUrl = urlCreator.createObjectURL(blob);
					$scope.userData.profilePicUrl = imageUrl;
				} else {
					$scope.userData.profilePicUrl = 'img/profile_bydefault.jfif';
				}
			}).catch(function(error) {
				$scope.userData.profilePicUrl = 'img/profile_bydefault.jfif';
			});

			// For uploading the profile pic by the employees.
			$scope.uploadProfilePic = function(files) {
				if (files && files[0]) {
					var file = files[0];
					var maxFileSize = 16777215; // 16MB in bytes
					var allowedTypes = ['image/jpeg', 'image/png', 'image/gif'];

					if (file.size > maxFileSize) {
						alert('File size exceeds the maximum allowed size of 16MB.');
						return;
					}

					if (allowedTypes.indexOf(file.type) === -1) {
						alert('Only image files (JPEG, PNG, GIF) are allowed.');
						return;
					}

					var formData = new FormData();
					formData.append("file", file);
					var authToken = $scope.userData.token;

					$http.post('http://localhost:8080/api/uploadProfilePic/' + $scope.userData.id, formData, {
						transformRequest: angular.identity,
						headers: {
							'Content-Type': undefined,
							'auth-token': authToken
						}
					}).then(function(response) {
						if (response.data && response.data.profilePicUrl) {
							$scope.userData.profilePicUrl = 'img/' + response.data.profilePicUrl;
							alert('Profile picture uploaded successfully.');
						} else {
							alert('Failed to upload profile picture.');
						}
					}).catch(function(error) {
						if (error.data) {
							alert('Error: ' + error.data);
						} else {
							alert('Error uploading profile picture. Please try again later.');
						}
					});
				}
			};

			// Function to trigger file input
			$scope.triggerFileInput = function(event) {
				document.getElementById('profilePicFileInput').click();
			};

			// Dropdown menu
			$scope.showDropdown = false;
			$scope.toggleDropdown = function() {
				$scope.showDropdown = !$scope.showDropdown;
			};

			// For showing the user profile Section
			$scope.showProfile = false;
			$scope.toggleProfile = function() {
				$scope.showProfile = !$scope.showProfile;
				if ($scope.showProfile) {
					$scope.showSpecialRequest = false;
				}
			};


			// Event Listener after the click 
			document.addEventListener('click', function(event) {
				var profileSection = document.querySelector('.profile-section');
				var userProfile = document.querySelector('.user-profile');

				var isClickInsideProfile = profileSection && profileSection.contains(event.target);
				var isClickInsideUserProfile = userProfile && userProfile.contains(event.target);

				// when not clicked any of them then disappear these
				if (!isClickInsideProfile  && !isClickInsideUserProfile) {
					$scope.$apply(function() {
						$scope.showProfile = false;
					});
				}
			});

            // Function to toggle back to  employees view and redirect to /leave-Request-form page
            $scope.togglebackToUserScreen = function() {
                    $location.path('/leave-request');
            }; 
            
            
			// Log-out with the use of auth-token
			$scope.logout = function() {
				var headers = {
					'auth-token': $window.sessionStorage.token
				};
				var requestConfig = {
					headers: headers
				};
				$http.get('/api/logout', requestConfig)
					.then(function successCallback(response) {
						$window.sessionStorage.clear();
						$location.path("/login");
					}, function errorCallback(response) {
						alert('Error logging out:', response);
					});
			};

			$scope.newUser = {};
			$scope.uploadFile = null;
			$scope.registrationSuccess = false;

			$scope.checkUserExists = function() {
				if ($scope.registrationForm.$invalid) {
					return; // Form validation failed, do not proceed
				}

				var user = {
					username: $scope.newUser.username,
					emailId: $scope.newUser.emailId,
					contactNo: $scope.newUser.contactNo,
					id: $scope.newUser.id,
					userId: $scope.newUser.userId
				};

				$http.post('http://localhost:8080/api/checkUserExists', user, {
					headers: {
						'Content-Type': 'application/json',
						'auth-token': $scope.userData.token
					}
				})
					.then(function(response) {
						let data = response.data;
						if (data.usernameExists || data.emailExists || data.contactNoExists || data.idExist || data.userIdExist) {
							let message = 'User already registered with the same:\n';
							if (data.usernameExists) message += '- Username\n';
							if (data.emailExists) message += '- Email ID\n';
							if (data.contactNoExists) message += '- Contact Number\n';
							if (data.idExist) message += '- Employee ID\n';
							if (data.userIdExist) message += '- User ID\n';
							$scope.userExistsError = message;
						} else {
							$scope.userExistsError = '';
							$scope.registerUser(); // Proceed to register user   
						}
					})
					.catch(function(error) {
						console.error('Error checking user existence:', error);
						$scope.userExistsError = 'An error occurred while checking user existence. Please try again.';
					});
			};




			$scope.roles = [
				{ id: 1, name: 'User' },
				{ id: 2, name: 'Admin' },
				{ id: 3, name: 'Manager' }
			];
             
             //Used for inject to trusted html which angularjs in not register as trusted.
                $scope.getTrustedHtml = function(html) {
        return $sce.trustAsHtml(html);
       }; 
       
			$scope.newUser = {
				id: "",
				userId: "",
				username: "",
				designation: "",
				department: "",
				role: [],
				userKey: "",
				gender:"",
				reportingManager: "",
				contactNumber: null,
				email: "",
				password: "",
				birthDate: null,
				active: true,
				anniversaryDate: null
			};

			$scope.registerUser = function() {
				$scope.submitted = true;

				if (!$scope.newUser.department) {
					alert('Please select a department.');
					return;
				}
				if (!$scope.newUser.designation) {
					alert('Please select a designation.');
					return;
				}

				if ($scope.registrationForm.$valid) {
					var randomKey = generateRandomKey(6, 8);
					var roledata = $scope.newUser.role.map(role => role.name).join(',');

					var newUserData = {
						id: $scope.newUser.id,
						userId: $scope.newUser.userId,
						username: $scope.newUser.username,
						designation: $scope.newUser.designation,
						department: $scope.newUser.department,
						role: roledata,
						userKey: randomKey,
						gender:$scope.newUser.gender,
						reportManager: $scope.newUser.reportingManager,
						contactNo: $scope.newUser.contactNumber,
						emailId: $scope.newUser.email,
						password: $scope.newUser.password,
						birthDate: $scope.newUser.birthDate,
						active: true,
						aniversaryDate: $scope.newUser.anniversaryDate
					};

					$http.post('http://localhost:8080/api/super-admin/register', newUserData, {
						headers: {
							'Content-Type': 'application/json',
							'auth-token': $scope.userData.token
						}
					}).then(function(response) {
						$scope.registrationSuccess = true;
						$scope.newUser = {
							id: "",
							userId: "",
							username: "",
							designation: "",
							department: "",
							role: [],
							userKey: "",
							gender:"",
							reportingManager: "",
							contactNumber: null,
							email: "",
							password: "",
							birthDate: null,
							active: true,
							anniversaryDate: null
						};
						$scope.registrationForm.$setPristine();
						$scope.registrationForm.$setUntouched();
						$scope.submitted = false;
						alert('User registered successfully!');
					}, function(error) {
						alert('Error registering user. Please try again.');
					});
				} else {
					alert('Please fill out the form correctly.');
				}
			};

			// Function to generate a random key of specified length
			function generateRandomKey(minLength, maxLength) {
				var length = Math.floor(Math.random() * (maxLength - minLength + 1)) + minLength;
				var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
				var result = '';
				for (var i = 0; i < length; i++) {
					result += characters.charAt(Math.floor(Math.random() * characters.length));
				}
				return result;
			}

			// Call checkUserExists function when the registration form is submitted
			$scope.submitRegistrationForm = function() {
				$scope.checkUserExists();
			};

			// Initialize scope variables
			$scope.departments = [];
			$scope.designations = [];
			$scope.newUser = {};

			// Fetch departments
			$scope.getDepartments = function() {
				$http.get('http://localhost:8080/api/departments', {
					headers: {
						'Content-Type': 'application/json',
						'auth-token': $scope.userData.token
					}
				}).then(function(response) {
					$scope.departments = response.data;
				}, function(error) {
					console.error('Error fetching departments:', error);
				});
			};

			// Fetch designations based on selected department
			$scope.getDesignations = function() {
				if ($scope.newUser.department) {
					$http.get('http://localhost:8080/api/designation', {
						params: { department: $scope.newUser.department },
						headers: {
							'Content-Type': 'application/json',
							'auth-token': $scope.userData.token
						}
					}).then(function(response) {
						$scope.designations = response.data;
					}, function(error) {
						console.error('Error fetching designations:', error);
					});
				}
			};
			// Call the function to fetch departments
			$scope.getDepartments();

			// Uploading Only Excel file for chekcing  the absent dates and then marking lop for that employee
			$scope.uploadExcel = function() {
				if (!$scope.file || !$scope.file.name.match(/\.(xls|xlsx)$/)) {
					alert('Please upload a valid Excel file.');
					return;
				}

				var formData = new FormData();
				formData.append('file', $scope.file);

				$http.post('http://localhost:8080/api/super-admin/upload-excel', formData, {
					transformRequest: angular.identity,
					headers: {
						'Content-Type': undefined,
						'auth-token': $scope.userData.token
					}
				}).then(function(response) {
					alert(response.data.message);
				}, function(error) {
					alert('Error uploading file: ' + (error.data && error.data.message ? error.data.message : 'Unknown error'));
				});
			};


			// Initialize months and years for dropdowns
			$scope.months = [
				{ value: 1, name: 'January' },
				{ value: 2, name: 'February' },
				{ value: 3, name: 'March' },
				{ value: 4, name: 'April' },
				{ value: 5, name: 'May' },
				{ value: 6, name: 'June' },
				{ value: 7, name: 'July' },
				{ value: 8, name: 'August' },
				{ value: 9, name: 'September' },
				{ value: 10, name: 'October' },
				{ value: 11, name: 'November' },
				{ value: 12, name: 'December' }
			];
			$scope.years = [];
			for (var i = 2000; i <= new Date().getFullYear(); i++) {
				$scope.years.push(i);
			}

			// Download user report
			$scope.downloadReport = function() {
				if (!$scope.selectedMonth || !$scope.selectedYear) {
					alert("Please select both a month and a year");
					return;
				}

				var url = 'http://localhost:8080/api/super-admin/download-report?month=' + $scope.selectedMonth + '&year=' + $scope.selectedYear;

				var config = {
					headers: {
						'auth-token': $scope.userData.token
					},
					responseType: 'arraybuffer'
				};

				$http.get(url, config)
					.then(function(response) {
						var blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
						var link = document.createElement('a');
						link.href = window.URL.createObjectURL(blob);
						link.download = 'Employees_Report_' + $scope.selectedYear + '_' + $scope.selectedMonth + '.xlsx';
						link.click();
					})
					.catch(function(error) {
						console.error('Error downloading report', error);
						alert('Error downloading report');
					});
			};


		}]);
