 package com.np.hrms.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.np.hrms.entities.HolidayMaster;
import java.sql.Date;
import java.util.List;

@Repository
public interface HolidayMasterRepository extends CrudRepository<HolidayMaster, Long> {

	@Query("SELECT h FROM HolidayMaster h WHERE h.type = 'Flexi' AND YEAR(h.date) = :year")
	List<HolidayMaster> findAllFlexiLeavesForYear(@Param("year") int year);
	
	
	
	@Query("SELECT h.date FROM HolidayMaster h WHERE h.type = 'Fixed' AND YEAR(h.date) = YEAR(CURRENT_DATE)")
	List<Date> getFixedHolidaysForCurrentYear();

	@Query("SELECT h.date FROM HolidayMaster h WHERE h.type = 'Flexi' AND YEAR(h.date) = YEAR(CURRENT_DATE)")
	List<Date> findAllFlexiLeavesForCurrentYear();


}
