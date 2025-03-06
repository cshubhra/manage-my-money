package com.finance.application.model;

/**
 * Enum representing different goal completion conditions.
 * This corresponds to the original 'goal_completion_condition' in the Ruby on Rails application.
 */
public enum GoalCompletionCondition {
    /**
     * The goal is achieved if the actual value is at least the goal value.
     */
    AT_LEAST,
    
    /**
     * The goal is achieved if the actual value is at most the goal value.
     */
    AT_MOST
}