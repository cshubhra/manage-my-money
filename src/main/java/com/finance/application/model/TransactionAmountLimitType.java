package com.finance.application.model;

/**
 * Enum representing different types of transaction amount limits.
 * This corresponds to the original 'transaction_amount_limit_type' in the Ruby on Rails application.
 */
public enum TransactionAmountLimitType {
    /**
     * Limit based on the number of transactions.
     */
    TRANSACTION_COUNT,
    
    /**
     * Limit based on the number of weeks.
     */
    WEEK_COUNT,
    
    /**
     * Limit to transactions in the current month.
     */
    THIS_MONTH,
    
    /**
     * Limit to transactions in the current and previous month.
     */
    THIS_AND_LAST_MONTH
}