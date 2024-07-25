package com.np.lms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.lms.entities.LeaveRequest;
import com.np.lms.entities.LeaveStatus;
import com.np.lms.repositories.LeaveRequestRepository;
import com.np.lms.repositories.LeaveStatusRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {

	@Autowired
	private LeaveRequestRepository leaveRequestRepository;
	@Autowired
	private LeaveStatusRepository leaveStatusRepository;

	public LeaveRequestService(LeaveRequestRepository leaveRequestRepository) {
		this.leaveRequestRepository = leaveRequestRepository;
	}

	// getting all recent leave request for a user based on him userId.
	public List<LeaveRequest> getLeaveRequestsByUserId(String userId) {
		return leaveRequestRepository.findByUserIdOrderByAppliedDateDesc(userId);
	}

	// Save applied leave requests by a user.
	public LeaveRequest saveLeaveRequest(LeaveRequest leaveRequest) {
		return leaveRequestRepository.save(leaveRequest);
	}

	// For updating LeaveRequest Status from pending to denied or Approve.
	public LeaveRequest updateLeaveRequest(LeaveRequest leaveRequest) {

		LeaveRequest leaveRequestData = null;
		Optional<LeaveRequest> existingRequest = leaveRequestRepository.findById(leaveRequest.getId());
		LeaveStatus existingStatus = leaveStatusRepository.findByUserId(leaveRequest.getUser().getId());

		if (existingRequest.isPresent()) {
			LeaveRequest updatedRequest = existingRequest.get();
			updatedRequest.setStatus(leaveRequest.getStatus());
			updatedRequest.setRemarks(leaveRequest.getRemarks());
			leaveRequestData = leaveRequestRepository.save(updatedRequest);
		}

		if (existingStatus != null) {
			// Update approved or declined count based on the status
			if (leaveRequest.getStatus().equals("Approved")) {

				// When it is a flexi types then
				if (leaveRequest.getType().equals("Flexi")) {
					existingStatus
							.setFlexiCount(existingStatus.getFlexiCount() + existingRequest.get().getNumberOfDays());
				}

				// if other than flexi then
				else {
					existingStatus.setApproved(existingStatus.getApproved() + existingRequest.get().getNumberOfDays());
					existingStatus
							.setAvailable((existingStatus.getAvailable() - existingRequest.get().getNumberOfDays()));
				}
			} else if (leaveRequest.getStatus().equals("Declined")) {
				existingStatus.setDeclined(existingStatus.getDeclined() + 1);
			}
			leaveStatusRepository.save(existingStatus);

		}
		return leaveRequestData;

	}

	public void cancelLeaveRequest(String id) {
		leaveRequestRepository.deleteRequestById(id);
	}

}
