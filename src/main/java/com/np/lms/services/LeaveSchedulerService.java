package com.np.lms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.np.lms.entities.LeaveStatus;
import com.np.lms.repositories.LeaveConfigRepository;
import com.np.lms.repositories.LeaveStatusRepository;

import java.util.List;

@Service
public class LeaveSchedulerService {

	@Autowired
	private LeaveStatusRepository leaveStatusRepository;
	@Autowired
	private LeaveConfigRepository leaveConfigRepository;

	private int monthlyLeaveIncrement;

	// @Scheduled(cron = "0 * * * * *") //Runs every minute for testing purpose.

	// Schedule the task to run at 11:59 PM on the last day of every month
	@Scheduled(cron = "59 59 23 L * ?")
	public void addMonthlyLeave() {

		// Finding the Monthly leave increment from Repository.
		monthlyLeaveIncrement = leaveConfigRepository.findMonthlyincrement();

		List<LeaveStatus> allLeaveStatuses = leaveStatusRepository.findAll();

		for (LeaveStatus leaveStatus : allLeaveStatuses) {
			// Add one leave to the total leaves
			leaveStatus.setTotal(leaveStatus.getTotal() + monthlyLeaveIncrement);
			// Add one leave to the available leaves
			leaveStatus.setAvailable(leaveStatus.getAvailable() + monthlyLeaveIncrement);
			leaveStatusRepository.save(leaveStatus);
		}
	}

	// Scheduling Every start of the year for all the user flexi count become zero.

	// @Scheduled(cron = "0 * * * * *") // Runs every minute for testing purpose.

	@Scheduled(cron = "0 0 0 1 1 ?") // Every Start of the year
	public void setFlexiCountToZeroEveryYear() {
		List<LeaveStatus> allLeaveStatusForFlexi = leaveStatusRepository.findAll();
		for (LeaveStatus leaveStatus : allLeaveStatusForFlexi) {
			leaveStatus.setFlexiCount(0);
			leaveStatusRepository.save(leaveStatus);
		}
	}

}
