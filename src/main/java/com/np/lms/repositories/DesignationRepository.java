package com.np.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.np.lms.entities.Designation;
import java.util.List;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {

    @Query("SELECT DISTINCT d.department FROM Designation d")
    List<String> findAllDepartments();
    
    @Query(value = "SELECT designationNames FROM Designation WHERE department = :departmentName")
    List<String> findDesignationNamesByDepartmentName(@Param("departmentName") String departmentName);

}
