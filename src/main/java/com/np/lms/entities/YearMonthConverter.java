package com.np.lms.entities;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.sql.Date;
import java.time.YearMonth;

// Just a Converter for the YearMonth to Sql Date format 
@Converter(autoApply = true)
public class YearMonthConverter implements AttributeConverter<YearMonth, Date> {

	@Override
	public Date convertToDatabaseColumn(YearMonth yearMonth) {
		if (yearMonth == null) {
			return null;
		}
		return Date.valueOf(yearMonth.atDay(1)); // Convert to the first day of the month
	}

	@Override
	public YearMonth convertToEntityAttribute(Date date) {
		if (date == null) {
			return null;
		}
		return YearMonth.from(date.toLocalDate());
	}
}
