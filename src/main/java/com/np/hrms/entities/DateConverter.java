package com.np.hrms.entities;

import java.sql.Date;
import java.time.LocalDate;

public class DateConverter {

	// Convert LocalDate to java.sql.Date
	public static Date toSqlDate(LocalDate localDate) {
		return localDate == null ? null : Date.valueOf(localDate);
	}

	// Convert java.sql.Date to LocalDate
	public static LocalDate toLocalDate(Date sqlDate) {
		return sqlDate == null ? null : sqlDate.toLocalDate();
	}
}
