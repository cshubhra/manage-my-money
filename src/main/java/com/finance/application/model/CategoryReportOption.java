package com.finance.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * CategoryReportOption entity that represents an association between a category and a report.
 * This maps directly to the CATEGORY_REPORT_OPTIONS table in the database.
 */
@Entity
@Table(name = "category_report_options")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryReportOption {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    private Report report;
}