# Target State Sequence Diagrams

This document outlines the key interaction flows in the target Angular and Spring Boot architecture through sequence diagrams.

## 1. Authentication Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant AuthGuard
    participant AuthService
    participant SpringBoot
    participant JWTService
    participant Database

    User->>Angular: Access Protected Route
    Angular->>AuthGuard: Check Authentication
    
    alt Not Authenticated
        AuthGuard->>Angular: Redirect to Login
        User->>Angular: Enter Credentials
        Angular->>AuthService: Login Request
        AuthService->>SpringBoot: POST /api/auth/login
        SpringBoot->>Database: Validate Credentials
        SpringBoot->>JWTService: Generate Token
        JWTService-->>SpringBoot: JWT Token
        SpringBoot-->>Angular: Return JWT Token
        Angular->>AuthService: Store Token
        AuthService-->>Angular: Navigate to Dashboard
    else Already Authenticated
        AuthGuard->>Angular: Allow Route Access
    end
```

## 2. Transfer Management Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant TransferComponent
    participant NgRxStore
    participant TransferService
    participant SpringBoot
    participant ValidationService
    participant Database

    User->>Angular: Create Transfer
    Angular->>TransferComponent: Input Transfer Data
    TransferComponent->>NgRxStore: Dispatch Create Action
    
    NgRxStore->>TransferService: Create Transfer Request
    TransferService->>SpringBoot: POST /api/transfers
    
    SpringBoot->>ValidationService: Validate Transfer
    ValidationService-->>SpringBoot: Validation Result
    
    alt Valid Transfer
        SpringBoot->>Database: Save Transfer
        Database-->>SpringBoot: Confirmation
        SpringBoot-->>TransferService: Success Response
        TransferService->>NgRxStore: Update State
        NgRxStore->>TransferComponent: Update View
        TransferComponent->>Angular: Show Success Message
    else Invalid Transfer
        SpringBoot-->>TransferService: Error Response
        TransferService->>NgRxStore: Error Action
        NgRxStore->>TransferComponent: Update View
        TransferComponent->>Angular: Show Error Message
    end
```

## 3. Report Generation Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant ReportComponent
    participant NgRxStore
    participant ReportService
    participant SpringBoot
    participant ReportGenerator
    participant Database

    User->>Angular: Request Report
    Angular->>ReportComponent: Set Report Parameters
    ReportComponent->>NgRxStore: Dispatch Report Action
    
    NgRxStore->>ReportService: Generate Report Request
    ReportService->>SpringBoot: POST /api/reports
    
    SpringBoot->>ReportGenerator: Process Report
    ReportGenerator->>Database: Fetch Data
    Database-->>ReportGenerator: Raw Data
    
    ReportGenerator->>ReportGenerator: Generate Charts
    ReportGenerator-->>SpringBoot: Report Data
    
    SpringBoot-->>ReportService: Report Response
    ReportService->>NgRxStore: Update State
    NgRxStore->>ReportComponent: Update View
    ReportComponent->>Angular: Display Report
```

## 4. Category Management Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant CategoryComponent
    participant NgRxStore
    participant CategoryService
    participant SpringBoot
    participant ValidationService
    participant Database

    User->>Angular: Manage Categories
    Angular->>CategoryComponent: Load Categories
    CategoryComponent->>NgRxStore: Request Categories
    
    NgRxStore->>CategoryService: Fetch Categories
    CategoryService->>SpringBoot: GET /api/categories
    SpringBoot->>Database: Query Categories
    Database-->>SpringBoot: Category Data
    SpringBoot-->>CategoryService: Category Response
    CategoryService->>NgRxStore: Update State
    NgRxStore->>CategoryComponent: Update View
    
    alt Create Category
        User->>Angular: Add Category
        Angular->>CategoryComponent: Input Category Data
        CategoryComponent->>NgRxStore: Dispatch Create Action
        NgRxStore->>CategoryService: Create Request
        CategoryService->>SpringBoot: POST /api/categories
        SpringBoot->>ValidationService: Validate Category
        ValidationService-->>SpringBoot: Validation Result
        SpringBoot->>Database: Save Category
        Database-->>SpringBoot: Confirmation
        SpringBoot-->>CategoryService: Success Response
        CategoryService->>NgRxStore: Update State
        NgRxStore->>CategoryComponent: Update View
    end
```

## 5. Multi-Currency Exchange Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant ExchangeComponent
    participant NgRxStore
    participant ExchangeService
    participant SpringBoot
    participant ExchangeProvider
    participant Database

    User->>Angular: Currency Exchange
    Angular->>ExchangeComponent: Input Exchange Details
    ExchangeComponent->>NgRxStore: Request Exchange Rate
    
    NgRxStore->>ExchangeService: Get Exchange Rate
    ExchangeService->>SpringBoot: GET /api/exchanges/rate
    
    SpringBoot->>ExchangeProvider: Fetch Current Rate
    ExchangeProvider-->>SpringBoot: Exchange Rate
    SpringBoot->>Database: Store Rate
    Database-->>SpringBoot: Confirmation
    
    SpringBoot-->>ExchangeService: Rate Response
    ExchangeService->>NgRxStore: Update State
    NgRxStore->>ExchangeComponent: Update View
    ExchangeComponent->>Angular: Display Rate
    
    alt Perform Exchange
        User->>Angular: Confirm Exchange
        Angular->>ExchangeComponent: Process Exchange
        ExchangeComponent->>NgRxStore: Dispatch Exchange Action
        NgRxStore->>ExchangeService: Execute Exchange
        ExchangeService->>SpringBoot: POST /api/exchanges
        SpringBoot->>Database: Record Exchange
        Database-->>SpringBoot: Confirmation
        SpringBoot-->>ExchangeService: Success Response
        ExchangeService->>NgRxStore: Update State
        NgRxStore->>ExchangeComponent: Update View
    end
```

These sequence diagrams illustrate the main interaction flows in the target architecture, showing how the Angular frontend communicates with the Spring Boot backend through RESTful APIs, and how the system handles different business processes. They demonstrate:

1. Clean separation of concerns between frontend and backend
2. State management using NgRx in Angular
3. Proper validation and error handling
4. Asynchronous communication patterns
5. Database interactions
6. Service layer abstraction
7. Component-based architecture in Angular
8. RESTful API design in Spring Boot

The diagrams serve as a guide for implementing the modernized system while maintaining proper architectural patterns and best practices.