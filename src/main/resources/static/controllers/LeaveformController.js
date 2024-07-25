angular.module('leaveManagementApp')
	.controller('LeaveformController', ['$scope','$sce', '$http', '$location', '$uibModal', 'AuthService', '$window', function($scope, $sce, $http, $location, $uibModal, AuthService, $window) {
               
               
    
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
                token: '',
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

                    $http.post('http://localhost:8080/api/uploadProfilePic/' + $scope.userData.id, formData, {
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

            // Show/hide special request section
            $scope.showSpecialRequest = false;
            $scope.toggleSpecialRequest = function() {
                $scope.showSpecialRequest = !$scope.showSpecialRequest;
                if ($scope.showSpecialRequest) {
                    $scope.showProfile = false;
                }
            };

            // Close profile and special request sections on outside click
            document.addEventListener('click', function(event) {
                var profileSection = document.querySelector('.profile-section');
                var specialRequestSection = document.querySelector('.special-request-section');
                var userProfile = document.querySelector('.user-profile');

                var isClickInsideProfile = profileSection && profileSection.contains(event.target);
                var isClickInsideSpecialRequest = specialRequestSection && specialRequestSection.contains(event.target);
                var isClickInsideUserProfile = userProfile && userProfile.contains(event.target);

                if (!isClickInsideProfile && !isClickInsideSpecialRequest && !isClickInsideUserProfile) {
                    $scope.$apply(function() {
                        $scope.showProfile = false;
                        $scope.showSpecialRequest = false;
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

			// Function to send special request
			$scope.sendSpecialRequest = function(recipientType) {
				if ($scope.specialRequestForm.$valid) {
					var specialRequest = {
						recipient: recipientType,
						reason: $scope.request.reason,
						user: $scope.userData
					};

					var authToken = $scope.userData.token;

					$http.post('/api/specialRequest', specialRequest, {
						headers: {
							'auth-token': authToken
						}
					})
						.then(function(response) {
							alert("Special Request submitted successfully");
							$scope.request.reason = '';
						})
						.catch(function(error) {
							alert("Failed to send special request. Error details: " + JSON.stringify(error));
						});
				}
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
				url: 'http://localhost:8080/api/leave-status/' + $scope.userData.id,
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
			$scope.flexiDaysUsed = 0;
			$scope.flexiLeaveDays = [];
			$scope.showFlexiModal = false;
			$scope.flexiCountLimit = 0;


			// Get All Flexi count used by the Employees
			$http({
				method: 'GET',
				url: 'http://localhost:8080/api/leave-status/' + $scope.userData.id,
				headers: {
					'auth-token': $scope.userData.token
				}
			})
				.then(function(response) {
					$scope.flexiDaysUsed = response.data.flexiCount;
				})
				.catch(function(error) {
					console.error('Error fetching leave status:', error);
					alert('There was an error fetching the leave status. Please try again.');
				});



			// Get all Leaves  for the year
			var currentYear = new Date().getFullYear();
			$http.get('http://localhost:8080/api/allFlexiLeaves', {
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



			// Get Flexi Limit for one Year
			$http({
				method: 'GET',
				url: 'http://localhost:8080/api/leave-config/flexi-limit',
				headers: {
					'auth-token': $scope.userData.token
				}
			})
				.then(function(response) {
					$scope.flexiCountLimit = response.data;
				})
				.catch(function(error) {
					console.error('Error fetching flexiLimitCount', error);
					alert('There was an error fetching the flexi limit. Please try again.');
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



			// Open and close modal functions for the Flexi Select.
			$scope.openFlexiModal = function() {
				if ($scope.flexiDaysUsed < $scope.flexiCountLimit) {
					$scope.showFlexiModal = true;
				} else {
					alert('You have no flexi leaves available now. This will be revised next year.');
				}
			};

			$scope.closeFlexiModal = function() {
				$scope.showFlexiModal = false;
			};


			// Setting the Start and end same as flexi Date
			$scope.selectFlexiDate = function(date) {
				var formattedDate = new Date(date.date);
				$scope.leaveRequest.fromDate = formattedDate;
				$scope.leaveRequest.toDate = formattedDate;
				$scope.leaveRequest.numberOfDays = 1;
				$scope.closeFlexiModal();
			};


			//functions (calculateDays, checkNumberOfDays, submitForm, etc.)
			$scope.calculateDays = function() {
				$scope.datesModified = true;

				if ($scope.leaveRequest.fromDate && $scope.leaveRequest.toDate) {
					const fromDate = new Date($scope.leaveRequest.fromDate);
					const toDate = new Date($scope.leaveRequest.toDate);

					if ($scope.leaveRequest.type === 'Flexi' && $scope.leaveRequest.numberOfDays === 1) {
						// If Flexi leave and numberOfDays is 1, handle it as single day selection
						$scope.leaveRequest.numberOfDays = 1;
					} else {
						if (toDate < fromDate) {
							$scope.leaveRequest.numberOfDays = 0;
						} else {
							let countDays = 0;
							let flexiDaysUsed = $scope.flexiDaysUsed || 0;

							for (let d = new Date(fromDate); d <= toDate; d.setDate(d.getDate() + 1)) {
								const currentDate = new Date(d);
								const currentDateStr = currentDate.toISOString().slice(0, 10);

								if ($scope.flexiLeaveDays.includes(currentDateStr)) {
									if (flexiDaysUsed < $scope.flexiCountLimit) {
										flexiDaysUsed++;
									} else {
										countDays++;
									}
								} else {
									countDays++;
								}
							}

							$scope.leaveRequest.numberOfDays = countDays;
							$scope.flexiDaysUsed = flexiDaysUsed; // Update flexiDaysUsed in scope
						}
					}
				} else {
					$scope.leaveRequest.numberOfDays = '';
				}

				$scope.checkNumberOfDays(); // Validate number of days
			};

			$scope.checkNumberOfDays = function() {
				if ($scope.leaveRequest.numberOfDays <= 0) {
					$scope.leaveForm.numberOfDays.$setValidity('invalidNumberOfDays', false);
				} else {
					$scope.leaveForm.numberOfDays.$setValidity('invalidNumberOfDays', true);
				}
			};

			// Submit leave request form data to DB
			$scope.submitForm = function() {
				if ($scope.leaveForm.$valid) {
					$http.post('http://localhost:8080/api/leave-request', $scope.leaveRequest, {
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
					$http.put('http://localhost:8080/api/leave-request/cancel-leave-request/' + request.id, null, {
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
				url: 'http://localhost:8080/api/leave-request/' + $scope.userData.id,
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
    function($http, $scope, $window,  $uibModalInstance, request, AuthService) {

        // Initialize User Data object
        $scope.userData = {
            id: '',
            contactNo: '',
            emailId: '',
            password: '',
            designation: '',
            username: '',m,
            report_manager: '',
            profilePicUrl: '',
            token: ''
        };

        // Function to fetch user data from local storage and initialize $scope.userData
        function initializeUserData() {
            $scope.userData.id = $window.sessionStorage.id || '';
            $scope.userData.token = $window.sessionStorage.token || '';
            $scope.userData.contactNo = $window.sessionStorage.contactNo || '';
            $scope.userData.emailId = $window.sessionStorage.emailId || '';
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

            $http.post('http://localhost:8080/api/super-admin/upload-excel', formData, {
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
            $scope.userData.token = $window.sessionStorage.token || '';
            $scope.userData.contactNo = $window.sessionStorage.contactNo || '';
            $scope.userData.emailId = $window.sessionStorage.emailId || '';
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