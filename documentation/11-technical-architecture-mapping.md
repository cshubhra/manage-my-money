# Technical Architecture Mapping

## Overview
This document maps the current Ruby on Rails components to their Angular/Spring Boot equivalents and provides technical implementation details.

## 1. Domain Model Mapping

### 1.1 Core Models

#### Category Model
```java
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Nested Set implementation
    @Column
    private Integer lft;

    @Column
    private Integer rgt;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children;

    // Additional fields and methods
}
```

#### Goal Model
```java
@Entity
@Table(name = "goals")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column
    private boolean includeSubcategories;

    @Enumerated(EnumType.STRING)
    private PeriodType periodType;

    @Enumerated(EnumType.STRING)
    private GoalType goalType;

    @Column(precision = 19, scale = 4)
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    // Additional fields and methods
}
```

## 2. Service Layer Mapping

### 2.1 Category Service
```java
@Service
@Transactional
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private NestedSetManager nestedSetManager;

    public Category moveCategory(Long categoryId, Long newParentId) {
        return nestedSetManager.moveNode(categoryId, newParentId);
    }

    public List<Category> getCategoryTree(Long userId) {
        return categoryRepository.findTreeByUserId(userId);
    }

    public Money calculateCategoryBalance(Long categoryId, LocalDate startDate, LocalDate endDate) {
        // Implementation of complex balance calculation
    }
}
```

### 2.2 Transfer Service
```java
@Service
@Transactional
public class TransferService {
    
    @Autowired
    private TransferRepository transferRepository;
    
    @Autowired
    private ExchangeService exchangeService;

    public Transfer createTransfer(TransferDTO transferDTO) {
        validateTransferBalance(transferDTO);
        return transferRepository.save(mapToEntity(transferDTO));
    }

    private void validateTransferBalance(TransferDTO transfer) {
        // Complex validation logic for multi-currency transfers
    }
}
```

## 3. Angular Component Structure

### 3.1 Category Management
```typescript
// category.component.ts
@Component({
    selector: 'app-category',
    template: `
        <mat-tree [dataSource]="dataSource" [treeControl]="treeControl">
            <mat-tree-node *matTreeNodeDef="let node" matTreeNodePadding>
                <button mat-icon-button (click)="addSubCategory(node)">
                    <mat-icon>add</mat-icon>
                </button>
                {{node.name}}
            </mat-tree-node>
        </mat-tree>
    `
})
export class CategoryComponent {
    private dataSource: MatTreeFlatDataSource<Category, FlatNode>;
    private treeControl: FlatTreeControl<FlatNode>;

    constructor(private categoryService: CategoryService) {
        this.initializeTree();
    }

    private initializeTree() {
        // Tree initialization logic
    }
}
```

### 3.2 Transfer Management
```typescript
// transfer.component.ts
@Component({
    selector: 'app-transfer',
    template: `
        <form [formGroup]="transferForm" (ngSubmit)="onSubmit()">
            <mat-form-field>
                <input matInput formControlName="description" placeholder="Description">
            </mat-form-field>
            <!-- Additional form fields -->
        </form>
    `
})
export class TransferComponent {
    transferForm: FormGroup;

    constructor(
        private fb: FormBuilder,
        private transferService: TransferService
    ) {
        this.initForm();
    }

    private initForm() {
        this.transferForm = this.fb.group({
            description: ['', Validators.required],
            items: this.fb.array([])
        });
    }
}
```

## 4. State Management

### 4.1 Category State
```typescript
// category.state.ts
export interface CategoryState {
    categories: Category[];
    selectedCategory: Category | null;
    loading: boolean;
    error: string | null;
}

export const initialState: CategoryState = {
    categories: [],
    selectedCategory: null,
    loading: false,
    error: null
};

export const categoryReducer = createReducer(
    initialState,
    on(loadCategories, state => ({ ...state, loading: true })),
    on(loadCategoriesSuccess, (state, { categories }) => ({
        ...state,
        categories,
        loading: false
    }))
);
```

### 4.2 Transfer State
```typescript
// transfer.state.ts
export interface TransferState {
    transfers: Transfer[];
    currentTransfer: Transfer | null;
    loading: boolean;
    error: string | null;
}

export const transferReducer = createReducer(
    initialState,
    on(createTransfer, state => ({ ...state, loading: true })),
    on(createTransferSuccess, (state, { transfer }) => ({
        ...state,
        transfers: [...state.transfers, transfer],
        loading: false
    }))
);
```

## 5. Data Access Layer

### 5.1 JPA Repositories
```java
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.user.id = :userId ORDER BY c.lft")
    List<Category> findTreeByUserId(@Param("userId") Long userId);

    @Query(nativeQuery = true, value = """
        SELECT c.* FROM categories c
        WHERE c.user_id = :userId
        AND c.lft > :left
        AND c.rgt < :right
        ORDER BY c.lft
    """)
    List<Category> findDescendants(
        @Param("userId") Long userId,
        @Param("left") Integer left,
        @Param("right") Integer right
    );
}
```

### 5.2 REST Controllers
```java
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/tree")
    public ResponseEntity<List<CategoryDTO>> getCategoryTree() {
        return ResponseEntity.ok(categoryService.getCategoryTree(
            SecurityUtils.getCurrentUserId()
        ));
    }
    
    @PostMapping("/{id}/move")
    public ResponseEntity<CategoryDTO> moveCategory(
        @PathVariable Long id,
        @RequestBody MoveCategoryRequest request
    ) {
        return ResponseEntity.ok(categoryService.moveCategory(
            id,
            request.getNewParentId()
        ));
    }
}
```

## 6. Authentication Implementation

### 6.1 Spring Security Configuration
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    private JwtTokenProvider tokenProvider;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/v1/auth/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(
                new JwtAuthenticationFilter(tokenProvider),
                UsernamePasswordAuthenticationFilter.class
            );
    }
}
```

### 6.2 Angular Auth Service
```typescript
// auth.service.ts
@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private currentUserSubject = new BehaviorSubject<User | null>(null);
    
    constructor(private http: HttpClient) {
        this.loadStoredUser();
    }
    
    login(credentials: LoginCredentials): Observable<User> {
        return this.http.post<AuthResponse>('/api/v1/auth/login', credentials)
            .pipe(
                map(response => {
                    this.storeToken(response.token);
                    return response.user;
                })
            );
    }
}
```

This technical mapping provides a foundation for implementing the modernized application while maintaining the complex business logic of the original Ruby on Rails application.