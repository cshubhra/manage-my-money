package com.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Currency entity that corresponds to the currencies table in the database.
 * This class represents a currency used in the money transfer application.
 * It maps to the Currency model in the original Ruby on Rails application.
 * 
 * Currencies can be system-defined (user_id is null) or user-defined.
 */
@Entity
@Table(name = "currencies", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"long_symbol", "user_id"}),
        @UniqueConstraint(columnNames = {"long_name", "user_id"})
})
@Data
@NoArgsConstructor
public class Currency {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String symbol;
    
    @NotBlank
    @Size(min = 3, max = 3)
    @Pattern(regexp = "[A-Z]{3}", message = "Long symbol must be a 3-character uppercase code")
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
    @JsonIgnore
    @ToString.Exclude
    private User user;
    
    @OneToMany(mappedBy = "currency")
    @JsonIgnore
    @ToString.Exclude
    private List<TransferItem> transferItems = new ArrayList<>();
    
    @OneToMany(mappedBy = "leftCurrency")
    @JsonIgnore
    @ToString.Exclude
    private List<Exchange> leftExchanges = new ArrayList<>();
    
    @OneToMany(mappedBy = "rightCurrency")
    @JsonIgnore
    @ToString.Exclude
    private List<Exchange> rightExchanges = new ArrayList<>();
    
    @OneToMany(mappedBy = "defaultCurrency")
    @JsonIgnore
    @ToString.Exclude
    private List<User> usersWithDefaultCurrency = new ArrayList<>();
    
    /**
     * Check if this is a system currency
     * 
     * @return true if this currency is system-defined (user is null)
     */
    public boolean isSystem() {
        return user == null;
    }
    
    /**
     * Get all exchanges (both left and right) for this currency
     * 
     * @return list of exchanges where this currency is either left or right
     */
    public List<Exchange> getExchanges() {
        List<Exchange> allExchanges = new ArrayList<>();
        allExchanges.addAll(leftExchanges);
        allExchanges.addAll(rightExchanges);
        return allExchanges;
    }
    
    /**
     * Set all symbol and name fields to the given value
     * 
     * @param symbol Symbol to set for all fields
     */
    public void setAll(String symbol) {
        this.symbol = symbol;
        this.longSymbol = symbol;
        this.name = symbol;
        this.longName = symbol;
    }
}