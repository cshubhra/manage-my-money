# Developer Guide for Modernization Project

## Overview

This guide provides detailed instructions for developers working on the modernization project to migrate the Ruby on Rails application to Angular (frontend) and Spring Boot (backend). It includes setup instructions, coding standards, and best practices.

## Development Environment Setup

### Prerequisites
1. **Java Development Kit (JDK)**
   - Version: JDK 17 or later
   - Set JAVA_HOME environment variable

2. **Node.js and npm**
   - Node.js version 16.x or later
   - npm version 8.x or later

3. **Development Tools**
   - IntelliJ IDEA / Eclipse for Spring Boot development
   - Visual Studio Code for Angular development
   - PostgreSQL 13.x or later
   - Git for version control

### Project Setup

#### Backend (Spring Boot)
```bash
# Clone the repository
git clone [repository-url]
cd [project-directory]

# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

#### Frontend (Angular)
```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
ng serve
```

## Coding Standards

### Java/Spring Boot Standards

1. **Project Structure**
```plaintext
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── finance/
│   │           ├── config/
│   │           ├── controller/
│   │           ├── model/
│   │           ├── repository/
│   │           ├── service/
│   │           └── util/
│   └── resources/
│       └── application.yml
└── test/
    └── java/
```

2. **Naming Conventions**
```java
// Classes - PascalCase
public class TransferService {}

// Methods and variables - camelCase
public TransferDTO createTransfer(TransferRequest request) {}

// Constants - UPPER_CASE
private static final String API_VERSION = "v1";
```

3. **Code Organization**
```java
@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {
    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<TransferDTO> createTransfer(@Valid @RequestBody TransferRequest request) {
        // Implementation
    }
}
```

### Angular Standards

1. **Project Structure**
```plaintext
src/
├── app/
│   ├── core/
│   │   ├── guards/
│   │   ├── interceptors/
│   │   └── services/
│   ├── features/
│   │   ├── transfers/
│   │   ├── categories/
│   │   └── reports/
│   ├── shared/
│   │   ├── components/
│   │   ├── models/
│   │   └── pipes/
│   └── store/
└── assets/
```

2. **Component Structure**
```typescript
// transfers.component.ts
@Component({
  selector: 'app-transfers',
  templateUrl: './transfers.component.html',
  styleUrls: ['./transfers.component.scss']
})
export class TransfersComponent implements OnInit {
  // Implementation
}
```

3. **State Management**
```typescript
// transfer.actions.ts
export const loadTransfers = createAction('[Transfer] Load Transfers');
export const loadTransfersSuccess = createAction(
  '[Transfer] Load Transfers Success',
  props<{ transfers: Transfer[] }>()
);
```

## Testing Standards

### Backend Testing
```java
@SpringBootTest
class TransferServiceTest {
    @MockBean
    private TransferRepository transferRepository;

    @Autowired
    private TransferService transferService;

    @Test
    void testCreateTransfer() {
        // Test implementation
    }
}
```

### Frontend Testing
```typescript
describe('TransferComponent', () => {
  let component: TransferComponent;
  let fixture: ComponentFixture<TransferComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TransferComponent ],
      imports: [ /* dependencies */ ],
      providers: [ /* services */ ]
    }).compileComponents();
  });

  it('should create transfer', () => {
    // Test implementation
  });
});
```

## API Integration

### Making API Calls
```typescript
// transfer.service.ts
@Injectable({
  providedIn: 'root'
})
export class TransferService {
  private apiUrl = 'api/v1/transfers';

  constructor(private http: HttpClient) {}

  createTransfer(transfer: TransferRequest): Observable<TransferDTO> {
    return this.http.post<TransferDTO>(this.apiUrl, transfer);
  }
}
```

### Error Handling
```typescript
// error.interceptor.ts
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      catchError(error => {
        // Error handling logic
        return throwError(error);
      })
    );
  }
}
```

## Database Migration

### Spring Boot Database Configuration
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/finance_db
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

### Flyway Migration
```sql
-- V1__create_transfers_table.sql
CREATE TABLE transfers (
    id BIGSERIAL PRIMARY KEY,
    description TEXT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Security Implementation

### Spring Security Configuration
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/v1/auth/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter(new JwtAuthenticationFilter(authenticationManager()));
    }
}
```

### Angular Security
```typescript
// auth.guard.ts
@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}
```

## Build and Deployment

### Backend Build
```bash
# Production build
./mvnw clean package -P prod

# Run tests
./mvnw test
```

### Frontend Build
```bash
# Production build
ng build --configuration production

# Run tests
ng test --watch=false --browsers=ChromeHeadless
```

### Docker Deployment
```dockerfile
# Backend Dockerfile
FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# Frontend Dockerfile
FROM nginx:alpine
COPY dist/frontend /usr/share/nginx/html
```

## Monitoring and Logging

### Spring Boot Actuator
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,info
```

### Angular Logging
```typescript
// logger.service.ts
@Injectable({
  providedIn: 'root'
})
export class LoggerService {
  log(message: string, level: LogLevel = LogLevel.Info) {
    // Logging implementation
  }
}
```

## Troubleshooting Guide

1. **Common Issues**
   - Database connection problems
   - CORS configuration
   - Authentication token issues
   - Build failures

2. **Debug Tools**
   - Spring Boot DevTools
   - Chrome DevTools
   - Angular Debug Tools
   - Database monitoring tools

## Performance Optimization

1. **Backend Optimization**
   - Query optimization
   - Caching implementation
   - Connection pooling
   - Async processing

2. **Frontend Optimization**
   - Lazy loading
   - Bundle optimization
   - Image optimization
   - Service worker implementation

This guide serves as a comprehensive reference for developers working on the modernization project. Follow these guidelines to ensure consistency and quality throughout the development process.