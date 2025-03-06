package com.finance.application.model;

/**
 * Enum representing different types of goals.
 * This corresponds to the original 'goal_type' in the Ruby on Rails application.
 */
public enum GoalType {
    /**
     * Percentage goal type represents a goal measured as a percentage.
     */
    PERCENT,
    
    /**
     * Value goal type represents a goal measured as a specific monetary value.
     */
    VALUE
}