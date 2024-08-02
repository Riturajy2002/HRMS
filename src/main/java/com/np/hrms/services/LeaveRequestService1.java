package com.np.hrms.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.np.hrms.dto.LeaveRequestDTO;
import com.np.hrms.dto.LeaveStatusDTO;
import com.np.hrms.dto.UserDTO;
import com.np.hrms.entities.LeaveRequest;
import com.np.hrms.entities.LeaveStatus;
import com.np.hrms.entities.User;
import com.np.hrms.repositories.LeaveRequestRepository;

@Service
public class LeaveRequestService1 {

	@Autowired
	private LeaveRequestRepository leaveRequestRepository;
    
	// Getting all leaveRequests Applied by aligned employees under a manager.
	public List<LeaveRequestDTO> getPendingLeaveRequests(String username) {
		List<LeaveRequest> leaveRequests = leaveRequestRepository
				.findPendingLeaveRequestsByReportManagerUsername(username);
		return leaveRequests.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	private LeaveRequestDTO convertToDTO(LeaveRequest leaveRequest) {
		LeaveRequestDTO dto = new LeaveRequestDTO();
		dto.setId(leaveRequest.getId());
		dto.setType(leaveRequest.getType());
		dto.setFromDate(leaveRequest.getFromDate());
		dto.setToDate(leaveRequest.getToDate());
		dto.setNumberOfDays(leaveRequest.getNumberOfDays());
		dto.setReason(leaveRequest.getReason());
		dto.setManager(leaveRequest.getManager());
		dto.setAppliedDate(leaveRequest.getAppliedDate());
		dto.setStatus(leaveRequest.getStatus());
		dto.setRemarks(leaveRequest.getRemarks());
		if (leaveRequest.getUser() != null) {
			UserDTO userDTO = convertUserToDTO(leaveRequest.getUser());
			dto.setUser(userDTO);
		}

		return dto;
	}

	private UserDTO convertUserToDTO(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setId(user.getId());
		userDTO.setUsername(user.getUsername());
		userDTO.setDesignation(user.getDesignation());
		userDTO.setContactNo(user.getContactNo());
		userDTO.setEmailId(user.getEmailId());
		userDTO.setReport_manager(user.getReportManager());

		// Setting leaveStatus
		if (user.getLeaveStatus() != null) {
			LeaveStatusDTO leaveStatusDTO = convertLeaveStatusToDTO(user.getLeaveStatus());
			userDTO.setLeaveStatus(leaveStatusDTO);
		}

		return userDTO;
	}

	private LeaveStatusDTO convertLeaveStatusToDTO(LeaveStatus leaveStatus) {
		LeaveStatusDTO dto = new LeaveStatusDTO();
		dto.setId(leaveStatus.getId());
		dto.setTotal(leaveStatus.getTotal());
		dto.setAvailable(leaveStatus.getAvailable());
		dto.setApproved(leaveStatus.getApproved());
		dto.setDeclined(leaveStatus.getDeclined());
		dto.setLopCount(leaveStatus.getLwpCount());

		// Convert User entity to UserDTO without circular references
		if (leaveStatus.getUser() != null) {
			UserDTO userDTO = new UserDTO();
			userDTO.setId(leaveStatus.getUser().getId());
			userDTO.setUsername(leaveStatus.getUser().getUsername());
			userDTO.setDesignation(leaveStatus.getUser().getDesignation());
			userDTO.setContactNo(leaveStatus.getUser().getContactNo());
			userDTO.setEmailId(leaveStatus.getUser().getEmailId());
			userDTO.setReport_manager(leaveStatus.getUser().getReportManager());
			// Note: Not setting leaveRequests to avoid circular references
			dto.setUser(userDTO);
		}

		return dto;
	}

}
