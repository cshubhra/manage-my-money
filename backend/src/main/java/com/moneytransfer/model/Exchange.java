package com.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Exchange entity that corresponds to the exchanges table in the database.
 * This class represents a currency exchange rate in the money transfer application.
 * It maps to the Exchange model in the original Ruby on Rails application.
 * 
 * Each exchange has a left and right currency, a rate, and a date.
 * The rate represents how many units of the right currency equal one unit of the left currency.
 */
@Entity
@Table(name = "exchanges")
@Data
@NoArgsConstructor
public class Exchange {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(nullable = false)
    private BigDecimal rate;
    
    @Column(name = "day")
    private LocalDate day;
    
    @ManyToOne
    @JoinColumn(name = "left_currency_id", nullable = false)
    private Currency leftCurrency;
    
    @ManyToOne
    @JoinColumn(name = "right_currency_id", nullable = false)
    private Currency rightCurrency;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;
    
    @OneToMany(mappedBy = "exchange")
    @JsonIgnore
    @ToString.Exclude
    private List<Conversion> conversions = new ArrayList<>();
    
    /**
     * Exchange an amount from one currency to another using this exchange rate
     * 
     * @param amount Amount to exchange
     * @param targetCurrency Target currency to convert to
     * @return Converted amount
     */
    public BigDecimal exchange(BigDecimal amount, Currency targetCurrency) {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        
        if (leftCurrency.getId().equals(targetCurrency.getId())) {
            // Converting from right to left (divide by rate)
            return amount.divide(rate, 2, RoundingMode.HALF_UP);
        } else if (rightCurrency.getId().equals(targetCurrency.getId())) {
            // Converting from left to right (multiply by rate)
            return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        } else {
            throw new IllegalArgumentException("Target currency must be either left or right currency of this exchange");
        }
    }
    
    /**
     * Get the inverse rate (1/rate)
     * 
     * @return Inverse of the exchange rate
     */
    @Transient
    public BigDecimal getInverseRate() {
        return BigDecimal.ONE.divide(rate, 6, RoundingMode.HALF_UP);
    }
    
    /**
     * Check if this exchange can be used to convert between the given currencies
     * 
     * @param fromCurrency Currency to convert from
     * @param toCurrency Currency to convert to
     * @return true if this exchange can be used for the conversion
     */
    public boolean canConvert(Currency fromCurrency, Currency toCurrency) {
        return (leftCurrency.getId().equals(fromCurrency.getId()) && rightCurrency.getId().equals(toCurrency.getId())) ||
               (leftCurrency.getId().equals(toCurrency.getId()) && rightCurrency.getId().equals(fromCurrency.getId()));
    }
}