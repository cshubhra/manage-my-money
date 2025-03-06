package com.finance.application.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * FlowReport entity that represents a flow (income/expense) report in the system.
 * This is a subtype of Report.
 */
@Entity
@DiscriminatorValue("FlowReport")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FlowReport extends Report {
    
    /**
     * Checks if this report is a flow report.
     * 
     * @return always true for FlowReport
     */
    @Override
    public boolean isFlowReport() {
        return true;
    }
}