package com.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Category entity that corresponds to the categories table in the database.
 * This class represents a category used for transfers in the money transfer application.
 * It maps to the Category model in the original Ruby on Rails application.
 *
 * Categories can be of types:
 * - ASSET: Resources/assets categories
 * - INCOME: Income categories
 * - EXPENSE: Expense categories
 * - LOAN: Loan categories
 * - BALANCE: Opening balance categories
 *
 * Categories are organized in a tree structure using the nested set model.
 */
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
public class Category {
    
    public enum CategoryType {
        ASSET, 
        INCOME, 
        EXPENSE, 
        LOAN, 
        BALANCE
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    private String description;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category_type_int")
    private CategoryType categoryType;
    
    @ManyToOne
    @JoinColumn(name = "system_category_id")
    private SystemCategory systemCategory;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;
    
    // Nested set model fields
    private int lft;
    private int rgt;
    
    // Opening balance fields
    @Column(name = "opening_balance")
    private BigDecimal openingBalance;
    
    @Column(name = "bank_account_number")
    private String bankAccountNumber;
    
    @Column(name = "loan_category")
    private Boolean loanCategory;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Cached level for nested set optimization
    @Column(name = "cached_level")
    private Integer cachedLevel;
    
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    @ToString.Exclude
    private List<TransferItem> transferItems = new ArrayList<>();
    
    /**
     * Check if this category is a top-level category
     * 
     * @return true if this is a top-level category
     */
    public boolean isTop() {
        return cachedLevel != null && cachedLevel == 0;
    }
    
    /**
     * Check if this category is a leaf (has no children)
     * 
     * @return true if this is a leaf category
     */
    public boolean isLeaf() {
        return rgt - lft == 1;
    }
    
    /**
     * Get the parent category
     * 
     * @return parent category or null if this is a top-level category
     */
    public Category getParent() {
        if (isTop()) {
            return null;
        }
        // This would require a repository call to find the parent
        // In Spring Data JPA, you would need to inject the repository
        // For the entity class, we simply define the relationship
        return null;
    }
    
    /**
     * Get all ancestors of this category
     * 
     * @return list of ancestor categories
     */
    public List<Category> getAncestors() {
        // This would require a repository call to find ancestors
        // Would typically be implemented in a service layer
        return new ArrayList<>();
    }
    
    /**
     * Get all descendants of this category
     * 
     * @return list of descendant categories
     */
    public List<Category> getDescendants() {
        // This would require a repository call to find descendants
        // Would typically be implemented in a service layer
        return new ArrayList<>();
    }
    
    /**
     * Get this category and all its descendants
     * 
     * @return list of this category and all descendant categories
     */
    public List<Category> getSelfAndDescendants() {
        List<Category> result = new ArrayList<>();
        result.add(this);
        result.addAll(getDescendants());
        return result;
    }
    
    /**
     * Get the children of this category
     * 
     * @return list of direct child categories
     */
    public List<Category> getChildren() {
        // This would require a repository call to find children
        // Would typically be implemented in a service layer
        return new ArrayList<>();
    }
    
    /**
     * Calculate the name including the path for this category
     * 
     * @return name with path
     */
    public String getNameWithPath() {
        // This would typically be implemented with proper path resolution
        return name;
    }
}