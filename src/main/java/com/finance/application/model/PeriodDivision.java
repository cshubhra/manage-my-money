package com.finance.application.model;

/**
 * Enum representing different period divisions for reports.
 * This corresponds to the original 'period_division' in the Ruby on Rails application.
 */
public enum PeriodDivision {
    /**
     * No division.
     */
    NONE,
    
    /**
     * Day division divides the period by days.
     */
    DAY,
    
    /**
     * Week division divides the period by weeks.
     */
    WEEK,
    
    /**
     * Month division divides the period by months.
     */
    MONTH,
    
    /**
     * Quarter division divides the period by quarters.
     */
    QUARTER,
    
    /**
     * Half-year division divides the period by half-years.
     */
    HALF_YEAR,
    
    /**
     * Year division divides the period by years.
     */
    YEAR
}