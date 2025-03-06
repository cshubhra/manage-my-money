package com.finance.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Conversion entity that represents a currency conversion used in a transfer.
 * This maps directly to the CONVERSIONS table in the database.
 */
@Entity
@Table(name = "conversions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_id", nullable = false)
    private Exchange exchange;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transfer_id", nullable = false)
    private Transfer transfer;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Checks if the exchange used in this conversion has no specific date.
     * 
     * @return true if the exchange has no date, false otherwise
     */
    public boolean isExchangeDateless() {
        return exchange != null && exchange.getDay() == null;
    }
}