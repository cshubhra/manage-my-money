package com.finance.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/**
 * SystemCategory entity that represents a system-defined category in the system.
 * This maps directly to the SYSTEM_CATEGORIES table in the database.
 */
@Entity
@Table(name = "system_categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemCategory {
    
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
    @JoinColumn(name = "parent_id")
    private SystemCategory parent;
    
    @OneToMany(mappedBy = "parent")
    private Set<SystemCategory> children = new HashSet<>();
    
    @Column(name = "lft")
    private Integer lft;
    
    @Column(name = "rgt")
    private Integer rgt;
    
    @Column(name = "level")
    private Integer level;
    
    @ManyToMany(mappedBy = "systemCategories")
    private Set<Category> categories = new HashSet<>();
    
    /**
     * Gets the cached level of this system category.
     * 
     * @return the level of this system category
     */
    public Integer getCachedLevel() {
        return level;
    }
    
    /**
     * Gets all ancestors of this system category.
     * 
     * @return a set of all ancestors
     */
    public Set<SystemCategory> getAncestors() {
        Set<SystemCategory> ancestors = new HashSet<>();
        SystemCategory current = this.parent;
        
        while (current != null) {
            ancestors.add(current);
            current = current.parent;
        }
        
        return ancestors;
    }
    
    /**
     * Gets this system category and all its ancestors.
     * 
     * @return a set containing this system category and all its ancestors
     */
    public Set<SystemCategory> getSelfAndAncestors() {
        Set<SystemCategory> selfAndAncestors = getAncestors();
        selfAndAncestors.add(this);
        return selfAndAncestors;
    }
}