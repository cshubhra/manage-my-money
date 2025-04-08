package com.example.transfer.controller;

import com.example.transfer.dto.ReportDTO;
import com.example.transfer.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Autowired
    private ObjectMapper objectMapper;

    private ReportDTO reportRequest;
    private ReportDTO reportResponse;

    @BeforeEach
    void setUp() {
        reportRequest = new ReportDTO();
        reportRequest.setReportName("Test Report");
        reportRequest.setReportType("TRANSFER_SUMMARY");
        reportRequest.setFormat("PDF");
        reportRequest.setColumns(Arrays.asList("id", "date", "status"));

        reportResponse = new ReportDTO();
        reportResponse.setReportName("Test Report");
        reportResponse.setReportType("TRANSFER_SUMMARY");
        reportResponse.setFormat("PDF");
        reportResponse.setStatus("COMPLETED");
        reportResponse.setGeneratedDate(LocalDateTime.now());
        reportResponse.setContent(new byte[]{1, 2, 3});
    }

    @Test
    void generateReport_PDF_Success() throws Exception {
        when(reportService.generateReport(any(ReportDTO.class))).thenReturn(reportResponse);

        mockMvc.perform(post("/api/reports/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/pdf"))
                .andExpect(header().exists("Content-Disposition"))
                .andExpect(content().bytes(reportResponse.getContent()));
    }

    @Test
    void generateReport_Excel_Success() throws Exception {
        reportRequest.setFormat("EXCEL");
        reportResponse.setFormat("EXCEL");
        when(reportService.generateReport(any(ReportDTO.class))).thenReturn(reportResponse);

        mockMvc.perform(post("/api/reports/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", 
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .andExpect(header().exists("Content-Disposition"))
                .andExpect(content().bytes(reportResponse.getContent()));
    }

    @Test
    void generateReport_InvalidRequest_BadRequest() throws Exception {
        reportRequest.setReportName(null); // Make request invalid

        mockMvc.perform(post("/api/reports/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reportRequest)))
                .andExpect(status().isBadRequest());
    }
}