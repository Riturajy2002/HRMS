package com.np.hrms.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.np.hrms.entities.LeaveConfig;

@Repository
public interface LeaveConfigRepository extends CrudRepository<LeaveConfig, Long> {

	@Query("SELECT value FROM LeaveConfig lc WHERE lc.type = 'monthlyLeaveIncrementCount'")
	int findMonthlyIncrement();
}
