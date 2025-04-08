package com.example.transfer.service;

import com.example.transfer.dto.ReportDTO;
import com.example.transfer.exception.ReportGenerationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {
    
    private final TransferService transferService;
    private final PdfGenerationService pdfService;
    private final ExcelGenerationService excelService;

    @Transactional(readOnly = true)
    public ReportDTO generateReport(ReportDTO request) {
        log.info("Generating report: {}", request.getReportName());
        
        try {
            switch (request.getReportType().toUpperCase()) {
                case "TRANSFER_SUMMARY":
                    return generateTransferSummaryReport(request);
                case "TRANSFER_DETAIL":
                    return generateTransferDetailReport(request);
                default:
                    throw new ReportGenerationException("Unsupported report type: " + request.getReportType());
            }
        } catch (Exception e) {
            log.error("Error generating report", e);
            throw new ReportGenerationException("Failed to generate report: " + e.getMessage());
        }
    }

    private ReportDTO generateTransferSummaryReport(ReportDTO request) {
        List<TransferDTO> transfers = transferService.getTransfersByFilters(request.getFilters());
        
        if ("PDF".equalsIgnoreCase(request.getFormat())) {
            request.setContent(pdfService.generateTransferSummary(transfers, request.getColumns()));
        } else if ("EXCEL".equalsIgnoreCase(request.getFormat())) {
            request.setContent(excelService.generateTransferSummary(transfers, request.getColumns()));
        }
        
        request.setStatus("COMPLETED");
        request.setGeneratedDate(LocalDateTime.now());
        return request;
    }

    private ReportDTO generateTransferDetailReport(ReportDTO request) {
        List<TransferDTO> transfers = transferService.getTransferDetailsWithItems(request.getFilters());
        
        if ("PDF".equalsIgnoreCase(request.getFormat())) {
            request.setContent(pdfService.generateTransferDetail(transfers, request.getColumns()));
        } else if ("EXCEL".equalsIgnoreCase(request.getFormat())) {
            request.setContent(excelService.generateTransferDetail(transfers, request.getColumns()));
        }
        
        request.setStatus("COMPLETED");
        request.setGeneratedDate(LocalDateTime.now());
        return request;
    }
}