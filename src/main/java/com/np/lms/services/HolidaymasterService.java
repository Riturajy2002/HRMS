package com.np.lms.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.lms.entities.HolidayMaster;
import com.np.lms.repositories.HolidayMasterRepository;

@Service
public class HolidaymasterService {
	@Autowired
	private HolidayMasterRepository holidayMasterRepository;

	// all flexi for a Year
	public List<HolidayMaster> findAllFlexiLeavesForTheYear(int year) {
		return holidayMasterRepository.findAllFlexiLeavesForYear(year);
	}

}
