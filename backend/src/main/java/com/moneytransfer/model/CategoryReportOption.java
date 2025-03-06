package com.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.awt.*;

/**
 * CategoryReportOption entity that corresponds to the category_report_options table in the database.
 * This class represents options for a category within a report in the money transfer application.
 * It maps to the CategoryReportOption model in the original Ruby on Rails application.
 * 
 * Each option specifies display settings for a category in a report.
 */
@Entity
@Table(name = "category_report_options")
@Data
@NoArgsConstructor
public class CategoryReportOption {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "report_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Report report;
    
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @Column(name = "include_subcategories")
    private Boolean includeSubcategories;
    
    @Column(name = "color_code")
    private String colorCode;
    
    @Column(name = "display_order")
    private Integer displayOrder;
    
    /**
     * Create a new category report option
     * 
     * @param report Report this option belongs to
     * @param category Category this option refers to
     * @param includeSubcategories Whether to include subcategories
     */
    public CategoryReportOption(Report report, Category category, Boolean includeSubcategories) {
        this.report = report;
        this.category = category;
        this.includeSubcategories = includeSubcategories;
    }
    
    /**
     * Get the color as a Color object
     * 
     * @return Color object based on the color code
     */
    @Transient
    public Color getColor() {
        if (colorCode == null || colorCode.isEmpty()) {
            return null;
        }
        try {
            return Color.decode(colorCode);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Set the color from a Color object
     * 
     * @param color Color to set
     */
    public void setColor(Color color) {
        if (color == null) {
            this.colorCode = null;
            return;
        }
        this.colorCode = String.format("#%02x%02x%02x", 
                color.getRed(), color.getGreen(), color.getBlue());
    }
}