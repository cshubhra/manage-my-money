package com.finance.application.model;

/**
 * Enum representing different algorithms for calculating balance across multiple currencies.
 * This corresponds to the original 'multi_currency_balance_calculating_algorithm' in the Ruby on Rails application.
 */
public enum MultiCurrencyBalanceCalculatingAlgorithm {
    /**
     * Show balances in all currencies without conversion.
     */
    SHOW_ALL_CURRENCIES,
    
    /**
     * Calculate using the newest exchange rates available.
     */
    CALCULATE_WITH_NEWEST_EXCHANGES,
    
    /**
     * Calculate using exchange rates closest to the transaction date.
     */
    CALCULATE_WITH_EXCHANGES_CLOSEST_TO_TRANSACTION,
    
    /**
     * Calculate using the newest exchange rates but with special handling.
     */
    CALCULATE_WITH_NEWEST_EXCHANGES_BUT,
    
    /**
     * Calculate using exchange rates closest to transaction date but with special handling.
     */
    CALCULATE_WITH_EXCHANGES_CLOSEST_TO_TRANSACTION_BUT
}