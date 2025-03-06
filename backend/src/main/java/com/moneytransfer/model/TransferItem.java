package com.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * TransferItem entity that corresponds to the transfer_items table in the database.
 * This class represents an individual item/entry within a financial transfer.
 * It maps to the TransferItem model in the original Ruby on Rails application.
 * 
 * Each transfer item has a value (positive for income, negative for expense),
 * belongs to a category, and has an associated currency.
 */
@Entity
@Table(name = "transfer_items")
@Data
@NoArgsConstructor
public class TransferItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(nullable = false)
    private BigDecimal value;
    
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "transfer_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private Transfer transfer;
    
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;
    
    /**
     * Check if this transfer item is an income (positive value)
     * 
     * @return true if value is greater than or equal to zero
     */
    @Transient
    public boolean isIncome() {
        return value != null && value.compareTo(BigDecimal.ZERO) >= 0;
    }
    
    /**
     * Check if this transfer item is an expense (negative value)
     * 
     * @return true if value is less than zero
     */
    @Transient
    public boolean isExpense() {
        return value != null && value.compareTo(BigDecimal.ZERO) < 0;
    }
    
    /**
     * Get the absolute value of this transfer item's value
     * 
     * @return absolute value
     */
    @Transient
    public BigDecimal getAbsoluteValue() {
        return value != null ? value.abs() : BigDecimal.ZERO;
    }
    
    /**
     * Create a copy of this transfer item
     * 
     * @return new transfer item with the same properties
     */
    public TransferItem copy() {
        TransferItem copy = new TransferItem();
        copy.setValue(this.value);
        copy.setDescription(this.description);
        copy.setCategory(this.category);
        copy.setCurrency(this.currency);
        // Don't copy transfer reference to avoid circular references
        return copy;
    }
}