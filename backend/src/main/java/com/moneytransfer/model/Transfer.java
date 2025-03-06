package com.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Transfer entity that corresponds to the transfers table in the database.
 * This class represents a financial transfer in the money transfer application.
 * It maps to the Transfer model in the original Ruby on Rails application.
 * 
 * A transfer consists of multiple transfer items (at least 2) and may include conversions between currencies.
 * The sum of all transfer items' values must be zero to maintain balance.
 */
@Entity
@Table(name = "transfers")
@Data
@NoArgsConstructor
public class Transfer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String description;
    
    @NotNull
    @Column(nullable = false)
    private LocalDate day;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    private User user;
    
    @Column(name = "import_guid")
    private String importGuid;
    
    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransferItem> transferItems = new ArrayList<>();
    
    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Conversion> conversions = new ArrayList<>();
    
    /**
     * Add a transfer item to this transfer
     * 
     * @param transferItem The transfer item to add
     * @return this transfer instance for method chaining
     */
    public Transfer addTransferItem(TransferItem transferItem) {
        transferItems.add(transferItem);
        transferItem.setTransfer(this);
        return this;
    }
    
    /**
     * Remove a transfer item from this transfer
     * 
     * @param transferItem The transfer item to remove
     * @return this transfer instance for method chaining
     */
    public Transfer removeTransferItem(TransferItem transferItem) {
        transferItems.remove(transferItem);
        transferItem.setTransfer(null);
        return this;
    }
    
    /**
     * Add a conversion to this transfer
     * 
     * @param conversion The conversion to add
     * @return this transfer instance for method chaining
     */
    public Transfer addConversion(Conversion conversion) {
        conversions.add(conversion);
        conversion.setTransfer(this);
        return this;
    }
    
    /**
     * Remove a conversion from this transfer
     * 
     * @param conversion The conversion to remove
     * @return this transfer instance for method chaining
     */
    public Transfer removeConversion(Conversion conversion) {
        conversions.remove(conversion);
        conversion.setTransfer(null);
        return this;
    }
    
    /**
     * Get the categories associated with this transfer's items
     * 
     * @return list of categories
     */
    @Transient
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        for (TransferItem item : transferItems) {
            if (item.getCategory() != null && !categories.contains(item.getCategory())) {
                categories.add(item.getCategory());
            }
        }
        return categories;
    }
    
    /**
     * Get the currencies associated with this transfer's items
     * 
     * @return list of currencies
     */
    @Transient
    public List<Currency> getCurrencies() {
        List<Currency> currencies = new ArrayList<>();
        for (TransferItem item : transferItems) {
            if (item.getCurrency() != null && !currencies.contains(item.getCurrency())) {
                currencies.add(item.getCurrency());
            }
        }
        return currencies;
    }
    
    /**
     * Compare this transfer to another transfer by date
     * 
     * @param otherTransfer The other transfer to compare to
     * @return comparison result
     */
    public int compareTo(Transfer otherTransfer) {
        return day.compareTo(otherTransfer.getDay());
    }
}