# Application Architecture

This document describes the high-level architecture of the Financial Management System after migration from Ruby on Rails to Angular and Spring Boot.

## Overall Architecture

The application follows a modern client-server architecture with a clear separation between frontend and backend:

```
┌─────────────────┐      ┌────────────────────────┐
│                 │      │                        │
│  Angular        │      │  Spring Boot           │
│  Frontend       │ <──> │  Backend               │
│                 │ HTTP │                        │
└─────────────────┘      └────────────────────────┘
                                    │
                                    │
                               ┌────▼─────┐
                               │          │
                               │ Database │
                               │          │
                               └──────────┘
```

## Frontend Architecture (Angular)

The Angular frontend follows a modular architecture organized by feature:

```
angular-frontend/
├── src/
│   ├── app/
│   │   ├── core/                # Core services and components
│   │   │   ├── auth/            # Authentication & authorization services
│   │   │   ├── interceptors/    # HTTP interceptors
│   │   │   └── services/        # Common services
│   │   │
│   │   ├── features/            # Feature modules
│   │   │   ├── categories/      # Category management
│   │   │   ├── currencies/      # Currency management
│   │   │   ├── exchanges/       # Exchange rate management
│   │   │   ├── goals/           # Financial goals
│   │   │   ├── reports/         # Report generation
│   │   │   ├── transfers/       # Transfer management
│   │   │   └── user/            # User profile management
│   │   │
│   │   ├── shared/              # Shared components, directives, and pipes
│   │   │   ├── components/      # Reusable components
│   │   │   ├── directives/      # Custom directives
│   │   │   └── pipes/           # Custom pipes
│   │   │
│   │   ├── app-routing.module.ts # Main routing configuration
│   │   ├── app.component.ts      # Root component
│   │   └── app.module.ts         # Root module
│   │
│   ├── assets/                   # Static assets
│   ├── environments/             # Environment configurations
│   └── index.html                # Main HTML entry point
```

## Backend Architecture (Spring Boot)

The Spring Boot backend follows a layered architecture:

```
spring-boot-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/financialmanager/
│   │   │       ├── config/           # Configuration classes
│   │   │       ├── controllers/      # REST controllers
│   │   │       │   ├── CategoryController
│   │   │       │   ├── CurrencyController
│   │   │       │   ├── ExchangeController
│   │   │       │   ├── GoalController
│   │   │       │   ├── ReportController
│   │   │       │   ├── TransferController
│   │   │       │   └── UserController
│   │   │       │
│   │   │       ├── dto/              # Data Transfer Objects
│   │   │       ├── exceptions/       # Custom exceptions
│   │   │       ├── models/           # Entity models
│   │   │       ├── repositories/     # Data access repositories
│   │   │       ├── security/         # Security configuration
│   │   │       ├── services/         # Business logic services
│   │   │       └── FinancialManagerApplication.java  # Main application class
│   │   │
│   │   └── resources/
│   │       ├── application.properties      # Application configuration
│   │       ├── db/migration/               # Flyway database migrations
│   │       └── static/                     # Static resources
│   │
│   └── test/  # Unit and integration tests
```

## Communication Flow

1. **Authentication Flow**
   - The user logs in through the Angular frontend
   - Angular makes a request to Spring Security endpoints
   - JWT tokens are issued for subsequent authenticated requests

2. **Data Flow**
   - Angular components call services that make HTTP requests to Spring Boot endpoints
   - Spring Boot controllers receive requests and delegate to the appropriate services
   - Services implement business logic and interact with repositories
   - Repositories use Spring Data JPA to interact with the database
   - Results flow back through the layers to the frontend

3. **Transaction Flow**
   - When a user creates a financial transaction:
     1. Angular validates input data in the frontend
     2. Angular sends a POST/PUT request to the appropriate endpoint
     3. Spring Boot performs validation and business logic processing
     4. Transaction data is saved in a transactional context
     5. Spring Boot returns results to Angular
     6. Angular updates UI to reflect changes

## Cross-Cutting Concerns

- **Authentication & Authorization**  
  JWT-based authentication with Spring Security and Angular interceptors

- **Data Validation**  
  Both client-side (Angular reactive forms) and server-side (Spring validators)

- **Error Handling**  
  Global error handlers in both Angular and Spring Boot

- **Logging**  
  Structured logging in the backend, console logging in development for frontend

- **Internationalization**  
  Angular i18n for frontend, Spring MessageSource for backend messages

## API Communication

All communication between frontend and backend happens through RESTful API endpoints following standard HTTP methods:

- **GET** - Retrieve data
- **POST** - Create new resources
- **PUT** - Update existing resources
- **DELETE** - Remove resources

API documentation is available via Swagger/OpenAPI at `/api/swagger-ui.html` when the application is running.