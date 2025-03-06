# Spring Boot Backend Architecture

This document outlines the architecture and structure for the Spring Boot backend application that will replace the Ruby on Rails backend.

## Overview

The Spring Boot backend will follow a layered architecture with:

1. **Controller Layer**: Handles HTTP requests and responses
2. **Service Layer**: Implements business logic
3. **Repository Layer**: Handles data access and persistence
4. **Domain Layer**: Contains entity classes and business objects
5. **DTO Layer**: Data transfer objects for API communication
6. **Configuration**: Security, database, and other configurations

## Application Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── financialapp/
│   │           ├── config/                # Configuration classes
│   │           │   ├── security/          # Security configuration
│   │           │   ├── database/          # Database configuration
│   │           │   ├── app/               # Application-specific configuration
│   │           │   └── swagger/           # API documentation
│   │           │
│   │           ├── controller/            # REST controllers
│   │           │   ├── AuthController.java
│   │           │   ├── CategoryController.java
│   │           │   ├── TransferController.java
│   │           │   ├── ReportController.java
│   │           │   ├── CurrencyController.java
│   │           │   ├── ExchangeController.java
│   │           │   ├── GoalController.java
│   │           │   └── UserController.java
│   │           │
│   │           ├── dto/                   # Data Transfer Objects
│   │           │   ├── request/           # Request DTOs
│   │           │   ├── response/          # Response DTOs
│   │           │   └── mapper/            # DTO-Entity mappers
│   │           │
│   │           ├── exception/             # Exception handling
│   │           │   ├── ApiError.java
│   │           │   ├── GlobalExceptionHandler.java
│   │           │   └── custom/            # Custom exceptions
│   │           │
│   │           ├── model/                 # Domain model (entities)
│   │           │   ├── User.java
│   │           │   ├── Category.java
│   │           │   ├── Transfer.java
│   │           │   ├── TransferItem.java
│   │           │   ├── Currency.java
│   │           │   ├── Exchange.java
│   │           │   ├── Goal.java
│   │           │   ├── Report.java
│   │           │   ├── enums/             # Enumeration types
│   │           │   └── embeddable/        # Embeddable value objects
│   │           │
│   │           ├── repository/            # Data repositories
│   │           │   ├── UserRepository.java
│   │           │   ├── CategoryRepository.java
│   │           │   ├── TransferRepository.java
│   │           │   ├── TransferItemRepository.java
│   │           │   ├── CurrencyRepository.java
│   │           │   ├── ExchangeRepository.java
│   │           │   ├── GoalRepository.java
│   │           │   └── ReportRepository.java
│   │           │
│   │           ├── service/               # Business services
│   │           │   ├── impl/              # Service implementations
│   │           │   ├── UserService.java
│   │           │   ├── CategoryService.java
│   │           │   ├── TransferService.java
│   │           │   ├── CurrencyService.java
│   │           │   ├── ExchangeService.java
│   │           │   ├── GoalService.java
│   │           │   └── ReportService.java
│   │           │
│   │           ├── security/              # Security components
│   │           │   ├── jwt/               # JWT components
│   │           │   ├── service/           # Security services
│   │           │   └── util/              # Security utilities
│   │           │
│   │           ├── util/                  # Utility classes
│   │           └── FinancialApplication.java  # Main application class
│   │
│   └── resources/
│       ├── application.yml                # Main application properties
│       ├── application-dev.yml            # Development environment properties
│       ├── application-prod.yml           # Production environment properties
│       └── db/                            # Database migration scripts
│           └── migration/                 # Flyway migrations
│
└── test/                                  # Test directories
    └── java/
        └── com/
            └── financialapp/
                ├── controller/            # Controller tests
                ├── service/               # Service tests
                └── repository/            # Repository tests
```

## Layer Details

### Controller Layer

The controller layer is responsible for handling HTTP requests and responses. It uses Spring Web annotations to define endpoints and maps DTOs to/from the service layer.

Example controller:

```java
@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(
            @RequestParam(required = false) CategoryType type,
            @RequestParam(defaultValue = "false") boolean includeBalance) {
        List<Category> categories = categoryService.findAllByUser(type, includeBalance);
        List<CategoryResponseDto> categoryDtos = categoryMapper.toDtoList(categories);
        return ResponseEntity.ok(categoryDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDetailResponseDto> getCategory(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        CategoryDetailResponseDto categoryDto = categoryMapper.toDetailDto(category);
        return ResponseEntity.ok(categoryDto);
    }

    @PostMapping
    public ResponseEntity<CategoryDetailResponseDto> createCategory(
            @Valid @RequestBody CategoryCreateDto categoryCreateDto) {
        Category category = categoryMapper.toEntity(categoryCreateDto);
        Category savedCategory = categoryService.create(category);
        CategoryDetailResponseDto categoryDto = categoryMapper.toDetailDto(savedCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(
            @PathVariable Long id, 
            @Valid @RequestBody CategoryUpdateDto categoryUpdateDto) {
        Category category = categoryMapper.toEntity(categoryUpdateDto);
        categoryService.update(id, category);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<CategoryBalanceDto> getCategoryBalance(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "false") boolean includeSubcategories,
            @RequestParam(required = false) BalanceAlgorithm algorithm) {
        CategoryBalance balance = categoryService.calculateBalance(id, startDate, endDate, includeSubcategories, algorithm);
        return ResponseEntity.ok(categoryMapper.toBalanceDto(balance));
    }
}
```

### Service Layer

The service layer implements business logic and orchestrates interactions between repositories and other services.

Example service:

```java
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final TransferItemRepository transferItemRepository;
    private final UserService userService;

    @Override
    public List<Category> findAllByUser(CategoryType type, boolean includeBalance) {
        User currentUser = userService.getCurrentUser();
        List<Category> categories;
        
        if (type != null) {
            categories = categoryRepository.findByUserIdAndCategoryTypeOrderByNameAsc(currentUser.getId(), type);
        } else {
            categories = categoryRepository.findByUserIdOrderByCategoryTypeAndNameAsc(currentUser.getId());
        }
        
        if (includeBalance) {
            calculateBalancesForCategories(categories);
        }
        
        return categories;
    }

    @Override
    public Category findById(Long id) {
        User currentUser = userService.getCurrentUser();
        return categoryRepository.findByIdAndUserId(id, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Override
    public Category create(Category category) {
        User currentUser = userService.getCurrentUser();
        category.setUser(currentUser);
        
        // Handle parent-child relationship
        if (category.getParentId() != null) {
            Category parent = findById(category.getParentId());
            category.setParent(parent);
            category.setCategoryType(parent.getCategoryType());
        }
        
        // Handle system category association
        if (category.getSystemCategoryId() != null) {
            // Logic to associate with system category
        }
        
        // Handle opening balance if provided
        if (category.getOpeningBalance() != null && category.getOpeningBalance().getAmount() != null) {
            category = categoryRepository.save(category);
            createOpeningBalanceTransfer(category, category.getOpeningBalance());
            return category;
        }
        
        return categoryRepository.save(category);
    }

    @Override
    public void update(Long id, Category categoryUpdate) {
        Category existingCategory = findById(id);
        
        // Update fields but preserve relationships and user
        existingCategory.setName(categoryUpdate.getName());
        existingCategory.setDescription(categoryUpdate.getDescription());
        
        // Handle parent change if needed
        if (categoryUpdate.getParentId() != null && 
            !categoryUpdate.getParentId().equals(existingCategory.getParentId())) {
            Category newParent = findById(categoryUpdate.getParentId());
            existingCategory.setParent(newParent);
        }
        
        categoryRepository.save(existingCategory);
    }

    @Override
    public void delete(Long id) {
        Category category = findById(id);
        
        // Check if top-level category
        if (category.getParent() == null) {
            throw new BusinessException("Cannot delete top-level category");
        }
        
        // Move children to parent
        List<Category> children = categoryRepository.findByParentId(id);
        Category parent = category.getParent();
        
        for (Category child : children) {
            child.setParent(parent);
            categoryRepository.save(child);
        }
        
        // Update transfer items to parent category
        transferItemRepository.updateCategoryForItems(id, parent.getId());
        
        // Delete the category
        categoryRepository.delete(category);
    }

    @Override
    public CategoryBalance calculateBalance(Long id, LocalDate startDate, LocalDate endDate, 
                                           boolean includeSubcategories, BalanceAlgorithm algorithm) {
        Category category = findById(id);
        User user = userService.getCurrentUser();
        
        algorithm = algorithm != null ? algorithm : user.getPreferences().getMultiCurrencyBalanceCalculatingAlgorithm();
        
        // Complex balance calculation logic based on the algorithm
        // ...
        
        return new CategoryBalance(/* calculated balance */);
    }

    private void createOpeningBalanceTransfer(Category category, OpeningBalance openingBalance) {
        // Logic to create an opening balance transfer
        // ...
    }

    private void calculateBalancesForCategories(List<Category> categories) {
        // Logic to calculate balances in bulk for a list of categories
        // ...
    }
}
```

### Repository Layer

The repository layer uses Spring Data JPA to define data access methods:

```java
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserIdOrderByCategoryTypeAndNameAsc(Long userId);
    
    List<Category> findByUserIdAndCategoryTypeOrderByNameAsc(Long userId, CategoryType type);
    
    Optional<Category> findByIdAndUserId(Long id, Long userId);
    
    List<Category> findByParentId(Long parentId);
    
    @Query("SELECT c FROM Category c WHERE c.user.id = :userId AND c.parent IS NULL AND c.categoryType = :type")
    Optional<Category> findTopLevelCategoryByType(@Param("userId") Long userId, @Param("type") CategoryType type);
    
    @Query("SELECT c FROM Category c WHERE c.lft >= :leftValue AND c.rgt <= :rightValue AND c.user.id = :userId AND c.categoryType = :type")
    List<Category> findDescendants(
            @Param("leftValue") Integer leftValue,
            @Param("rightValue") Integer rightValue,
            @Param("userId") Long userId,
            @Param("type") CategoryType type);
}
```

### Domain Model

The domain model consists of JPA entities that map to the database:

```java
@Entity
@Table(name = "categories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(nullable = false)
    private String name;
    
    private String description;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category_type_int")
    private CategoryType categoryType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
    
    @Column(name = "parent_id", insertable = false, updatable = false)
    private Long parentId;
    
    // Nested set model fields
    private Integer lft;
    private Integer rgt;
    
    @Column(name = "import_guid")
    private String importGuid;
    
    private Boolean imported;
    
    private String email;
    
    @Column(columnDefinition = "TEXT")
    private String bankinfo;
    
    private String bankAccountNumber;
    
    @Column(name = "loan_category")
    private Boolean loanCategory;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToMany
    @JoinTable(
        name = "categories_system_categories",
        joinColumns = @JoinColumn(name = "category_id"),
        inverseJoinColumns = @JoinColumn(name = "system_category_id")
    )
    private Set<SystemCategory> systemCategories = new HashSet<>();
    
    @OneToMany(mappedBy = "category")
    private List<TransferItem> transferItems = new ArrayList<>();
    
    @OneToMany(mappedBy = "category")
    private List<Goal> goals = new ArrayList<>();
    
    @Transient
    private OpeningBalance openingBalance;
    
    @Transient
    private Long systemCategoryId;
    
    @Transient
    private Map<String, BigDecimal> balance;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Methods from original Category class adapted as needed
    public boolean isTop() {
        return parent == null;
    }
    
    public boolean canBecomeLoanCategory() {
        return parent != null && categoryType == CategoryType.LOAN;
    }
    
    // Other business logic methods as needed
}
```

### DTO Layer

DTOs are used for data transfer between the API and service layer:

```java
@Data
@Builder
public class CategoryResponseDto {
    private Long id;
    private String name;
    private String description;
    private CategoryType categoryType;
    private Long parentId;
    private boolean hasChildren;
    private int level;
    private Map<String, BigDecimal> balance;
}

@Data
@Builder
public class CategoryDetailResponseDto {
    private Long id;
    private String name;
    private String description;
    private CategoryType categoryType;
    private Long parentId;
    private String path;
    private int level;
    private List<CategoryResponseDto> children;
}

@Data
@Builder
public class CategoryCreateDto {
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    
    private CategoryType categoryType;
    
    private Long parentId;
    
    private Long systemCategoryId;
    
    private OpeningBalanceDto openingBalance;
}

@Data
@Builder
public class CategoryUpdateDto {
    @NotBlank(message = "Name is required")
    private String name;
    
    private String description;
    
    private Long parentId;
}

@Data
@Builder
public class CategoryBalanceDto {
    private Map<String, BigDecimal> balance;
    private BigDecimal balanceInDefaultCurrency;
    private String defaultCurrency;
}
```

### Mapper Layer

Mappers are used to convert between entities and DTOs:

```java
@Mapper(componentModel = "spring", uses = {CurrencyMapper.class})
public interface CategoryMapper {
    @Mapping(target = "hasChildren", expression = "java(determineHasChildren(category))")
    @Mapping(target = "level", expression = "java(calculateLevel(category))")
    CategoryResponseDto toDto(Category category);
    
    List<CategoryResponseDto> toDtoList(List<Category> categories);
    
    @Mapping(target = "path", expression = "java(buildPath(category))")
    @Mapping(target = "level", expression = "java(calculateLevel(category))")
    @Mapping(target = "children", expression = "java(mapChildren(category))")
    CategoryDetailResponseDto toDetailDto(Category category);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "lft", ignore = true)
    @Mapping(target = "rgt", ignore = true)
    @Mapping(target = "importGuid", ignore = true)
    @Mapping(target = "imported", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "systemCategories", ignore = true)
    @Mapping(target = "transferItems", ignore = true)
    @Mapping(target = "goals", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "parent", ignore = true)
    Category toEntity(CategoryCreateDto dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "categoryType", ignore = true)
    @Mapping(target = "lft", ignore = true)
    @Mapping(target = "rgt", ignore = true)
    @Mapping(target = "importGuid", ignore = true)
    @Mapping(target = "imported", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "bankinfo", ignore = true)
    @Mapping(target = "bankAccountNumber", ignore = true)
    @Mapping(target = "loanCategory", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "systemCategories", ignore = true)
    @Mapping(target = "transferItems", ignore = true)
    @Mapping(target = "goals", ignore = true)
    @Mapping(target = "balance", ignore = true)
    @Mapping(target = "systemCategoryId", ignore = true)
    @Mapping(target = "openingBalance", ignore = true)
    @Mapping(target = "parent", ignore = true)
    void updateEntityFromDto(CategoryUpdateDto dto, @MappingTarget Category category);
    
    CategoryBalanceDto toBalanceDto(CategoryBalance balance);
    
    default boolean determineHasChildren(Category category) {
        // Logic to determine if category has children
        return false; // placeholder
    }
    
    default int calculateLevel(Category category) {
        // Logic to calculate category level
        return 0; // placeholder
    }
    
    default String buildPath(Category category) {
        // Logic to build category path
        return ""; // placeholder
    }
    
    default List<CategoryResponseDto> mapChildren(Category category) {
        // Logic to map children
        return Collections.emptyList(); // placeholder
    }
}
```

## Security Architecture

The application will use Spring Security with JWT authentication:

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
```

### JWT Implementation

```java
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        jwt = authHeader.substring(7);
        username = jwtService.extractUsername(jwt);
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
```

## Exception Handling

Centralized exception handling:

```java
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                LocalDateTime.now()
        );
        return buildResponseEntity(apiError);
    }
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(
            BusinessException ex, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                LocalDateTime.now()
        );
        return buildResponseEntity(apiError);
    }
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                "Validation error",
                LocalDateTime.now(),
                errors
        );
        return buildResponseEntity(apiError);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(
            Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                LocalDateTime.now()
        );
        return buildResponseEntity(apiError);
    }
    
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
```

## Database Migration

Using Flyway for database migrations:

```sql
-- V1__Initial_Schema.sql
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  login VARCHAR(40) UNIQUE,
  name VARCHAR(100) DEFAULT '',
  email VARCHAR(100),
  password VARCHAR(60) NOT NULL,
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  activation_code VARCHAR(40),
  activated_at TIMESTAMP,
  transaction_amount_limit_type_int INT NOT NULL DEFAULT 2,
  transaction_amount_limit_value INT,
  include_transactions_from_subcategories BOOLEAN NOT NULL DEFAULT FALSE,
  multi_currency_balance_calculating_algorithm_int INT NOT NULL DEFAULT 0,
  default_currency_id BIGINT NOT NULL DEFAULT 1,
  invert_saldo_for_income BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE currencies (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  symbol VARCHAR(255) NOT NULL,
  long_symbol VARCHAR(255) NOT NULL,
  name VARCHAR(255) NOT NULL,
  long_name VARCHAR(255) NOT NULL,
  user_id BIGINT,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Add foreign key constraint after currencies table is created
ALTER TABLE users 
ADD CONSTRAINT fk_users_default_currency 
FOREIGN KEY (default_currency_id) REFERENCES currencies(id);

-- More tables, constraints, and indexes would follow
```

## Testing Strategy

The application will use JUnit 5 and Mockito for testing, with a focus on unit tests for services and integration tests for repositories and controllers.

Example service test:

```java
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    
    @Mock
    private TransferItemRepository transferItemRepository;
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private CategoryServiceImpl categoryService;
    
    private User testUser;
    private Category testCategory;
    
    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .login("testuser")
                .email("test@example.com")
                .build();
        
        testCategory = Category.builder()
                .id(1L)
                .name("Test Category")
                .description("Test Description")
                .categoryType(CategoryType.EXPENSE)
                .user(testUser)
                .build();
    }
    
    @Test
    void findById_whenCategoryExists_returnCategory() {
        // given
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(categoryRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(testCategory));
        
        // when
        Category result = categoryService.findById(1L);
        
        // then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Category", result.getName());
        
        verify(userService).getCurrentUser();
        verify(categoryRepository).findByIdAndUserId(1L, 1L);
    }
    
    @Test
    void findById_whenCategoryNotExists_throwException() {
        // given
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(categoryRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());
        
        // when & then
        assertThrows(ResourceNotFoundException.class, () -> categoryService.findById(1L));
        
        verify(userService).getCurrentUser();
        verify(categoryRepository).findByIdAndUserId(1L, 1L);
    }
    
    // More tests for other service methods
}
```

Example controller test:

```java
@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private CategoryService categoryService;
    
    @MockBean
    private CategoryMapper categoryMapper;
    
    private Category testCategory;
    private CategoryResponseDto testCategoryDto;
    
    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id(1L)
                .name("Test Category")
                .description("Test Description")
                .categoryType(CategoryType.EXPENSE)
                .build();
        
        testCategoryDto = CategoryResponseDto.builder()
                .id(1L)
                .name("Test Category")
                .description("Test Description")
                .categoryType(CategoryType.EXPENSE)
                .build();
    }
    
    @Test
    void getCategory_whenCategoryExists_returnCategoryDto() throws Exception {
        // given
        when(categoryService.findById(1L)).thenReturn(testCategory);
        when(categoryMapper.toDetailDto(testCategory)).thenReturn(
                CategoryDetailResponseDto.builder()
                        .id(1L)
                        .name("Test Category")
                        .description("Test Description")
                        .categoryType(CategoryType.EXPENSE)
                        .build()
        );
        
        // when & then
        mockMvc.perform(get("/api/v1/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Category"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.categoryType").value("EXPENSE"));
        
        verify(categoryService).findById(1L);
        verify(categoryMapper).toDetailDto(testCategory);
    }
    
    // More tests for other controller methods
}
```