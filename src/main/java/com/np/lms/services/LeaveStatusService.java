package com.np.lms.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.lms.entities.LeaveStatus;
import com.np.lms.entities.User;
import com.np.lms.repositories.LeaveStatusRepository;
import com.np.lms.repositories.UserRepository;

@Service
public class LeaveStatusService {
	@Autowired
	private LeaveStatusRepository leaveStatusRepository;

	@Autowired
	private UserRepository userRepository;

	// For saving the leave Status for any user
	public LeaveStatus saveLeaveStatus(LeaveStatus leaveStatus1) {
		String userId = leaveStatus1.getUser().getId();
		int total = leaveStatus1.getTotal();
		int available = leaveStatus1.getAvailable();

		// Check if the user exists in the database
		Optional<User> optionalUser = userRepository.findById(userId);

		if (optionalUser.isPresent()) {
			// Create a new LeaveStatus object
			LeaveStatus leaveStatus = new LeaveStatus();

			// Set the user
			leaveStatus.setUser(optionalUser.get());

			// Set total and available leave
			leaveStatus.setTotal(total);
			leaveStatus.setAvailable(available);

			// Set default values for other fields
			leaveStatus.setApproved(0);
			leaveStatus.setDeclined(0);
			leaveStatus.setFlexiCount(0);
			leaveStatus.setFlexiCount(0);

			// Save the leaveStatus object
			return leaveStatusRepository.save(leaveStatus);
		} else {
			throw new IllegalArgumentException("User not found with ID: " + userId);
		}
	}

	// Get the leave Status for any user based on his userID
	public LeaveStatus getStatusByUserId(String userId) {
		return leaveStatusRepository.findByUserId(userId);
	}
}
