package com.np.hrms.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.np.hrms.entities.DateConverter;
import com.np.hrms.entities.HolidayMaster;
import com.np.hrms.repositories.HolidayMasterRepository;

@Service
public class HolidayService {

	@Autowired
	private HolidayMasterRepository holidayMasterRepository;

	public List<LocalDate> getFixedHolidays() {
		List<java.sql.Date> sqlDates = holidayMasterRepository.getFixedHolidaysForCurrentYear();
		return sqlDates.stream().map(date -> date.toLocalDate()).collect(Collectors.toList());
	}

	public void saveHoliday(HolidayMaster holiday) {
		holiday.setDate(DateConverter.toSqlDate(holiday.getDate().toLocalDate()));
		holidayMasterRepository.save(holiday);
	}
}
