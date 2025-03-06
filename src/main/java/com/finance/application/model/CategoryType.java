package com.finance.application.model;

/**
 * Enum representing different types of financial categories.
 * This corresponds to the original 'category_type' in the Ruby on Rails application.
 */
public enum CategoryType {
    /**
     * Asset category type represents assets like bank accounts, cash, etc.
     */
    ASSET,
    
    /**
     * Income category type represents sources of income like salary, dividends, etc.
     */
    INCOME,
    
    /**
     * Expense category type represents expenses like food, rent, etc.
     */
    EXPENSE,
    
    /**
     * Loan category type represents loans, debts, etc.
     */
    LOAN,
    
    /**
     * Balance category type represents opening balances and equity.
     */
    BALANCE
}