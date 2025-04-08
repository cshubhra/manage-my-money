package com.example.transfer.controller;

import com.example.transfer.dto.ReportDTO;
import com.example.transfer.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateReport(@RequestBody ReportDTO request) {
        log.info("Received report generation request for type: {}", request.getReportType());
        
        ReportDTO generatedReport = reportService.generateReport(request);
        
        HttpHeaders headers = new HttpHeaders();
        String filename = generateFileName(generatedReport);
        headers.setContentDispositionFormData("attachment", filename);
        
        if ("PDF".equalsIgnoreCase(generatedReport.getFormat())) {
            headers.setContentType(MediaType.APPLICATION_PDF);
        } else if ("EXCEL".equalsIgnoreCase(generatedReport.getFormat())) {
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        }
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(generatedReport.getContent());
    }

    private String generateFileName(ReportDTO report) {
        String extension = "PDF".equalsIgnoreCase(report.getFormat()) ? "pdf" : "xlsx";
        return String.format("%s_%s.%s", 
                report.getReportType().toLowerCase(),
                report.getGeneratedDate().toString().replace(":", "-"),
                extension);
    }
}