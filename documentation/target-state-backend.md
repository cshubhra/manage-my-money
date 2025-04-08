# Target State Backend Architecture (Spring Boot)

## Overview
The backend will be implemented as a modern Spring Boot application following a layered architecture and RESTful principles.

## Architecture Diagram

```mermaid
graph TB
    subgraph "Spring Boot Application"
        subgraph "Web Layer"
            UC[User Controller]
            TC[Transfer Controller]
            CC[Category Controller]
            GC[Goal Controller]
            RC[Report Controller]
            EC[Exchange Controller]
        end

        subgraph "Security Layer"
            JWT[JWT Filter]
            Auth[Authentication]
            ACL[Access Control]
        end

        subgraph "Service Layer"
            US[User Service]
            TS[Transfer Service]
            CS[Category Service]
            GS[Goal Service]
            RS[Report Service]
            ES[Exchange Service]
        end

        subgraph "Repository Layer"
            UR[User Repository]
            TR[Transfer Repository]
            CR[Category Repository]
            GR[Goal Repository]
            ER[Exchange Repository]
        end

        subgraph "Domain Layer"
            User[User Entity]
            Transfer[Transfer Entity]
            Category[Category Entity]
            Goal[Goal Entity]
            Exchange[Exchange Entity]
        end
    end

    subgraph "External Systems"
        DB[(Database)]
        Cache[(Redis Cache)]
        MQ[Message Queue]
    end

    Web Layer --> Security Layer
    Security Layer --> Service Layer
    Service Layer --> Repository Layer
    Repository Layer --> Domain Layer
    Domain Layer --> DB
    Service Layer --> Cache
    Service Layer --> MQ
```

## Project Structure

```plaintext
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── financial/
│   │           ├── Application.java
│   │           ├── config/
│   │           │   ├── SecurityConfig.java
│   │           │   ├── SwaggerConfig.java
│   │           │   └── CacheConfig.java
│   │           ├── controller/
│   │           │   ├── UserController.java
│   │           │   ├── TransferController.java
│   │           │   └── ...
│   │           ├── service/
│   │           │   ├── UserService.java
│   │           │   ├── TransferService.java
│   │           │   └── ...
│   │           ├── repository/
│   │           │   ├── UserRepository.java
│   │           │   ├── TransferRepository.java
│   │           │   └── ...
│   │           ├── model/
│   │           │   ├── entity/
│   │           │   │   ├── User.java
│   │           │   │   ├── Transfer.java
│   │           │   │   └── ...
│   │           │   └── dto/
│   │           │       ├── UserDTO.java
│   │           │       ├── TransferDTO.java
│   │           │       └── ...
│   │           ├── security/
│   │           │   ├── JwtTokenProvider.java
│   │           │   └── SecurityUser.java
│   │           └── exception/
│   │               ├── GlobalExceptionHandler.java
│   │               └── CustomException.java
│   └── resources/
│       ├── application.yml
│       └── db/migration/
└── test/
```

## Implementation Details

### 1. Domain Layer (Entities)

```java
@Entity
@Table(name = "transfers")
@Data
@NoArgsConstructor
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL)
    private List<TransferItem> items;

    private BigDecimal amount;
    private LocalDateTime date;
    private String description;

    @Version
    private Long version;
}
```

### 2. Repository Layer

```java
@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    @Query("SELECT t FROM Transfer t WHERE t.user.id = :userId")
    Page<Transfer> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT t FROM Transfer t " +
           "WHERE t.user.id = :userId " +
           "AND t.date BETWEEN :startDate AND :endDate")
    List<Transfer> findByUserIdAndDateBetween(
        @Param("userId") Long userId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );
}
```

### 3. Service Layer

```java
@Service
@Transactional
@Slf4j
public class TransferService {
    private final TransferRepository transferRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public TransferService(
        TransferRepository transferRepository,
        UserService userService,
        CategoryService categoryService
    ) {
        this.transferRepository = transferRepository;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    public TransferDTO createTransfer(TransferDTO transferDTO) {
        User user = userService.getCurrentUser();
        Transfer transfer = new Transfer();
        // mapping and validation logic
        return convertToDTO(transferRepository.save(transfer));
    }

    @Cacheable(value = "transfers", key = "#userId")
    public Page<TransferDTO> getUserTransfers(Long userId, Pageable pageable) {
        return transferRepository.findByUserId(userId, pageable)
            .map(this::convertToDTO);
    }
}
```

### 4. Controller Layer

```java
@RestController
@RequestMapping("/api/v1/transfers")
@Tag(name = "Transfer Management", description = "Endpoints for managing transfers")
public class TransferController {
    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    @Operation(summary = "Create a new transfer")
    public ResponseEntity<TransferDTO> createTransfer(
        @Valid @RequestBody TransferDTO transferDTO
    ) {
        return ResponseEntity.ok(transferService.createTransfer(transferDTO));
    }

    @GetMapping
    @Operation(summary = "Get user transfers")
    public ResponseEntity<Page<TransferDTO>> getUserTransfers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(transferService.getUserTransfers(
            SecurityUtils.getCurrentUserId(),
            PageRequest.of(page, size)
        ));
    }
}
```

### 5. Security Configuration

```java
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/v1/auth/**").permitAll()
            .antMatchers("/api/v1/**").authenticated()
            .and()
            .apply(new JwtConfigurer(jwtTokenProvider));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 6. Exception Handling

```java
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(
        CustomException ex
    ) {
        ErrorResponse error = new ErrorResponse(
            ex.getStatus(),
            ex.getMessage()
        );
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
        ConstraintViolationException ex
    ) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Validation failed: " + ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
```

### 7. Caching Configuration

```java
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {
    @Bean
    public CacheManager cacheManager() {
        RedisCacheConfiguration config = RedisCacheConfiguration
            .defaultCacheConfig()
            .entryTtl(Duration.ofHours(1));

        return RedisCacheManager.builder(
            RedisCacheWriter.nonLockingRedisCacheWriter(
                redisConnectionFactory()
            )
        )
        .cacheDefaults(config)
        .build();
    }
}
```

### 8. Logging Configuration

```yaml
logging:
  level:
    root: INFO
    com.financial: DEBUG
    org.springframework.web: INFO
    org.hibernate: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

### 9. Database Migration

```sql
-- V1__init.sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE transfers (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    description VARCHAR(255),
    date TIMESTAMP NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## Performance Considerations

1. Caching Strategy
- Redis caching for frequently accessed data
- Cache invalidation on updates
- Configurable TTL for different cache types

2. Database Optimization
- Proper indexing
- Query optimization
- Connection pooling
- Lazy loading where appropriate

3. API Performance
- Pagination for large datasets
- DTOs to limit data transfer
- Compression for responses
- API versioning

## Monitoring and Metrics

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

## Testing Strategy

```java
@SpringBootTest
class TransferServiceTest {
    @MockBean
    private TransferRepository transferRepository;

    @Autowired
    private TransferService transferService;

    @Test
    void createTransfer_ValidData_Success() {
        // Test implementation
    }
}
```

This architecture provides a robust, scalable, and maintainable backend that will serve the Angular frontend effectively.