package com.np.hrms.controllers;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.np.hrms.auth.Secured;
import com.np.hrms.dto.LeaveRequestDTO;
import com.np.hrms.entities.HolidayMaster;
import com.np.hrms.entities.LeaveRequest;
import com.np.hrms.entities.LeaveStatus;
import com.np.hrms.entities.LeaveUpdateRequest;
import com.np.hrms.entities.User;
import com.np.hrms.enums.Role;
import com.np.hrms.repositories.HolidayMasterRepository;
import com.np.hrms.repositories.LeaveMasterRepository;
import com.np.hrms.repositories.LeaveRequestRepository;
import com.np.hrms.services.LeaveRequestService;
import com.np.hrms.services.LeaveRequestService1;

@RestController
@RequestMapping("/api")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;
    
    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    
    @Autowired
    private HolidayMasterRepository holidayMasterRepository;

    @Autowired
    private LeaveRequestService1 leaveRequestService1;
    
    
    @Autowired 
    private LeaveMasterRepository leaveMasterRepository;

    
    // Getting all leaveRequests Applied by aligned employees under a manager.
    @GetMapping("/admin-dashboard/all")
    @Secured({ Role.Manager })
    public List<LeaveRequestDTO> getAllPendingLeaveRequests(@RequestParam String username) {
        return leaveRequestService1.getPendingLeaveRequests(username);
    }

    // Get all recent leave requests for a user based on their userId.
    @GetMapping("/leave-request/{userId}")
    @Secured({ Role.Admin, Role.User, Role.Manager })
    public ResponseEntity<List<LeaveRequest>> getLeaveRequestsByUserId(@PathVariable("userId") String userId) {
        List<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequestsByUserId(userId);
        return ResponseEntity.ok(leaveRequests);
    }

    // Getting all Flexi Days Count.
    @GetMapping("/allFlexiLeaves")
    @Secured({ Role.Admin, Role.User, Role.Manager })
    public List<HolidayMaster> getFlexiLeaveDays(@RequestParam int year) {
        return holidayMasterRepository.findAllFlexiLeavesForYear(year);
    }

    // Create a new leave request
    @PostMapping("/leave-request")
    @Secured({ Role.User })
    public LeaveRequest createLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        return leaveRequestService.saveLeaveRequest(leaveRequest);
    }
 
    // Update leave request status from pending to denied or approved by the manager
    @PostMapping("/leave-request/update")
    @Secured({ Role.Manager })
    public LeaveRequest updateLeaveRequest(@RequestBody LeaveUpdateRequest leaveUpdateRequest) {
        return leaveRequestService.updateLeaveRequest(leaveUpdateRequest.getLeaveRequest());
    }

    // Cancel leave request by requestId
    @PutMapping("/leave-request/cancel-leave-request/{id}")
    @Secured({ Role.User })
    public ResponseEntity<?> cancelLeaveRequest(@PathVariable("id") String id) {
        try {
            // Call service to cancel leave request by requestId
            leaveRequestService.cancelLeaveRequest(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error cancelling leave request.");
        }
    }
	
    
    // For Showing the Leave Status on the User DashBoard.
	@GetMapping("/leave-status")
	@Secured({ Role.Admin, Role.User, Role.Manager })
	public LeaveStatus getLeaveStatus(@RequestParam String userId) {
		LeaveStatus leaveStatus = new LeaveStatus();
		leaveStatus.setTotal(leaveRequestService.getTotalLeave(userId));
		leaveStatus.setApproved(leaveRequestService.getTotalApprovedLeave(userId));
		leaveStatus.setAvailable(leaveRequestService.getTotalAvailableLeave(userId));
		leaveStatus.setDeclined(leaveRequestService.getTotalDeclinedLeave(userId));

		return leaveStatus;
	}
	
	// Mthod for caculating the number of Days Excluding weekends and FixedHolidays  as well.
	private int calculateEffectiveLeaveDays(LocalDate fromDate, LocalDate toDate, List<LocalDate> fixedHolidays) {
		int totalDays = 0;
		LocalDate currentDate = fromDate;

		while (!currentDate.isAfter(toDate)) {
			boolean isFixedHoliday = fixedHolidays.contains(currentDate);
			boolean isWeekend = !isWorkingDay(currentDate);

			// Count days only if it's a working day and not a fixed holiday
			if (!isWeekend && !isFixedHoliday) {
				totalDays++;
			}
			currentDate = currentDate.plusDays(1);
		}
		return totalDays;
	}

	private boolean isWorkingDay(LocalDate date) {
		DayOfWeek dayOfWeek = date.getDayOfWeek();
		return !(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY);
	}

	// Mthod for caculating the number of Days including weekends and FixedHolidays as well.
	private int calculateDaysIncludingSandwichAndFixedHolidays(LocalDate fromDate, LocalDate toDate) {
		int totalDays = 0;
		LocalDate currentDate = fromDate;
		while (!currentDate.isAfter(toDate)) {
			// Count every day including weekends and holidays
			totalDays++;
			currentDate = currentDate.plusDays(1);
		}
		return totalDays;
	}

	
	@PostMapping("/checkLeave")
	@Secured({ Role.Manager, Role.User, Role.Admin })
	public ResponseEntity<Map<String, String>> checkLeave(@RequestBody User user, @RequestParam String type,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

		// Fetch fixed holidays from the database
		List<java.sql.Date> sqlDates = holidayMasterRepository.getFixedHolidaysForCurrentYear();
		List<LocalDate> fixedHolidays = sqlDates.stream().map(Date::toLocalDate).collect(Collectors.toList());
        
		// For Storing the reponse of this function to return.
		Map<String, String> response = new HashMap<>();
		
		
		// Here we will write all the condition Regarding the Maternity Leave
		if (type.equals("Maternity")) {

			// Case 1:- Employee Should be Female type.
			if (!"Female".equals(user.getGender())) {
				response.put("message", "Only Female Employees are eligible for this type of leave.");
				return ResponseEntity.ok(response);
			}

			// Case 2:- In that Particular employee have not applied any Time for this leave bcz this can user appply only once.
			int maternityAppliedCount = leaveRequestRepository.getMaternityAppliedCount(user.getId());
			int maternityMaxAllowedCount = leaveMasterRepository.getMaternityMaxAllowedCount();
			if (maternityAppliedCount >= maternityMaxAllowedCount) {
				response.put("message", "You have Exceed Number of time Allowed for this Leave");
				return ResponseEntity.ok(response);
			}

			// Case 3: Max Number of Days Employee can Select
			int maternityMaxDaysLimit = leaveMasterRepository.getMaternityMaxDaysLimit();
			int maternityTypeCredited = leaveRequestRepository.getMaternityTypeCredited(user.getId());
			int maternityTypeDebited = leaveRequestRepository.getMaternityTypeDebited(user.getId());
			int maxMaternityDaysCanApply = (maternityMaxDaysLimit + maternityTypeCredited) - maternityTypeDebited;
			int totalMaternityApplyDays = calculateDaysIncludingSandwichAndFixedHolidays(fromDate, toDate);
			
			if (totalMaternityApplyDays > maxMaternityDaysCanApply) {
				response.put("message", "You have only " + maxMaternityDaysCanApply
						+ " days available, so you cannot apply for " + totalMaternityApplyDays + " days of leave.");
				return ResponseEntity.ok(response);
			}
			response.put("message", "Total Maternity leave days applied: " + totalMaternityApplyDays);
		}

		// Here we will write all the condition Regarding the Paternity Leave
		if (type.equals("Paternity")) {

			// Case 1:- Employee Should be Female type.
			if (!"Male".equals(user.getGender())) {
				response.put("message", "Only Male Employees are eligible for this type of leave.");
				return ResponseEntity.ok(response);
			}

			// Case 2:- In that Particular employee have not applied any Time for this leave bcz this can user appply only once.
			int paternityAppliedCount = leaveRequestRepository.getPaternityAppliedCount(user.getId());
			int paternityMaxAllowedCount = leaveMasterRepository.getPaternityMaxAllowedCount();
			if (paternityAppliedCount >= paternityMaxAllowedCount) {
				response.put("message", "You have Exceed Number of time Allowed for this Leave");
				return ResponseEntity.ok(response);
			}

			// Case 3: Max Number of Days Employee can Select
			int paternityMaxDaysLimit = leaveMasterRepository.getPaternityMaxDaysLimit();
			int paternityTypeCredited = leaveRequestRepository.getPaternityTypeCredited(user.getId());
			int paternityTypeDebited = leaveRequestRepository.getPaternityTypeDebited(user.getId());
			int maxPaternityDaysCanApply = (paternityMaxDaysLimit + paternityTypeCredited) - paternityTypeDebited;

			// Caculataling Number of Days Including the sanwich cases and Fixed Holidays as well.
			int totalPaternityApplyDays = calculateDaysIncludingSandwichAndFixedHolidays(fromDate, toDate);

			if (totalPaternityApplyDays > maxPaternityDaysCanApply) {
				response.put("messaege", "You have only " + maxPaternityDaysCanApply
						+ " days available, so you cannot apply for " + totalPaternityApplyDays + " days of leave.");
				return ResponseEntity.ok(response);
			}

			response.put("message", "Total Paternity leave days applied: " + totalPaternityApplyDays);
		}

		// Here we will write all the condition Regarding the Componsatory Leave
		if (type.equals("Componsatory")) {

			// Case 1:- In that Particular employee have not applied any Time for this leave
			// bcz this can user appply only once.
			int componsatoryAppliedCount = leaveRequestRepository.getComponsatoryAppliedCount(user.getId());
			int componsatoryMaxAllowedCount = leaveMasterRepository.getComponsatoryMaxAllowedCount();
			if (componsatoryAppliedCount >= componsatoryMaxAllowedCount) {
				response.put("message", "You have exceeded the number of times allowed for this leave.");
				return ResponseEntity.ok(response);
			}

			// Case 2: Max Number of Days Employee can Select
			int componsatoryMaxDaysLimit = leaveMasterRepository.getComponsatoryMaxDaysLimit();
			int componsatoryTypeCredited = leaveRequestRepository.getComponsatoryTypeCredited(user.getId());
			int componsatoryTypeDebited = leaveRequestRepository.getComponsatoryTypeDebited(user.getId());
			int maxComponsatoryDaysCanApply = (componsatoryMaxDaysLimit + componsatoryTypeCredited)
					- componsatoryTypeDebited;

			// Caculataling Number of Days Not Including the sanwich cases and Fixed
			// Holidays.
			int totalComponsatoryApplyDays = calculateEffectiveLeaveDays(fromDate, toDate, fixedHolidays);

			if (totalComponsatoryApplyDays > maxComponsatoryDaysCanApply) {
				response.put("message", "You have only " + maxComponsatoryDaysCanApply
						+ " days available, so you cannot apply for " + totalComponsatoryApplyDays + " days of leave.");
				return ResponseEntity.ok(response);
			}

			response.put("message", "Total Compensatory leave days applied: " + totalComponsatoryApplyDays);
		}

		// Here we will write all the condition Regarding the Flexi Leave
		if (type.equals("Flexi")) {

			// Case 1 :- Check that the Selected date is included as a Flexi in DB or not.
			List<java.sql.Date> sqlDates1 = holidayMasterRepository.findAllFlexiLeavesForCurrentYear();
			List<LocalDate> flexiHolidays = sqlDates1.stream().map(Date::toLocalDate).collect(Collectors.toList());

			// Case 1: Check that the selected date is included as a Flexi in DB or not
			if (!flexiHolidays.contains(fromDate) || !flexiHolidays.contains(toDate)) {
				response.put("message", "The selected dates are not available as flexi holidays.");
				return ResponseEntity.ok(response);
			}

			// Case 2:- In that Particular employee have not applied more than two Time for
			// this leave
			// bcz this can user appply only Twice in a Year.
			int flexiAppliedCount = leaveRequestRepository.getFlexiAppliedCount(user.getId());
			int fLexiMaxAllowedCount = leaveMasterRepository.getFlexiMaxAllowedCount();
			if (flexiAppliedCount >= fLexiMaxAllowedCount) {
				response.put("message", "You have exceeded the number of times allowed for this leave.");
				return ResponseEntity.ok(response);
			}

			// Case 3: Max Number of Days Employee can Select
			int flexiMaxDaysLimit = leaveMasterRepository.getFlexiMaxDaysLimit();
			int flexiTypeCredited = leaveRequestRepository.getFlexiTypeCredited(user.getId());
			int flexiTypeDebited = leaveRequestRepository.getFlexiTypeDebited(user.getId());
			int maxFlexiDaysCanApply = (flexiMaxDaysLimit + flexiTypeCredited) - flexiTypeDebited;

			// Caculataling Number of Days Not Including the sanwich cases and Fixed
			// Holidays.
			int totalFlexiApplyDays = calculateEffectiveLeaveDays(fromDate, toDate, fixedHolidays);

			if (totalFlexiApplyDays > maxFlexiDaysCanApply) {
				response.put("message", "You have only " + maxFlexiDaysCanApply
						+ " days available, so you cannot apply for " + totalFlexiApplyDays + " days of leave.");
				return ResponseEntity.ok(response);
			}

			response.put("message", "Total Flexi leave days applied: " + totalFlexiApplyDays);
		}

		// Here we will write all the condition Regarding the Casual Leave
		if (type.equals("Casual")) {

			// Case 1: Max Number of Days Employee can Select
			int casualTypeCredited = leaveRequestRepository.getCasualTypeCredited(user.getId());
			int casualTypeDebited = leaveRequestRepository.getCasualTypeDebited(user.getId());
			int maxCasualDaysCanApply = (casualTypeCredited - casualTypeDebited);

			// Caculataling Number of Days Not Including the sanwich cases and Fixed
			// Holidays.
			// well.
			int totalCasualApplyDays = calculateEffectiveLeaveDays(fromDate, toDate, fixedHolidays);

			if (totalCasualApplyDays > maxCasualDaysCanApply) {
				response.put("message", "You have only " + maxCasualDaysCanApply
						+ " days available, so you cannot apply for " + totalCasualApplyDays + " days of leave.");
				return ResponseEntity.ok(response);
			}

			response.put("message", "Total Casual leave days applied: " + totalCasualApplyDays);
		}

		// Here we will write all the condition Regarding the Casual Leave
		if (type.equals("Medical")) {

			// Case 1: Max Number of Days Employee can Select
			int medicalTypeCredited = leaveRequestRepository.getMedicalTypeCredited(user.getId());
			int medicalTypeDebited = leaveRequestRepository.getMedicalTypeDebited(user.getId());
			int maxMedicalDaysCanApply = (medicalTypeCredited - medicalTypeDebited);

			// Caculataling Number of Days Not Including the sanwich cases and Fixed
			// Holidays.
			// well.
			int totalMedicalApplyDays = calculateEffectiveLeaveDays(fromDate, toDate, fixedHolidays);

			if (totalMedicalApplyDays > maxMedicalDaysCanApply) {
				response.put("message", "You have only " + maxMedicalDaysCanApply
						+ " days available, so you cannot apply for " + totalMedicalApplyDays + " days of leave.");
				return ResponseEntity.ok(response);
			}

			response.put("message", "Total Medical leave days applied: " + totalMedicalApplyDays);
		}

		return ResponseEntity.ok(response);
	}   
}
