package com.np.lms.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;

@Entity
public class LeaveStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@JsonBackReference
	private User user;
	private int total;
	private int available;
	private int approved;
	private int declined;
	private int lwpCount;
	private int flexiCount;

	public int getFlexiCount() {
		return flexiCount;
	}

	public void setFlexiCount(int flexiCount) {
		this.flexiCount = flexiCount;
	}

	public LeaveStatus(Long id, User user, int total, int available, int approved, int declined, int lwpCount,
			int flexiCount) {
		super();
		this.id = id;
		this.user = user;
		this.total = total;
		this.available = available;
		this.approved = approved;
		this.declined = declined;
		this.lwpCount = lwpCount;
		this.flexiCount = flexiCount;
	}

	public LeaveStatus() {
		super();

	}

	public int getLopCount() {
		return lwpCount;
	}

	public void setLopCount(int lopCount) {
		this.lwpCount = lopCount;
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

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getAvailable() {
		return available;
	}

	public void setAvailable(int available) {
		this.available = available;
	}

	public int getApproved() {
		return approved;
	}

	public void setApproved(int approved) {
		this.approved = approved;
	}

	public int getDeclined() {
		return declined;
	}

	public void setDeclined(int declined) {
		this.declined = declined;
	}

}
