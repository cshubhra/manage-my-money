# Sequence Diagrams

This document contains sequence diagrams for the major workflows in the financial management system.

## User Authentication Flow

```mermaid
sequenceDiagram
    participant User
    participant Angular as Angular Frontend
    participant Auth as Auth Service
    participant Spring as Spring Boot Backend
    participant DB as Database
    
    User->>Angular: Enter login credentials
    Angular->>Spring: POST /api/auth/login
    Spring->>DB: Validate credentials
    DB->>Spring: Return user data
    Spring->>Spring: Generate JWT token
    Spring->>Angular: Return JWT + user info
    Angular->>Auth: Store token in auth service
    Angular->>User: Redirect to dashboard
```

## Category Management Flow

```mermaid
sequenceDiagram
    participant User
    participant Angular as Angular Frontend
    participant Spring as Spring Boot Backend
    participant DB as Database
    
    User->>Angular: Navigate to categories
    Angular->>Spring: GET /api/categories
    Spring->>DB: Fetch categories
    DB->>Spring: Return categories
    Spring->>Angular: Return categories as tree
    Angular->>User: Display category hierarchy
    
    User->>Angular: Create new category
    Angular->>Spring: POST /api/categories
    Spring->>DB: Save new category
    DB->>Spring: Confirm save
    Spring->>Angular: Return updated category
    Angular->>User: Show updated category list
```

## Transfer Creation Flow

```mermaid
sequenceDiagram
    participant User
    participant Angular as Angular Frontend
    participant Spring as Spring Boot Backend
    participant DB as Database
    
    User->>Angular: Navigate to new transfer
    Angular->>Spring: GET /api/categories
    Spring->>Angular: Return categories list
    Angular->>Spring: GET /api/currencies
    Spring->>Angular: Return currencies list
    Angular->>User: Display transfer form
    
    User->>Angular: Fill transfer details and submit
    Angular->>Angular: Validate form data
    Angular->>Spring: POST /api/transfers
    Spring->>Spring: Validate transfer balance
    Spring->>DB: Save transfer data
    DB->>Spring: Confirm save
    Spring->>Angular: Return created transfer
    Angular->>User: Show confirmation & updated balance
```

## Report Generation Flow

```mermaid
sequenceDiagram
    participant User
    participant Angular as Angular Frontend
    participant Spring as Spring Boot Backend
    participant DB as Database
    
    User->>Angular: Navigate to reports
    Angular->>Spring: GET /api/reports/types
    Spring->>Angular: Return available report types
    Angular->>User: Display report options
    
    User->>Angular: Select report parameters
    Angular->>Spring: POST /api/reports/generate
    Spring->>DB: Query data for report
    DB->>Spring: Return report data
    Spring->>Spring: Process data calculations
    Spring->>Angular: Return processed report data
    Angular->>Angular: Format for display/visualization
    Angular->>User: Display report with charts/tables
```

## Currency Exchange Management Flow

```mermaid
sequenceDiagram
    participant User
    participant Angular as Angular Frontend
    participant Spring as Spring Boot Backend
    participant DB as Database
    
    User->>Angular: Navigate to currencies/exchanges
    Angular->>Spring: GET /api/currencies
    Spring->>Angular: Return currencies
    Angular->>Spring: GET /api/exchanges
    Spring->>Angular: Return exchange rates
    Angular->>User: Display currencies and rates
    
    User->>Angular: Create/update exchange rate
    Angular->>Spring: POST/PUT /api/exchanges
    Spring->>DB: Save exchange data
    DB->>Spring: Confirm save
    Spring->>Angular: Return updated exchange
    Angular->>User: Show updated exchange rates
```