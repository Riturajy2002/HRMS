package com.np.lms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.lms.entities.LeaveRequest;
import com.np.lms.repositories.LeaveRequestRepository;

@Service
public class AdminDashboardService {
	@Autowired
	private LeaveRequestRepository leaveRequestRepository;

	// Getting all leaveRequests Applied by a user based on their userId and in
	// descending order by Applied Date.
	public List<LeaveRequest> getLeaveRequestsByUserId(String userId) {
		return leaveRequestRepository.findByUserIdOrderByAppliedDateDesc(userId);
	}

	// Getting all leaveRequests Applied by all the users in descending order by
	// Applied Date.
	public List<LeaveRequest> getAllLeaveRequests(String username) {
		return leaveRequestRepository.findPendingLeaveRequestsByReportManagerUsername(username);
	}
}
