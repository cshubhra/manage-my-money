# Angular Application Structure

This document outlines the structure of our Angular frontend application for the financial management system.

## Project Structure

```
src/
├── app/
│   ├── core/                    # Core module (services, guards, interceptors)
│   │   ├── auth/                # Authentication related components
│   │   │   ├── auth.service.ts 
│   │   │   ├── auth.guard.ts
│   │   │   ├── token.service.ts
│   │   │   └── ...
│   │   ├── http/                # HTTP interceptors
│   │   │   ├── auth.interceptor.ts
│   │   │   ├── error.interceptor.ts
│   │   │   └── ...
│   │   ├── services/            # Core services
│   │   │   ├── category.service.ts
│   │   │   ├── transfer.service.ts
│   │   │   ├── currency.service.ts
│   │   │   ├── exchange.service.ts
│   │   │   ├── report.service.ts
│   │   │   ├── user.service.ts
│   │   │   └── ...
│   │   └── core.module.ts
│   │
│   ├── shared/                  # Shared module (components, directives, pipes)
│   │   ├── components/          # Reusable components
│   │   │   ├── confirmation-dialog/
│   │   │   ├── currency-selector/
│   │   │   ├── date-picker/
│   │   │   ├── category-tree/
│   │   │   └── ...
│   │   ├── directives/          # Custom directives
│   │   ├── pipes/               # Custom pipes
│   │   ├── models/              # Shared data models/interfaces
│   │   ├── validators/          # Custom form validators
│   │   └── shared.module.ts
│   │
│   ├── features/                # Feature modules
│   │   ├── auth/                # Auth feature (login, register, etc.)
│   │   │   ├── components/
│   │   │   ├── pages/
│   │   │   │   ├── login/
│   │   │   │   ├── register/
│   │   │   │   └── ...
│   │   │   ├── auth-routing.module.ts
│   │   │   └── auth.module.ts
│   │   │
│   │   ├── dashboard/           # Dashboard feature
│   │   │   ├── components/
│   │   │   ├── pages/
│   │   │   ├── dashboard-routing.module.ts
│   │   │   └── dashboard.module.ts
│   │   │
│   │   ├── categories/          # Categories feature
│   │   │   ├── components/
│   │   │   ├── pages/
│   │   │   │   ├── category-list/
│   │   │   │   ├── category-detail/
│   │   │   │   ├── category-form/
│   │   │   │   └── ...
│   │   │   ├── categories-routing.module.ts
│   │   │   └── categories.module.ts
│   │   │
│   │   ├── transfers/           # Transfers feature
│   │   │   ├── components/
│   │   │   ├── pages/
│   │   │   │   ├── transfer-list/
│   │   │   │   ├── transfer-detail/
│   │   │   │   ├── transfer-form/
│   │   │   │   └── ...
│   │   │   ├── transfers-routing.module.ts
│   │   │   └── transfers.module.ts
│   │   │
│   │   ├── currencies/          # Currencies feature
│   │   │   ├── components/
│   │   │   ├── pages/
│   │   │   ├── currencies-routing.module.ts
│   │   │   └── currencies.module.ts
│   │   │
│   │   ├── reports/             # Reports feature
│   │   │   ├── components/
│   │   │   ├── pages/
│   │   │   ├── reports-routing.module.ts
│   │   │   └── reports.module.ts
│   │   │
│   │   └── user-settings/       # User settings feature
│   │       ├── components/
│   │       ├── pages/
│   │       ├── user-settings-routing.module.ts
│   │       └── user-settings.module.ts
│   │
│   ├── app-routing.module.ts    # Main routing module
│   ├── app.component.ts         # Root component
│   ├── app.component.html       # Root component template
│   ├── app.component.scss       # Root component styles
│   └── app.module.ts            # Root module
│
├── assets/                      # Static assets
│   ├── images/
│   ├── i18n/                    # Internationalization files
│   └── ...
│
├── environments/                # Environment configuration
│   ├── environment.ts
│   └── environment.prod.ts
│
└── theme/                       # Theme-related styles
    ├── variables.scss
    ├── mixins.scss
    └── ...
```

## Core Models and Interfaces

### User Model

```typescript
export interface User {
  id: number;
  login: string;
  name: string;
  email: string;
  preferences: UserPreferences;
}

export interface UserPreferences {
  defaultCurrencyId: number;
  includeTransactionsFromSubcategories: boolean;
  transactionAmountLimitType: TransactionAmountLimitType;
  transactionAmountLimitValue: number;
  multiCurrencyBalanceCalculatingAlgorithm: MultiCurrencyBalanceCalculatingAlgorithm;
  invertSaldoForIncome: boolean;
}

export enum TransactionAmountLimitType {
  TRANSACTION_COUNT = 0,
  WEEK_COUNT = 1,
  THIS_MONTH = 2,
  THIS_AND_LAST_MONTH = 3
}

export enum MultiCurrencyBalanceCalculatingAlgorithm {
  SHOW_ALL_CURRENCIES = 0,
  CALCULATE_WITH_NEWEST_EXCHANGES = 1,
  CALCULATE_WITH_EXCHANGES_CLOSEST_TO_TRANSACTION = 2,
  CALCULATE_WITH_NEWEST_EXCHANGES_BUT = 3,
  CALCULATE_WITH_EXCHANGES_CLOSEST_TO_TRANSACTION_BUT = 4
}
```

### Category Model

```typescript
export interface Category {
  id: number;
  name: string;
  description?: string;
  categoryType: CategoryType;
  parent?: Category | number;
  children?: Category[];
  level?: number;
  path?: string;
  loanCategory?: boolean;
  email?: string;
  bankAccountNumber?: string;
  bankinfo?: string;
}

export enum CategoryType {
  ASSET = 0,
  INCOME = 1,
  EXPENSE = 2,
  LOAN = 3,
  BALANCE = 4
}
```

### Transfer Model

```typescript
export interface Transfer {
  id: number;
  description: string;
  day: Date | string;
  transferItems: TransferItem[];
  conversions?: Conversion[];
}

export interface TransferItem {
  id?: number;
  description: string;
  value: number;
  categoryId: number;
  currencyId: number;
  category?: Category;
  currency?: Currency;
}

export interface Conversion {
  id?: number;
  exchangeId: number;
  exchange?: Exchange;
}
```

### Currency Model

```typescript
export interface Currency {
  id: number;
  symbol: string;
  longSymbol: string;
  name: string;
  longName: string;
  isSystem?: boolean;
}
```

### Exchange Model

```typescript
export interface Exchange {
  id?: number;
  leftCurrencyId: number;
  rightCurrencyId: number;
  leftToRight: number;
  rightToLeft: number;
  day?: Date | string;
  leftCurrency?: Currency;
  rightCurrency?: Currency;
}
```

## Key Services

### Auth Service

```typescript
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  
  constructor(
    private http: HttpClient,
    private tokenService: TokenService
  ) {
    // Initialize current user from token if available
    this.loadUserFromToken();
  }
  
  login(credentials: { login: string, password: string }): Observable<{ token: string, user: User }> {
    return this.http.post<{ token: string, user: User }>('/api/auth/login', credentials)
      .pipe(
        tap(response => {
          this.tokenService.saveToken(response.token);
          this.currentUserSubject.next(response.user);
        })
      );
  }
  
  register(userData: { login: string, name: string, email: string, password: string }): Observable<any> {
    return this.http.post('/api/auth/signup', userData);
  }
  
  logout(): void {
    this.tokenService.removeToken();
    this.currentUserSubject.next(null);
  }
  
  isLoggedIn(): boolean {
    return this.tokenService.getToken() !== null;
  }
  
  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }
  
  private loadUserFromToken(): void {
    const token = this.tokenService.getToken();
    if (token) {
      // Get user data from token or from API
      this.getUserProfile().subscribe();
    }
  }
  
  getUserProfile(): Observable<User> {
    return this.http.get<User>('/api/users/profile')
      .pipe(
        tap(user => this.currentUserSubject.next(user))
      );
  }
}
```

### Category Service

```typescript
@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  constructor(private http: HttpClient) {}
  
  getCategories(type?: CategoryType): Observable<Category[]> {
    let params = new HttpParams();
    if (type !== undefined) {
      params = params.set('type', type.toString());
    }
    return this.http.get<Category[]>('/api/categories', { params });
  }
  
  getCategory(id: number): Observable<Category> {
    return this.http.get<Category>(`/api/categories/${id}`);
  }
  
  createCategory(category: Partial<Category>): Observable<Category> {
    return this.http.post<Category>('/api/categories', category);
  }
  
  updateCategory(id: number, category: Partial<Category>): Observable<Category> {
    return this.http.put<Category>(`/api/categories/${id}`, category);
  }
  
  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`/api/categories/${id}`);
  }
  
  buildCategoryTree(categories: Category[]): Category[] {
    // Implementation for building hierarchical tree from flat categories array
    // ...
  }
}
```

### Transfer Service

```typescript
@Injectable({
  providedIn: 'root'
})
export class TransferService {
  constructor(private http: HttpClient) {}
  
  getTransfers(params: {
    limit?: number,
    offset?: number,
    startDate?: string | Date,
    endDate?: string | Date,
    categoryId?: number
  } = {}): Observable<Page<Transfer>> {
    let httpParams = new HttpParams();
    
    Object.entries(params).forEach(([key, value]) => {
      if (value !== undefined) {
        if (value instanceof Date) {
          httpParams = httpParams.set(key, value.toISOString().split('T')[0]);
        } else {
          httpParams = httpParams.set(key, value.toString());
        }
      }
    });
    
    return this.http.get<Page<Transfer>>('/api/transfers', { params: httpParams });
  }
  
  getTransfer(id: number): Observable<Transfer> {
    return this.http.get<Transfer>(`/api/transfers/${id}`);
  }
  
  createTransfer(transfer: Partial<Transfer>): Observable<Transfer> {
    return this.http.post<Transfer>('/api/transfers', transfer);
  }
  
  updateTransfer(id: number, transfer: Partial<Transfer>): Observable<Transfer> {
    return this.http.put<Transfer>(`/api/transfers/${id}`, transfer);
  }
  
  deleteTransfer(id: number): Observable<void> {
    return this.http.delete<void>(`/api/transfers/${id}`);
  }
}
```

### Currency Service

```typescript
@Injectable({
  providedIn: 'root'
})
export class CurrencyService {
  constructor(private http: HttpClient) {}
  
  getCurrencies(): Observable<Currency[]> {
    return this.http.get<Currency[]>('/api/currencies');
  }
  
  createCurrency(currency: Partial<Currency>): Observable<Currency> {
    return this.http.post<Currency>('/api/currencies', currency);
  }
  
  updateCurrency(id: number, currency: Partial<Currency>): Observable<Currency> {
    return this.http.put<Currency>(`/api/currencies/${id}`, currency);
  }
  
  deleteCurrency(id: number): Observable<void> {
    return this.http.delete<void>(`/api/currencies/${id}`);
  }
}
```

### Exchange Service

```typescript
@Injectable({
  providedIn: 'root'
})
export class ExchangeService {
  constructor(private http: HttpClient) {}
  
  getExchanges(params: {
    leftCurrencyId?: number,
    rightCurrencyId?: number,
    date?: string | Date
  } = {}): Observable<Exchange[]> {
    let httpParams = new HttpParams();
    
    Object.entries(params).forEach(([key, value]) => {
      if (value !== undefined) {
        if (value instanceof Date) {
          httpParams = httpParams.set(key, value.toISOString().split('T')[0]);
        } else {
          httpParams = httpParams.set(key, value.toString());
        }
      }
    });
    
    return this.http.get<Exchange[]>('/api/exchanges', { params: httpParams });
  }
  
  createExchange(exchange: Partial<Exchange>): Observable<Exchange> {
    return this.http.post<Exchange>('/api/exchanges', exchange);
  }
  
  updateExchange(id: number, exchange: Partial<Exchange>): Observable<Exchange> {
    return this.http.put<Exchange>(`/api/exchanges/${id}`, exchange);
  }
  
  deleteExchange(id: number): Observable<void> {
    return this.http.delete<void>(`/api/exchanges/${id}`);
  }
  
  getNewestExchange(leftCurrencyId: number, rightCurrencyId: number): Observable<Exchange> {
    return this.http.get<Exchange>(`/api/exchanges/newest`, {
      params: new HttpParams()
        .set('leftCurrencyId', leftCurrencyId.toString())
        .set('rightCurrencyId', rightCurrencyId.toString())
    });
  }
}
```

## Key Components

### Categories List Component

```typescript
@Component({
  selector: 'app-category-list',
  templateUrl: './category-list.component.html',
  styleUrls: ['./category-list.component.scss']
})
export class CategoryListComponent implements OnInit {
  categories: Category[] = [];
  categoryTree: Category[] = [];
  categorySaldos: { [id: number]: number } = {};
  selectedCategoryType: CategoryType = CategoryType.EXPENSE;
  
  constructor(
    private categoryService: CategoryService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}
  
  ngOnInit(): void {
    this.loadCategories();
  }
  
  loadCategories(): void {
    this.categoryService.getCategories(this.selectedCategoryType).subscribe(
      categories => {
        this.categories = categories;
        this.categoryTree = this.categoryService.buildCategoryTree(categories);
      },
      error => {
        this.snackBar.open('Failed to load categories', 'Close', { duration: 3000 });
      }
    );
  }
  
  onCategoryTypeChange(type: CategoryType): void {
    this.selectedCategoryType = type;
    this.loadCategories();
  }
  
  openCategoryForm(category?: Category): void {
    const dialogRef = this.dialog.open(CategoryFormDialogComponent, {
      width: '500px',
      data: {
        category: category || { categoryType: this.selectedCategoryType },
        isNew: !category
      }
    });
    
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadCategories();
      }
    });
  }
  
  deleteCategory(category: Category): void {
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '350px',
      data: {
        title: 'Delete Category',
        message: `Are you sure you want to delete "${category.name}"?`,
        confirmText: 'Delete',
        cancelText: 'Cancel'
      }
    });
    
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.categoryService.deleteCategory(category.id).subscribe(
          () => {
            this.snackBar.open('Category deleted', 'Close', { duration: 3000 });
            this.loadCategories();
          },
          error => {
            this.snackBar.open('Failed to delete category', 'Close', { duration: 3000 });
          }
        );
      }
    });
  }
}
```

### Transfer Form Component

```typescript
@Component({
  selector: 'app-transfer-form',
  templateUrl: './transfer-form.component.html',
  styleUrls: ['./transfer-form.component.scss']
})
export class TransferFormComponent implements OnInit {
  transferForm: FormGroup;
  categories: Category[] = [];
  currencies: Currency[] = [];
  isEditMode = false;
  
  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private transferService: TransferService,
    private categoryService: CategoryService,
    private currencyService: CurrencyService,
    private snackBar: MatSnackBar
  ) {
    this.createForm();
  }
  
  ngOnInit(): void {
    this.loadCategories();
    this.loadCurrencies();
    
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.loadTransfer(+id);
      }
    });
  }
  
  createForm(): void {
    this.transferForm = this.fb.group({
      description: ['', Validators.required],
      day: [new Date(), Validators.required],
      transferItems: this.fb.array([
        this.createTransferItemFormGroup(-1),  // negative for expenses
        this.createTransferItemFormGroup(1)    // positive for income/account
      ])
    });
  }
  
  createTransferItemFormGroup(defaultValue: number = 0): FormGroup {
    return this.fb.group({
      description: ['', Validators.required],
      value: [defaultValue, [Validators.required, Validators.pattern(/^-?\d+(\.\d{1,2})?$/)]],
      categoryId: [null, Validators.required],
      currencyId: [null, Validators.required]
    });
  }
  
  get transferItemsFormArray(): FormArray {
    return this.transferForm.get('transferItems') as FormArray;
  }
  
  loadCategories(): void {
    this.categoryService.getCategories().subscribe(
      categories => this.categories = categories,
      error => this.snackBar.open('Failed to load categories', 'Close', { duration: 3000 })
    );
  }
  
  loadCurrencies(): void {
    this.currencyService.getCurrencies().subscribe(
      currencies => this.currencies = currencies,
      error => this.snackBar.open('Failed to load currencies', 'Close', { duration: 3000 })
    );
  }
  
  loadTransfer(id: number): void {
    this.transferService.getTransfer(id).subscribe(
      transfer => {
        this.transferForm.patchValue({
          description: transfer.description,
          day: new Date(transfer.day)
        });
        
        // Clear default transfer items
        while (this.transferItemsFormArray.length > 0) {
          this.transferItemsFormArray.removeAt(0);
        }
        
        // Add loaded transfer items
        transfer.transferItems.forEach(item => {
          const itemFormGroup = this.createTransferItemFormGroup();
          itemFormGroup.patchValue({
            description: item.description,
            value: item.value,
            categoryId: item.categoryId,
            currencyId: item.currencyId
          });
          this.transferItemsFormArray.push(itemFormGroup);
        });
      },
      error => {
        this.snackBar.open('Failed to load transfer', 'Close', { duration: 3000 });
        this.router.navigate(['/transfers']);
      }
    );
  }
  
  onSubmit(): void {
    if (this.transferForm.invalid) {
      this.markFormGroupTouched(this.transferForm);
      return;
    }
    
    const transferData = this.transferForm.value;
    
    if (this.isEditMode) {
      const id = +this.route.snapshot.paramMap.get('id')!;
      this.transferService.updateTransfer(id, transferData).subscribe(
        () => {
          this.snackBar.open('Transfer updated successfully', 'Close', { duration: 3000 });
          this.router.navigate(['/transfers']);
        },
        error => {
          this.snackBar.open('Failed to update transfer', 'Close', { duration: 3000 });
        }
      );
    } else {
      this.transferService.createTransfer(transferData).subscribe(
        () => {
          this.snackBar.open('Transfer created successfully', 'Close', { duration: 3000 });
          this.router.navigate(['/transfers']);
        },
        error => {
          this.snackBar.open('Failed to create transfer', 'Close', { duration: 3000 });
        }
      );
    }
  }
  
  addTransferItem(): void {
    this.transferItemsFormArray.push(this.createTransferItemFormGroup());
  }
  
  removeTransferItem(index: number): void {
    if (this.transferItemsFormArray.length <= 2) {
      this.snackBar.open('A transfer must have at least two items', 'Close', { duration: 3000 });
      return;
    }
    this.transferItemsFormArray.removeAt(index);
  }
  
  private markFormGroupTouched(formGroup: FormGroup): void {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
      if (control instanceof FormArray) {
        control.controls.forEach(c => {
          if (c instanceof FormGroup) {
            this.markFormGroupTouched(c);
          }
        });
      }
    });
  }
}
```