package com.np.hrms.entities;

public class LeaveStatus {

	private Long id;
	private User user;
	private int total;
	private int available;
	private int approved;
	private int declined;
	private int lwpCount;

	public LeaveStatus(Long id, User user, int total, int available, int approved, int declined,
			int lwpCount) {
		super();
		this.id = id;
		this.user = user;
		this.total = total;
		this.available = available;
		this.approved = approved;
		this.declined = declined;
		this.lwpCount = lwpCount;
	}

	public LeaveStatus() {
		super();
		// TODO Auto-generated constructor stub
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

	public int getLwpCount() {
		return lwpCount;
	}

	public void setLwpCount(int lwpCount) {
		this.lwpCount = lwpCount;
	}
}