package com.np.hrms.entities;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "holiday_master")
public class HolidayMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long Id;

	@Column(name = "name")
	private String name;

	@Column(name = "day")
	private String day;

	@Column(name = "type")
	private String type;

	@Column(name = "date")
	private Date date;

	public HolidayMaster(Long id, Date date, String name, String type, String day) {
		super();
		Id = id;
		this.date = date;
		this.name = name;
		this.type = type;
		this.day = day;
	}

	public HolidayMaster() {
		super();
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getName() {
		return name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDay() {
		return day;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
