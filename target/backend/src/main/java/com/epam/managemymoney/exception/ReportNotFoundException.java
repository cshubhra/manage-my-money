package com.epam.managemymoney.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReportNotFoundException extends RuntimeException {

    public ReportNotFoundException(String message) {
        super(message);
    }

    public ReportNotFoundException(Long reportId) {
        super(String.format("Report not found with id: %d", reportId));
    }

    public ReportNotFoundException(String reportType, Long userId) {
        super(String.format("Report of type '%s' not found for user id: %d", reportType, userId));
    }

    public ReportNotFoundException(String reportType, Long userId, String dateRange) {
        super(String.format("Report of type '%s' not found for user id: %d in date range: %s",
                reportType, userId, dateRange));
    }
}
