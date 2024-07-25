
angular.module('leaveManagementApp')
	.controller('AdminDashboardController', ['$scope', '$http', '$location', '$uibModal', '$window', function($scope, $http, $location, $uibModal,  $window) {

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

		// Function to fetch user data from session storage and initialize $scope.userData
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
		}).catch(function() {
			$scope.userData.profilePicUrl = 'img/profile_bydefault.jfif';
		});

		// For uploading the profile pic by the employees.
		$scope.uploadProfilePic = function(files) {
			if (files && files[0]) {
				var file = files[0];
				var maxFileSize = 16777215; 
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
		$scope.triggerFileInput = function() {
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

		// For Showing the special Request Section
		$scope.showSpecialRequest = false;
		$scope.toggleSpecialRequest = function() {
			$scope.showSpecialRequest = !$scope.showSpecialRequest;
			if ($scope.showSpecialRequest) {
				$scope.showProfile = false;
			}
		};

		// Event Listener after the click 
		document.addEventListener('click', function(event) {
			var profileSection = document.querySelector('.profile-section');
			var specialRequestSection = document.querySelector('.special-request-section');
			var userProfile = document.querySelector('.user-profile');

			var isClickInsideProfile = profileSection && profileSection.contains(event.target);
			var isClickInsideSpecialRequest = specialRequestSection && specialRequestSection.contains(event.target);
			var isClickInsideUserProfile = userProfile && userProfile.contains(event.target);

			// when not clicked any of them then disappear these
			if (!isClickInsideProfile && !isClickInsideSpecialRequest && !isClickInsideUserProfile) {
				$scope.$apply(function() {
					$scope.showProfile = false;
					$scope.showSpecialRequest = false;
				});
			}
		});

		// Fetch unread special requests for the logged-in Hr-Admin
		$scope.unreadNotificationsCount = 0;
		var authToken = $scope.userData.token;

		$http.get('http://localhost:8080/api/unreadSpecialRequests', {
			params: { username: $scope.userData.designation },
			headers: {
				'Content-Type': 'application/json',
				'auth-token': authToken
			}
		})
			.then(function(response) {
				$scope.unreadNotifications = response.data.filter(item => typeof item === 'object');
				$scope.unreadNotificationsCount = $scope.unreadNotifications.length;
			})
			.catch(function(error) {
				console.error('Error fetching unread special requests:', error);
			});

		// Toggle notifications modal
		$scope.showRequestsModal = false;
		$scope.toggleNotifications = function() {
			$scope.showRequestsModal = !$scope.showRequestsModal;
		};

		// Mark special request as read
		$scope.markAsRead = function(specialRequest) {
			var requestData = { id: specialRequest.id };

			$http.post('http://localhost:8080/api/markSpecialRequestAsRead', requestData, {
				headers: {
					'Content-Type': 'application/json',
					'auth-token': authToken
				}
			})
				.then(function() {
					var index = $scope.unreadNotifications.indexOf(specialRequest);
					if (index !== -1) {
						$scope.unreadNotifications.splice(index, 1);
						$scope.unreadNotificationsCount--; // Decrease count
						alert("Special Request marked as Read Successfully.");
						if ($scope.unreadNotificationsCount === 0) {
							$scope.showRequestsModal = false;
						}
					}
				})
				.catch(function(error) {
					console.error('Error marking special request as read:', error);
				});
		};

		$scope.closeModal = function() {
			$scope.showRequestsModal = false;
		};

		// Event listener to close modal when clicking outside
		document.addEventListener('click', function(event) {
			var notificationIcon = document.querySelector('.notification-icon');
			var modal = document.querySelector('.special-requests-modal');

			// Check if the click is inside the notification icon or modal
			var isClickInsideIcon = notificationIcon && notificationIcon.contains(event.target);
			var isClickInsideModal = modal && modal.contains(event.target);

			// Close modal if clicked outside of icon and modal
			if (!isClickInsideIcon && !isClickInsideModal) {
				$scope.$apply(function() {
					$scope.showRequestsModal = false;
				});
			}
		});

		// Function to send special request
		$scope.sendSpecialRequest = function(recipientType) {
			if ($scope.specialRequestForm.$valid) {
				var specialRequest = {
					recipient: recipientType,
					reason: $scope.request.reason,
					user: $scope.userData
				};
				$http.post('http://localhost:8080/api/specialRequest', specialRequest, {
					headers: {
						'Content-Type': 'application/json',
						'auth-token': authToken
					}
				})
					.then(function() {
						alert("Special Request Submitted successfully");
						$scope.request.reason = '';
					})
					.catch(function(error) {
						console.error("Error details:", error); // Log the entire error object
						alert("Failed to send special request. Error details: " + JSON.stringify(error));
					});
			}
		};

		$scope.showRequestsModal = false;

		$scope.closeModal = function() {
			$scope.showRequestsModal = false;
		};

		// Event listener to close modal when clicking outside
		document.addEventListener('click', function(event) {
			var notificationIcon = document.querySelector('.notification-icon');
			var modal = document.querySelector('.special-requests-modal');

			// Check if the click is inside the notification icon or modal
			var isClickInsideIcon = notificationIcon && notificationIcon.contains(event.target);
			var isClickInsideModal = modal && modal.contains(event.target);

			// Close modal if clicked outside of icon and modal
			if (!isClickInsideIcon && !isClickInsideModal) {
				$scope.$apply(function() {
					$scope.showRequestsModal = false;
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
				.then(function successCallback() {
					$window.sessionStorage.clear();
					$location.path("/login");
				}, function errorCallback(response) {
					alert('Error logging out:', response);
				});
		};




		// Function to fetch leave requests based on user roles
		function fetchLeaveRequests() {
			var userId = $scope.userData.id;
			var username = $scope.userData.username;
			var roles = $scope.userData.roles.map(function(role) {
				return role.toLowerCase();
			});

			if (roles.includes('manager')) {
				$http.get('http://localhost:8080/api/admin-dashboard/all', {
					params: { username: username },
					headers: {
						'auth-token': $scope.userData.token
					}
				})
					.then(function(response) {
						$scope.leaverequests = response.data;
					})
					.catch(function(error) {
						console.error('Error fetching leave requests:', error);
						alert('There was an error fetching the leave requests. Please try again.');
					});
			} else {
				$http.get('http://localhost:8080/api/' + userId, {
					headers: {
						'auth-token': $scope.userData.token
					}
				})
					.then(function(response) {
						$scope.leaverequests = response.data;
					})
					.catch(function(error) {
						console.error('Error fetching leave requests:', error);
						alert('There was an error fetching the leave requests. Please try again.');
					});
			}
		}

		fetchLeaveRequests();

		// When manager clicks on approve button
		$scope.approveLeave = function(request) {
			request.status = 'Approved';
			let updateRequest = {
				leaveRequest: request
			};

			$http.post('http://localhost:8080/api/leave-request/update', updateRequest, {
				headers: {
					'auth-token': $scope.userData.token
				}
			})
				.then(function() {
					console.log('Leave request approved successfully.');
					alert('Leave request approved successfully.');
				})
				.catch(function(error) {
					console.error('Error approving leave request:', error);
					alert('There was an error approving the leave request. Please try again.');
				});
		};


		//When user Click on Decline Request Button then  a pop will show.
		$scope.toggleRemarkInput = function(request) {
			var modalInstance = $uibModal.open({
				templateUrl: 'views/remarkModalContent.html',
				controller: 'RemarkModalController',
				resolve: {
					request: function() {
						return request;
					}
				}
			});
			modalInstance.result.then(function(remarks) {
				if (remarks) {
					request.remarks = remarks;
					$scope.decline(request);
				}
			}, function() {
				console.log('Modal dismissed at: ' + new Date());
			});
		};




		// When Manager Click on submit button with remarks then
		$scope.decline = function(request) {
			if (request.remarks) {
				request.status = 'Declined';
				let updateRequest = {
					leaveRequest: request
				};

				$http({
					method: 'POST',
					url: 'http://localhost:8080/api/leave-request/update',
					data: updateRequest,
					headers: {
						'Content-Type': 'application/json',
						'auth-token': $scope.userData.token // Assuming userData.token is initialized properly
					}
				})
					.then(function() {
						console.log('Leave request declined successfully.');
						alert('Leave request declined successfully.');
					})
					.catch(function(error) {
						console.error('Error declining leave request:', error);
						alert('There was an error declining the leave request. Please try again.');
					});
			} else {
				alert('Please provide remarks for declining the leave request.');
			}
		};




		// when user on cancel Decline then make false remarkInput
		$scope.cancelDecline = function(request) {
			request.showRemarkInput = false;
			request.remarks = '';
		};



		$scope.viewUserScreen = function(request) {
			var modalInstance = $uibModal.open({
				templateUrl: 'views/userScreenModal.html',
				controller: 'UserScreenModalController',
				resolve: {
					request: function() {
						return request;
					}
				}
			});

		}
	}])



	// Controller for the decline with remark pop up.
	.controller('RemarkModalController', ['$scope', '$uibModalInstance', 'request', function($scope, $uibModalInstance, request) {
		$scope.request = request;
		$scope.remarks = '';

		$scope.submit = function() {
			$uibModalInstance.close($scope.remarks);
		};

		$scope.cancel = function() {
			$uibModalInstance.dismiss('cancel');
		};

	}])




//User Details Screen Controller 
angular.module('leaveManagementApp')
	.controller('UserScreenModalController', ['$scope', '$http', '$location', '$window', '$uibModalInstance', 'request',
		function($scope, $http, $location, $window, $uibModalInstance, request) {

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
				$scope.userData.emailId = $window.sessionStorage.emailId;
				$scope.userData.designation = $window.sessionStorage.designation;
				$scope.userData.username = $window.sessionStorage.username;
				$scope.userData.report_manager = $window.sessionStorage.report_manager;
				$scope.userData.profilePicUrl = $window.sessionStorage.profilePicUrl;
				$scope.userData.roles = $window.sessionStorage.roles ? $window.sessionStorage.roles.split(',') : [];
			}

			// Call the initialization function on controller load
			initializeUserData();

			// Ensure userData is initialized before making HTTP requests
			if (!$scope.userData.token) {
				alert('User token is missing. Please log in again.');
				$uibModalInstance.dismiss('cancel');
				return;
			}
			var empId = request.user.id;
			// Fetch leave status based on userId
			$scope.counts = {
				total: 0,
				available: 0,
				approved: 0,
				declined: 0
			};
			$http({
				method: 'GET',
				url: 'http://localhost:8080/api/leave-status/' + empId,
				headers: {
					'auth-token': $scope.userData.token
				}
			}).then(function(response) {
				$scope.counts.total = response.data.total;
				$scope.counts.available = response.data.available;
				$scope.counts.approved = response.data.approved;
				$scope.counts.declined = response.data.declined;
			}).catch(function(error) {
				alert('There was an error fetching the leave status. Please try again.');
			});


			// Fetch all leave requests applied by the user based on userId
			$scope.leaverequests = [];
			$http({
				method: 'GET',
				url: 'http://localhost:8080/api/leave-request/' + empId,
				headers: {
					'auth-token': $scope.userData.token
				}
			}).then(function(response) {
				$scope.leaverequests = response.data;
			}).catch(function() {
				alert('There was an error fetching the leave requests. Please try again.');
			});
			$scope.cancel = function() {
				$uibModalInstance.dismiss('cancel');
			};

		}
	]);
