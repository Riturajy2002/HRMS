package com.np.lms.repositories;

import com.np.lms.entities.HolidayMaster;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface HolidayMasterRepository extends CrudRepository<HolidayMaster, Long> {

	@Query("SELECT h FROM HolidayMaster h WHERE h.type = 'Flexi' AND YEAR(h.date) = :year")
	List<HolidayMaster> findAllFlexiLeavesForYear(@Param("year") int year);
}
