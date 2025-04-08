package com.example.report.controller;

import com.example.report.model.Report;
import com.example.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Report>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Report> getReport(@PathVariable Long id) {
        Report report = reportService.getReport(id);
        return ResponseEntity.ok(report);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Report> createReport(@Valid @RequestBody Report report) {
        Report createdReport = reportService.createReport(report);
        return ResponseEntity.ok(createdReport);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Report> updateReport(
            @PathVariable Long id,
            @Valid @RequestBody Report reportDetails) {
        Report updatedReport = reportService.updateReport(id, reportDetails);
        return ResponseEntity.ok(updatedReport);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/download")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Resource> downloadReport(@PathVariable Long id) {
        Report report = reportService.getReport(id);
        Resource resource = reportService.getReportResource(report.getFilePath());
        
        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=\"" + report.getName() + "\"")
            .body(resource);
    }
}