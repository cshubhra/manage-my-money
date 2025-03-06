package com.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * Conversion entity that corresponds to the conversions table in the database.
 * This class represents a currency conversion used in a transfer in the money transfer application.
 * It maps to the Conversion model in the original Ruby on Rails application.
 * 
 * A conversion links a transfer with an exchange rate that was used for currency conversion.
 */
@Entity
@Table(name = "conversions")
@Data
@NoArgsConstructor
public class Conversion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "transfer_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Transfer transfer;
    
    @ManyToOne
    @JoinColumn(name = "exchange_id", nullable = false)
    private Exchange exchange;
    
    /**
     * Create a new conversion
     * 
     * @param transfer Transfer this conversion belongs to
     * @param exchange Exchange rate used for this conversion
     */
    public Conversion(Transfer transfer, Exchange exchange) {
        this.transfer = transfer;
        this.exchange = exchange;
    }
    
    /**
     * Check if this conversion matches the given currencies
     * 
     * @param currency1 First currency
     * @param currency2 Second currency
     * @return true if this conversion can convert between the two currencies
     */
    public boolean matchesCurrencies(Currency currency1, Currency currency2) {
        return exchange.canConvert(currency1, currency2);
    }
}