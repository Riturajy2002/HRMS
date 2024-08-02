package com.np.hrms.dto;

public class LeaveStatusDTO {
	private int total;
	private int available;
	private int approved;
	private int declined;
	private int lwpCount;
	private int flexiCount;
	private UserDTO user;
	private Long id;

	public LeaveStatusDTO(int total, int available, int approved, int declined, int lopCount, int flexiCount,
			int pending, UserDTO user, Long id) {
		super();
		this.total = total;
		this.available = available;
		this.approved = approved;
		this.declined = declined;
		this.lwpCount = lopCount;
		this.user = user;
		this.id = id;
		this.flexiCount = flexiCount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public int getLopCount() {
		return lwpCount;
	}

	public void setLopCount(int lopCount) {
		this.lwpCount = lopCount;
	}

	public void setFlexiCount(int flexiCount) {
		this.flexiCount = flexiCount;
	}

	public int getFlexiCount() {
		return flexiCount;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public LeaveStatusDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
}
