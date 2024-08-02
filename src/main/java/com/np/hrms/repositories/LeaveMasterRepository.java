package com.np.hrms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.np.hrms.entities.LeaveMaster;

@Repository
public interface LeaveMasterRepository extends JpaRepository<LeaveMaster,Long> {
	

    @Query(value = "SELECT max_applicable FROM leave_master WHERE leave_name = 'Maternity'", nativeQuery = true)
    int getMaternityMaxAllowedCount();
    @Query(value = "SELECT max_applicable FROM leave_master WHERE leave_name = 'Paternity'", nativeQuery = true)
    int getPaternityMaxAllowedCount();
    @Query(value = "SELECT max_applicable FROM leave_master WHERE leave_name = 'Componsatory'", nativeQuery = true)
    int  getComponsatoryMaxAllowedCount();
    @Query(value = "SELECT max_applicable FROM leave_master WHERE leave_name = 'Flexi'", nativeQuery = true)
    int getFlexiMaxAllowedCount();
    
    @Query(value = "SELECT no_of_days FROM leave_master WHERE leave_name = 'Maternity'", nativeQuery = true)
    int getMaternityMaxDaysLimit();
    
    @Query(value = "SELECT no_of_days FROM leave_master WHERE leave_name = 'Paternity'", nativeQuery = true)
    int getPaternityMaxDaysLimit();
    
    @Query(value = "SELECT no_of_days FROM leave_master WHERE leave_name = 'Componsatory'", nativeQuery = true)
    int getComponsatoryMaxDaysLimit();
    
    @Query(value = "SELECT no_of_days FROM leave_master WHERE leave_name = 'Flexi'", nativeQuery = true)
    int getFlexiMaxDaysLimit();
}
