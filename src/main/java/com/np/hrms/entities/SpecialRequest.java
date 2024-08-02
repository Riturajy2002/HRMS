package com.np.lms.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Date;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpecialRequest {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	private String reason;
	private String recipient;
	private Date appliedDate;
	private int specialRequestStatus;

	public SpecialRequest() {
	}

	public SpecialRequest(Long id, User user, String reason, String recipient, int specialRequestStatus) {
		this.id = id;
		this.user = user;
		this.reason = reason;
		this.recipient = recipient;
		this.appliedDate = new Date();
		this.specialRequestStatus = specialRequestStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public Date getAppliedDate() {
		return appliedDate;
	}

	public void setAppliedDate(Date appliedDate) {
		this.appliedDate = appliedDate;
	}

	public int getSpecialRequestStatus() {
		return specialRequestStatus;
	}

	public void setSpecialRequestStatus(int specialRequestStatus) {
		this.specialRequestStatus = specialRequestStatus;
	}
}
