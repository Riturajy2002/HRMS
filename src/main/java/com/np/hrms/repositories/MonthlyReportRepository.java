package com.np.hrms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.np.hrms.entities.MonthlyReport;
import java.time.YearMonth;
import java.util.List;

@Repository
public interface MonthlyReportRepository extends JpaRepository<MonthlyReport, Long> {
	
	
	List<MonthlyReport> findByReportYearMonth(YearMonth yearMonth);
}
