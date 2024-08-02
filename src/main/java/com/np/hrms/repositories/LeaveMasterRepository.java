package com.np.hrms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.np.hrms.entities.LeaveMaster;

@Repository
public interface LeaveMasterRepository extends JpaRepository<LeaveMaster, Long> {

	// For Getting total apply time of Maternity leave for a employee in a Year.
	@Query(value = "SELECT max_applicable FROM leave_master WHERE leave_name = 'Maternity'", nativeQuery = true)
	int getMaternityMaxAllowedCount();

	// For Getting total apply time of Paternity leave for a employee in a Year.
	@Query(value = "SELECT max_applicable FROM leave_master WHERE leave_name = 'Paternity'", nativeQuery = true)
	int getPaternityMaxAllowedCount();

	// For Getting total apply time of Componsatory leave for a employee in a Year.
	@Query(value = "SELECT max_applicable FROM leave_master WHERE leave_name = 'Componsatory'", nativeQuery = true)
	int getComponsatoryMaxAllowedCount();

	// For Getting total apply time of Flexi leave for a employee in a Year.
	@Query(value = "SELECT max_applicable FROM leave_master WHERE leave_name = 'Flexi'", nativeQuery = true)
	int getFlexiMaxAllowedCount();

	// For Finding the Maternity limit in a Year.
	@Query(value = "SELECT no_of_days FROM leave_master WHERE leave_name = 'Maternity'", nativeQuery = true)
	int getMaternityMaxDaysLimit();

	// For Finding the Paternity limit in a Year.
	@Query(value = "SELECT no_of_days FROM leave_master WHERE leave_name = 'Paternity'", nativeQuery = true)
	int getPaternityMaxDaysLimit();

	// For Finding the Componsatory limit in a Year.
	@Query(value = "SELECT no_of_days FROM leave_master WHERE leave_name = 'Componsatory'", nativeQuery = true)
	int getComponsatoryMaxDaysLimit();

	// For Finding the Flexi limit in a Year.
	@Query(value = "SELECT no_of_days FROM leave_master WHERE leave_name = 'Flexi'", nativeQuery = true)
	int getFlexiMaxDaysLimit();
}
