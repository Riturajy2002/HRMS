package com.np.lms.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


//This is the entity table for types of the leaves.
@Entity
@Table(name = "leave_master")
public class LeaveMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "leave_type_id")
	private Long id;

	@Column(name = "leave_name")
	private String leaveName;

	@Column(name = "applicable_for")
	private String applicableFor;

	@Column(name = "no_of_days")
	private int noOfDays;

	@Column(name = "sandwich_included")
	private boolean sandWichIncluded;

	@Column(name = "types_of_leaves")
	private String typesOfLeaves;

	@Column(name = "applicable_in_a_year")
	private int numberOfTimeApplicable;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLeaveName() {
		return leaveName;
	}

	public LeaveMaster() {
		super();
	}

	public LeaveMaster(Long id, String leaveName, String applicableFor, int noOfDays, boolean sandWichIncluded,
			String typesOfLeaves, int numberOfTimeApplicable) {
		super();
		this.id = id;
		this.leaveName = leaveName;
		this.applicableFor = applicableFor;
		this.noOfDays = noOfDays;
		this.sandWichIncluded = sandWichIncluded;
		this.typesOfLeaves = typesOfLeaves;
		this.numberOfTimeApplicable = numberOfTimeApplicable;
	}

	public void setLeaveName(String leaveName) {
		this.leaveName = leaveName;
	}

	public String getApplicableFor() {
		return applicableFor;
	}

	public void setApplicableFor(String applicableFor) {
		this.applicableFor = applicableFor;
	}

	public int getNoOfDays() {
		return noOfDays;
	}

	public void setNoOfDays(int noOfDays) {
		this.noOfDays = noOfDays;
	}

	public boolean isSandWichIncluded() {
		return sandWichIncluded;
	}

	public void setSandWichIncluded(boolean sandWichIncluded) {
		this.sandWichIncluded = sandWichIncluded;
	}

	public String getTypesOfLeaves() {
		return typesOfLeaves;
	}

	public void setTypesOfLeaves(String typesOfLeaves) {
		this.typesOfLeaves = typesOfLeaves;
	}

	public int getNumberOfTimeApplicable() {
		return numberOfTimeApplicable;
	}

	public void setNumberOfTimeApplicable(int numberOfTimeApplicable) {
		this.numberOfTimeApplicable = numberOfTimeApplicable;
	}

}
