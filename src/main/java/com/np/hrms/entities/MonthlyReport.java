package com.np.hrms.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.YearMonth;

@Entity

// Maintaining User monthly record for the admin download
public class MonthlyReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String userId;
	private String userName;
	private YearMonth reportYearMonth; // Changed type to YearMonth
	private int availableLeaves;
	private int approvedLeaves;
	private int lwpCount;
    
	@Column(name = "designation")
	private String designation;
	
	@Column(name = "reporting_manager")
	private String reporting_manager;
	

	public MonthlyReport() {
		super();
	}

	public MonthlyReport(Long id, String userId, String userName, YearMonth reportYearMonth, int availableLeaves,
			int approvedLeaves, int lwpCount, String designation , String reporting_manager)  {
		super();
		this.id = id;
		this.userId = userId;
		this.userName = userName;
		this.reportYearMonth = reportYearMonth;
		this.availableLeaves = availableLeaves;
		this.approvedLeaves = approvedLeaves;
		this.lwpCount = lwpCount;
		this.designation = designation;
		this.reporting_manager = reporting_manager;
	}

	// Getters and setters

	public Long getId() {
		return id;
	}
    
	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getReporting_manager() {
		return reporting_manager;
	}

	public void setReporting_manager(String reporting_manager) {
		this.reporting_manager = reporting_manager;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId2) {
		this.userId = userId2;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public YearMonth getReportYearMonth() {
		return reportYearMonth;
	}

	public void setReportYearMonth(YearMonth reportYearMonth) {
		this.reportYearMonth = reportYearMonth;
	}

	public int getAvailableLeaves() {
		return availableLeaves;
	}

	public void setAvailableLeaves(int availableLeaves) {
		this.availableLeaves = availableLeaves;
	}

	public int getApprovedLeaves() {
		return approvedLeaves;
	}

	public void setApprovedLeaves(int approvedLeaves) {
		this.approvedLeaves = approvedLeaves;
	}

	public int getLwpCount() {
		return lwpCount;
	}

	public void setLwpCount(int lwpCount) {
		this.lwpCount = lwpCount;
	}
}
