package com.np.hrms.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.np.hrms.entities.MonthlyReport;
import com.np.hrms.repositories.MonthlyReportRepository;

@Service
public class ExcelReportService {

	@Autowired
	private MonthlyReportRepository monthlyReportRepository;

	public byte[] generateMonthlyReport(YearMonth yearMonth) throws IOException {
		List<MonthlyReport> reports = monthlyReportRepository.findByReportYearMonth(yearMonth);

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("Monthly Report");

			// Title Row--->First Row
			Row titleRow = sheet.createRow(0);
			Cell titleCell = titleRow.createCell(0);
			titleCell.setCellValue("Monthly Status Report (Basic Report)");
			CellStyle titleStyle = workbook.createCellStyle();
			Font titleFont = workbook.createFont();
			titleFont.setBold(true);
			titleFont.setFontHeightInPoints((short) 16);
			titleStyle.setFont(titleFont);
			titleStyle.setAlignment(HorizontalAlignment.CENTER);
			titleCell.setCellStyle(titleStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

			// Create row for the logo and date information---->Second Row
			Row row1 = sheet.createRow(1);
			row1.setHeightInPoints(50);
			InputStream logoInputStream = getClass()
					.getResourceAsStream("/static/img/Novel-Patterns-Pvt-Ltd3-removebg-preview (1).png");
			if (logoInputStream != null) {
				byte[] bytes = IOUtils.toByteArray(logoInputStream);
				int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
				logoInputStream.close();
				CreationHelper helper = workbook.getCreationHelper();
				Drawing<?> drawing = sheet.createDrawingPatriarch();
				ClientAnchor anchor = helper.createClientAnchor();
				anchor.setCol1(0);
				anchor.setRow1(1);
				anchor.setCol2(3);
				anchor.setRow2(2);
				Picture pict = drawing.createPicture(anchor, pictureIdx);
				pict.resize(1);
			}

			// Merge cells for the logo
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

			// Create cell for month report date
			Cell monthCell = row1.createCell(3);
			monthCell.setCellValue("Month Report for: " + yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
			CellStyle monthStyle = workbook.createCellStyle();
			monthStyle.setAlignment(HorizontalAlignment.CENTER);
			monthCell.setCellStyle(monthStyle);
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 4));

			// Formatting of Printing Date in the Format of dd-MM-yyyy HH:mm:ss
			LocalDateTime myDateObj = LocalDateTime.now();
			DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
			String formattedDate = myDateObj.format(myFormatObj);

			// Create cell for printed date
			Cell dateCell = row1.createCell(5);
			dateCell.setCellValue("Printed Date: " + formattedDate);
			CellStyle dateStyle = workbook.createCellStyle();
			dateStyle.setAlignment(HorizontalAlignment.RIGHT);
			dateCell.setCellStyle(dateStyle);
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 5, 7));

			// Header Row
			Row headerRow = sheet.createRow(2);
			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerStyle.setFont(headerFont);
			String[] headers = { " Emp ID ", "User Name", "designation", "reporting_manager", "Available Leaves",
					"Approved Leaves", "LWP Count", "Year Month" };
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(headerStyle);
			}

			// Adjust column width based on content length
			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}

			// Data Rows
			int rowIndex = 3;
			for (MonthlyReport report : reports) {
				Row row = sheet.createRow(rowIndex++);
				row.createCell(0).setCellValue(report.getUserId());
				row.createCell(1).setCellValue(report.getUserName());
				row.createCell(2).setCellValue(report.getDesignation());
				row.createCell(3).setCellValue(report.getReporting_manager());
				row.createCell(4).setCellValue(report.getAvailableLeaves());
				row.createCell(5).setCellValue(report.getApprovedLeaves());
				row.createCell(6).setCellValue(report.getLwpCount());
				row.createCell(7).setCellValue(report.getReportYearMonth().toString());

				// Apply borders to data rows
				for (int i = 0; i < headers.length; i++) {
					CellStyle style = workbook.createCellStyle();
					style.setBorderTop(BorderStyle.THIN);
					style.setBorderBottom(BorderStyle.THIN);
					style.setBorderLeft(BorderStyle.THIN);
					style.setBorderRight(BorderStyle.THIN);
					row.getCell(i).setCellStyle(style);
				}
			}

			// Auto-size "User Name" column width based on content length after data
			// population
			sheet.autoSizeColumn(1);

			workbook.write(out);
			return out.toByteArray();
		}
	}
}
