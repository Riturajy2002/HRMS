package com.np.lms.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.np.lms.entities.LeaveStatus;
import com.np.lms.entities.User;

@Repository
public interface LeaveStatusRepository extends JpaRepository<LeaveStatus, Long> {
	// Get the leave Status for any user based on him userID

	LeaveStatus findByUserId(String string);

	// for updating the total and available by the scheduler.
	List<LeaveStatus> findAll();

	// For saving the updation of lop Count
	void save(Optional<LeaveStatus> leaveStatus);

}
