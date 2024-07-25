package com.np.lms.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "designations")
public class Designation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "designation_names")
	private String designationNames;
	
	@Column(name = "department_name")
	private String department;

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getDesignationNames() {
		return designationNames;
	}

	public void setDesignationNames(String designationNames) {
		this.designationNames = designationNames;
	}

	public Designation(Long id, String designationNames, String department) {
		super();
		this.id = id;
		this.designationNames = designationNames;
		this.department = department;
	}

	public Designation() {
		super();
		// TODO Auto-generated constructor stub
	}
}
