# Spring Boot Backend Architecture

This document details the architecture, components, and organization of the Spring Boot backend for the Financial Management System.

## Project Structure

The Spring Boot application follows a layered architecture:

```
src/
├── main/
│   ├── java/
│   │   └── com/financialmanager/
│   │       ├── config/
│   │       ├── controller/
│   │       ├── dto/
│   │       ├── exception/
│   │       ├── model/
│   │       ├── repository/
│   │       ├── security/
│   │       ├── service/
│   │       └── FinancialManagerApplication.java
│   └── resources/
│       ├── db/migration/
│       └── application.properties
└── test/
    └── java/...
```

## Core Components

### Models (Entities)

The JPA entities represent the domain model of the application:

Example User Entity:
```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "default_currency_id")
    private Currency defaultCurrency;
    
    @Column(name = "multi_currency_balance_algorithm")
    @Enumerated(EnumType.STRING)
    private CurrencyBalanceAlgorithm balanceAlgorithm;
    
    @Column(name = "invert_saldo_for_income")
    private boolean invertSaldoForIncome = true;
    
    // Getters, setters, etc.
}
```

Example Category Entity with self-referencing relationship:
```java
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "category_type", nullable = false)
    private CategoryType categoryType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "loan_category")
    private boolean loanCategory;
    
    @Column(name = "bank_account_number")
    private String bankAccountNumber;
    
    // For hierarchical data using the nested set model
    private int lft;
    private int rgt;
    
    // Getters, setters, etc.
}
```

### Repositories

The repositories provide data access using Spring Data JPA:

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserIdAndParentIsNull(Long userId);
    List<Category> findByUserIdAndCategoryType(Long userId, CategoryType categoryType);
    Optional<Category> findByIdAndUserId(Long id, Long userId);
}

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findByUserIdOrderByDayDescIdDesc(Long userId, Pageable pageable);
    List<Transfer> findByUserIdAndDayBetweenOrderByDayDescIdDesc(
        Long userId, LocalDate startDate, LocalDate endDate);
}
```

### DTOs (Data Transfer Objects)

DTOs handle data transfer between the API and service layers:

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private CategoryType categoryType;
    private Long parentId;
    private boolean loanCategory;
    private String bankAccountNumber;
    private List<CategoryDTO> children;
    
    // Constructors, getters, setters
}

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO {
    private Long id;
    private String description;
    private LocalDate day;
    private List<TransferItemDTO> items;
    private List<ConversionDTO> conversions;
    
    // Constructors, getters, setters
}

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferItemDTO {
    private Long id;
    private String description;
    private Long categoryId;
    private Long currencyId;
    private BigDecimal value;
    
    // Constructors, getters, setters
}
```

### Services

Services implement the business logic of the application:

```java
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    
    public List<Category> getUserCategories() {
        User currentUser = userService.getCurrentUser();
        return categoryRepository.findByUserIdAndParentIsNull(currentUser.getId());
    }
    
    public Category getCategory(Long id) {
        User currentUser = userService.getCurrentUser();
        return categoryRepository.findByIdAndUserId(id, currentUser.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
    
    @Transactional
    public Category createCategory(CategoryDTO categoryDTO) {
        User currentUser = userService.getCurrentUser();
        
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setCategoryType(categoryDTO.getCategoryType());
        category.setUser(currentUser);
        
        if (categoryDTO.getParentId() != null) {
            Category parent = getCategory(categoryDTO.getParentId());
            category.setParent(parent);
        }
        
        return categoryRepository.save(category);
    }
    
    // Additional methods for CRUD operations, balance calculations, etc.
}

@Service
@RequiredArgsConstructor
public class TransferService {
    private final TransferRepository transferRepository;
    private final TransferItemRepository transferItemRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    
    public List<Transfer> getRecentTransfers(int limit) {
        User currentUser = userService.getCurrentUser();
        return transferRepository.findByUserIdOrderByDayDescIdDesc(
            currentUser.getId(), 
            PageRequest.of(0, limit)
        );
    }
    
    @Transactional
    public Transfer createTransfer(TransferDTO transferDTO) {
        User currentUser = userService.getCurrentUser();
        
        // Validate transfer balance
        validateTransferBalance(transferDTO);
        
        Transfer transfer = new Transfer();
        transfer.setDescription(transferDTO.getDescription());
        transfer.setDay(transferDTO.getDay());
        transfer.setUser(currentUser);
        
        // Save transfer first to get ID
        transfer = transferRepository.save(transfer);
        
        // Create and associate transfer items
        List<TransferItem> items = transferDTO.getItems().stream()
            .map(itemDTO -> createTransferItem(itemDTO, transfer))
            .collect(Collectors.toList());
        
        transfer.setItems(items);
        
        // Handle currency conversions if needed
        if (transferDTO.getConversions() != null && !transferDTO.getConversions().isEmpty()) {
            handleConversions(transferDTO.getConversions(), transfer);
        }
        
        return transferRepository.save(transfer);
    }
    
    private void validateTransferBalance(TransferDTO transferDTO) {
        BigDecimal sum = transferDTO.getItems().stream()
            .map(TransferItemDTO::getValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
            
        if (sum.abs().compareTo(new BigDecimal("0.001")) > 0) {
            throw new BusinessException("Transfer items must balance to zero");
        }
    }
    
    // Additional methods for transfer management
}
```

### Controllers

Controllers handle HTTP requests and delegate to services:

```java
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;
    
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        List<Category> categories = categoryService.getUserCategories();
        List<CategoryDTO> categoryDTOs = categories.stream()
            .map(categoryMapper::toDTO)
            .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        Category category = categoryService.getCategory(id);
        return ResponseEntity.ok(categoryMapper.toDTO(category));
    }
    
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        Category createdCategory = categoryService.createCategory(categoryDTO);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(categoryMapper.toDTO(createdCategory));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id, 
            @Valid @RequestBody CategoryDTO categoryDTO) {
        categoryDTO.setId(id);
        Category updatedCategory = categoryService.updateCategory(categoryDTO);
        return ResponseEntity.ok(categoryMapper.toDTO(updatedCategory));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/tree")
    public ResponseEntity<List<CategoryDTO>> getCategoryTree() {
        List<CategoryDTO> categoryTree = categoryService.getUserCategoryTree();
        return ResponseEntity.ok(categoryTree);
    }
    
    @GetMapping("/{id}/balance")
    public ResponseEntity<Map<String, BigDecimal>> getCategoryBalance(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean includeSubcategories) {
        Map<String, BigDecimal> balance = categoryService.getCategoryBalance(
            id, 
            includeSubcategories != null ? includeSubcategories : false
        );
        return ResponseEntity.ok(balance);
    }
}
```

### Security Configuration

The application uses Spring Security with JWT for authentication:

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors().and().csrf().disable()
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint)
            )
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/auth/**").permitAll()
            .antMatchers("/api/docs/**").permitAll()
            .anyRequest().authenticated();
            
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
```

JWT Authentication Filter:

```java
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Long userId = tokenProvider.getUserIdFromToken(jwt);
                
                UserDetails userDetails = userDetailsService.loadUserById(userId);
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                    
                authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));
                    
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
```

## Exception Handling

Centralized exception handling with a global exception handler:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(
            HttpStatus.NOT_FOUND, 
            ex.getMessage(), 
            request.getDescription(false));
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(
            BusinessException ex, WebRequest request) {
        ApiError apiError = new ApiError(
            HttpStatus.CONFLICT, 
            ex.getMessage(), 
            request.getDescription(false));
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.toList());
            
        ApiError apiError = new ApiError(
            HttpStatus.BAD_REQUEST,
            "Validation error",
            request.getDescription(false),
            errors);
            
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaughtException(
            Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred",
            request.getDescription(false));
            
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private LocalDateTime timestamp = LocalDateTime.now();
    private HttpStatus status;
    private String message;
    private String path;
    private List<String> details;
    
    public ApiError(HttpStatus status, String message, String path) {
        this.status = status;
        this.message = message;
        this.path = path;
        this.details = new ArrayList<>();
    }
}
```

## Database Management

The application uses Flyway for database migrations:

```sql
-- V1__create_initial_schema.sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    default_currency_id BIGINT,
    multi_currency_balance_algorithm VARCHAR(50) NOT NULL DEFAULT 'SHOW_ALL_CURRENCIES',
    invert_saldo_for_income BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE currencies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL,
    long_symbol VARCHAR(10) NOT NULL,
    name VARCHAR(100) NOT NULL,
    long_name VARCHAR(100) NOT NULL,
    user_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

ALTER TABLE users
ADD CONSTRAINT fk_default_currency 
FOREIGN KEY (default_currency_id) REFERENCES currencies(id);

CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    category_type VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    parent_id BIGINT,
    lft INT NOT NULL,
    rgt INT NOT NULL,
    loan_category BOOLEAN DEFAULT FALSE,
    bank_account_number VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES categories(id)
);

-- Additional tables for transfers, goals, etc.
```

## Testing Strategy

The backend uses a comprehensive testing approach:

### Unit Tests

```java
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private CategoryService categoryService;
    
    @Test
    void createCategory_WithValidData_ShouldCreateCategory() {
        // Arrange
        User currentUser = new User();
        currentUser.setId(1L);
        
        CategoryDTO dto = new CategoryDTO();
        dto.setName("Groceries");
        dto.setCategoryType(CategoryType.EXPENSE);
        
        when(userService.getCurrentUser()).thenReturn(currentUser);
        
        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName(dto.getName());
        savedCategory.setCategoryType(dto.getCategoryType());
        
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);
        
        // Act
        Category result = categoryService.createCategory(dto);
        
        // Assert
        assertNotNull(result);
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getCategoryType(), result.getCategoryType());
        assertEquals(currentUser, result.getUser());
        
        verify(categoryRepository).save(any(Category.class));
    }
}
```

### Integration Tests

```java
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    private User testUser;
    
    @BeforeEach
    public void setup() {
        // Create test user
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword(new BCryptPasswordEncoder().encode("password"));
        userRepository.save(testUser);
        
        // Setup JWT authentication
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getUserIdFromToken(anyString())).thenReturn(testUser.getId());
    }
    
    @Test
    public void createCategory_WithValidData_ReturnsCreatedCategory() throws Exception {
        // Arrange
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Test Category");
        categoryDTO.setCategoryType(CategoryType.EXPENSE);
        
        // Act & Assert
        mockMvc.perform(post("/api/categories")
            .header("Authorization", "Bearer fake-jwt-token")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(categoryDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(categoryDTO.getName()))
            .andExpect(jsonPath("$.categoryType").value(categoryDTO.getCategoryType().name()));
    }
}
```

## Application Configuration

The application uses Spring's configuration mechanisms:

```properties
# application.properties
spring.application.name=financial-manager
server.port=8080

# Database configuration
spring.datasource.url=jdbc:mysql://localhost:3306/financial_manager?useSSL=false
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Flyway configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true

# JWT Configuration
app.jwt.secret=YOUR_JWT_SECRET_KEY
app.jwt.expirationInMs=86400000
app.jwt.refreshExpirationInMs=604800000

# Jackson configuration
spring.jackson.serialization.WRITE_DATES_AS_TIMESTAMPS=false
spring.jackson.date-format=yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
spring.jackson.time-zone=UTC

# Logging
logging.level.com.financialmanager=INFO
logging.level.org.springframework.web=INFO
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```