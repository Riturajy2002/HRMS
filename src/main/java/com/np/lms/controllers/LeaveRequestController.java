package com.np.lms.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.np.lms.DTO.LeaveRequestDTO;
import com.np.lms.auth.Secured;
import com.np.lms.entities.HolidayMaster;
import com.np.lms.entities.LeaveRequest;
import com.np.lms.entities.LeaveUpdateRequest;
import com.np.lms.enums.Role;
import com.np.lms.services.HolidaymasterService;
import com.np.lms.services.LeaveRequestService;
import com.np.lms.services.LeaveRequestService1;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8080")
public class LeaveRequestController {

	@Autowired
	private LeaveRequestService leaveRequestService;

	@Autowired
	private HolidaymasterService holidayMasterService;
    
	
	@Autowired
	private LeaveRequestService1 leaveRequestService1;

	// Getting all leaveRequests Applied by aligned employees under a manager.
	@GetMapping("/admin-dashboard/all")
	@Secured({ Role.Manager })
	public List<LeaveRequestDTO> getAllPendingLeaveRequests(@RequestParam String username) {
		return leaveRequestService1.getPendingLeaveRequests(username);
	}
	
	// Get all recent leave requests for a user based on their userId.
	@GetMapping("/leave-request/{userId}")
	@Secured({ Role.Admin, Role.User, Role.Manager })
	public ResponseEntity<List<LeaveRequest>> getLeaveRequestsByUserId(@PathVariable("userId") String userId) {
		List<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequestsByUserId(userId);
		return ResponseEntity.ok(leaveRequests);
	}

	// Getting all Flexi Days Count.
	@GetMapping("/allFlexiLeaves")
	@Secured({ Role.Admin, Role.User, Role.Manager })
	public List<HolidayMaster> getFlexiLeaveDays(@RequestParam int year) {
		return holidayMasterService.findAllFlexiLeavesForTheYear(year);
	}

	// Create a new leave request
	@PostMapping("/leave-request")
	@Secured({ Role.User })
	public LeaveRequest createLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
		return leaveRequestService.saveLeaveRequest(leaveRequest);
	}

	// Update leave request status from pending to denied or approved by the manager
	@PostMapping("/leave-request/update")
	@Secured({ Role.Manager })
	public LeaveRequest updateLeaveRequest(@RequestBody LeaveUpdateRequest leaveUpdateRequest) {
		return leaveRequestService.updateLeaveRequest(leaveUpdateRequest.getLeaveRequest());
	}

	// Cancel leave request by requestId
	@PutMapping("/leave-request/cancel-leave-request/{id}")
	@Secured({ Role.User })
	public ResponseEntity<?> cancelLeaveRequest(@PathVariable("id") String id) {
		try {
			// Call service to cancel leave request by requestId
			leaveRequestService.cancelLeaveRequest(id);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error cancelling leave request.");
		}
	}
}
