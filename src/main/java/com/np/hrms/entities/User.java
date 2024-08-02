package com.np.hrms.entities;

import java.sql.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "user")
public class User {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "user_id")
	private String userId;

	@Column(name = "username")
	private String username;

	@Column(name = "designation")
	private String designation;

	@Column(name = "department")
	private String department;

	@Column(name = "role")
	private String role;

	@Column(name = "user_key")
	private String userKey;

	@Column(name = "report_manager")
	private String reportManager;

	@Column(name = "contact_no")
	private Long contactNo;

	@Column(name = "email_id")
	private String emailId;

	@Lob
	@Column(name = "password")
	private String password;

	@Column(name = "birth_date")
	private Date birthDate;

	@Column(name = "active")
	private boolean active;

	@Column(name = "aniversary_date")
	private Date aniversaryDate;

	@Column(name = "gender")
	private String gender;

	@Column(name = "profile_pic_url", length = 16777215)
	private String profilePicUrl;

	@OneToMany(mappedBy = "user")
	@JsonManagedReference
	private List<LeaveRequest> leaveRequests;

	@Transient
	private LeaveStatus leaveStatus;

	public User() {

	}

	public User(String id, String userId, String username, String password, String designation, String department,
			String role, String userKey, Long contactNo, String emailId, String reportManager, Boolean active,
			Date birthDate, Date aniversaryDate, String profilePicUrl, String gender, List<LeaveRequest> leaveRequests,
			LeaveStatus leaveStatus) {
		super();
		this.id = id;
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.designation = designation;
		this.department = department;
		this.role = role;
		this.userKey = userKey;
		this.contactNo = contactNo;
		this.emailId = emailId;
		this.reportManager = reportManager;
		this.birthDate = birthDate;
		this.aniversaryDate = aniversaryDate;
		this.leaveRequests = leaveRequests;
		this.leaveStatus = leaveStatus;
		this.gender = gender;
		this.profilePicUrl = profilePicUrl;
		this.active = active;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<LeaveRequest> getLeaveRequests() {
		return leaveRequests;
	}

	public void setLeaveRequests(List<LeaveRequest> leaveRequests) {
		this.leaveRequests = leaveRequests;
	}

	public LeaveStatus getLeaveStatus() {
		return leaveStatus;
	}

	public void setLeaveStatus(LeaveStatus leaveStatus) {
		this.leaveStatus = leaveStatus;
	}

	public User(String id) {
		this.id = id;
	}

	public String getProfilePicUrl() {
		return profilePicUrl;
	}

	public void setProfilePicUrl(String profilePicUrl) {
		this.profilePicUrl = profilePicUrl;
	}

	public String getReportManager() {
		return reportManager;
	}

	public void setReportManager(String reportManager) {
		this.reportManager = reportManager;
	}

	public String getDepartment() {
		return department;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Long getContactNo() {
		return contactNo;
	}

	public void setContactNo(Long contactNo) {
		this.contactNo = contactNo;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getDeparment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setAniversaryDate(Date aniversaryDate) {
		this.aniversaryDate = aniversaryDate;
	}

	public Date getAniversaryDate() {
		return aniversaryDate;
	}
}
