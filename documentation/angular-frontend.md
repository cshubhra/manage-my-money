# Angular Frontend Architecture

This document outlines the architecture, components, and organization of the Angular frontend for the Financial Management System.

## Application Structure

The Angular application follows a modular architecture with a feature-based organization:

```
src/
├── app/
│   ├── core/                # Core application functionality
│   ├── features/            # Feature modules
│   ├── shared/              # Shared components and utilities
│   ├── app-routing.module.ts
│   └── app.module.ts
├── assets/
├── environments/
└── index.html
```

## Core Module

The Core module contains services and components that are loaded once and shared across the entire application:

```
core/
├── authentication/
│   ├── auth.guard.ts        # Route guard for protected routes
│   ├── auth.service.ts      # Authentication service
│   ├── token.service.ts     # JWT token handling
│   └── user.service.ts      # User management
├── http/
│   ├── error.interceptor.ts # HTTP error handling
│   ├── jwt.interceptor.ts   # Add JWT to requests
│   └── loader.interceptor.ts # Loading indicators
├── services/
│   ├── notification.service.ts # User notifications
│   └── state.service.ts        # Application state management
├── layout/
│   ├── header/
│   ├── footer/
│   ├── sidebar/
│   └── layout.component.ts
└── core.module.ts
```

## Feature Modules

Each major feature area is organized into a separate module with its own components, services, and routing:

### Categories Module

```
features/categories/
├── components/
│   ├── category-list/
│   ├── category-form/
│   ├── category-detail/
│   └── category-tree/
├── models/
│   ├── category.model.ts
│   └── category-type.enum.ts
├── services/
│   └── category.service.ts
├── categories-routing.module.ts
└── categories.module.ts
```

### Transfers Module

```
features/transfers/
├── components/
│   ├── transfer-list/
│   ├── transfer-form/
│   ├── transfer-detail/
│   ├── transfer-item-form/
│   └── quick-transfer/
├── models/
│   ├── transfer.model.ts
│   └── transfer-item.model.ts
├── services/
│   └── transfer.service.ts
├── transfers-routing.module.ts
└── transfers.module.ts
```

### Currency and Exchange Module

```
features/currency/
├── components/
│   ├── currency-list/
│   ├── currency-form/
│   ├── exchange-list/
│   └── exchange-form/
├── models/
│   ├── currency.model.ts
│   └── exchange.model.ts
├── services/
│   ├── currency.service.ts
│   └── exchange.service.ts
├── currency-routing.module.ts
└── currency.module.ts
```

### Goals Module

```
features/goals/
├── components/
│   ├── goal-list/
│   ├── goal-form/
│   └── goal-detail/
├── models/
│   ├── goal.model.ts
│   └── goal-type.enum.ts
├── services/
│   └── goal.service.ts
├── goals-routing.module.ts
└── goals.module.ts
```

### Reports Module

```
features/reports/
├── components/
│   ├── report-list/
│   ├── report-form/
│   ├── report-display/
│   ├── report-charts/
│   └── report-filters/
├── models/
│   ├── report.model.ts
│   └── report-type.enum.ts
├── services/
│   └── report.service.ts
├── reports-routing.module.ts
└── reports.module.ts
```

## Shared Module

The Shared module contains components, directives, and pipes that are used across multiple feature modules:

```
shared/
├── components/
│   ├── loading-spinner/
│   ├── confirmation-modal/
│   ├── error-message/
│   ├── currency-selector/
│   ├── date-picker/
│   └── pagination/
├── directives/
│   ├── click-outside.directive.ts
│   └── number-format.directive.ts
├── pipes/
│   ├── money-format.pipe.ts
│   └── local-date.pipe.ts
├── utils/
│   ├── form.utils.ts
│   └── date.utils.ts
└── shared.module.ts
```

## State Management

The application uses a combination of Angular services with RxJS for state management:

- Simple state is managed through services with BehaviorSubjects
- More complex state uses NgRx for features that require it (reports, transfers)

Example state service:
```typescript
@Injectable({
  providedIn: 'root'
})
export class CategoryStateService {
  private categoriesSubject = new BehaviorSubject<Category[]>([]);
  public categories$ = this.categoriesSubject.asObservable();
  
  constructor(private categoryService: CategoryService) {}
  
  loadCategories(): void {
    this.categoryService.getCategories().subscribe(
      categories => this.categoriesSubject.next(categories)
    );
  }
  
  addCategory(category: Category): void {
    this.categoryService.createCategory(category).subscribe(
      newCategory => {
        const current = this.categoriesSubject.getValue();
        this.categoriesSubject.next([...current, newCategory]);
      }
    );
  }
  
  // Additional methods for updating, deleting, etc.
}
```

## Routing Structure

The application uses a hierarchical routing structure:

```typescript
const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', loadChildren: () => import('./features/dashboard/dashboard.module').then(m => m.DashboardModule) },
      { path: 'categories', loadChildren: () => import('./features/categories/categories.module').then(m => m.CategoriesModule) },
      { path: 'transfers', loadChildren: () => import('./features/transfers/transfers.module').then(m => m.TransfersModule) },
      { path: 'currencies', loadChildren: () => import('./features/currency/currency.module').then(m => m.CurrencyModule) },
      { path: 'goals', loadChildren: () => import('./features/goals/goals.module').then(m => m.GoalsModule) },
      { path: 'reports', loadChildren: () => import('./features/reports/reports.module').then(m => m.ReportsModule) },
      { path: 'settings', loadChildren: () => import('./features/settings/settings.module').then(m => m.SettingsModule) }
    ]
  },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'forgot-password', component: ForgotPasswordComponent },
  { path: '**', redirectTo: 'dashboard' }
];
```

## Forms Strategy

- **Simple Forms**: Template-driven forms for simple inputs
- **Complex Forms**: Reactive forms for multi-step or complex forms (e.g., transfers with multiple items)

Example reactive form for a transfer:

```typescript
createForm(): FormGroup {
  return this.fb.group({
    description: ['', Validators.required],
    day: [new Date(), Validators.required],
    items: this.fb.array([
      this.createTransferItemForm(),
      this.createTransferItemForm()
    ], [Validators.required, this.balanceValidator])
  });
}

createTransferItemForm(): FormGroup {
  return this.fb.group({
    description: [''],
    categoryId: [null, Validators.required],
    currencyId: [this.userService.defaultCurrencyId, Validators.required],
    value: [0, [Validators.required, Validators.pattern(/^-?\d*\.?\d{0,2}$/)]]
  });
}

balanceValidator(control: AbstractControl): ValidationErrors | null {
  const itemControls = (control as FormArray).controls;
  const sum = itemControls.reduce((acc, item) => {
    return acc + parseFloat(item.get('value')?.value || 0);
  }, 0);
  
  return Math.abs(sum) < 0.001 ? null : { unbalanced: true };
}
```

## Http Communication

Services communicate with the backend API using Angular's HttpClient:

```typescript
@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private apiUrl = environment.apiUrl + '/categories';

  constructor(private http: HttpClient) {}

  getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(this.apiUrl);
  }

  getCategory(id: number): Observable<Category> {
    return this.http.get<Category>(`${this.apiUrl}/${id}`);
  }

  createCategory(category: Category): Observable<Category> {
    return this.http.post<Category>(this.apiUrl, category);
  }

  updateCategory(category: Category): Observable<Category> {
    return this.http.put<Category>(`${this.apiUrl}/${category.id}`, category);
  }

  deleteCategory(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
```

## Error Handling

Error handling is centralized through an HTTP interceptor:

```typescript
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private notificationService: NotificationService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'An unknown error occurred';
        
        if (error.error instanceof ErrorEvent) {
          // Client-side error
          errorMessage = `Error: ${error.error.message}`;
        } else {
          // Server-side error
          if (error.error && error.error.message) {
            errorMessage = error.error.message;
          } else if (error.status === 401) {
            errorMessage = 'Unauthorized access. Please login again.';
          } else if (error.status === 404) {
            errorMessage = 'The requested resource was not found.';
          }
        }
        
        this.notificationService.showError(errorMessage);
        return throwError(errorMessage);
      })
    );
  }
}
```

## Styling Strategy

The application uses:

- SCSS for styling
- A custom theme based on Angular Material
- Responsive design principles with mobile-first approach
- CSS Grid and Flexbox for layouts

Component styles follow this pattern:

```
component-name/
├── component-name.component.ts
├── component-name.component.html
└── component-name.component.scss
```

## Testing Strategy

- **Unit Tests**: Jasmine/Karma for services, pipes, and isolated component tests
- **Component Tests**: Testing component interaction with TestBed
- **Integration Tests**: Testing feature modules with mocked services
- **E2E Tests**: Cypress for critical user flows

Example test for a service:

```typescript
describe('CategoryService', () => {
  let service: CategoryService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CategoryService]
    });

    service = TestBed.inject(CategoryService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should retrieve categories', () => {
    const mockCategories: Category[] = [
      { id: 1, name: 'Food', categoryType: 'EXPENSE' },
      { id: 2, name: 'Rent', categoryType: 'EXPENSE' }
    ];

    service.getCategories().subscribe(categories => {
      expect(categories.length).toBe(2);
      expect(categories).toEqual(mockCategories);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/categories`);
    expect(req.request.method).toBe('GET');
    req.flush(mockCategories);
  });
});
```