# Reporting System Architecture

## Overview
This document details the reporting system architecture for the modernized application, focusing on maintaining the complex reporting functionality while improving performance and maintainability.

## 1. Report Types and Hierarchy

### 1.1 Base Report Structure
```typescript
// Angular Interface
interface BaseReport {
    id: number;
    name: string;
    periodType: PeriodType;
    periodStart: Date;
    periodEnd: Date;
    reportViewType: ReportViewType;
    userId: number;
    isRelativePeriod: boolean;
}

// Spring Boot Entity
@Entity
@Table(name = "reports")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    private PeriodType periodType;
    
    @Column
    private LocalDate periodStart;
    
    @Column
    private LocalDate periodEnd;
    
    @Enumerated(EnumType.STRING)
    private ReportViewType reportViewType;
    
    @Column(nullable = false)
    private boolean relativePeriod;
}
```

### 1.2 Report Types
```java
@Entity
@DiscriminatorValue("SHARE")
public class ShareReport extends Report {
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @Column
    private Integer depth;
    
    @Column
    private Integer maxCategoriesValuesCount;
}

@Entity
@DiscriminatorValue("FLOW")
public class FlowReport extends Report {
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<CategoryReportOption> categories;
}

@Entity
@DiscriminatorValue("VALUE")
public class ValueReport extends Report {
    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL)
    private List<CategoryReportOption> categories;
    
    @Column
    private Integer periodDivision;
}
```

## 2. Report Generation Service

### 2.1 Report Service Interface
```java
public interface ReportService {
    Report createReport(ReportDTO reportDTO);
    ReportData generateReport(Long reportId);
    List<Report> getUserReports(Long userId);
    void scheduleReport(Long reportId, Schedule schedule);
}
```

### 2.2 Report Generation Implementation
```java
@Service
public class ReportGenerationService {
    @Autowired
    private TransferRepository transferRepository;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private CacheManager cacheManager;
    
    @Async("reportExecutor")
    public CompletableFuture<ReportData> generateReportAsync(Report report) {
        return switch(report.getType()) {
            case SHARE -> generateShareReport((ShareReport) report);
            case FLOW -> generateFlowReport((FlowReport) report);
            case VALUE -> generateValueReport((ValueReport) report);
        };
    }
    
    private CompletableFuture<ReportData> generateShareReport(ShareReport report) {
        // Implement share report generation logic
        return CompletableFuture.completedFuture(reportData);
    }
}
```

## 3. Report Caching Strategy

### 3.1 Cache Configuration
```java
@Configuration
@EnableCaching
public class ReportCacheConfig {
    @Bean
    public CacheManager cacheManager() {
        RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory())
            .cacheDefaults(defaultConfig())
            .withInitialCacheConfigurations(customConfigMap())
            .build();
        return cacheManager;
    }
    
    private RedisCacheConfiguration defaultConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(1))
            .serializeKeysWith(RedisSerializationContext
                .SerializationPair
                .fromSerializer(new StringRedisSerializer()));
    }
}
```

### 3.2 Cache Implementation
```java
@Service
public class CachedReportService implements ReportService {
    @Cacheable(
        value = "reports",
        key = "#reportId",
        condition = "#report.isCacheable()"
    )
    public ReportData generateReport(Long reportId) {
        // Report generation logic
    }
    
    @CacheEvict(value = "reports", key = "#reportId")
    public void invalidateReport(Long reportId) {
        // Cache invalidation logic
    }
}
```

## 4. Report Visualization Components

### 4.1 Base Chart Component
```typescript
// report-chart.component.ts
@Component({
    selector: 'app-report-chart',
    template: `
        <div class="chart-container">
            <ng-container [ngSwitch]="report.reportViewType">
                <app-pie-chart *ngSwitchCase="'pie'" [data]="chartData">
                </app-pie-chart>
                <app-line-chart *ngSwitchCase="'linear'" [data]="chartData">
                </app-line-chart>
                <app-bar-chart *ngSwitchCase="'bar'" [data]="chartData">
                </app-bar-chart>
            </ng-container>
        </div>
    `
})
export class ReportChartComponent implements OnInit {
    @Input() report: Report;
    chartData: ChartData;
    
    constructor(private reportService: ReportService) {}
    
    ngOnInit() {
        this.loadReportData();
    }
    
    private loadReportData() {
        this.reportService.generateReport(this.report.id)
            .pipe(
                map(data => this.transformToChartData(data))
            )
            .subscribe(chartData => this.chartData = chartData);
    }
}
```

### 4.2 Chart Data Models
```typescript
interface ChartData {
    labels: string[];
    datasets: ChartDataset[];
    options: ChartOptions;
}

interface ChartDataset {
    label: string;
    data: number[];
    backgroundColor?: string[];
    borderColor?: string;
}
```

## 5. Report Export Service

### 5.1 Export Interface
```java
public interface ReportExporter {
    byte[] exportToPdf(Report report, ReportData data);
    byte[] exportToExcel(Report report, ReportData data);
    byte[] exportToCsv(Report report, ReportData data);
}
```

### 5.2 Export Implementation
```java
@Service
public class ReportExportService implements ReportExporter {
    @Autowired
    private PDFGenerator pdfGenerator;
    
    @Autowired
    private ExcelGenerator excelGenerator;
    
    @Override
    public byte[] exportToPdf(Report report, ReportData data) {
        return pdfGenerator.generateReport(report, data);
    }
    
    @Override
    public byte[] exportToExcel(Report report, ReportData data) {
        return excelGenerator.generateReport(report, data);
    }
}
```

## 6. Scheduled Reports

### 6.1 Schedule Configuration
```java
@Configuration
@EnableScheduling
public class ReportScheduleConfig {
    @Bean
    public ThreadPoolTaskScheduler reportScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.setThreadNamePrefix("ReportScheduler-");
        return scheduler;
    }
}
```

### 6.2 Schedule Implementation
```java
@Service
public class ScheduledReportService {
    @Autowired
    private ReportService reportService;
    
    @Autowired
    private EmailService emailService;
    
    @Scheduled(cron = "0 0 1 * * ?") // Daily at 1 AM
    public void generateScheduledReports() {
        List<Report> scheduledReports = reportService.getScheduledReports();
        for (Report report : scheduledReports) {
            ReportData data = reportService.generateReport(report.getId());
            emailService.sendReportEmail(report.getUserEmail(), data);
        }
    }
}
```

## 7. Performance Optimization

### 7.1 Query Optimization
```java
@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("""
        SELECT r FROM Report r
        LEFT JOIN FETCH r.categories c
        LEFT JOIN FETCH c.category
        WHERE r.userId = :userId
        AND r.periodStart >= :startDate
        AND r.periodEnd <= :endDate
    """)
    List<Report> findReportsWithData(
        @Param("userId") Long userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}
```

### 7.2 Performance Monitoring
```java
@Aspect
@Component
public class ReportPerformanceMonitor {
    @Around("execution(* com.finance.service.ReportService.generateReport(..))")
    public Object monitorReportGeneration(ProceedingJoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        
        reportMetrics.record(
            "report.generation",
            endTime - startTime,
            Tags.of("reportType", getReportType(joinPoint))
        );
        
        return result;
    }
}
```

This architecture ensures efficient report generation, caching, and delivery while maintaining the complex business logic of the original Ruby on Rails application.