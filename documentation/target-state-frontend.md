# Target State Frontend Architecture (Angular)

## Overview
The frontend will be implemented as a modern Single Page Application (SPA) using Angular, following best practices and a component-based architecture.

## Architecture Diagram

```mermaid
graph TB
    subgraph "Angular Application"
        subgraph "Core Module"
            Auth[Authentication Service]
            HTTP[HTTP Interceptor]
            Guard[Auth Guard]
            Store[State Management]
        end

        subgraph "Feature Modules"
            subgraph "User Module"
                Login[Login Component]
                Register[Register Component]
                Profile[Profile Component]
            end

            subgraph "Transfer Module"
                TransferList[Transfer List]
                TransferForm[Transfer Form]
                TransferDetail[Transfer Detail]
            end

            subgraph "Category Module"
                CategoryList[Category List]
                CategoryForm[Category Form]
            end

            subgraph "Goals Module"
                GoalList[Goal List]
                GoalForm[Goal Form]
                GoalProgress[Goal Progress]
            end

            subgraph "Reports Module"
                ReportDashboard[Report Dashboard]
                Charts[Charts Component]
                ExportReport[Export Component]
            end

            subgraph "Exchange Module"
                ExchangeList[Exchange List]
                ExchangeForm[Exchange Form]
                RateCalculator[Rate Calculator]
            end
        end

        subgraph "Shared Module"
            Components[Shared Components]
            Pipes[Custom Pipes]
            Directives[Directives]
        end

        subgraph "Services Layer"
            UserService[User Service]
            TransferService[Transfer Service]
            CategoryService[Category Service]
            GoalService[Goal Service]
            ReportService[Report Service]
            ExchangeService[Exchange Service]
        end
    end

    subgraph "External"
        API[Spring Boot API]
    end

    Services Layer --> API
    Feature Modules --> Services Layer
    Feature Modules --> Store
    Core Module --> API
```

## Component Structure

### Core Module
- Authentication service
- HTTP interceptors
- Route guards
- State management (NgRx)
- Error handling
- Loading indicators

### Feature Modules

1. User Module
```typescript
@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent,
    ProfileComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedModule,
    UserRoutingModule
  ]
})
export class UserModule { }
```

2. Transfer Module
```typescript
@NgModule({
  declarations: [
    TransferListComponent,
    TransferFormComponent,
    TransferDetailComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedModule,
    TransferRoutingModule
  ]
})
export class TransferModule { }
```

3. Category Module
```typescript
@NgModule({
  declarations: [
    CategoryListComponent,
    CategoryFormComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SharedModule,
    CategoryRoutingModule
  ]
})
export class CategoryModule { }
```

### Shared Module
```typescript
@NgModule({
  declarations: [
    LoadingSpinnerComponent,
    CurrencyFormatPipe,
    DateFormatPipe,
    ConfirmDialogComponent
  ],
  imports: [
    CommonModule,
    MaterialModule
  ],
  exports: [
    LoadingSpinnerComponent,
    CurrencyFormatPipe,
    DateFormatPipe,
    ConfirmDialogComponent,
    MaterialModule
  ]
})
export class SharedModule { }
```

## State Management

Using NgRx for state management:

```typescript
// State interface
interface AppState {
  auth: AuthState;
  transfers: TransferState;
  categories: CategoryState;
  goals: GoalState;
  exchanges: ExchangeState;
}

// Example actions
enum TransferActionTypes {
  LOAD_TRANSFERS = '[Transfer] Load Transfers',
  LOAD_TRANSFERS_SUCCESS = '[Transfer] Load Transfers Success',
  LOAD_TRANSFERS_FAILURE = '[Transfer] Load Transfers Failure',
  ADD_TRANSFER = '[Transfer] Add Transfer',
  // ...
}
```

## Service Layer

Example Transfer Service:
```typescript
@Injectable({
  providedIn: 'root'
})
export class TransferService {
  private apiUrl = 'api/transfers';

  constructor(private http: HttpClient) {}

  getTransfers(): Observable<Transfer[]> {
    return this.http.get<Transfer[]>(this.apiUrl);
  }

  getTransfer(id: number): Observable<Transfer> {
    return this.http.get<Transfer>(`${this.apiUrl}/${id}`);
  }

  createTransfer(transfer: Transfer): Observable<Transfer> {
    return this.http.post<Transfer>(this.apiUrl, transfer);
  }

  // ...
}
```

## Routing Structure

```typescript
const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent
      },
      {
        path: 'transfers',
        loadChildren: () => import('./transfers/transfers.module')
          .then(m => m.TransfersModule)
      },
      {
        path: 'categories',
        loadChildren: () => import('./categories/categories.module')
          .then(m => m.CategoriesModule)
      },
      // ...
    ]
  },
  {
    path: 'auth',
    component: AuthLayoutComponent,
    children: [
      {
        path: 'login',
        component: LoginComponent
      },
      {
        path: 'register',
        component: RegisterComponent
      }
    ]
  }
];
```

## Security Implementation

1. Authentication
```typescript
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject: BehaviorSubject<User>;
  
  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<User>(
      JSON.parse(localStorage.getItem('currentUser'))
    );
  }

  login(email: string, password: string): Observable<User> {
    return this.http.post<User>('/api/auth/login', { email, password })
      .pipe(
        tap(user => {
          localStorage.setItem('currentUser', JSON.stringify(user));
          this.currentUserSubject.next(user);
        })
      );
  }

  // ...
}
```

2. HTTP Interceptor
```typescript
@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const currentUser = this.authService.currentUserValue;
    if (currentUser && currentUser.token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${currentUser.token}`
        }
      });
    }
    return next.handle(request);
  }
}
```

## Error Handling

```typescript
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private messageService: MessageService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = 'An unknown error occurred!';
        if (error.error instanceof ErrorEvent) {
          // Client-side error
          errorMessage = error.error.message;
        } else {
          // Server-side error
          errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
        }
        this.messageService.showError(errorMessage);
        return throwError(errorMessage);
      })
    );
  }
}
```

## Testing Strategy

1. Component Testing
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
  });

  // ... test cases
});
```

2. Service Testing
```typescript
describe('TransferService', () => {
  let service: TransferService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TransferService]
    });

    service = TestBed.inject(TransferService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  // ... test cases
});
```

## Build and Deployment

```json
{
  "configurations": {
    "production": {
      "optimization": true,
      "outputHashing": "all",
      "sourceMap": false,
      "extractCss": true,
      "namedChunks": false,
      "aot": true,
      "extractLicenses": true,
      "vendorChunk": false,
      "buildOptimizer": true,
      "budgets": [
        {
          "type": "initial",
          "maximumWarning": "2mb",
          "maximumError": "5mb"
        }
      ]
    }
  }
}
```

This architecture provides a scalable, maintainable, and performant frontend application that will integrate seamlessly with the Spring Boot backend.