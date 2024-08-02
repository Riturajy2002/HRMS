package com.np.hrms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.np.hrms.entities.LeaveRequest;
import com.np.hrms.entities.User;
import com.np.hrms.repositories.LeaveConfigRepository;
import com.np.hrms.repositories.LeaveRequestRepository;
import com.np.hrms.repositories.UserRepository;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class LeaveSchedulerService {

	@Autowired
	private LeaveConfigRepository leaveConfigRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LeaveRequestRepository leaveRequestRepository;


	// @Scheduled(cron = "0 * * * * *") //Runs every minute for testing purpose.
	@Scheduled(cron = "0 0 0 1 * ?") // Runs at 12:00 AM on the first day of every month
	public void creditMonthlyLeave() {
		// Finding the Monthly leave increment from Repository.
		int monthlyLeaveIncrement = leaveConfigRepository.findMonthlyIncrement();

		List<User> allUsers = userRepository.findAll();
		for (User user : allUsers) {
			// Generate leave requests for Casual and Medical leave types
			generateLeaveRequest(user, "Casual", monthlyLeaveIncrement);
			//generateLeaveRequest(user, "Medical", monthlyLeaveIncrement);
		}
	}

	private void generateLeaveRequest(User user, String leaveType, int numberOfDays) {
		LeaveRequest leaveRequest = new LeaveRequest();
		leaveRequest.setUser(user);
		leaveRequest.setManager(user.getReportManager());
		leaveRequest.setType(leaveType);
		leaveRequest.setLeaveOperationType("Credit");
		leaveRequest.setNumberOfDays(numberOfDays);
		leaveRequest.setReason("Monthly Credited Leave");
		leaveRequest.setRemarks("This Leave is Credited By the Schedular Automatically.");
		leaveRequest.setStatus("Approved");

		java.util.Date currentDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());

		leaveRequest.setAppliedDate(currentDate);
		leaveRequest.setFromDate(currentDate);
		leaveRequest.setToDate(currentDate);

		leaveRequestRepository.save(leaveRequest);
	}

	
}
