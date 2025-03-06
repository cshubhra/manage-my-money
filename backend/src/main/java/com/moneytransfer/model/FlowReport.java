package com.moneytransfer.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

/**
 * FlowReport entity that represents a flow report in the money transfer application.
 * This class extends the Report class and is used for tracking cash flow over time.
 * It maps to the FlowReport model in the original Ruby on Rails application.
 */
@Entity
@DiscriminatorValue("FLOW_REPORT")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class FlowReport extends Report {
    
    /**
     * Generate flow report data showing cash flow over time for the selected categories
     * 
     * @return Map with dates as keys and cash flow values as values
     */
    @Override
    public Object generateReportData() {
        // In a real implementation, this would query the database for transfer data
        // within the date range and for the selected categories
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("type", "flow");
        reportData.put("startDate", getCalculatedStartDate());
        reportData.put("endDate", getCalculatedEndDate());
        
        // This would be replaced with actual data in the service implementation
        Map<String, Double> sampleData = new HashMap<>();
        sampleData.put("2023-01-01", 100.0);
        sampleData.put("2023-01-15", 250.0);
        sampleData.put("2023-02-01", 175.0);
        reportData.put("data", sampleData);
        
        return reportData;
    }
}