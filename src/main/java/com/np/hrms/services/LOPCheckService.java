package com.np.hrms.services;

import java.sql.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.np.hrms.entities.LeaveRequest;
import com.np.hrms.entities.MonthlyReport;
import com.np.hrms.entities.User;
import com.np.hrms.repositories.LeaveRequestRepository;
import com.np.hrms.repositories.MonthlyReportRepository;
import com.np.hrms.repositories.UserRepository;

@Service
public class LOPCheckService {

	@Autowired
	private LeaveRequestRepository leaveRequestRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LeaveRequestService leaveRequestService;

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

			// Generate monthly report
			YearMonth yearMonth = YearMonth.now(); // Assuming the current month and year for the report
			MonthlyReport report = new MonthlyReport();
			report.setUserId(userId);
			report.setUserName(user.getUsername());
			report.setDesignation(user.getDesignation());
			report.setReporting_manager(user.getReportManager());
			report.setReportYearMonth(yearMonth);
			report.setAvailableLeaves(leaveRequestService.getTotalAvailableLeave(userId));
			report.setApprovedLeaves((int) leaveRequestService.getTotalApprovedLeave(userId));
			report.setLwpCount(lopCount);
			monthlyReportRepository.save(report);
		}
	}
}
