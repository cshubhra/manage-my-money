package com.epam.managemymoney.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequestDTO {

    @NotNull(message = "Report type cannot be null")
    private String reportType; // FLOW, CATEGORY, SHARE, VALUE, MULTIPLE_CATEGORY

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Start date cannot be null")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "End date cannot be null")
    private LocalDate endDate;

    private List<Long> categoryIds;

    private String currencyCode;

    private String groupBy; // DAILY, WEEKLY, MONTHLY, YEARLY

    private Boolean includeSubcategories;

    @Builder.Default
    private Boolean compareWithPreviousPeriod = false;

    private String chartType; // LINE, BAR, PIE

    @Builder.Default
    private Boolean includeTransfers = true;

    private String sortBy; // AMOUNT, DATE, CATEGORY

    private String sortDirection; // ASC, DESC
}