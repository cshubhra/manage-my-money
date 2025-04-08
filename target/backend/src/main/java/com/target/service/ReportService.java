package com.target.service;

import com.target.model.Report;
import com.target.repository.ReportRepository;
import com.target.dto.ReportDTO;
import com.target.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ReportService {
    private final ReportRepository reportRepository;
    
    @Autowired
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Report generateReport(ReportDTO reportDTO) {
        Report report = new Report();
        report.setReportName(reportDTO.getReportName());
        report.setReportType(reportDTO.getReportType());
        report.setFormat(reportDTO.getFormat());
        report.setGeneratedBy(reportDTO.getGeneratedBy());
        report.setParameters(reportDTO.getParameters());

        byte[] reportContent;
        if ("PDF".equalsIgnoreCase(reportDTO.getFormat())) {
            reportContent = generatePdfReport(reportDTO);
        } else {
            reportContent = generateExcelReport(reportDTO);
        }

        // Save report content to file system or cloud storage
        String filePath = saveReportContent(reportContent, reportDTO);
        report.setFilePath(filePath);

        return reportRepository.save(report);
    }

    private byte[] generatePdfReport(ReportDTO reportDTO) {
        try {
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();
            
            // Add report header
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph header = new Paragraph(reportDTO.getReportName(), headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            document.add(header);
            
            // Add report content based on type
            if ("TRANSFER_SUMMARY".equals(reportDTO.getReportType())) {
                addTransferSummaryContent(document, reportDTO);
            } else if ("TRANSFER_DETAIL".equals(reportDTO.getReportType())) {
                addTransferDetailContent(document, reportDTO);
            }
            
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF report", e);
        }
    }

    private byte[] generateExcelReport(ReportDTO reportDTO) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(reportDTO.getReportName());
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            // Add report content based on type
            if ("TRANSFER_SUMMARY".equals(reportDTO.getReportType())) {
                createTransferSummarySheet(sheet, headerStyle, reportDTO);
            } else if ("TRANSFER_DETAIL".equals(reportDTO.getReportType())) {
                createTransferDetailSheet(sheet, headerStyle, reportDTO);
            }
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating Excel report", e);
        }
    }

    private String saveReportContent(byte[] content, ReportDTO reportDTO) {
        // Implementation for saving report content to file system or cloud storage
        // Returns the file path or URL where the report is saved
        return "reports/" + reportDTO.getReportName() + "_" + 
               LocalDateTime.now().toString().replace(":", "-") + 
               ("PDF".equalsIgnoreCase(reportDTO.getFormat()) ? ".pdf" : ".xlsx");
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReport(Long id) {
        return reportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Report not found with id: " + id));
    }

    public byte[] getReportContent(Long id) {
        Report report = getReport(id);
        // Implementation to retrieve report content from file system or cloud storage
        return new byte[0]; // Placeholder
    }
}