package com.np.lms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.np.lms.entities.LeaveRequest;
import com.np.lms.entities.User;

import java.sql.Date;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {

	// Getting all leaveRequests applied by a user based on user id and in
	// descending order by applied date.
	List<LeaveRequest> findByUserIdOrderByAppliedDateDesc(String userId);

	// Getting all leaveRequests applied by all the users in descending order by
	// applied date.
	@Query("SELECT lr FROM LeaveRequest lr JOIN FETCH lr.user u WHERE lr.status = 'Pending' AND u.reportManager = :username ORDER BY lr.appliedDate DESC")
	List<LeaveRequest> findPendingLeaveRequestsByReportManagerUsername(@Param("username") String username);

	// Find leave requests for a user where a given date lies between fromDate and
	// toDate.
	@Query("SELECT lr FROM LeaveRequest lr WHERE lr.user = :user AND :date BETWEEN lr.fromDate AND lr.toDate")
	List<LeaveRequest> findByUserAndDateBetween(@Param("user") User user, @Param("date") Date date);

	// Custom query to delete a leave request by id.
	@Modifying
	@Transactional
	@Query("DELETE FROM LeaveRequest lr WHERE lr.id = :id")
	void deleteRequestById(@Param("id") String id);
}
