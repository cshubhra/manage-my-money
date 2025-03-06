package com.moneytransfer.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ValueReport entity that represents a value report in the money transfer application.
 * This class extends the Report class and is used for showing actual values for categories.
 * It maps to the ValueReport model in the original Ruby on Rails application.
 */
@Entity
@DiscriminatorValue("VALUE_REPORT")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ValueReport extends Report {
    
    /**
     * Generate value report data showing the actual values for categories
     * 
     * @return Map with category values and bar chart data
     */
    @Override
    public Object generateReportData() {
        // In a real implementation, this would query the database for transfer data
        // within the date range and for the selected categories
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("type", "value");
        reportData.put("startDate", getCalculatedStartDate());
        reportData.put("endDate", getCalculatedEndDate());
        
        // This would be replaced with actual data in the service implementation
        List<Map<String, Object>> barChartData = new ArrayList<>();
        
        Map<String, Object> bar1 = new HashMap<>();
        bar1.put("category", "Food");
        bar1.put("value", 350.0);
        barChartData.add(bar1);
        
        Map<String, Object> bar2 = new HashMap<>();
        bar2.put("category", "Housing");
        bar2.put("value", 450.0);
        barChartData.add(bar2);
        
        Map<String, Object> bar3 = new HashMap<>();
        bar3.put("category", "Entertainment");
        bar3.put("value", 200.0);
        barChartData.add(bar3);
        
        reportData.put("data", barChartData);
        
        return reportData;
    }
}