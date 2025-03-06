# Spring Boot Application Structure

This document outlines the structure of our Spring Boot backend application for the financial management system.

## Package Structure

```
com.finance.manager/
├── config/             # Configuration classes
│   ├── SecurityConfig.java
│   ├── JwtConfig.java
│   ├── WebConfig.java
│   └── ...
├── controller/         # REST API controllers
│   ├── AuthController.java
│   ├── CategoryController.java
│   ├── TransferController.java
│   ├── CurrencyController.java
│   ├── ExchangeController.java
│   ├── ReportController.java
│   └── UserController.java
├── dto/                # Data Transfer Objects
│   ├── request/        # Request DTOs
│   └── response/       # Response DTOs
├── entity/             # JPA entity classes
│   ├── User.java
│   ├── Category.java
│   ├── Transfer.java
│   ├── TransferItem.java
│   ├── Currency.java
│   ├── Exchange.java
│   ├── Conversion.java
│   ├── Goal.java
│   ├── Report.java
│   └── ...
├── repository/         # Spring Data JPA repositories
│   ├── UserRepository.java
│   ├── CategoryRepository.java
│   ├── TransferRepository.java
│   ├── TransferItemRepository.java
│   ├── CurrencyRepository.java
│   ├── ExchangeRepository.java
│   └── ...
├── service/            # Business logic services
│   ├── UserService.java
│   ├── CategoryService.java
│   ├── TransferService.java
│   ├── CurrencyService.java
│   ├── ExchangeService.java
│   ├── ReportService.java
│   └── ...
├── security/           # Security-related classes
│   ├── JwtTokenProvider.java
│   ├── JwtTokenFilter.java
│   ├── UserDetailsServiceImpl.java
│   └── ...
├── exception/          # Custom exceptions and handlers
│   ├── ResourceNotFoundException.java
│   ├── GlobalExceptionHandler.java
│   └── ...
└── util/               # Utility classes
```

## Core Entity Classes

### User Entity

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, length = 40)
    private String login;
    
    @Column(length = 100)
    private String name;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(name = "crypted_password")
    private String password;
    
    private String salt;
    
    @Column(name = "remember_token")
    private String rememberToken;
    
    @Column(name = "remember_token_expires_at")
    private LocalDateTime rememberTokenExpiresAt;
    
    @Column(name = "activation_code")
    private String activationCode;
    
    @Column(name = "activated_at")
    private LocalDateTime activatedAt;
    
    @Column(name = "transaction_amount_limit_type_int")
    private Integer transactionAmountLimitTypeInt;
    
    @Column(name = "transaction_amount_limit_value")
    private Integer transactionAmountLimitValue;
    
    @Column(name = "include_transactions_from_subcategories")
    private Boolean includeTransactionsFromSubcategories;
    
    @Column(name = "multi_currency_balance_calculating_algorithm_int")
    private Integer multiCurrencyBalanceCalculatingAlgorithmInt;
    
    @ManyToOne
    @JoinColumn(name = "default_currency_id")
    private Currency defaultCurrency;
    
    @Column(name = "invert_saldo_for_income")
    private Boolean invertSaldoForIncome;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Category> categories;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Transfer> transfers;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Currency> currencies;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Exchange> exchanges;
    
    // Getters and setters
}
```

### Category Entity

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
    
    @Column(name = "category_type_int")
    private Integer categoryTypeInt;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> children;
    
    // Nested set pattern fields
    private Integer lft;
    private Integer rgt;
    
    @Column(name = "import_guid")
    private String importGuid;
    
    private Boolean imported;
    
    private String email;
    
    private String bankinfo;
    
    @Column(name = "bank_account_number")
    private String bankAccountNumber;
    
    @Column(name = "loan_category")
    private Boolean loanCategory;
    
    @OneToMany(mappedBy = "category")
    private List<TransferItem> transferItems;
    
    // Getters and setters
}
```

### Transfer Entity

```java
@Entity
@Table(name = "transfers")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private LocalDate day;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(name = "import_guid")
    private String importGuid;
    
    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL)
    private List<TransferItem> transferItems;
    
    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL)
    private List<Conversion> conversions;
    
    // Getters and setters
}
```

### TransferItem Entity

```java
@Entity
@Table(name = "transfer_items")
public class TransferItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal value;
    
    @ManyToOne
    @JoinColumn(name = "transfer_id", nullable = false)
    private Transfer transfer;
    
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @ManyToOne
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;
    
    @Column(name = "import_guid")
    private String importGuid;
    
    // Getters and setters
}
```

### Currency Entity

```java
@Entity
@Table(name = "currencies")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String symbol;
    
    @Column(name = "long_symbol", nullable = false)
    private String longSymbol;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "long_name", nullable = false)
    private String longName;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "currency")
    private List<TransferItem> transferItems;
    
    // Getters and setters
}
```

### Exchange Entity

```java
@Entity
@Table(name = "exchanges")
public class Exchange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "left_currency_id", nullable = false)
    private Currency leftCurrency;
    
    @ManyToOne
    @JoinColumn(name = "right_currency_id", nullable = false)
    private Currency rightCurrency;
    
    @Column(name = "left_to_right", nullable = false, precision = 8, scale = 4)
    private BigDecimal leftToRight;
    
    @Column(name = "right_to_left", nullable = false, precision = 8, scale = 4)
    private BigDecimal rightToLeft;
    
    private LocalDate day;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "exchange")
    private List<Conversion> conversions;
    
    // Getters and setters
}
```

### Conversion Entity

```java
@Entity
@Table(name = "conversions")
public class Conversion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "transfer_id")
    private Transfer transfer;
    
    @ManyToOne
    @JoinColumn(name = "exchange_id")
    private Exchange exchange;
    
    // Getters and setters
}
```

## Key Service Classes

### CategoryService

```java
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    
    @Autowired
    public CategoryService(CategoryRepository categoryRepository, UserService userService) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }
    
    public List<Category> getAllCategoriesForUser() {
        User currentUser = userService.getCurrentUser();
        return categoryRepository.findAllByUserOrderByCategoryTypeIntAscLftAsc(currentUser);
    }
    
    public List<Category> getTopCategoriesForUser() {
        User currentUser = userService.getCurrentUser();
        return categoryRepository.findByUserAndParentIsNull(currentUser);
    }
    
    public Category getCategory(Long id) {
        User currentUser = userService.getCurrentUser();
        return categoryRepository.findByIdAndUser(id, currentUser)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }
    
    @Transactional
    public Category createCategory(CategoryRequest request) {
        User currentUser = userService.getCurrentUser();
        
        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setCategoryTypeInt(request.getCategoryTypeInt());
        category.setUser(currentUser);
        
        if (request.getParentId() != null) {
            Category parent = categoryRepository.findByIdAndUser(request.getParentId(), currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Parent category not found"));
            category.setParent(parent);
            category.setCategoryTypeInt(parent.getCategoryTypeInt());
        }
        
        Category savedCategory = categoryRepository.save(category);
        
        // Handle opening balance if provided
        if (request.getOpeningBalance() != null && request.getOpeningBalanceCurrencyId() != null) {
            createOpeningBalanceTransfer(savedCategory, request.getOpeningBalance(), request.getOpeningBalanceCurrencyId());
        }
        
        // Handle nested set updates
        updateNestedSet(savedCategory);
        
        return savedCategory;
    }
    
    @Transactional
    public Category updateCategory(Long id, CategoryRequest request) {
        // Implementation for updating a category
        // ...
    }
    
    @Transactional
    public void deleteCategory(Long id) {
        // Implementation for deleting a category
        // ...
    }
    
    private void createOpeningBalanceTransfer(Category category, BigDecimal amount, Long currencyId) {
        // Implementation for creating an opening balance transfer
        // ...
    }
    
    private void updateNestedSet(Category category) {
        // Implementation for maintaining nested set pattern
        // ...
    }
}
```

### TransferService

```java
@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private final TransferItemRepository transferItemRepository;
    private final CategoryRepository categoryRepository;
    private final CurrencyRepository currencyRepository;
    private final UserService userService;
    
    @Autowired
    public TransferService(
        TransferRepository transferRepository,
        TransferItemRepository transferItemRepository,
        CategoryRepository categoryRepository, 
        CurrencyRepository currencyRepository,
        UserService userService
    ) {
        this.transferRepository = transferRepository;
        this.transferItemRepository = transferItemRepository;
        this.categoryRepository = categoryRepository;
        this.currencyRepository = currencyRepository;
        this.userService = userService;
    }
    
    public Page<Transfer> getTransfersForUser(Pageable pageable, LocalDate startDate, LocalDate endDate, Long categoryId) {
        User currentUser = userService.getCurrentUser();
        
        if (startDate != null && endDate != null) {
            if (categoryId != null) {
                Category category = categoryRepository.findByIdAndUser(categoryId, currentUser)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
                
                return transferRepository.findByCategoryAndDateRange(category, startDate, endDate, pageable);
            } else {
                return transferRepository.findByUserAndDayBetween(currentUser, startDate, endDate, pageable);
            }
        } else {
            if (categoryId != null) {
                Category category = categoryRepository.findByIdAndUser(categoryId, currentUser)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
                
                return transferRepository.findByCategory(category, pageable);
            } else {
                return transferRepository.findByUser(currentUser, pageable);
            }
        }
    }
    
    public Transfer getTransfer(Long id) {
        User currentUser = userService.getCurrentUser();
        return transferRepository.findByIdAndUser(id, currentUser)
            .orElseThrow(() -> new ResourceNotFoundException("Transfer not found"));
    }
    
    @Transactional
    public Transfer createTransfer(TransferRequest request) {
        User currentUser = userService.getCurrentUser();
        
        Transfer transfer = new Transfer();
        transfer.setDescription(request.getDescription());
        transfer.setDay(request.getDay());
        transfer.setUser(currentUser);
        
        Transfer savedTransfer = transferRepository.save(transfer);
        
        // Create transfer items
        List<TransferItem> items = new ArrayList<>();
        for (TransferItemRequest itemRequest : request.getTransferItems()) {
            TransferItem item = new TransferItem();
            item.setDescription(itemRequest.getDescription());
            item.setValue(itemRequest.getValue());
            item.setTransfer(savedTransfer);
            
            Category category = categoryRepository.findByIdAndUser(itemRequest.getCategoryId(), currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            item.setCategory(category);
            
            Currency currency = currencyRepository.findById(itemRequest.getCurrencyId())
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found"));
            item.setCurrency(currency);
            
            items.add(item);
        }
        
        transferItemRepository.saveAll(items);
        savedTransfer.setTransferItems(items);
        
        // Validate balance
        validateTransferBalance(savedTransfer);
        
        return savedTransfer;
    }
    
    @Transactional
    public Transfer updateTransfer(Long id, TransferRequest request) {
        // Implementation for updating a transfer
        // ...
    }
    
    @Transactional
    public void deleteTransfer(Long id) {
        // Implementation for deleting a transfer
        // ...
    }
    
    private void validateTransferBalance(Transfer transfer) {
        // Validate that transfer items balance equals zero in each currency
        // ...
    }
}
```