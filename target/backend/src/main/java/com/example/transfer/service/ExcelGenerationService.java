package com.example.transfer.service;

import com.example.transfer.dto.TransferDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@Slf4j
public class ExcelGenerationService {

    public byte[] generateTransferSummary(List<TransferDTO> transfers, List<String> columns) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Transfer Summary");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            for (int i = 0; i < columns.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns.get(i));
                cell.setCellStyle(headerStyle);
            }

            // Create data rows
            int rowNum = 1;
            for (TransferDTO transfer : transfers) {
                Row row = sheet.createRow(rowNum++);
                populateTransferRow(row, transfer, columns);
            }

            // Autosize columns
            for (int i = 0; i < columns.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            log.error("Error generating Excel file", e);
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }

    public byte[] generateTransferDetail(List<TransferDTO> transfers, List<String> columns) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Transfer Details");
            
            int rowNum = 0;
            CellStyle headerStyle = createHeaderStyle(workbook);
            
            for (TransferDTO transfer : transfers) {
                // Add transfer header
                Row transferRow = sheet.createRow(rowNum++);
                Cell headerCell = transferRow.createCell(0);
                headerCell.setCellValue("Transfer ID: " + transfer.getId());
                headerCell.setCellStyle(headerStyle);

                // Add column headers
                Row headerRow = sheet.createRow(rowNum++);
                for (int i = 0; i < columns.size(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns.get(i));
                    cell.setCellStyle(headerStyle);
                }

                // Add transfer details
                Row detailRow = sheet.createRow(rowNum++);
                populateTransferRow(detailRow, transfer, columns);

                // Add empty row for spacing
                rowNum++;
            }

            // Autosize columns
            for (int i = 0; i < columns.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
            
        } catch (Exception e) {
            log.error("Error generating Excel file", e);
            throw new RuntimeException("Failed to generate Excel report", e);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        return headerStyle;
    }

    private void populateTransferRow(Row row, TransferDTO transfer, List<String> columns) {
        for (int i = 0; i < columns.size(); i++) {
            Cell cell = row.createCell(i);
            String columnName = columns.get(i);
            
            switch (columnName.toLowerCase()) {
                case "id":
                    cell.setCellValue(transfer.getId());
                    break;
                case "date":
                    cell.setCellValue(transfer.getDate().toString());
                    break;
                case "status":
                    cell.setCellValue(transfer.getStatus());
                    break;
                case "amount":
                    cell.setCellValue(transfer.getAmount().doubleValue());
                    break;
                default:
                    cell.setCellValue("");
            }
        }
    }
}