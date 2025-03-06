# Angular Frontend Architecture

This document outlines the architecture and component structure for the Angular frontend application that will replace the Ruby on Rails views.

## Overview

The Angular application will follow a modular architecture with:

1. **Core Module**: Services, guards, and interceptors used throughout the application
2. **Shared Module**: Common components, pipes, and directives used across features
3. **Feature Modules**: Domain-specific functionality organized by feature area
4. **Lazy Loading**: Feature modules loaded on demand to improve initial load time
5. **State Management**: Services with behavior subjects for simpler features, NgRx for more complex state

## Application Structure

```
src/
├── app/
│   ├── core/                    # Core services and utilities
│   │   ├── auth/                # Authentication services
│   │   ├── http/                # HTTP interceptors
│   │   ├── services/            # Core services
│   │   └── core.module.ts       # Core module definition
│   │
│   ├── shared/                  # Shared components
│   │   ├── components/          # Reusable components
│   │   ├── directives/          # Custom directives
│   │   ├── pipes/               # Custom pipes
│   │   └── shared.module.ts     # Shared module definition
│   │
│   ├── features/                # Feature modules
│   │   ├── auth/                # Authentication features
│   │   ├── dashboard/           # Dashboard features
│   │   ├── categories/          # Category management
│   │   ├── transfers/           # Transfer management
│   │   ├── reports/             # Reporting features
│   │   ├── goals/               # Goal management
│   │   ├── currencies/          # Currency management
│   │   └── settings/            # User settings
│   │
│   ├── layouts/                 # Layout components
│   │   ├── main-layout/         # Main application layout
│   │   └── auth-layout/         # Authentication layout
│   │
│   ├── app-routing.module.ts    # Main routing module
│   ├── app.component.ts         # Root component
│   └── app.module.ts            # Root module
│
├── assets/                      # Static assets
├── environments/                # Environment configurations
└── index.html                   # Main HTML file
```

## Core Module Components

### Authentication Services

- **AuthService**: Manages authentication state and token storage
- **AuthGuard**: Protects routes requiring authentication
- **JwtInterceptor**: Adds authentication token to outgoing requests
- **ErrorInterceptor**: Handles HTTP errors (401, 403, 500, etc.)

### Core Services

- **ApiService**: Base service for API communication
- **NotificationService**: Handles application notifications and toasts
- **LoadingService**: Manages application loading state
- **ErrorHandlingService**: Central error handling
- **LocalStorageService**: Wrapper for browser storage

## Shared Module Components

### Reusable UI Components

- **AppTable**: Enhanced table component with sorting and filtering
- **Pagination**: Reusable pagination component
- **DateRangePicker**: Date range selection component
- **Confirmation**: Confirmation dialog component
- **FormControls**: Enhanced form controls with validation

### Directives

- **HasPermissionDirective**: Conditionally show/hide elements based on permissions
- **ClickOutsideDirective**: Detect clicks outside an element
- **NumberOnlyDirective**: Restrict input to numbers only

### Pipes

- **CurrencyFormatPipe**: Format amounts with currency symbols
- **LocalDatePipe**: Format dates according to user preferences
- **SafeHtmlPipe**: Safely display HTML content

## Feature Modules

### Dashboard Module

Components:
- **Dashboard**: Overview of user's financial situation
- **RecentTransactions**: Shows latest transactions
- **QuickTransfer**: Form for quick transfers
- **BalanceSummary**: Summary of account balances
- **GoalsProgress**: Progress overview of active goals

### Categories Module

Components:
- **CategoriesList**: Hierarchical list of categories
- **CategoryDetail**: View and edit category details
- **CategoryForm**: Create/edit category form
- **CategoryBalanceChart**: Visual representation of category balance
- **CategoryTransactions**: Transactions for selected category

### Transfers Module

Components:
- **TransfersList**: List of user transfers with filtering
- **TransferDetail**: Detailed view of a transfer
- **TransferForm**: Create/edit transfer form
- **TransferItems**: CRUD for transfer items
- **MultiCurrencyForm**: Support for multi-currency transfers

### Reports Module

Components:
- **ReportsList**: List of available reports
- **ReportDetail**: View report results
- **ReportForm**: Create/edit report forms for different report types
- **ReportCharts**: Various charts for data visualization
- **CategorySelector**: Component for selecting categories in reports

### Goals Module

Components:
- **GoalsList**: List of financial goals
- **GoalDetail**: Detailed view of a goal with progress
- **GoalForm**: Create/edit goal form
- **GoalProgress**: Visual representation of goal progress

### Currencies Module

Components:
- **CurrenciesList**: List of currencies available to user
- **CurrencyForm**: Create/edit currency form
- **ExchangesList**: List of exchange rates
- **ExchangeForm**: Create/edit exchange rate form

### Settings Module

Components:
- **ProfileSettings**: User profile information
- **AppSettings**: Application preferences
- **SecuritySettings**: Password and security options

## Layouts

### Main Layout

Components:
- **Header**: Application header with user info and main navigation
- **Sidebar**: Side navigation menu
- **Footer**: Application footer
- **NotificationArea**: Area for displaying notifications

### Auth Layout

Components:
- **LoginForm**: User login form
- **RegisterForm**: User registration form
- **ForgotPasswordForm**: Password recovery form

## State Management

For simpler state requirements, we'll use services with RxJS:

```typescript
@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private categoriesSubject = new BehaviorSubject<Category[]>([]);
  public categories$ = this.categoriesSubject.asObservable();

  constructor(private http: HttpClient) {}

  loadCategories(): Observable<Category[]> {
    return this.http.get<Category[]>('/api/v1/categories').pipe(
      tap(categories => this.categoriesSubject.next(categories))
    );
  }
}
```

For more complex state management, we'll use NgRx:

```
features/
├── transfers/
│   ├── store/
│   │   ├── actions/
│   │   │   └── transfer.actions.ts
│   │   ├── effects/
│   │   │   └── transfer.effects.ts
│   │   ├── reducers/
│   │   │   └── transfer.reducer.ts
│   │   ├── selectors/
│   │   │   └── transfer.selectors.ts
│   │   └── state/
│   │       └── transfer.state.ts
```

## Routing Strategy

The application will use lazy-loaded feature modules with a routing structure like:

```typescript
const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', loadChildren: () => import('./features/dashboard/dashboard.module').then(m => m.DashboardModule) },
      { path: 'categories', loadChildren: () => import('./features/categories/categories.module').then(m => m.CategoriesModule) },
      { path: 'transfers', loadChildren: () => import('./features/transfers/transfers.module').then(m => m.TransfersModule) },
      { path: 'reports', loadChildren: () => import('./features/reports/reports.module').then(m => m.ReportsModule) },
      { path: 'goals', loadChildren: () => import('./features/goals/goals.module').then(m => m.GoalsModule) },
      { path: 'currencies', loadChildren: () => import('./features/currencies/currencies.module').then(m => m.CurrenciesModule) },
      { path: 'settings', loadChildren: () => import('./features/settings/settings.module').then(m => m.SettingsModule) }
    ]
  },
  {
    path: '',
    component: AuthLayoutComponent,
    children: [
      { path: 'login', component: LoginComponent },
      { path: 'register', component: RegisterComponent },
      { path: 'forgot-password', component: ForgotPasswordComponent }
    ]
  },
  { path: '**', redirectTo: '' }
];
```

## Form Strategy

Forms will be implemented using Reactive Forms for better control and validation:

```typescript
@Component({
  selector: 'app-transfer-form',
  templateUrl: './transfer-form.component.html'
})
export class TransferFormComponent implements OnInit {
  transferForm: FormGroup;

  constructor(private fb: FormBuilder) {}

  ngOnInit() {
    this.transferForm = this.fb.group({
      description: ['', Validators.required],
      day: [new Date(), Validators.required],
      items: this.fb.array([])
    });
  }

  get items(): FormArray {
    return this.transferForm.get('items') as FormArray;
  }

  addItem(type: 'INCOME' | 'OUTCOME') {
    const item = this.fb.group({
      description: ['', Validators.required],
      transferItemType: [type, Validators.required],
      value: [0, [Validators.required, Validators.min(0.01)]],
      categoryId: [null, Validators.required],
      currencyId: [1, Validators.required]
    });

    this.items.push(item);
  }

  removeItem(index: number) {
    this.items.removeAt(index);
  }
}
```

## API Communication

Services will use HttpClient for API communication:

```typescript
@Injectable({
  providedIn: 'root'
})
export class TransferService {
  constructor(private http: HttpClient) {}

  getTransfers(params: any): Observable<PagedResponse<Transfer>> {
    return this.http.get<PagedResponse<Transfer>>('/api/v1/transfers', { params });
  }

  getTransfer(id: number): Observable<Transfer> {
    return this.http.get<Transfer>(`/api/v1/transfers/${id}`);
  }

  createTransfer(transfer: TransferCreate): Observable<Transfer> {
    return this.http.post<Transfer>('/api/v1/transfers', transfer);
  }

  updateTransfer(id: number, transfer: TransferUpdate): Observable<void> {
    return this.http.put<void>(`/api/v1/transfers/${id}`, transfer);
  }

  deleteTransfer(id: number): Observable<void> {
    return this.http.delete<void>(`/api/v1/transfers/${id}`);
  }
}
```

## Error Handling

Centralized error handling with interceptor:

```typescript
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private notificationService: NotificationService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'An unknown error occurred!';
        
        if (error.error instanceof ErrorEvent) {
          // Client-side error
          errorMessage = `Error: ${error.error.message}`;
        } else {
          // Server-side error
          if (error.error && error.error.message) {
            errorMessage = error.error.message;
          } else {
            switch (error.status) {
              case 401:
                errorMessage = 'Unauthorized. Please login again.';
                // Redirect to login page
                break;
              case 403:
                errorMessage = 'You do not have permission to perform this action.';
                break;
              case 404:
                errorMessage = 'The requested resource was not found.';
                break;
              case 422:
                errorMessage = 'Validation error. Please check your input.';
                break;
              case 500:
                errorMessage = 'Server error. Please try again later.';
                break;
            }
          }
        }
        
        this.notificationService.error(errorMessage);
        return throwError(() => new Error(errorMessage));
      })
    );
  }
}
```

## Authentication Flow

The authentication flow will be implemented using JWT:

```typescript
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject = new BehaviorSubject<User | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();
  
  constructor(
    private http: HttpClient,
    private router: Router,
    private localStorage: LocalStorageService
  ) {
    const storedUser = this.localStorage.getItem('currentUser');
    if (storedUser) {
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }
  
  get isLoggedIn(): boolean {
    const user = this.currentUserSubject.value;
    return !!user && !!user.token;
  }
  
  login(username: string, password: string): Observable<User> {
    return this.http.post<User>('/api/v1/auth/login', { username, password })
      .pipe(
        tap(user => {
          this.localStorage.setItem('currentUser', JSON.stringify(user));
          this.currentUserSubject.next(user);
        })
      );
  }
  
  logout(): void {
    this.localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }
  
  register(user: UserRegistration): Observable<any> {
    return this.http.post<any>('/api/v1/auth/register', user);
  }
}
```