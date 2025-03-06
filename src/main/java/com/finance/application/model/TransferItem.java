package com.finance.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * TransferItem entity that represents a line item in a financial transaction.
 * This maps directly to the TRANSFER_ITEMS table in the database.
 */
@Entity
@Table(name = "transfer_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    
    @NotNull
    @Column(precision = 8, scale = 2, nullable = false)
    private BigDecimal value;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id", nullable = false)
    private Transfer transfer;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;
    
    @Column(name = "import_guid")
    private String importGuid;
    
    @Transient
    private TransferItemType transferItemType;
    
    /**
     * Gets the type of this transfer item (income or outcome).
     * 
     * @return the type of this transfer item
     */
    public TransferItemType getTransferItemType() {
        if (this.transferItemType != null) {
            return this.transferItemType;
        }
        if (this.value == null) {
            throw new IllegalStateException("Value must be set to determine transfer item type");
        }
        return this.value.compareTo(BigDecimal.ZERO) >= 0 ? TransferItemType.INCOME : TransferItemType.OUTCOME;
    }
    
    /**
     * Sets the type of this transfer item.
     * This will affect the sign of the value.
     * 
     * @param transferItemType the type to set
     */
    public void setTransferItemType(TransferItemType transferItemType) {
        this.transferItemType = transferItemType;
        if (this.value != null) {
            if (transferItemType == TransferItemType.INCOME && this.value.compareTo(BigDecimal.ZERO) < 0) {
                this.value = this.value.negate();
            } else if (transferItemType == TransferItemType.OUTCOME && this.value.compareTo(BigDecimal.ZERO) > 0) {
                this.value = this.value.negate();
            }
        }
    }
}