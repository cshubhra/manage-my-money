package com.epam.managemymoney.service;

import com.epam.managemymoney.model.Report;
import com.epam.managemymoney.model.FileType;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ReportGeneratorService {

    private final PdfGeneratorService pdfGeneratorService;
    private final ExcelGeneratorService excelGeneratorService;
    private final CsvGeneratorService csvGeneratorService;

    public ReportGeneratorService(
        PdfGeneratorService pdfGeneratorService,
        ExcelGeneratorService excelGeneratorService,
        CsvGeneratorService csvGeneratorService
    ) {
        this.pdfGeneratorService = pdfGeneratorService;
        this.excelGeneratorService = excelGeneratorService;
        this.csvGeneratorService = csvGeneratorService;
    }

    public String generateReport(Report report) {
        String fileName = generateFileName(report);
        String filePath = "reports/" + fileName;

        switch (report.getFileType()) {
            case PDF:
                //return pdfGeneratorService.generateReport(report, filePath);
               return "PDF";
            case EXCEL:
                //return excelGeneratorService.generateReport(report, filePath);
                return "EXCEL";
            case CSV:
                //return csvGeneratorService.generateReport(report, filePath);
               return "CSV";
            default:
                throw new IllegalArgumentException("Unsupported file type: " + report.getFileType());
        }
    }

    private String generateFileName(Report report) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return String.format("%s_%s.%s",
            report.getName().replaceAll("\\s+", "_").toLowerCase(),
            now.format(formatter),
            getFileExtension(report.getFileType())
        );
    }

    private String getFileExtension(FileType fileType) {
        switch (fileType) {
            case PDF:
                return "pdf";
            case EXCEL:
                return "xlsx";
            case CSV:
                return "csv";
            default:
                throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }

    public void deleteReportFile(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (Exception e) {
            // Log error but don't throw - file might have been already deleted
        }
    }
}