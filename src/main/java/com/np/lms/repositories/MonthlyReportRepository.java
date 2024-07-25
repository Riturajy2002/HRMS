package com.np.lms.repositories;

import com.np.lms.entities.MonthlyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;

@Repository
public interface MonthlyReportRepository extends JpaRepository<MonthlyReport, Long> {
	List<MonthlyReport> findByReportYearMonth(YearMonth yearMonth);
}
