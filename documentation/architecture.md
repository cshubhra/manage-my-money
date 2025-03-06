# Application Architecture

## Original Ruby on Rails Architecture

The original application follows the standard Ruby on Rails MVC architecture:

### Models
- User: Authentication and user data
- Category: Organization of financial transactions
- Currency: Different currency types
- Exchange: Currency exchange rates and transactions
- Goal: Financial goals tracking
- Report: Custom financial reports
- Transfer: Financial transfers
- TransferItem: Individual items within a transfer

### Controllers
- ApplicationController: Base controller
- CategoriesController: CRUD for categories
- CurrenciesController: CRUD for currencies
- ExchangesController: Handle currency exchanges
- GoalsController: Financial goal management
- ReportsController: Generate and manage reports
- SessionsController: Authentication
- TransfersController: Manage financial transfers
- UsersController: User management

### Views
A collection of ERB templates organized by controller, handling the presentation logic.

## New Angular/Spring Boot Architecture

The new architecture separates concerns between frontend and backend:

### Backend (Spring Boot)

#### Core Components
- **Controllers**: REST API endpoints
- **Services**: Business logic layer
- **Repositories**: Data access layer
- **Models/Entities**: Data structures
- **DTOs**: Data transfer objects
- **Config**: Application configuration

#### Package Structure
```
com.financialapp/
├── config/
├── controller/
├── dto/
├── exception/
├── model/
├── repository/
├── security/
├── service/
└── util/
```

### Frontend (Angular)

#### Core Components
- **Components**: UI building blocks
- **Services**: Data access and business logic
- **Models**: TypeScript interfaces/classes
- **Guards**: Route protection
- **Interceptors**: HTTP request/response handling
- **Modules**: Feature organization

#### Folder Structure
```
src/
├── app/
│   ├── core/
│   │   ├── authentication/
│   │   ├── guards/
│   │   ├── interceptors/
│   │   ├── models/
│   │   └── services/
│   ├── features/
│   │   ├── categories/
│   │   ├── currencies/
│   │   ├── dashboard/
│   │   ├── exchanges/
│   │   ├── goals/
│   │   ├── reports/
│   │   ├── transfers/
│   │   └── user-profile/
│   ├── shared/
│   │   ├── components/
│   │   ├── directives/
│   │   └── pipes/
│   └── app.module.ts
├── assets/
└── environments/
```

## Communication Flow

1. Angular frontend makes HTTP requests to Spring Boot backend
2. Spring Boot processes requests through controllers
3. Controllers delegate to services for business logic
4. Services use repositories for data access
5. Data is transferred using DTOs
6. Spring Boot returns HTTP responses with JSON data
7. Angular processes and displays the data

## Security Architecture

- JWT-based authentication
- Role-based access control
- HTTPS encryption
- CSRF protection
- Input validation
- XSS prevention