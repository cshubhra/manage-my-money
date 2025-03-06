package com.moneytransfer.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * SystemCategory entity that corresponds to the system_categories table in the database.
 * This class represents predefined system categories in the money transfer application.
 * It maps to the SystemCategory model in the original Ruby on Rails application.
 * 
 * System categories provide templates that users can choose from when creating their own categories.
 */
@Entity
@Table(name = "system_categories")
@Data
@NoArgsConstructor
public class SystemCategory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    private String description;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category_type_int")
    private Category.CategoryType categoryType;
    
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private SystemCategory parent;
    
    @OneToMany(mappedBy = "parent")
    private List<SystemCategory> children = new ArrayList<>();
    
    @OneToMany(mappedBy = "systemCategory")
    private List<Category> userCategories = new ArrayList<>();
    
    private Integer level;
    
    /**
     * Check if this system category is of the specified type
     * 
     * @param type Category type to check
     * @return true if this system category is of the specified type
     */
    public boolean isOfType(Category.CategoryType type) {
        return this.categoryType == type;
    }
    
    /**
     * Check if this system category is a top-level category
     * 
     * @return true if this is a top-level system category
     */
    public boolean isTop() {
        return parent == null;
    }
    
    /**
     * Find all system categories of a specific type
     * 
     * @param category Category to match type with
     * @return list of system categories of the same type as the given category
     */
    public static List<SystemCategory> findAllByType(Category category) {
        // This would typically be implemented in a repository
        // Here we're just showing the method signature
        return new ArrayList<>();
    }
}