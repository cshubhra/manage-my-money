# Target State Sequence Diagrams

This document outlines the key interactions in the target state architecture with Angular frontend and Spring Boot backend.

## Authentication Flow

```mermaid
sequenceDiagram
    actor User
    participant AngularApp
    participant AuthService
    participant API
    participant JWTService
    participant Database

    User->>AngularApp: Enter login credentials
    AngularApp->>AuthService: login(credentials)
    AuthService->>API: POST /api/auth/login
    API->>JWTService: validateCredentials()
    JWTService->>Database: findUser()
    Database-->>JWTService: user data
    JWTService->>JWTService: generate JWT token
    JWTService-->>API: JWT token
    API-->>AuthService: JWT token + user info
    AuthService->>AuthService: store token
    AuthService-->>AngularApp: auth success
    AngularApp->>AngularApp: redirect to dashboard
```

## Transfer Creation Flow

```mermaid
sequenceDiagram
    actor User
    participant AngularApp
    participant TransferService
    participant StateManager
    participant API
    participant Backend
    participant Cache
    participant Database

    User->>AngularApp: Fill transfer form
    AngularApp->>TransferService: createTransfer(data)
    TransferService->>StateManager: dispatch(CREATE_TRANSFER)
    TransferService->>API: POST /api/transfers
    API->>Backend: createTransfer()
    Backend->>Database: save transfer
    Database-->>Backend: confirmation
    Backend->>Cache: invalidate cache
    Backend-->>API: transfer data
    API-->>TransferService: transfer response
    TransferService->>StateManager: dispatch(CREATE_TRANSFER_SUCCESS)
    StateManager-->>AngularApp: state updated
    AngularApp->>User: show success message
```

## Report Generation Flow

```mermaid
sequenceDiagram
    actor User
    participant AngularApp
    participant ReportService
    participant StateManager
    participant API
    participant Backend
    participant Cache
    participant Database

    User->>AngularApp: Request report
    AngularApp->>ReportService: generateReport(params)
    ReportService->>StateManager: dispatch(GENERATE_REPORT)
    ReportService->>API: GET /api/reports
    API->>Backend: generateReport()
    Backend->>Cache: check cached report
    Cache-->>Backend: cache miss
    Backend->>Database: fetch data
    Database-->>Backend: raw data
    Backend->>Backend: process report
    Backend->>Cache: store report
    Backend-->>API: report data
    API-->>ReportService: report response
    ReportService->>StateManager: dispatch(GENERATE_REPORT_SUCCESS)
    StateManager-->>AngularApp: state updated
    AngularApp->>User: display report
```

## Category Management Flow

```mermaid
sequenceDiagram
    actor User
    participant AngularApp
    participant CategoryService
    participant StateManager
    participant API
    participant Backend
    participant Cache
    participant Database

    User->>AngularApp: Create category
    AngularApp->>CategoryService: createCategory(data)
    CategoryService->>StateManager: dispatch(CREATE_CATEGORY)
    CategoryService->>API: POST /api/categories
    API->>Backend: createCategory()
    Backend->>Database: save category
    Database-->>Backend: confirmation
    Backend->>Cache: invalidate categories
    Backend-->>API: category data
    API-->>CategoryService: category response
    CategoryService->>StateManager: dispatch(CREATE_CATEGORY_SUCCESS)
    StateManager-->>AngularApp: state updated
    AngularApp->>User: show success message
```

## Goal Progress Tracking Flow

```mermaid
sequenceDiagram
    actor User
    participant AngularApp
    participant GoalService
    participant StateManager
    participant API
    participant Backend
    participant Database

    User->>AngularApp: View goal progress
    AngularApp->>GoalService: getGoalProgress(goalId)
    GoalService->>StateManager: dispatch(FETCH_GOAL_PROGRESS)
    GoalService->>API: GET /api/goals/{id}/progress
    API->>Backend: calculateProgress()
    Backend->>Database: fetch goal & transfers
    Database-->>Backend: goal & transfer data
    Backend->>Backend: calculate progress
    Backend-->>API: progress data
    API-->>GoalService: progress response
    GoalService->>StateManager: dispatch(FETCH_GOAL_PROGRESS_SUCCESS)
    StateManager-->>AngularApp: state updated
    AngularApp->>User: display progress
```

## Currency Exchange Flow

```mermaid
sequenceDiagram
    actor User
    participant AngularApp
    participant ExchangeService
    participant StateManager
    participant API
    participant Backend
    participant ExternalRateAPI
    participant Database

    User->>AngularApp: Input exchange details
    AngularApp->>ExchangeService: calculateExchange(data)
    ExchangeService->>API: POST /api/exchanges/calculate
    API->>Backend: calculateExchange()
    Backend->>ExternalRateAPI: getCurrentRate()
    ExternalRateAPI-->>Backend: exchange rate
    Backend->>Backend: calculate amount
    Backend-->>API: calculation result
    API-->>ExchangeService: exchange data
    ExchangeService->>StateManager: update state
    StateManager-->>AngularApp: display result
    User->>AngularApp: Confirm exchange
    AngularApp->>ExchangeService: executeExchange(data)
    ExchangeService->>API: POST /api/exchanges
    API->>Backend: processExchange()
    Backend->>Database: save exchange
    Database-->>Backend: confirmation
    Backend-->>API: exchange record
    API-->>ExchangeService: exchange confirmation
    ExchangeService->>StateManager: update state
    StateManager-->>AngularApp: display confirmation
    AngularApp->>User: show success message
```

These sequence diagrams illustrate the main interactions between the frontend and backend components in the modernized architecture. They show how the system handles:

1. User authentication with JWT
2. Transfer management with state updates
3. Report generation with caching
4. Category management
5. Goal progress tracking
6. Currency exchange operations

Key improvements in the new architecture:
- Client-side state management
- Caching strategy
- Real-time updates
- Improved error handling
- Better separation of concerns
- Enhanced security with JWT
- Optimized data flow