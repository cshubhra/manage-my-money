package com.finance.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Exchange entity that represents a currency exchange rate.
 * This maps directly to the EXCHANGES table in the database.
 */
@Entity
@Table(name = "exchanges")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Exchange {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "left_currency_id", nullable = false)
    private Currency leftCurrency;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "right_currency_id", nullable = false)
    private Currency rightCurrency;
    
    @NotNull
    @Positive
    @Column(name = "left_to_right", precision = 8, scale = 4, nullable = false)
    private BigDecimal leftToRight;
    
    @NotNull
    @Positive
    @Column(name = "right_to_left", precision = 8, scale = 4, nullable = false)
    private BigDecimal rightToLeft;
    
    private LocalDate day;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "exchange", cascade = CascadeType.ALL)
    private Set<Conversion> conversions = new HashSet<>();
    
    /**
     * Validates that the two currencies are not the same.
     * 
     * @return true if the currencies are different, false otherwise
     */
    public boolean validateCurrenciesDifferent() {
        if (leftCurrency == null || rightCurrency == null) {
            return true;
        }
        return !leftCurrency.getId().equals(rightCurrency.getId());
    }
    
    /**
     * Exchanges an amount from one currency to another.
     * 
     * @param amount the amount to exchange
     * @param currency the currency of the amount
     * @return the exchanged amount in the other currency
     * @throws IllegalArgumentException if the currency is invalid
     */
    public BigDecimal exchange(BigDecimal amount, Currency currency) {
        if (leftCurrency.equals(currency)) {
            return amount.multiply(rightToLeft).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else if (rightCurrency.equals(currency)) {
            return amount.multiply(leftToRight).setScale(2, BigDecimal.ROUND_HALF_UP);
        } else {
            throw new IllegalArgumentException("Invalid currency for this exchange");
        }
    }
}