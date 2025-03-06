package com.moneytransfer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Goal entity that corresponds to the goals table in the database.
 * This class represents a financial goal in the money transfer application.
 * It maps to the Goal model in the original Ruby on Rails application.
 * 
 * Goals can be set for specific categories with target values and deadlines.
 */
@Entity
@Table(name = "goals")
@Data
@NoArgsConstructor
public class Goal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    private String description;
    
    @NotNull
    @Column(nullable = false)
    private BigDecimal targetValue;
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private User user;
    
    @Column(name = "is_cyclic")
    private Boolean cyclic;
    
    @Column(name = "num_of_days")
    private Integer numOfDays;
    
    @Column(name = "period_type_int")
    private Integer periodTypeInt;
    
    @Column(name = "period_day")
    private Integer periodDay;
    
    @Column(name = "period_month")
    private Integer periodMonth;
    
    /**
     * Get the current value towards this goal based on transactions in the category
     * 
     * @return current value
     */
    @Transient
    public BigDecimal getCurrentValue() {
        // In a real implementation, this would query the database for transfer data
        // within the date range and for the selected category
        // This is a placeholder implementation
        return BigDecimal.ZERO;
    }
    
    /**
     * Get the percentage of completion towards this goal
     * 
     * @return percentage complete (0-100)
     */
    @Transient
    public double getPercentComplete() {
        if (targetValue == null || targetValue.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }
        
        BigDecimal currentValue = getCurrentValue();
        if (currentValue == null) {
            return 0;
        }
        
        double percentage = currentValue.divide(targetValue, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal(100))
                .doubleValue();
                
        return Math.min(100, Math.max(0, percentage));
    }
    
    /**
     * Get the remaining value needed to reach the goal
     * 
     * @return remaining value
     */
    @Transient
    public BigDecimal getRemainingValue() {
        BigDecimal currentValue = getCurrentValue();
        if (currentValue == null || targetValue == null) {
            return targetValue;
        }
        
        return targetValue.subtract(currentValue);
    }
    
    /**
     * Check if this goal is achieved
     * 
     * @return true if goal is achieved
     */
    @Transient
    public boolean isAchieved() {
        return getPercentComplete() >= 100;
    }
    
    /**
     * Check if this goal has expired
     * 
     * @return true if goal has expired (end date is in the past)
     */
    @Transient
    public boolean isExpired() {
        return endDate != null && endDate.isBefore(LocalDate.now());
    }
}