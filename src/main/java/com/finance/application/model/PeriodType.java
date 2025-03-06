package com.finance.application.model;

/**
 * Enum representing different period types for goals and reports.
 */
public enum PeriodType {
    /**
     * Selected period type represents a custom date range.
     */
    SELECTED,
    
    /**
     * Day period type represents a single day.
     */
    DAY,
    
    /**
     * Week period type represents a calendar week.
     */
    WEEK,
    
    /**
     * Month period type represents a calendar month.
     */
    MONTH,
    
    /**
     * Quarter period type represents a calendar quarter (3 months).
     */
    QUARTER,
    
    /**
     * Half-year period type represents a half calendar year (6 months).
     */
    HALF_YEAR,
    
    /**
     * Year period type represents a calendar year.
     */
    YEAR
}