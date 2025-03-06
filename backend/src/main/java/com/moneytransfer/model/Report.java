package com.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Report entity that corresponds to the reports table in the database.
 * This class represents a financial report in the money transfer application.
 * It maps to the Report model and its subclasses in the original Ruby on Rails application.
 * 
 * A report can be of several types:
 * - FLOW_REPORT: Shows cash flow over time
 * - SHARE_REPORT: Shows distribution/shares between categories
 * - VALUE_REPORT: Shows values/amounts for categories
 * This is implemented using the Single Table Inheritance pattern in JPA.
 */
@Entity
@Table(name = "reports")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "report_type", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
public abstract class Report {
    
    public enum PeriodType {
        CUSTOM,
        THIS_MONTH,
        LAST_MONTH,
        THIS_YEAR,
        LAST_YEAR,
        ALL_TIME
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private User user;
    
    @Column(name = "start_day")
    private LocalDate startDay;
    
    @Column(name = "end_day")
    private LocalDate endDay;
    
    private Boolean temporary;
    
    @Column(name = "relative_period")
    private Boolean relativePeriod;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "period_type")
    private PeriodType periodType;
    
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryReportOption> categoryOptions = new ArrayList<>();
    
    /**
     * Get the start date for this report based on period type
     * 
     * @return calculated start date
     */
    @Transient
    public LocalDate getCalculatedStartDate() {
        if (!Boolean.TRUE.equals(relativePeriod) || periodType == null) {
            return startDay;
        }
        
        LocalDate now = LocalDate.now();
        
        switch (periodType) {
            case THIS_MONTH:
                return now.withDayOfMonth(1);
            case LAST_MONTH:
                return now.minusMonths(1).withDayOfMonth(1);
            case THIS_YEAR:
                return now.withDayOfYear(1);
            case LAST_YEAR:
                return now.minusYears(1).withDayOfYear(1);
            case ALL_TIME:
                return LocalDate.of(1900, 1, 1); // Far in the past
            default:
                return startDay;
        }
    }
    
    /**
     * Get the end date for this report based on period type
     * 
     * @return calculated end date
     */
    @Transient
    public LocalDate getCalculatedEndDate() {
        if (!Boolean.TRUE.equals(relativePeriod) || periodType == null) {
            return endDay;
        }
        
        LocalDate now = LocalDate.now();
        
        switch (periodType) {
            case THIS_MONTH:
                return now.withDayOfMonth(now.lengthOfMonth());
            case LAST_MONTH:
                LocalDate lastMonth = now.minusMonths(1);
                return lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());
            case THIS_YEAR:
                return now.withDayOfYear(now.lengthOfYear());
            case LAST_YEAR:
                LocalDate lastYear = now.minusYears(1);
                return lastYear.withDayOfYear(lastYear.lengthOfYear());
            case ALL_TIME:
                return now; // Today
            default:
                return endDay;
        }
    }
    
    /**
     * Add a category option to this report
     * 
     * @param option The category option to add
     * @return this report instance for method chaining
     */
    public Report addCategoryOption(CategoryReportOption option) {
        categoryOptions.add(option);
        option.setReport(this);
        return this;
    }
    
    /**
     * Remove a category option from this report
     * 
     * @param option The category option to remove
     * @return this report instance for method chaining
     */
    public Report removeCategoryOption(CategoryReportOption option) {
        categoryOptions.remove(option);
        option.setReport(null);
        return this;
    }
    
    /**
     * Generate the report data. This should be implemented by subclasses.
     * 
     * @return report data in the appropriate format for the report type
     */
    public abstract Object generateReportData();
}