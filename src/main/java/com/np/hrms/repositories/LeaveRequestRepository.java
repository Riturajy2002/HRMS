package com.np.hrms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.np.hrms.entities.LeaveRequest;
import com.np.hrms.entities.User;
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

	
	
	
	
	
	
	
	@Query(value = "SELECT COUNT(*) FROM leave_management.leave_request " + "WHERE type = 'Maternity' AND "
			+ "status = 'Approved' AND " + "leave_operation_type = 'Debit' AND "
			+ "EXTRACT(YEAR FROM applied_date) = EXTRACT(YEAR FROM CURRENT_DATE) AND "
			+ "user_id = :userId", nativeQuery = true)
	int getMaternityAppliedCount(@Param("userId") String userId);

	@Query(value = "SELECT COUNT(*) FROM leave_management.leave_request " + "WHERE type = 'Paternity' AND "
			+ "status = 'Approved' AND " + "leave_operation_type = 'Debit' AND "
			+ "EXTRACT(YEAR FROM applied_date) = EXTRACT(YEAR FROM CURRENT_DATE) AND "
			+ "user_id = :userId", nativeQuery = true)
	int getPaternityAppliedCount(@Param("userId") String userId);

	@Query(value = "SELECT COUNT(*) FROM leave_management.leave_request " + "WHERE type = 'Componsatory' AND "
			+ "status = 'Approved' AND " + "leave_operation_type = 'Debit' AND "
			+ "EXTRACT(YEAR FROM applied_date) = EXTRACT(YEAR FROM CURRENT_DATE) AND "
			+ "user_id = :userId", nativeQuery = true)
	int getComponsatoryAppliedCount(@Param("userId") String userId);

	@Query(value = "SELECT COUNT(*) FROM leave_management.leave_request " + "WHERE type = 'Flexi' AND "
			+ "status = 'Approved' AND " + "leave_operation_type = 'Debit' AND "
			+ "EXTRACT(YEAR FROM applied_date) = EXTRACT(YEAR FROM CURRENT_DATE) AND "
			+ "user_id = :userId", nativeQuery = true)
	int getFlexiAppliedCount(@Param("userId") String userId);

	
	
	
	
	
	
	
	// For Checking no of count if any Type Credited leave
	@Query(value = "SELECT COALESCE(SUM(number_of_days), 0) FROM leave_management.leave_request "
			+ "WHERE type = 'Maternity' AND " + "leave_operation_type = 'Credit' AND " + "status = 'Approved'"
			+ "AND user_id = :userId", nativeQuery = true)
	int getMaternityTypeCredited(@Param("userId") String userId);

	// For Checking no of count if any paternity Type Credited leave
	@Query(value = "SELECT COALESCE(SUM(number_of_days), 0) FROM leave_management.leave_request "
			+ "WHERE type = 'Paternity' AND " + "leave_operation_type = 'Credit' AND " + "status = 'Approved'"
			+ "AND user_id = :userId", nativeQuery = true)
	int getPaternityTypeCredited(@Param("userId") String userId);

	// For Checking no of count if any Componsatory Type Credited leave
	@Query(value = "SELECT COALESCE(SUM(number_of_days), 0) FROM leave_management.leave_request "
			+ "WHERE type = 'Componsatory' AND " + "leave_operation_type = 'Credit' AND " + "status = 'Approved'"
			+ "AND user_id = :userId", nativeQuery = true)
	int getComponsatoryTypeCredited(@Param("userId") String userId);

	// For Checking no of count if any Flexi Type Credited leave
	@Query(value = "SELECT COALESCE(SUM(number_of_days), 0) FROM leave_management.leave_request "
			+ "WHERE type = 'Flexi' AND " + "leave_operation_type = 'Credit' AND " + "status = 'Approved'"
			+ "AND user_id = :userId", nativeQuery = true)
	int getFlexiTypeCredited(@Param("userId") String userId);

	// For Checking no of count if any Casual Type Credited leave
	@Query(value = "SELECT COALESCE(SUM(number_of_days), 0) FROM leave_management.leave_request "
			+ "WHERE type = 'Casual' AND " + "leave_operation_type = 'Credit' AND " + "status = 'Approved'"
			+ "AND user_id = :userId", nativeQuery = true)
	int getCasualTypeCredited(@Param("userId") String userId);

	// For Checking no of count if any Mediacl Type Credited leave
	@Query(value = "SELECT COALESCE(SUM(number_of_days), 0) FROM leave_management.leave_request "
			+ "WHERE type = 'Medical' AND " + "leave_operation_type = 'Credit' AND " + "status = 'Approved'"
			+ "AND user_id = :userId", nativeQuery = true)
	int getMedicalTypeCredited(@Param("userId") String userId);

	
	
	
	
	
	
	// For Checking no of count if any Maternity Type Debited leave
	@Query(value = "SELECT COALESCE(SUM(number_of_days), 0) FROM leave_management.leave_request "
			+ "WHERE type = 'Maternity' AND " + "leave_operation_type = 'Debit' AND " + "status = 'Approved'"
			+ "AND user_id = :userId", nativeQuery = true)
	int getMaternityTypeDebited(@Param("userId") String userId);

	// For Checking no of count if any Paternity Type Debited leave
	@Query(value = "SELECT COALESCE(SUM(number_of_days), 0) FROM leave_management.leave_request "
			+ "WHERE type = 'Paternity' AND " + "leave_operation_type = 'Debit' AND " + "status = 'Approved'"
			+ "AND user_id = :userId", nativeQuery = true)
	int getPaternityTypeDebited(@Param("userId") String userId);

	// For Checking no of count if any Componsatory Type Debited leave
	@Query(value = "SELECT COALESCE(SUM(number_of_days), 0) FROM leave_management.leave_request "
			+ "WHERE type = 'Componsatory' AND " + "leave_operation_type = 'Debit' AND " + "status = 'Approved'"
			+ "AND user_id = :userId", nativeQuery = true)
	int getComponsatoryTypeDebited(@Param("userId") String userId);

	// For Checking no of count if any Componsatory Type Debited leave
	@Query(value = "SELECT COALESCE(SUM(number_of_days), 0) FROM leave_management.leave_request "
			+ "WHERE type = 'Flexi' AND " + "leave_operation_type = 'Debit' AND " + "status = 'Approved'"
			+ "AND user_id = :userId", nativeQuery = true)
	int getFlexiTypeDebited(@Param("userId") String userId);

	// For Checking no of count if any Componsatory Type Debited leave
	@Query(value = "SELECT COALESCE(SUM(number_of_days), 0) FROM leave_management.leave_request "
			+ "WHERE type = 'Casual' AND " + "leave_operation_type = 'Debit' AND " + "status = 'Approved'"
			+ "AND user_id = :userId", nativeQuery = true)
	int getCasualTypeDebited(@Param("userId") String userId);

	// For Checking no of count if any Componsatory Type Debited leave
	@Query(value = "SELECT COALESCE(SUM(number_of_days), 0) FROM leave_management.leave_request "
			+ "WHERE type = 'Medical' AND " + "leave_operation_type = 'Debit' AND " + "status = 'Approved'"
			+ "AND user_id = :userId", nativeQuery = true)
	int getMedicalTypeDebited(@Param("userId") String userId);

	
	
	
	
	@Query("SELECT COALESCE(SUM(l.numberOfDays), 0) FROM LeaveRequest l WHERE   l.leaveOperationType = 'Debit' AND l.user.id = :userId AND l.status = 'Approved'")
	int getTotalDebitLeave( @Param("userId") String userId);

	@Query("SELECT COALESCE(SUM(l.numberOfDays), 0) FROM LeaveRequest l WHERE   l.leaveOperationType = 'Credit' AND l.user.id = :userId AND l.status = 'Approved'")
	int getTotalCreditLeave( @Param("userId") String userId);

	@Query("SELECT COALESCE(SUM(l.numberOfDays), 0) FROM LeaveRequest l WHERE   l.status = 'Declined' AND l.leaveOperationType = 'Debit' AND l.user.id = :userId")
	int getTotalDeclinedLeave(@Param("userId") String userId);

	
	
	
	
	
	
	
	
	
	
	
}
