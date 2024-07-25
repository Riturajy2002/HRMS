package com.np.lms.services;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelService {
	public Map<String, List<LocalDate>> processExcelFile(MultipartFile file) throws IOException {
		Workbook workbook;

		try (InputStream fis = file.getInputStream()) {
			String fileName = file.getOriginalFilename();
			if (fileName != null && fileName.endsWith(".xls")) {
				workbook = new HSSFWorkbook(fis);
			} else if (fileName != null && fileName.endsWith(".xlsx")) {
				workbook = new XSSFWorkbook(fis);
			} else {
				throw new IOException("Unsupported file format");
			}

			// Map to convert month abbreviations to Month enums
			Map<String, Month> monthMap = new HashMap<>();
			monthMap.put("JAN", Month.JANUARY);
			monthMap.put("FEB", Month.FEBRUARY);
			monthMap.put("MAR", Month.MARCH);
			monthMap.put("APR", Month.APRIL);
			monthMap.put("MAY", Month.MAY);
			monthMap.put("JUN", Month.JUNE);
			monthMap.put("JUL", Month.JULY);
			monthMap.put("AUG", Month.AUGUST);
			monthMap.put("SEP", Month.SEPTEMBER);
			monthMap.put("OCT", Month.OCTOBER);
			monthMap.put("NOV", Month.NOVEMBER);
			monthMap.put("DEC", Month.DECEMBER);

			Map<String, List<LocalDate>> absentDates = new HashMap<>();

			// Process all sheets
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);
				if (sheet == null) {
					continue;
				}

				// Find the Month and Year from the cell
				Row dateRow = sheet.getRow(2);
				if (dateRow == null) {
					continue;
				}

				Cell dateCell = dateRow.getCell(1);
				if (dateCell == null || dateCell.getCellType() != CellType.STRING) {
					continue;
				}

				String dateStr = dateCell.getStringCellValue().trim();

				// Extract the month and year from the string
				String[] dateRangeParts = dateStr.split("\\s+");
				if (dateRangeParts.length < 3) {
					continue;
				}

				Month month = monthMap.get(dateRangeParts[0].toUpperCase());
				int year;
				try {
					year = Integer.parseInt(dateRangeParts[2]);
				} catch (NumberFormatException e) {
					continue;
				}

				// Find the header row dynamically
				int headerRowIndex = -1;
				for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
					Row row = sheet.getRow(rowIndex);
					if (row != null) {
						Cell cell0 = row.getCell(0);
						Cell cell1 = row.getCell(1);
						Cell cell2 = row.getCell(2);
						if (cell0 != null && cell1 != null && cell2 != null && cell0.getCellType() == CellType.STRING
								&& "Sl".equalsIgnoreCase(cell0.getStringCellValue().trim())) {
							headerRowIndex = rowIndex;
							break;
						}
					}
				}
				if (headerRowIndex == -1) {
					continue;
				}

				// Find the column indices for relevant headers
				Row headerRow = sheet.getRow(headerRowIndex);
				int nameColumnIndex = -1;
				int empCodeColumnIndex = -1;
				int dateStartColumnIndex = -1;

				for (int col = 0; col < headerRow.getLastCellNum(); col++) {
					Cell cell = headerRow.getCell(col);
					if (cell != null && cell.getCellType() == CellType.STRING) {
						String cellValue = cell.getStringCellValue().trim().toUpperCase();
						if (cellValue.equals("NAME")) {
							nameColumnIndex = col;
						} else if (cellValue.equals("EMP. CODE")) {
							empCodeColumnIndex = col;
						} else if (cellValue.matches("\\d+\\s.*")) {
							dateStartColumnIndex = col;
							break;
						}
					}
				}

				if (nameColumnIndex == -1 || empCodeColumnIndex == -1 || dateStartColumnIndex == -1) {
					continue;
				}

				// Extract dates from the header row
				List<LocalDate> dates = new ArrayList<>();
				for (int col = dateStartColumnIndex; col < headerRow.getLastCellNum(); col++) {
					Cell cell = headerRow.getCell(col);
					if (cell != null && cell.getCellType() == CellType.STRING) {
						String cellValue = cell.getStringCellValue().trim();
						String[] parts = cellValue.split("\\s+");
						if (parts.length > 0) {
							String firstPart = parts[0];
							if (firstPart.matches("\\d+")) {
								try {
									int day = Integer.parseInt(firstPart);
									LocalDate date = LocalDate.of(year, month, day);
									dates.add(date);
								} catch (NumberFormatException e) {
								}
							}
						}
					}
				}

				// Process each row for employee data starting from row index after the header
				// row
				for (int rowIdx = headerRowIndex + 1; rowIdx <= sheet.getLastRowNum(); rowIdx++) {
					Row row = sheet.getRow(rowIdx);
					if (row == null)
						continue;

					Cell empCodeCell = row.getCell(empCodeColumnIndex);
					Cell nameCell = row.getCell(nameColumnIndex);
					if (empCodeCell == null || empCodeCell.getCellType() != CellType.STRING)
						continue;

					String empCode = empCodeCell.getStringCellValue(); // Keep as string

					String employeeName = nameCell.getStringCellValue();
					List<LocalDate> employeeAbsentDates = new ArrayList<>();

					int dateIndex = 0;

					for (int col = dateStartColumnIndex; col < row.getLastCellNum(); col++) {
						Cell cell = row.getCell(col);
						if (cell != null && cell.getCellType() == CellType.STRING) {
							String cellValue = cell.getStringCellValue().trim();
							if (!cellValue.isEmpty()) {
								if ("A".equals(cellValue)) {
									if (dateIndex < dates.size()) {
										employeeAbsentDates.add(dates.get(dateIndex));
									}
								}
								dateIndex++;
							}
						}
					}

					absentDates.put(empCode + " -- " + employeeName, employeeAbsentDates);
				}
			}

			workbook.close();
			return absentDates;
		}
	}
}
