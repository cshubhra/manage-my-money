package com.example.report.service;

import com.example.report.model.Report;
import com.example.report.model.ReportType;
import com.example.report.model.FileType;
import com.example.report.repository.ReportRepository;
import com.example.report.exception.ReportNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReportService {
    
    private final ReportRepository reportRepository;
    private final ReportGeneratorService reportGeneratorService;

    @Autowired
    public ReportService(ReportRepository reportRepository, ReportGeneratorService reportGeneratorService) {
        this.reportRepository = reportRepository;
        this.reportGeneratorService = reportGeneratorService;
    }

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReport(Long id) {
        return reportRepository.findById(id)
            .orElseThrow(() -> new ReportNotFoundException("Report not found with id: " + id));
    }

    public Report createReport(Report report) {
        // Generate the report file
        String filePath = reportGeneratorService.generateReport(report);
        report.setFilePath(filePath);
        return reportRepository.save(report);
    }

    public Report updateReport(Long id, Report reportDetails) {
        Report report = getReport(id);
        report.setName(reportDetails.getName());
        report.setType(reportDetails.getType());
        report.setStartDate(reportDetails.getStartDate());
        report.setEndDate(reportDetails.getEndDate());
        report.setParameters(reportDetails.getParameters());
        report.setFileType(reportDetails.getFileType());
        
        // Regenerate the report if necessary
        String filePath = reportGeneratorService.generateReport(report);
        report.setFilePath(filePath);
        
        return reportRepository.save(report);
    }

    public void deleteReport(Long id) {
        Report report = getReport(id);
        reportRepository.delete(report);
        reportGeneratorService.deleteReportFile(report.getFilePath());
    }
}