package com.epam.managemymoney.service;

import com.epam.managemymoney.model.Report;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExcelGeneratorService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public byte[] generateReportExcel(Report report, Map<String, Object> reportData) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Financial Report");

            // Create cell styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);

            int rowNum = 0;

            // Add title
            rowNum = addTitle(sheet, titleStyle, rowNum, report);

            // Add report metadata
            rowNum = addReportMetadata(sheet, headerStyle, dateStyle, rowNum, report);

            // Add summary section
            if (reportData.containsKey("summary")) {
                rowNum = addSummarySection(sheet, headerStyle, currencyStyle, rowNum,
                        (Map<String, Object>) reportData.get("summary"));
            }

            // Add details section
            if (reportData.containsKey("details")) {
                rowNum = addDetailsSection(sheet, headerStyle, currencyStyle, rowNum,
                        (List<Map<String, Object>>) reportData.get("details"));
            }

            // Auto-size columns
            autoSizeColumns(sheet);

            // Write to byte array
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                workbook.write(baos);
                return baos.toByteArray();
            }

        } catch (Exception e) {
            log.error("Error generating Excel report", e);
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("yyyy-mm-dd"));
        return style;
    }

    private CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        return style;
    }

    private int addTitle(Sheet sheet, CellStyle titleStyle, int rowNum, Report report) {
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Financial Report - " + report.getReportType());
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
        return rowNum + 1;
    }

    private int addReportMetadata(Sheet sheet, CellStyle headerStyle, CellStyle dateStyle,
                                  int rowNum, Report report) {
        // Report Type
        Row row = sheet.createRow(rowNum++);
        Cell headerCell = row.createCell(0);
        headerCell.setCellValue("Report Type:");
        headerCell.setCellStyle(headerStyle);
        row.createCell(1).setCellValue(report.getReportType());

        // Period
        row = sheet.createRow(rowNum++);
        headerCell = row.createCell(0);
        headerCell.setCellValue("Period:");
        headerCell.setCellStyle(headerStyle);
        Cell dateCell = row.createCell(1);
        dateCell.setCellValue(report.getStartDate().format(DATE_FORMATTER) +
                " to " +
                report.getEndDate().format(DATE_FORMATTER));
        dateCell.setCellStyle(dateStyle);

        // Currency
        row = sheet.createRow(rowNum++);
        headerCell = row.createCell(0);
        headerCell.setCellValue("Currency:");
        headerCell.setCellStyle(headerStyle);
        row.createCell(1).setCellValue(report.getCurrencyCode());

        return rowNum + 1;
    }

    private int addSummarySection(Sheet sheet, CellStyle headerStyle, CellStyle currencyStyle,
                                  int rowNum, Map<String, Object> summary) {
        Row headerRow = sheet.createRow(rowNum++);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("Summary");
        headerCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 1));

        for (Map.Entry<String, Object> entry : summary.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());

            Cell valueCell = row.createCell(1);
            Object value = entry.getValue();
            if (value instanceof Number) {
                valueCell.setCellValue(((Number) value).doubleValue());
                valueCell.setCellStyle(currencyStyle);
            } else {
                valueCell.setCellValue(value.toString());
            }
        }

        return rowNum + 1;
    }

    private int addDetailsSection(Sheet sheet, CellStyle headerStyle, CellStyle currencyStyle,
                                  int rowNum, List<Map<String, Object>> details) {
        if (details.isEmpty()) {
            return rowNum;
        }

        // Create header row
        Row headerRow = sheet.createRow(rowNum++);
        int cellNum = 0;
        for (String header : details.get(0).keySet()) {
            Cell cell = headerRow.createCell(cellNum++);
            cell.setCellValue(header);
            cell.setCellStyle(headerStyle);
        }

        // Create data rows
        for (Map<String, Object> row : details) {
            Row dataRow = sheet.createRow(rowNum++);
            cellNum = 0;
            for (Object value : row.values()) {
                Cell cell = dataRow.createCell(cellNum++);
                if (value instanceof Number) {
                    cell.setCellValue(((Number) value).doubleValue());
                    cell.setCellStyle(currencyStyle);
                } else if (value instanceof java.time.LocalDate) {
                    cell.setCellValue(((java.time.LocalDate) value).format(DATE_FORMATTER));
                } else {
                    cell.setCellValue(value.toString());
                }
            }
        }

        return rowNum + 1;
    }

    private void autoSizeColumns(Sheet sheet) {
        int columnCount = sheet.getRow(0).getPhysicalNumberOfCells();
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}