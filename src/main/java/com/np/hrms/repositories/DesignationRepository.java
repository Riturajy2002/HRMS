package com.np.hrms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.np.hrms.entities.Designation;

import java.util.List;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {
    
	// For getting all the departments.
	@Query("SELECT DISTINCT d.department FROM Designation d")
	List<String> findAllDepartments();

	// For Finding the designations according to the selected department.
	@Query(value = "SELECT designationNames FROM Designation WHERE department = :departmentName")
	List<String> findDesignationNamesByDepartmentName(@Param("departmentName") String departmentName);

}
