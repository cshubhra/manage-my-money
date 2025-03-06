package com.finance.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * User entity that represents a user in the system.
 * This maps directly to the USER table in the database.
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(min = 3, max = 40)
    @Column(nullable = false, unique = true)
    private String login;
    
    @Size(max = 100)
    private String name;
    
    @Email
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, unique = true)
    private String email;
    
    @NotBlank
    @Column(nullable = false)
    private String password;
    
    @Column(name = "activation_code")
    private String activationCode;
    
    @Column(name = "activated_at")
    private LocalDateTime activatedAt;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "transaction_amount_limit_type", nullable = false)
    private TransactionAmountLimitType transactionAmountLimitType = TransactionAmountLimitType.THIS_MONTH;
    
    @Column(name = "transaction_amount_limit_value")
    private Integer transactionAmountLimitValue;
    
    @Column(name = "include_transactions_from_subcategories", nullable = false)
    private boolean includeTransactionsFromSubcategories;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "multi_currency_balance_calculating_algorithm", nullable = false)
    private MultiCurrencyBalanceCalculatingAlgorithm multiCurrencyBalanceCalculatingAlgorithm = 
            MultiCurrencyBalanceCalculatingAlgorithm.SHOW_ALL_CURRENCIES;
    
    @Column(name = "invert_saldo_for_income", nullable = false)
    private boolean invertSaldoForIncome = true;
    
    @ManyToOne
    @JoinColumn(name = "default_currency_id", nullable = false)
    private Currency defaultCurrency;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Category> categories = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transfer> transfers = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Currency> currencies = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Exchange> exchanges = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Goal> goals = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Report> reports = new HashSet<>();
    
    /**
     * Checks if the user account is active.
     * 
     * @return true if the user is activated, false otherwise
     */
    public boolean isActive() {
        return activationCode == null && activatedAt != null;
    }
    
    /**
     * Activates the user account.
     */
    public void activate() {
        this.activatedAt = LocalDateTime.now();
        this.activationCode = null;
    }
}