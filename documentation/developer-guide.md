# Developer Guide

## Overview
This guide provides instructions for developers working on the modernization project to migrate from Ruby on Rails to Angular and Spring Boot.

## Development Environment Setup

### Prerequisites
1. Java Development Kit (JDK) 17 or later
2. Node.js 16.x or later
3. Angular CLI 15.x or later
4. Maven 3.8.x or later
5. Git
6. IDE (recommended: IntelliJ IDEA, Visual Studio Code)
7. PostgreSQL 13.x or later
8. Redis (for caching)

### Frontend Setup (Angular)

1. Install Angular CLI:
```bash
npm install -g @angular/cli
```

2. Clone the repository and navigate to frontend directory:
```bash
git clone <repository-url>
cd frontend
```

3. Install dependencies:
```bash
npm install
```

4. Start development server:
```bash
ng serve
```

### Backend Setup (Spring Boot)

1. Navigate to backend directory:
```bash
cd backend
```

2. Build the project:
```bash
mvn clean install
```

3. Run the application:
```bash
mvn spring-boot:run
```

## Project Structure

### Frontend Structure
```plaintext
frontend/
├── src/
│   ├── app/
│   │   ├── core/
│   │   │   ├── auth/
│   │   │   ├── guards/
│   │   │   ├── interceptors/
│   │   │   └── services/
│   │   ├── features/
│   │   │   ├── transfers/
│   │   │   ├── categories/
│   │   │   ├── goals/
│   │   │   └── reports/
│   │   ├── shared/
│   │   │   ├── components/
│   │   │   ├── models/
│   │   │   └── pipes/
│   │   └── store/
│   │       ├── actions/
│   │       ├── effects/
│   │       └── reducers/
│   ├── assets/
│   └── environments/
```

### Backend Structure
```plaintext
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── financial/
│   │   │           ├── config/
│   │   │           ├── controller/
│   │   │           ├── model/
│   │   │           ├── repository/
│   │   │           └── service/
│   │   └── resources/
│   └── test/
```

## Development Guidelines

### Code Style

#### Angular (TypeScript)
```typescript
// Use interfaces for data models
interface Transfer {
  id: number;
  amount: number;
  description: string;
  date: Date;
  category: Category;
}

// Use proper naming conventions
export class TransferListComponent implements OnInit {
  private readonly destroy$ = new Subject<void>();
  transfers: Transfer[] = [];

  constructor(private transferService: TransferService) {}

  ngOnInit(): void {
    this.loadTransfers();
  }

  private loadTransfers(): void {
    this.transferService.getTransfers()
      .pipe(takeUntil(this.destroy$))
      .subscribe(transfers => this.transfers = transfers);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
```

#### Spring Boot (Java)
```java
@Service
@Transactional
@Slf4j
public class TransferService {
    private static final String TRANSFER_CACHE = "transfers";
    private final TransferRepository transferRepository;

    @Autowired
    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;
    }

    @Cacheable(TRANSFER_CACHE)
    public List<TransferDTO> getUserTransfers(Long userId) {
        log.debug("Fetching transfers for user: {}", userId);
        return transferRepository.findByUserId(userId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
}
```

### Testing Guidelines

#### Angular Tests
```typescript
describe('TransferListComponent', () => {
  let component: TransferListComponent;
  let fixture: ComponentFixture<TransferListComponent>;
  let transferService: jasmine.SpyObj<TransferService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('TransferService', ['getTransfers']);
    
    await TestBed.configureTestingModule({
      declarations: [ TransferListComponent ],
      providers: [
        { provide: TransferService, useValue: spy }
      ]
    }).compileComponents();

    transferService = TestBed.inject(TransferService) as jasmine.SpyObj<TransferService>;
  });

  it('should load transfers on init', () => {
    const mockTransfers = [
      { id: 1, amount: 100, description: 'Test' }
    ];
    transferService.getTransfers.and.returnValue(of(mockTransfers));

    fixture.detectChanges();

    expect(component.transfers).toEqual(mockTransfers);
  });
});
```

#### Spring Boot Tests
```java
@SpringBootTest
class TransferServiceTest {
    @MockBean
    private TransferRepository transferRepository;

    @Autowired
    private TransferService transferService;

    @Test
    void getUserTransfers_ValidUserId_ReturnsTransfers() {
        // Arrange
        Long userId = 1L;
        List<Transfer> mockTransfers = Arrays.asList(
            new Transfer(1L, userId, BigDecimal.valueOf(100))
        );
        when(transferRepository.findByUserId(userId))
            .thenReturn(mockTransfers);

        // Act
        List<TransferDTO> result = transferService.getUserTransfers(userId);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAmount())
            .isEqualTo(BigDecimal.valueOf(100));
    }
}
```

### Git Workflow

1. Branch Naming Convention:
```plaintext
feature/[TICKET-NUMBER]-short-description
bugfix/[TICKET-NUMBER]-short-description
hotfix/[TICKET-NUMBER]-short-description
```

2. Commit Message Format:
```plaintext
[TICKET-NUMBER] Category: Short description

Detailed description of changes
```

3. Pull Request Process:
- Create feature branch from development
- Implement changes
- Write/update tests
- Create pull request
- Get code review
- Merge after approval

### Database Migrations

```sql
-- V1__create_transfers_table.sql
CREATE TABLE transfers (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    description VARCHAR(255),
    date TIMESTAMP NOT NULL,
    category_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);
```

### API Documentation

Use Swagger annotations in controllers:

```java
@RestController
@RequestMapping("/api/v1/transfers")
@Tag(name = "Transfer Management")
public class TransferController {

    @Operation(summary = "Create new transfer")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Transfer created",
            content = @Content(schema = @Schema(implementation = TransferDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input"
        )
    })
    @PostMapping
    public ResponseEntity<TransferDTO> createTransfer(
        @RequestBody @Valid TransferDTO transfer
    ) {
        // Implementation
    }
}
```

### Error Handling

#### Frontend
```typescript
@Injectable({
  providedIn: 'root'
})
export class ErrorHandlingInterceptor implements HttpInterceptor {
  constructor(private messageService: MessageService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'An error occurred';
        
        if (error.error instanceof ErrorEvent) {
          errorMessage = error.error.message;
        } else {
          errorMessage = `Error: ${error.status}\n${error.message}`;
        }

        this.messageService.showError(errorMessage);
        return throwError(() => error);
      })
    );
  }
}
```

#### Backend
```java
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
        ResourceNotFoundException ex
    ) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
        ValidationException ex
    ) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
```

### Security Guidelines

1. JWT Configuration:
```java
@Configuration
public class JwtConfig {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(secret, expiration);
    }
}
```

2. CORS Configuration:
```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = 
            new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:4200"
        ));
        config.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS"
        ));
        config.setAllowedHeaders(Arrays.asList(
            "Authorization", "Content-Type"
        ));
        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}
```

### Deployment Guidelines

1. Frontend Deployment:
```bash
# Build for production
ng build --configuration=production

# Docker build
docker build -t financial-frontend .
```

2. Backend Deployment:
```bash
# Build JAR
mvn clean package

# Docker build
docker build -t financial-backend .
```

3. Docker Compose:
```yaml
version: '3.8'
services:
  frontend:
    image: financial-frontend:latest
    ports:
      - "80:80"
    depends_on:
      - backend

  backend:
    image: financial-backend:latest
    ports:
      - "8080:8080"
    depends_on:
      - postgres
      - redis

  postgres:
    image: postgres:13
    environment:
      POSTGRES_DB: financial
      POSTGRES_USER: financial
      POSTGRES_PASSWORD: secret

  redis:
    image: redis:6
```

### Monitoring and Logging

1. Logging Configuration:
```yaml
logging:
  level:
    root: INFO
    com.financial: DEBUG
    org.springframework.web: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

2. Metrics Configuration:
```java
@Configuration
public class MetricsConfig {
    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config()
            .commonTags("application", "financial-app");
    }
}
```

### Performance Optimization

1. Frontend:
- Use OnPush change detection
- Implement proper unsubscribe patterns
- Lazy load modules
- Implement caching strategies

2. Backend:
- Use appropriate database indexes
- Implement caching
- Use pagination for large datasets
- Optimize database queries

### Troubleshooting Guide

1. Common Issues:
- CORS errors
- JWT token issues
- Database connection problems
- Cache invalidation issues

2. Debug Tools:
- Browser DevTools
- Spring Boot Actuator
- Logging
- Database query analyzer

3. Performance Analysis:
- Angular Performance Profiler
- JVM Profiling
- Database Query Plans
- Network Analysis Tools

This developer guide provides a foundation for maintaining and extending the application. Keep it updated as new patterns and solutions emerge during development.