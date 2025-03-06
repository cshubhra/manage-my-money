package com.finance.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Transfer entity that represents a financial transaction in the system.
 * This maps directly to the TRANSFERS table in the database.
 */
@Entity
@Table(name = "transfers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;
    
    @NotNull
    @Column(nullable = false)
    private LocalDate day;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "import_guid")
    private String importGuid;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransferItem> transferItems = new ArrayList<>();
    
    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Conversion> conversions = new HashSet<>();
    
    @ManyToMany
    @JoinTable(
        name = "transfers_currencies",
        joinColumns = @JoinColumn(name = "transfer_id"),
        inverseJoinColumns = @JoinColumn(name = "currency_id")
    )
    private Set<Currency> currencies = new HashSet<>();
    
    @ManyToMany
    @JoinTable(
        name = "transfers_categories",
        joinColumns = @JoinColumn(name = "transfer_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
    
    /**
     * Adds a transfer item to this transfer.
     * 
     * @param transferItem the transfer item to add
     */
    public void addTransferItem(TransferItem transferItem) {
        transferItems.add(transferItem);
        transferItem.setTransfer(this);
    }
    
    /**
     * Removes a transfer item from this transfer.
     * 
     * @param transferItem the transfer item to remove
     */
    public void removeTransferItem(TransferItem transferItem) {
        transferItems.remove(transferItem);
        transferItem.setTransfer(null);
    }
    
    /**
     * Adds a conversion to this transfer.
     * 
     * @param conversion the conversion to add
     */
    public void addConversion(Conversion conversion) {
        conversions.add(conversion);
        conversion.setTransfer(this);
    }
    
    /**
     * Removes a conversion from this transfer.
     * 
     * @param conversion the conversion to remove
     */
    public void removeConversion(Conversion conversion) {
        conversions.remove(conversion);
        conversion.setTransfer(null);
    }
}