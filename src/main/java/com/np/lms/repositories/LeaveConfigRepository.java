package com.np.lms.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.np.lms.entities.LeaveConfig;

@Repository
public interface LeaveConfigRepository extends CrudRepository<LeaveConfig, Long> {

	@Query("SELECT value FROM LeaveConfig lc WHERE lc.type = 'monthlyLeaveIncrementCount'")
	int findMonthlyincrement();

	@Query("SELECT value FROM LeaveConfig lc WHERE lc.type = 'flexiLimitPerYear'")
	int findFlexiLimitForYear();
}
