package com.example.transfer.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReportDTO {
    private Long id;
    private String reportName;
    private String reportType;
    private LocalDateTime generatedDate;
    private String status;
    private String format;
    private List<String> columns;
    private List<String> filters;
    private byte[] content;
}