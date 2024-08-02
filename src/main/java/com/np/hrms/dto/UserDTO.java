package com.np.hrms.dto;

import java.util.List;

import com.np.hrms.entities.User;

public class UserDTO {

	public UserDTO(User user) {
		super();
		// TODO Auto-generated constructor stub
	}

	private String id;
	private String username;
	private String designation;
	private Long contactNo;
	private String emailId;
	private String report_manager;
	private List<LeaveRequestDTO> leaveRequests;
	private LeaveStatusDTO leaveStatus;

	public String getId() {
		return id;
	}

	public void setId(String string) {
		this.id = string;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation  = designation;
	}

	public Long getContactNo() {
		return contactNo;
	}

	public void setContactNo(Long contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getReport_manager() {
		return report_manager;
	}

	public void setReport_manager(String report_manager) {
		this.report_manager = report_manager;
	}

	public List<LeaveRequestDTO> getLeaveRequests() {
		return leaveRequests;
	}

	public void setLeaveRequests(List<LeaveRequestDTO> leaveRequests) {
		this.leaveRequests = leaveRequests;
	}

	public LeaveStatusDTO getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(LeaveStatusDTO leaveStatus) {
		this.leaveStatus = leaveStatus;
	}

	public UserDTO(String id, String username, String role, Long contactNo, String emailId, String report_manager,
			List<LeaveRequestDTO> leaveRequests, LeaveStatusDTO leaveStatus) {
		super();
		this.id = id;
		this.username = username;
		this.designation = role;
		this.contactNo = contactNo;
		this.emailId = emailId;
		this.report_manager = report_manager;
		this.leaveRequests = leaveRequests;
		this.leaveStatus = leaveStatus;
	}

	public UserDTO() {
		// TODO Auto-generated constructor stub
	}
}
