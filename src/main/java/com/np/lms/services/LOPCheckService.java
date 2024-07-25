package com.np.lms.services;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.lms.entities.LeaveRequest;
import com.np.lms.entities.LeaveStatus;
import com.np.lms.entities.MonthlyReport;
import com.np.lms.entities.User;
import com.np.lms.repositories.LeaveRequestRepository;
import com.np.lms.repositories.LeaveStatusRepository;
import com.np.lms.repositories.MonthlyReportRepository;
import com.np.lms.repositories.UserRepository;

@Service
public class LOPCheckService {

	@Autowired
	private LeaveRequestRepository leaveRequestRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LeaveStatusRepository leaveStatusRepository;

	@Autowired
	private MonthlyReportRepository monthlyReportRepository;

	public void updateLOPStatus(Map<String, List<LocalDate>> absentDates) {
		// Iterate over each employee's absent dates
		for (Map.Entry<String, List<LocalDate>> entry : absentDates.entrySet()) {
			String employeeDetail = entry.getKey();
			List<LocalDate> datesList = entry.getValue();

	    	String[] details = employeeDetail.split(" -- ");
			String empCode = details[0];
			String userId = empCode;
 
			// Fetch user from database
			Optional<User> userOpt = userRepository.findById(userId);
			if (!userOpt.isPresent()) {
				continue;
			}
			User user = userOpt.get();

			// Check each absent date
			int lopCount = 0;
			for (LocalDate date : datesList) {
				// Convert LocalDate to java.sql.Date
				Date sqlDate = Date.valueOf(date);

				List<LeaveRequest> leaveRequests = leaveRequestRepository.findByUserAndDateBetween(user, sqlDate);
				if (leaveRequests.isEmpty() || "Declined".equalsIgnoreCase(leaveRequests.get(0).getStatus())
						|| "Pending".equalsIgnoreCase(leaveRequests.get(0).getStatus())) {
					lopCount++;
				}
			}
			// Updation of LOP count for the user in LeaveStatus
			LeaveStatus leaveStatus = leaveStatusRepository.findByUserId(userId);
			if (leaveStatus != null) {
				leaveStatus.setLopCount(lopCount);
				leaveStatusRepository.save(leaveStatus);

				// Generate monthly report
				YearMonth yearMonth = YearMonth.now(); // Assuming the current month and year for the report
				MonthlyReport report = new MonthlyReport();
				report.setUserId(userId);
				report.setUserName(user.getUsername());
				report.setReportYearMonth(yearMonth);
				report.setAvailableLeaves(leaveStatus.getAvailable());
				report.setApprovedLeaves((int) leaveStatusRepository.findByUserId(userId).getApproved());
				report.setDeclinedRequests((int) leaveStatusRepository.findByUserId(userId).getDeclined());
				report.setLwpCount(lopCount);
				monthlyReportRepository.save(report);
			}
		}
	}
}
