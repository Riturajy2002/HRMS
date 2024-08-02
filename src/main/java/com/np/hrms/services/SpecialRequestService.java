package com.np.lms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.np.lms.entities.SpecialRequest;
import com.np.lms.repositories.SpecialRequestRepository;

import java.util.Date; // Import Date class for current date
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialRequestService {

	@Autowired
	private SpecialRequestRepository specialRequestRepository;

	// For Saving the Special Request Raised by the User.
	public void saveSpecialRequest(SpecialRequest specialRequest) {
		if ("manager".equals(specialRequest.getRecipient())) {
			specialRequest.setRecipient(specialRequest.getUser().getReportManager());
			specialRequest.setSpecialRequestStatus(0);
		} else {
			specialRequest.setRecipient("admin");
			specialRequest.setSpecialRequestStatus(0);
		}
		specialRequest.setAppliedDate(new Date());
		specialRequestRepository.save(specialRequest);
	}

	// For finding all the request raided by the employees coming under a manager.
	public List<SpecialRequest> findUnreadSpecialRequests(String designation) {
		List<SpecialRequest> unreadRequests = specialRequestRepository.findUnreadSpecialRequests(designation);
		List<SpecialRequest> validRequests = unreadRequests.stream()
				.filter(request -> request instanceof SpecialRequest).collect(Collectors.toList());
		return validRequests;
	}

	// For marking the Special Request as Readed.
	public void markSpecialRequestAsRead(Long requestId) {
		java.util.Optional<SpecialRequest> optionalSpecialRequest = specialRequestRepository.findById(requestId);
		if (optionalSpecialRequest.isPresent()) {
			SpecialRequest specialRequest = optionalSpecialRequest.get();
			specialRequest.setSpecialRequestStatus(1); 
			specialRequestRepository.save(specialRequest);
		} else {
			throw new IllegalArgumentException("Special request not found with id: " + requestId);
		}
	}

}