package com.target.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String reportName;
    
    @Column(nullable = false)
    private String reportType;
    
    @Column(nullable = false)
    private String format;
    
    @Column(name = "generated_by")
    private String generatedBy;
    
    @Column(name = "generated_at")
    private LocalDateTime generatedAt;
    
    @Column(name = "file_path")
    private String filePath;
    
    @Column(name = "parameters")
    private String parameters;
    
    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
    }
}