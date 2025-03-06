package com.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * User entity that corresponds to the users table in the database.
 * This class represents a user of the money transfer application.
 * It maps to the User model in the original Ruby on Rails application.
 * 
 * Transaction amount limit types:
 * - TRANSACTION_COUNT: Limit by number of transactions
 * - WEEK_COUNT: Limit by number of weeks
 * - THIS_MONTH: Show only this month
 * - THIS_AND_LAST_MONTH: Show this and last month
 * 
 * Multi-currency balance calculating algorithms:
 * - SHOW_ALL_CURRENCIES: Show all currencies separately
 * - CALCULATE_WITH_NEWEST_EXCHANGES: Use newest exchange rates
 * - CALCULATE_WITH_EXCHANGES_CLOSEST_TO_TRANSACTION: Use exchange rates closest to transaction date
 * - CALCULATE_WITH_NEWEST_EXCHANGES_BUT: Use newest exchange rates but custom logic
 * - CALCULATE_WITH_EXCHANGES_CLOSEST_TO_TRANSACTION_BUT: Use closest exchange rates but custom logic
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    
    public enum TransactionAmountLimitType {
        TRANSACTION_COUNT,
        WEEK_COUNT,
        THIS_MONTH,
        THIS_AND_LAST_MONTH
    }
    
    public enum MultiCurrencyBalanceCalculatingAlgorithm {
        SHOW_ALL_CURRENCIES,
        CALCULATE_WITH_NEWEST_EXCHANGES,
        CALCULATE_WITH_EXCHANGES_CLOSEST_TO_TRANSACTION,
        CALCULATE_WITH_NEWEST_EXCHANGES_BUT,
        CALCULATE_WITH_EXCHANGES_CLOSEST_TO_TRANSACTION_BUT
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(min = 3, max = 40)
    @Column(unique = true)
    private String login;
    
    @Size(max = 100)
    private String name;
    
    @NotBlank
    @Size(min = 6, max = 100)
    @Email
    @Column(unique = true)
    private String email;
    
    @NotBlank
    @JsonIgnore
    private String password;
    
    @JsonIgnore
    private String salt;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @JsonIgnore
    private String rememberToken;
    private LocalDateTime rememberTokenExpiresAt;
    
    @JsonIgnore
    private String activationCode;
    private LocalDateTime activatedAt;
    
    @Enumerated(EnumType.ORDINAL)
    private TransactionAmountLimitType transactionAmountLimitType = TransactionAmountLimitType.THIS_MONTH;
    
    private Integer transactionAmountLimitValue;
    
    private boolean includeTransactionsFromSubcategories;
    
    @Enumerated(EnumType.ORDINAL)
    private MultiCurrencyBalanceCalculatingAlgorithm multiCurrencyBalanceCalculatingAlgorithm = MultiCurrencyBalanceCalculatingAlgorithm.SHOW_ALL_CURRENCIES;
    
    @ManyToOne
    @JoinColumn(name = "default_currency_id")
    private Currency defaultCurrency;
    
    private boolean invertSaldoForIncome = true;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transfer> transfers = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Currency> currencies = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Exchange> exchanges = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();
    
    /**
     * Check if user is active
     * 
     * @return true if user is active (activation code is null)
     */
    public boolean isActive() {
        return activationCode == null;
    }
    
    /**
     * Check if user was recently activated
     * 
     * @return true if activated time is recent
     */
    public boolean isRecentlyActivated() {
        if (activatedAt == null) {
            return false;
        }
        // If activated within the last hour
        return activatedAt.isAfter(LocalDateTime.now().minusHours(1));
    }
    
    /**
     * Activate the user account
     */
    public void activate() {
        this.activatedAt = LocalDateTime.now();
        this.activationCode = null;
    }
    
    /**
     * Check if transaction amount limit requires a value
     * 
     * @return true if transaction amount limit type requires a value
     */
    public boolean isTransactionAmountLimitWithValue() {
        return transactionAmountLimitType == TransactionAmountLimitType.TRANSACTION_COUNT || 
               transactionAmountLimitType == TransactionAmountLimitType.WEEK_COUNT;
    }
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}