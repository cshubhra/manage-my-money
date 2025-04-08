package com.epam.managemymoney.controller;

import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import javax.validation.Valid;
import com.epam.managemymoney.service.ReportService;
import com.epam.managemymoney.dto.ReportDTO;
import com.epam.managemymoney.dto.ReportRequestDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/reports")
@Slf4j
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Resource> generateReport(@Valid @RequestBody ReportRequestDTO request) {
        Resource report = reportService.generateReport(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + request.getReportType() + ".pdf\"")
                .body(report);
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getAvailableReportTypes() {
        return ResponseEntity.ok(reportService.getAvailableReportTypes());
    }

    @GetMapping("/history")
    public ResponseEntity<List<ReportDTO>> getReportHistory() {
        return ResponseEntity.ok(reportService.getReportHistory());
    }
}