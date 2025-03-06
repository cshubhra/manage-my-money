package com.finance.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Currency entity that represents a currency in the system.
 * This maps directly to the CURRENCIES table in the database.
 */
@Entity
@Table(name = "currencies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String symbol;
    
    @NotBlank
    @Size(min = 3, max = 3)
    @Pattern(regexp = "[A-Z]{3}")
    @Column(name = "long_symbol", nullable = false)
    private String longSymbol;
    
    @NotBlank
    @Column(nullable = false)
    private String name;
    
    @NotBlank
    @Column(name = "long_name", nullable = false)
    private String longName;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "currency")
    private Set<TransferItem> transferItems = new HashSet<>();
    
    @OneToMany(mappedBy = "leftCurrency")
    private Set<Exchange> leftExchanges = new HashSet<>();
    
    @OneToMany(mappedBy = "rightCurrency")
    private Set<Exchange> rightExchanges = new HashSet<>();
    
    @OneToMany(mappedBy = "currency")
    private Set<Goal> goals = new HashSet<>();
    
    @OneToMany(mappedBy = "defaultCurrency")
    private Set<User> usersWithDefaultCurrency = new HashSet<>();
    
    /**
     * Checks if this is a system currency (not user-defined).
     * 
     * @return true if this is a system currency, false otherwise
     */
    public boolean isSystem() {
        return user == null;
    }
    
    /**
     * Gets all exchanges this currency is involved in.
     * 
     * @return a set of all exchanges involving this currency
     */
    public Set<Exchange> getExchanges() {
        Set<Exchange> allExchanges = new HashSet<>(leftExchanges);
        allExchanges.addAll(rightExchanges);
        return allExchanges;
    }
}