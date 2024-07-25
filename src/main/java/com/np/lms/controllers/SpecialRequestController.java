package com.np.lms.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.np.lms.auth.Secured;
import com.np.lms.entities.SpecialRequest;
import com.np.lms.enums.Role;
import com.np.lms.services.SpecialRequestService;

@RestController
@RequestMapping("/api")
public class SpecialRequestController {

	@Autowired
	private SpecialRequestService specialRequestService;

	// For Saving the Special Request raised by the users.
	@PostMapping("/specialRequest")
	@Secured({ Role.User, Role.Admin, Role.Manager })
	public ResponseEntity<String> handleSpecialRequest(@RequestBody SpecialRequest specialRequest) {
		try {
			specialRequestService.saveSpecialRequest(specialRequest);
			return ResponseEntity.ok().body("{\"message\": \"Special request sent successfully\"}");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to process special request: " + e.getMessage());
		}
	}

	// For Getting the special request to the manager dashbaord or Hr-Admin
	// Dashboard .
	@GetMapping("/unreadSpecialRequests")
	@Secured({ Role.Admin, Role.Manager })
	public ResponseEntity<List<SpecialRequest>> getUnreadSpecialRequests(@RequestParam("username") String designation) {
		try {
			List<SpecialRequest> unreadRequests = specialRequestService.findUnreadSpecialRequests(designation);
			return ResponseEntity.ok().body(unreadRequests);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	// For Marking the Request as read after read by hr or manager.
	@PostMapping("/markSpecialRequestAsRead")
	@Secured({ Role.Admin, Role.Manager })
	public ResponseEntity<String> markSpecialRequestAsRead(@RequestBody Map<String, Long> requestBody) {
		try {
			Long requestId = requestBody.get("id");
			specialRequestService.markSpecialRequestAsRead(requestId);
			return ResponseEntity.ok().body("{\"message\": \"Special request marked as read\"}");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to mark special request as read: " + e.getMessage());
		}
	}

}
