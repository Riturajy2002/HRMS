package com.np.hrms.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "leave_request")
public class LeaveRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long Id;

	@Column(name = "type")
	private String type;

	@Column(name = "leave_operation_type")
	private String leaveOperationType;

	@Column(name = "from_date")
	private Date fromDate;

	@Column(name = "to_date")
	private Date toDate;

	@Column(name = "number_of_days")
	private int numberOfDays;

	@Column(name = "reason")
	private String reason;

	@Column(name = "manager")
	private String manager;

	@Column(name = "applied_date")
	private Date appliedDate;

	@Column(name = "status")
	private String status;

	@Column(name = "remarks")
	private String remarks;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	@JsonBackReference
	private User user;

	public LeaveRequest() {
		super();
	}

	public LeaveRequest(Long id, String type, String leaveOperationType, Date fromDate, Date toDate, int numberOfDays,
			String reason, String manager, Date appliedDate, String status, String remarks, User user) {
		super();
		Id = id;
		this.type = type;
		this.leaveOperationType = leaveOperationType;
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

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getLeaveOperationType() {
		return leaveOperationType;
	}

	public void setLeaveOperationType(String leaveOperationType) {
		this.leaveOperationType = leaveOperationType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getAppliedDate() {
		return appliedDate;
	}

	public void setAppliedDate(Date appliedDate) {
		this.appliedDate = appliedDate;
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

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
