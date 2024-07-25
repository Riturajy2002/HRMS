package com.np.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.np.lms.entities.LeaveRequest;

@Repository
public interface AdminDashboardRepository extends JpaRepository<LeaveRequest, Long> {

}