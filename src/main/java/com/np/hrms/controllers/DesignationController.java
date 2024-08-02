package com.np.hrms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.np.hrms.auth.Secured;
import com.np.hrms.enums.Role;
import com.np.hrms.repositories.DesignationRepository;

@RestController
@RequestMapping("/api")
public class DesignationController {

	@Autowired
	private DesignationRepository designationRepository;

	// For getting all the departments.
	@GetMapping("/departments")
	@Secured({ Role.Admin })
	public List<String> getAllDepartments() {
		return designationRepository.findAllDepartments();
	}

	// For Finding the designations according to the selected department.
	@GetMapping("/designation")
	@Secured({ Role.Admin })
	public List<String> getAllDesignationsBasedOnDepartments(@RequestParam("department") String department) {
		return designationRepository.findDesignationNamesByDepartmentName(department);
	}

}
