package com.np.hrms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.np.hrms.entities.LeaveRequest;
import com.np.hrms.repositories.LeaveRequestRepository;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {

	@Autowired
	private LeaveRequestRepository leaveRequestRepository;

	public LeaveRequestService(LeaveRequestRepository leaveRequestRepository) {
		this.leaveRequestRepository = leaveRequestRepository;
	}

	// getting all recent leave request for a user based on him userId.
	public List<LeaveRequest> getLeaveRequestsByUserId(String userId) {
		return leaveRequestRepository.findByUserIdOrderByAppliedDateDesc(userId);
	}

	// Save applied leave requests by a user.
	public LeaveRequest saveLeaveRequest(LeaveRequest leaveRequest) {
		leaveRequest.setLeaveOperationType("Debit");
		return leaveRequestRepository.save(leaveRequest);
	}

	// For updating LeaveRequest Status from pending to denied or Approve.
	public LeaveRequest updateLeaveRequest(LeaveRequest leaveRequest) {

		LeaveRequest leaveRequestData = null;
		Optional<LeaveRequest> existingRequest = leaveRequestRepository.findById(leaveRequest.getId());
		if (existingRequest.isPresent()) {
			LeaveRequest updatedRequest = existingRequest.get();
			updatedRequest.setStatus(leaveRequest.getStatus());
			updatedRequest.setRemarks(leaveRequest.getRemarks());
			leaveRequestData = leaveRequestRepository.save(updatedRequest);
		}
		return leaveRequestData;

	}
    
	//For Cancelling the leave Rrequest till the manager have not Approved or Declined.
	public void cancelLeaveRequest(String id) {
		leaveRequestRepository.deleteRequestById(id);
	}

	// For getting the total Leaves
	public int getTotalLeave(String userId) {
		int debited = leaveRequestRepository.getTotalDebitLeave(userId);
		int credited = leaveRequestRepository.getTotalCreditLeave(userId);
		return credited + debited;
	}

	// For getting the Total Available Leave
	public int getTotalAvailableLeave(String userId) {
		int total = getTotalLeave(userId);
		int debited = leaveRequestRepository.getTotalDebitLeave(userId);
		return total - debited;
	}

	// For getting the total Approved Leaves.
	public int getTotalApprovedLeave(String userId) {
		int debited = leaveRequestRepository.getTotalDebitLeave(userId);
		return debited;
	}

	// For getting the total Declined leave Requests.
	public int getTotalDeclinedLeave(String userId) {
		return leaveRequestRepository.getTotalDeclinedLeave(userId);
	}

}
