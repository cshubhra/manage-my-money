package com.finance.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Category entity that represents a financial category in the system.
 * This maps directly to the CATEGORIES table in the database.
 */
@Entity
@Table(name = "categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category_type_int")
    private CategoryType categoryType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<Category> children = new HashSet<>();
    
    @Column(name = "lft")
    private Integer lft;
    
    @Column(name = "rgt")
    private Integer rgt;
    
    @Column(name = "import_guid")
    private String importGuid;
    
    private Boolean imported;
    
    @Email
    private String email;
    
    @Column(columnDefinition = "TEXT")
    private String bankinfo;
    
    @Column(name = "bank_account_number")
    private String bankAccountNumber;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "loan_category")
    private Boolean loanCategory;
    
    @OneToMany(mappedBy = "category")
    private Set<TransferItem> transferItems = new HashSet<>();
    
    @OneToMany(mappedBy = "category")
    private Set<Goal> goals = new HashSet<>();
    
    @ManyToMany
    @JoinTable(
        name = "categories_system_categories",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "system_category_id")
    )
    private Set<SystemCategory> systemCategories = new HashSet<>();
    
    @OneToMany(mappedBy = "category")
    private Set<CategoryReportOption> categoryReportOptions = new HashSet<>();
    
    /**
     * Checks if this category is a top-level category (i.e., has no parent).
     * 
     * @return true if this is a top-level category, false otherwise
     */
    public boolean isTop() {
        return parent == null;
    }
    
    /**
     * Checks if this category is a leaf category (i.e., has no children).
     * 
     * @return true if this is a leaf category, false otherwise
     */
    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }
}