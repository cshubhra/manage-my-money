package com.target.controller;

import com.target.model.Report;
import com.target.service.ReportService;
import com.target.dto.ReportDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Report> generateReport(@Valid @RequestBody ReportDTO reportDTO) {
        Report report = reportService.generateReport(reportDTO);
        return ResponseEntity.ok(report);
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Report> getReport(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReport(id));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) {
        Report report = reportService.getReport(id);
        byte[] content = reportService.getReportContent(id);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType("PDF".equalsIgnoreCase(report.getFormat()) ? 
            MediaType.APPLICATION_PDF : 
            MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", report.getReportName() + 
            ("PDF".equalsIgnoreCase(report.getFormat()) ? ".pdf" : ".xlsx"));
        
        return ResponseEntity.ok()
            .headers(headers)
            .body(content);
    }
}