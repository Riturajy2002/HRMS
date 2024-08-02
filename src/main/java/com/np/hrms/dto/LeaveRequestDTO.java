package com.np.hrms.dto;

import java.sql.Date;

public class LeaveRequestDTO {

	public LeaveRequestDTO(Long id, String type, Date fromDate, Date toDate, int numberOfDays, String reason,
			String manager, Date appliedDate, String status, String remarks, UserDTO user) {
		super();
		this.id = id;
		this.type = type;
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.numberOfDays = numberOfDays;
		this.reason = reason;
		this.manager = manager;
		this.appliedDate = appliedDate;
		this.status = status;
		this.remarks = remarks;
		this.user = user;
	}

	public LeaveRequestDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	private Long id;
	private String type;
	private Date fromDate;
	private Date toDate;
	private int numberOfDays;
	private String reason;
	private String manager;
	private Date appliedDate;
	private String status;
	private String remarks;
	private UserDTO user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(java.util.Date date) {
		this.fromDate = new Date(date.getTime());
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(java.util.Date date) {
		this.toDate = new Date(date.getTime());
	}

	public int getNumberOfDays() {
		return numberOfDays;
	}

	public void setNumberOfDays(int numberOfDays) {
		this.numberOfDays = numberOfDays;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public void setAppliedDate(java.util.Date date) {
		this.appliedDate = new Date(date.getTime());
	}

	public Date getAppliedDate() {
		return appliedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

}
