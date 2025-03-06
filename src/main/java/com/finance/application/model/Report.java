package com.finance.application.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Report entity that represents a financial report in the system.
 * This maps directly to the REPORTS table in the database.
 */
@Entity
@Table(name = "reports")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Report {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String name;
    
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "period_type_int", nullable = false)
    private PeriodType periodType;
    
    @Column(name = "period_start")
    private LocalDate periodStart;
    
    @Column(name = "period_end")
    private LocalDate periodEnd;
    
    @NotNull
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "report_view_type_int", nullable = false)
    private ReportViewType reportViewType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer depth = 0;
    
    @Column(name = "max_categories_values_count", nullable = false, columnDefinition = "integer default 0")
    private Integer maxCategoriesValuesCount = 0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "period_division_int", nullable = false, columnDefinition = "integer default 5")
    private PeriodDivision periodDivision = PeriodDivision.MONTH;
    
    @NotNull
    @Column(nullable = false)
    private Boolean temporary = false;
    
    @NotNull
    @Column(name = "relative_period", nullable = false)
    private Boolean relativePeriod = false;
    
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CategoryReportOption> categoryReportOptions = new HashSet<>();
    
    /**
     * Adds a category report option to this report.
     * 
     * @param option the category report option to add
     */
    public void addCategoryReportOption(CategoryReportOption option) {
        categoryReportOptions.add(option);
        option.setReport(this);
    }
    
    /**
     * Removes a category report option from this report.
     * 
     * @param option the category report option to remove
     */
    public void removeCategoryReportOption(CategoryReportOption option) {
        categoryReportOptions.remove(option);
        option.setReport(null);
    }
    
    /**
     * Checks if this report is a flow report.
     * 
     * @return true if this is a flow report, false otherwise
     */
    public boolean isFlowReport() {
        return false;
    }
    
    /**
     * Checks if this report is a share report.
     * 
     * @return true if this is a share report, false otherwise
     */
    public boolean isShareReport() {
        return false;
    }
    
    /**
     * Checks if this report is a value report.
     * 
     * @return true if this is a value report, false otherwise
     */
    public boolean isValueReport() {
        return false;
    }
}