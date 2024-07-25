package com.np.lms.entities;

// Wrapper class for the updation of leave after manager approve or declined.
public class LeaveUpdateRequest {
	private LeaveRequest leaveRequest;
	private LeaveStatus leaveStatus;

	public LeaveRequest getLeaveRequest() {
		return leaveRequest;
	}

	public void setLeaveRequest(LeaveRequest leaveRequest) {
		this.leaveRequest = leaveRequest;
	}

	public LeaveStatus getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(LeaveStatus leaveStatus) {
		this.leaveStatus = leaveStatus;
	}
}
