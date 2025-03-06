package com.finance.application.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Goal entity that represents a financial goal in the system.
 * This maps directly to the GOALS table in the database.
 */
@Entity
@Table(name = "goals")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Goal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String description;
    
    @Column(name = "include_subcategories")
    private Boolean includeSubcategories;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "period_type_int")
    private PeriodType periodType;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "goal_type_int", nullable = false, columnDefinition = "integer default 0")
    private GoalType goalType = GoalType.PERCENT;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "goal_completion_condition_int", nullable = false, columnDefinition = "integer default 0")
    private GoalCompletionCondition goalCompletionCondition = GoalCompletionCondition.AT_LEAST;
    
    @NotNull
    private Float value;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    private Currency currency;
    
    @NotNull
    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;
    
    @NotNull
    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;
    
    @NotNull
    @Column(name = "is_cyclic", nullable = false)
    private Boolean isCyclic = false;
    
    @NotNull
    @Column(name = "is_finished", nullable = false)
    private Boolean isFinished = false;
    
    @Column(name = "cycle_group")
    private Integer cycleGroup;
    
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    /**
     * Gets the unit of the goal value (percentage or currency symbol).
     * 
     * @return the unit symbol as a string
     */
    public String getUnit() {
        if (goalType == GoalType.PERCENT) {
            return "% of " + category.getParent().getName();
        } else {
            return currency.getSymbol();
        }
    }
    
    /**
     * Checks if the goal has been achieved.
     * 
     * @param actualValue the current actual value for comparison
     * @return true if the goal is achieved, false otherwise
     */
    public boolean isPositive(float actualValue) {
        if (goalCompletionCondition == GoalCompletionCondition.AT_MOST) {
            return actualValue <= value;
        } else {
            return actualValue >= value;
        }
    }
    
    /**
     * Marks the goal as finished.
     */
    public void finish() {
        this.isFinished = true;
        this.isCyclic = false;
        this.periodEnd = LocalDate.now();
    }
    
    /**
     * Checks if the goal is finished or past its end date.
     * 
     * @return true if the goal is finished, false otherwise
     */
    public boolean isFinished() {
        return this.isFinished || this.periodEnd.isBefore(LocalDate.now());
    }
}