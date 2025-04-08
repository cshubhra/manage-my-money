package com.example.transfer.service;

import com.example.transfer.dto.ReportDTO;
import com.example.transfer.dto.TransferDTO;
import com.example.transfer.exception.ReportGenerationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private TransferService transferService;

    @Mock
    private PdfGenerationService pdfService;

    @Mock
    private ExcelGenerationService excelService;

    @InjectMocks
    private ReportService reportService;

    private ReportDTO reportRequest;
    private List<TransferDTO> mockTransfers;

    @BeforeEach
    void setUp() {
        reportRequest = new ReportDTO();
        reportRequest.setReportName("Test Report");
        reportRequest.setReportType("TRANSFER_SUMMARY");
        reportRequest.setFormat("PDF");
        reportRequest.setColumns(Arrays.asList("id", "date", "status"));

        mockTransfers = Arrays.asList(
            createMockTransfer(1L),
            createMockTransfer(2L)
        );
    }

    @Test
    void generateReport_Summary_PDF_Success() {
        when(transferService.getTransfersByFilters(any())).thenReturn(mockTransfers);
        when(pdfService.generateTransferSummary(any(), any())).thenReturn(new byte[]{1, 2, 3});

        ReportDTO result = reportService.generateReport(reportRequest);

        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        assertNotNull(result.getGeneratedDate());
        assertArrayEquals(new byte[]{1, 2, 3}, result.getContent());
    }

    @Test
    void generateReport_Detail_Excel_Success() {
        reportRequest.setReportType("TRANSFER_DETAIL");
        reportRequest.setFormat("EXCEL");

        when(transferService.getTransferDetailsWithItems(any())).thenReturn(mockTransfers);
        when(excelService.generateTransferDetail(any(), any())).thenReturn(new byte[]{4, 5, 6});

        ReportDTO result = reportService.generateReport(reportRequest);

        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
        assertNotNull(result.getGeneratedDate());
        assertArrayEquals(new byte[]{4, 5, 6}, result.getContent());
    }

    @Test
    void generateReport_UnsupportedType_ThrowsException() {
        reportRequest.setReportType("UNSUPPORTED");

        assertThrows(ReportGenerationException.class, () -> {
            reportService.generateReport(reportRequest);
        });
    }

    @Test
    void generateReport_ServiceError_ThrowsException() {
        when(transferService.getTransfersByFilters(any()))
            .thenThrow(new RuntimeException("Database error"));

        assertThrows(ReportGenerationException.class, () -> {
            reportService.generateReport(reportRequest);
        });
    }

    private TransferDTO createMockTransfer(Long id) {
        TransferDTO transfer = new TransferDTO();
        transfer.setId(id);
        transfer.setDate(LocalDateTime.now());
        transfer.setStatus("COMPLETED");
        transfer.setAmount(100.0);
        return transfer;
    }
}