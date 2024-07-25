package com.np.lms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.np.lms.repositories.LeaveConfigRepository;

@RestController
@RequestMapping("/api")
public class LeaveConfigController {

	@Autowired
	private LeaveConfigRepository leaveConfigRepository;

	// Getting Flexi Limit for an Employee in a year.
	@GetMapping("/leave-config/flexi-limit")
	public int findFlexiLimitForTheYear(@RequestHeader("auth-token") String authToken) {
		return leaveConfigRepository.findFlexiLimitForYear();
	}
}
