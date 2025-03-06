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
 * ShareReport entity that represents a share/distribution report in the money transfer application.
 * This class extends the Report class and is used for showing distribution of money across categories.
 * It maps to the ShareReport model in the original Ruby on Rails application.
 */
@Entity
@DiscriminatorValue("SHARE_REPORT")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ShareReport extends Report {
    
    /**
     * Generate share report data showing the distribution across categories
     * 
     * @return Map with category data and pie chart values
     */
    @Override
    public Object generateReportData() {
        // In a real implementation, this would query the database for transfer data
        // within the date range and for the selected categories
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("type", "share");
        reportData.put("startDate", getCalculatedStartDate());
        reportData.put("endDate", getCalculatedEndDate());
        
        // This would be replaced with actual data in the service implementation
        List<Map<String, Object>> pieChartData = new ArrayList<>();
        
        Map<String, Object> slice1 = new HashMap<>();
        slice1.put("category", "Food");
        slice1.put("value", 350.0);
        slice1.put("percentage", 35.0);
        pieChartData.add(slice1);
        
        Map<String, Object> slice2 = new HashMap<>();
        slice2.put("category", "Housing");
        slice2.put("value", 450.0);
        slice2.put("percentage", 45.0);
        pieChartData.add(slice2);
        
        Map<String, Object> slice3 = new HashMap<>();
        slice3.put("category", "Entertainment");
        slice3.put("value", 200.0);
        slice3.put("percentage", 20.0);
        pieChartData.add(slice3);
        
        reportData.put("data", pieChartData);
        reportData.put("total", 1000.0);
        
        return reportData;
    }
}