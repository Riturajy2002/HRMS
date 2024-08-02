package com.np.hrms.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.np.hrms.auth.Secured;
import com.np.hrms.entities.User;
import com.np.hrms.enums.Role;
import com.np.hrms.repositories.SqlDAO;
import com.np.hrms.repositories.UserRepository;
import com.np.hrms.services.ExcelReportService;
import com.np.hrms.services.ExcelService;
import com.np.hrms.services.LOPCheckService;

@RestController
@RequestMapping("/api")
public class SuperAdminController {

	@Autowired
	private ExcelService excelService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SqlDAO sqlDao;

	@Autowired
	private LOPCheckService lopCheckService;

	@Autowired
	private ExcelReportService excelReportService;

	// Register for a new user
	@PostMapping("/super-admin/register")
	@Secured({ Role.Admin })
	public User RegisterNewUser(@RequestBody User user) {
		User reponseUser = null;
		int noOfRows = sqlDao.saveUser(user);
		if (noOfRows > 0) {
			reponseUser = userRepository.findByUserId(user.getUserId());
		}
		return reponseUser;
	}

	// For uploading the excel to process and find the no. of absents of a user.
	@PostMapping("/super-admin/upload-excel")
	@Secured({ Role.Admin })
	public ResponseEntity<Map<String, String>> uploadExcelFile(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("message", "Please select a file!"));
		}

		try {
			// Getting the number of absent Dates.
			Map<String, List<LocalDate>> absentDates = excelService.processExcelFile(file);
			// marking the lopCount if leave is not approved
			lopCheckService.updateLOPStatus(absentDates);
			return ResponseEntity.status(HttpStatus.OK)
					.body(Collections.singletonMap("message", "File uploaded and processed successfully"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("message", "Failed to process the file: " + e.getMessage()));
		}
	}

	// For Downloading report By Slection Month and Year
	@GetMapping("/super-admin/download-report")
	@Secured({ Role.Admin })
	public ResponseEntity<byte[]> getMonthlyReport(@RequestParam("month") int month, @RequestParam("year") int year) {
		YearMonth yearMonth = YearMonth.of(year, month);

		byte[] excelData;
		try {
			excelData = excelReportService.generateMonthlyReport(yearMonth);
		} catch (IOException e) {
			return ResponseEntity.status(500).body(null);
		}

		return ResponseEntity.ok()
				.header("Content-Disposition", "attachment; filename=User_Report_" + year + "_" + month + ".xlsx")
				.body(excelData);
	}

}
