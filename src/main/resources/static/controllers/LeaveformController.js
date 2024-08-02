angular.module('leaveManagementApp')
	.controller('LeaveformController', ['$scope','$sce', '$http', '$location', '$uibModal', 'AuthService', '$window', function($scope, $sce, $http, $location, $uibModal, AuthService, $window) {
               
               
    
            // Initialize User Data object
            $scope.userData = {
                id: '',
                contactNo: '',
                email_id: '',
                password: '',
                designation: '',
                username: '',
                report_manager: '',
                profilePicUrl: '',
                token: '',
                gender:'',
                roles: []
            };

            // Function to fetch user data from local storage and initialize $scope.userData
            function initializeUserData() {
                $scope.userData.id = $window.sessionStorage.id;
                $scope.userData.userId = $window.sessionStorage.userId;
                $scope.userData.token = $window.sessionStorage.token;
                $scope.userData.contactNo = $window.sessionStorage.contactNo;
                $scope.userData.email_id = $window.sessionStorage.email_id;
                $scope.userData.designation = $window.sessionStorage.designation;
                $scope.userData.username = $window.sessionStorage.username;
                $scope.userData.report_manager = $window.sessionStorage.report_manager;
                $scope.userData.profilePicUrl = $window.sessionStorage.profilePicUrl;
                $scope.userData.gender = $window.sessionStorage.gender;
                $scope.userData.roles = $window.sessionStorage.roles ? $window.sessionStorage.roles.split(',') : [];
            }

            // Call the initialization function on controller load
            initializeUserData();

            // Function to check if user is an admin
            $scope.isAdmin = function() {
                var roles = $scope.userData.roles.map(function(role) {
                    return role.toLowerCase().trim(); 
                });
                return roles.includes('admin');
            };

            // Function to check if user is a manager
            $scope.isManager = function() {
                var roles = $scope.userData.roles.map(function(role) {
                    return role.toLowerCase().trim(); 
                });
                return roles.includes('manager');
            };



            // Function to toggle add new employees view and redirect to /super-admin
            $scope.toggleAddNewEmployees = function() {
                if ($scope.isAdmin()) {
                    $location.path('/super-admin');
                }
            };
            
		$scope.showUploadModal = function(request) {
			var modalInstance = $uibModal.open({
				templateUrl: 'views/uploadModal.html',
				controller: 'UploadModalController',
				resolve: {
					request: function() {
						return request;
					}
				}
			});
		};
         
        $scope.showDownloadModal = function(request) {
		var modalInstance = $uibModal.open({
				templateUrl: 'views/downloadReport.html',
				controller: 'DownloadModalController',
				resolve: {
					request: function() {
						return request;
					}
				}
			});
		};
         
         
            // Function to toggle manager dashboard view and redirect to /admin-dashboard
            $scope.toggleManagerDashboard = function() {
                if ($scope.isManager()) {
                    $location.path('/admin-dashboard');
                }
            };

            // Fetch profile picture from the backend
            $http.get('/api/profilePic/' + $scope.userData.id, {
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

            // Upload profile picture by the employees
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

                    $http.post('/api/uploadProfilePic/' + $scope.userData.id, formData, {
                        transformRequest: angular.identity,
                        headers: {
                            'Content-Type': undefined,
                            'auth-token': $scope.userData.token
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

            // Show/hide user profile section
            $scope.showProfile = false;
            $scope.toggleProfile = function() {
                $scope.showProfile = !$scope.showProfile;
                if ($scope.showProfile) {
                    $scope.showSpecialRequest = false;
                }
            };

            // Close profile  sections on outside click
            document.addEventListener('click', function(event) {
                var profileSection = document.querySelector('.profile-section');
                var userProfile = document.querySelector('.user-profile');

                var isClickInsideProfile = profileSection && profileSection.contains(event.target);
                var isClickInsideUserProfile = userProfile && userProfile.contains(event.target);

                if (!isClickInsideProfile && !isClickInsideUserProfile) {
                    $scope.$apply(function() {
                        $scope.showProfile = false;
                    });
                }
            });

            // Log out with the use of auth-token
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

		// Fetch leave status based on userId
		$scope.counts = {
			total: 0,
			available: 0,
			approved: 0,
			declined: 0,
		};

		$http({
			method: 'GET',
			url: '/api/leave-status',
			params: {
				userId: $scope.userData.id
			},
			headers: {
				'auth-token': $scope.userData.token
			}
		}).then(function(response) {
			$scope.counts.total = response.data.total;
			$scope.counts.available = response.data.available;
			$scope.counts.approved = response.data.approved;
			$scope.counts.declined = response.data.declined;
		}).catch(function(error) {
			console.error('Error fetching leave status:', error);
			alert('There was an error fetching the leave status. Please try again.');
		});


		// Initialize Leave request data
		$scope.leaveRequest = {
			type: 'Casual',
			fromDate: '',
			toDate: '',
			numberOfDays: '',
			reason: '',
			manager: $scope.userData.report_manager,
			user: $scope.userData,
			appliedDate: new Date(),
			status: 'Pending'
		};

		// Initial setup
		$scope.flexiLeaveDays = [];
		$scope.showFlexiModal = false;

		// Get all Leaves for the year
		var currentYear = new Date().getFullYear();
		$http.get('/api/allFlexiLeaves', {
			params: { year: currentYear },
			headers: {
				'auth-token': $scope.userData.token
			}
		})
			.then(function(response) {
				$scope.flexiLeaveDays = response.data;
			})
			.catch(function(error) {
				console.error('Error fetching flexiLeaveDays:', error);
				alert('There was an error fetching the flexi leave days. Please try again.');
			});

		// Watch for changes in leave type
		$scope.$watch('leaveRequest.type', function(newVal, oldVal) {
			if (newVal === 'Flexi') {
				$scope.isFlexiLeave = true;
				$scope.openFlexiModal();
			} else {
				$scope.isFlexiLeave = false;
			}
		});

		// Open and close modal functions for the Flexi Select
		$scope.openFlexiModal = function() {
			$scope.showFlexiModal = true;
		};

		$scope.closeFlexiModal = function() {
			$scope.showFlexiModal = false;
		};

		// Setting the Start and end same as flexi Date
		$scope.selectFlexiDate = function(date) {
			var formattedDate = new Date(date.date);
			$scope.leaveRequest.fromDate = formattedDate;
			$scope.leaveRequest.toDate = formattedDate;
			$scope.closeFlexiModal();
			$scope.checkEligibility();
		};

		// Checking the Eligibility Criteria For apply the leaves
		$scope.checkEligibility = function() {
			var user = {
				id: $scope.userData.id,
				contactNo: $scope.userData.contactNo,
				emailId: $scope.userData.email_id,
				designation: $scope.userData.designation,
				username: $scope.userData.username,
				report_manager: $scope.userData.report_manager,
				gender: $scope.userData.gender,
			};

			// Format dates to ISO.DATE format (yyyy-MM-dd)
			var formattedFromDate = $scope.leaveRequest.fromDate.toISOString().split('T')[0];
			var formattedToDate = $scope.leaveRequest.toDate.toISOString().split('T')[0];

			$http({
				method: 'POST',
				url: '/api/checkLeave',
				headers: {
					'auth-token': $scope.userData.token
				},
				params: {
					type: $scope.leaveRequest.type,
					fromDate: formattedFromDate,
					toDate: formattedToDate
				},
				data: user
			}).then(function(response) {
				var message = response.data.message;
				// Extract the number of days from the message if applicable
				var match = message.match(/Total.*days applied: (\d+)/);
				if (match) {
					$scope.leaveRequest.numberOfDays = parseInt(match[1], 10);
					//alert('Applied Days :'+$scope.leaveRequest.numberOfDays)
				} else {
					alert(message);
					//$scope.resetForm();
				}
			}, function(error) {
				alert('Error checking eligibility: ' + error.statusText);
			});
		};

		// Submit leave request form data to DB
		$scope.submitForm = function() {
			if ($scope.leaveForm.$valid) {
				$http.post('/api/leave-request', $scope.leaveRequest, {
					headers: {
						'auth-token': $scope.userData.token
					}
				})
					.then(function(response) {
						alert('Leave request submitted successfully!');
						$scope.resetForm();
					})
					.catch(function(error) {
						alert('There was an error submitting your leave request. Please try again.');
					});
			}
		};

		// Reset form data after submitting the form
		$scope.resetForm = function() {
			$scope.leaveRequest = {
				type: 'Casual',
				fromDate: '',
				toDate: '',
				numberOfDays: '',
				reason: '',
				manager: $scope.userData.report_manager,
				appliedDate: new Date(),
				status: 'Pending'
			};
			$scope.leaveForm.$setPristine(); // No fields have been modified yet
			$scope.leaveForm.$setUntouched(); // The fields have not been touched yet
			$scope.datesModified = false; // Reset the flag
		};


			// Function to cancel a leave request
			$scope.cancelRequest = function(request) {
				if (request.status === 'Pending') {
					$http.put('/api/leave-request/cancel-leave-request/' + request.id, null, {
						headers: {
							'auth-token': $scope.userData.token
						}
					})
						.then(function(response) {
							request.status = 'Cancelled';
							alert("Request Cancelled Successfully");
						})
						.catch(function(error) {
							console.error('Error cancelling leave request:', error);
							alert('There was an error cancelling the leave request. Please try again.');
						});
				} else {
					alert('You can only cancel leave requests with Pending status.');
				}
			};

			// Fetch all leave requests applied by the user based on userId
			$scope.leaverequests = [];
			$http({
				method: 'GET',
				url: '/api/leave-request/' + $scope.userData.id,
				headers: {
					'auth-token': $scope.userData.token
				}
			}).then(function(response) {
				$scope.leaverequests = response.data;
			})
				.catch(function(error) {
					alert('There was an error fetching the leave requests. Please try again.');
				});

		}]);
		


// Attendence Update Section Controller.
angular.module('leaveManagementApp').controller('UploadModalController', ['$http', '$scope', '$window', '$uibModalInstance', 'AuthService',
	function($http, $scope, $window, $uibModalInstance, request, AuthService) {

		// Initialize User Data object
		$scope.userData = {
			id: '',
			contactNo: '',
			email_id: '',
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
			$scope.userData.token = $window.sessionStorage.token || '';
			$scope.userData.contactNo = $window.sessionStorage.contactNo || '';
			$scope.userData.email_id= $window.sessionStorage.email_id || '';
			$scope.userData.designation = $window.sessionStorage.designation || '';
			$scope.userData.username = $window.sessionStorage.username || '';
			$scope.userData.report_manager = $window.sessionStorage.report_manager || '';
			$scope.userData.profilePicUrl = $window.sessionStorage.profilePicUrl || '';
		}

		// Call the initialization function on controller load
		initializeUserData();

		// Function to upload Excel file
		$scope.uploadExcel = function() {
			if (!$scope.file || !$scope.file.name.match(/\.(xls|xlsx)$/)) {
				alert('Please upload a valid Excel file.');
				return;
			}

			var formData = new FormData();
			formData.append('file', $scope.file);

			$http.post('/api/super-admin/upload-excel', formData, {
				transformRequest: angular.identity,
				headers: {
					'Content-Type': undefined,
					'auth-token': $scope.userData.token
				}
			}).then(function(response) {
				alert(response.data.message);
				// Close the modal after upload
				$uibModalInstance.close();
			}, function(error) {
				alert('Error uploading file: ' + (error.data && error.data.message ? error.data.message : 'Unknown error'));
			});
		};

		// Function to cancel and close the modal
		$scope.cancel = function() {
			$uibModalInstance.dismiss('cancel');
		};
	}
]);


// Download Monthly Report Section Controller
angular.module('leaveManagementApp').controller('DownloadModalController', ['$http', '$scope', '$window', '$uibModalInstance',
	function($http, $scope, $window, $uibModalInstance) {
		// Initialize User Data object
		$scope.userData = {
			id: '',
			contactNo: '',
			email_id: '',
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

			var url = '/api/super-admin/download-report?month=' + $scope.selectedMonth + '&year=' + $scope.selectedYear;

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
					$uibModalInstance.close();
				})
				.catch(function(error) {
					console.error('Error downloading report', error);
					alert('Error downloading report');
				});
		};

		// Function to cancel and close the modal
		$scope.cancel = function() {
			$uibModalInstance.dismiss('cancel');
		};
	}
]);