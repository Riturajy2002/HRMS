package com.np.lms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.np.lms.auth.Secured;
import com.np.lms.entities.LeaveStatus;
import com.np.lms.enums.Role;
import com.np.lms.services.LeaveStatusService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class LeaveStatusController {

	@Autowired
	private LeaveStatusService leaveStatusService;

	// For saving the leave status for any user
	@PostMapping("/leave-status")
	@Secured({ Role.Admin })
	public ResponseEntity<LeaveStatus> createLeaveStatus(@RequestBody LeaveStatus leaveStatus,
			@RequestHeader("auth-token") String authToken) {
		LeaveStatus savedLeaveStatus = leaveStatusService.saveLeaveStatus(leaveStatus);
		return new ResponseEntity<>(savedLeaveStatus, HttpStatus.CREATED);
	}

	// Get the leave status for any user based on userID
	@GetMapping("/leave-status/{userId}")
	@Secured({ Role.Admin, Role.User, Role.Manager })
	public ResponseEntity<LeaveStatus> getLeaveStatus(@PathVariable String userId,
			@RequestHeader("auth-token") String authToken) {
		LeaveStatus leaveStatus = leaveStatusService.getStatusByUserId(userId);
		return new ResponseEntity<>(leaveStatus, HttpStatus.OK);
	}
}
