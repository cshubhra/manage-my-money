# Target State Sequence Diagram

## Key User Flows in the Modernized Application

This document illustrates the primary interaction flows in the target Angular/Spring Boot application.

### 1. User Authentication Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant AuthService
    participant HttpInterceptor
    participant SpringBoot
    participant JWTService
    participant UserService
    participant Database
    
    User->>Angular: Navigate to login page
    Angular->>Angular: Route to login component
    
    User->>Angular: Enter credentials
    Angular->>AuthService: login(username, password)
    AuthService->>SpringBoot: POST /api/auth/login
    SpringBoot->>UserService: authenticate(username, password)
    UserService->>Database: Find user and verify password
    Database-->>UserService: User data
    UserService->>JWTService: generateToken(user)
    JWTService-->>SpringBoot: JWT token
    SpringBoot-->>AuthService: Return JWT token & user info
    AuthService->>AuthService: Store token in storage/cookies
    AuthService-->>Angular: Authentication result
    
    alt Authentication successful
        Angular->>Angular: Navigate to dashboard
        Angular->>HttpInterceptor: Register token for future requests
    else Authentication failed
        Angular->>Angular: Display error message
    end
    
    Note over User,Database: Subsequent API Calls
    
    User->>Angular: Request protected resource
    Angular->>HttpInterceptor: Intercept request
    HttpInterceptor->>HttpInterceptor: Add Authorization header
    HttpInterceptor->>SpringBoot: API request with JWT
    SpringBoot->>JWTService: validateToken(token)
    JWTService-->>SpringBoot: Token validation result
    SpringBoot->>SpringBoot: Process API request
    SpringBoot-->>Angular: API response
    Angular-->>User: Display data
```

### 2. Transfer Creation Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant TransferService
    participant NgRx
    participant HttpClient
    participant SpringBoot
    participant TransferController
    participant TransferService
    participant CategoryService
    participant Database
    
    User->>Angular: Navigate to transfers page
    Angular->>NgRx: Dispatch loadCategories action
    Angular->>NgRx: Dispatch loadCurrencies action
    NgRx->>HttpClient: GET /api/categories
    NgRx->>HttpClient: GET /api/currencies
    HttpClient->>SpringBoot: API requests
    SpringBoot->>CategoryService: findByUser(userId)
    SpringBoot->>Database: Query categories/currencies
    Database-->>SpringBoot: Data
    SpringBoot-->>HttpClient: JSON response
    HttpClient-->>NgRx: Update store
    NgRx-->>Angular: State updated
    Angular-->>User: Display transfer form
    
    User->>Angular: Fill transfer form
    User->>Angular: Submit form
    Angular->>Angular: Validate form input
    Angular->>NgRx: Dispatch createTransfer action
    NgRx->>TransferService: createTransfer(transferData)
    TransferService->>HttpClient: POST /api/transfers
    HttpClient->>SpringBoot: Transfer data
    SpringBoot->>TransferController: createTransfer(transferDTO)
    TransferController->>TransferService: createTransfer(transferDTO)
    TransferService->>Database: Begin transaction
    TransferService->>Database: Save transfer
    TransferService->>Database: Save transfer items
    TransferService->>Database: Commit transaction
    Database-->>TransferService: Success
    TransferService-->>TransferController: Transfer entity
    TransferController-->>HttpClient: Transfer response
    HttpClient-->>NgRx: Action success
    NgRx-->>NgRx: Update transfers state
    NgRx-->>Angular: State updated
    Angular-->>User: Display success notification
    Angular->>Angular: Navigate to or update transfers list
```

### 3. Report Generation Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant ReportsService
    participant NgRx
    participant HttpClient
    participant SpringBoot
    participant ReportController
    participant ReportService
    participant TransactionService
    participant Database
    
    User->>Angular: Navigate to reports page
    Angular->>NgRx: Dispatch loadReports action
    NgRx->>HttpClient: GET /api/reports
    HttpClient->>SpringBoot: Request reports
    SpringBoot->>ReportService: findByUser(userId)
    ReportService->>Database: Query reports
    Database-->>ReportService: Report data
    ReportService-->>SpringBoot: Report DTOs
    SpringBoot-->>HttpClient: JSON response
    HttpClient-->>NgRx: Update reports state
    NgRx-->>Angular: State updated
    Angular-->>User: Display reports list
    
    User->>Angular: Request to create new report
    Angular->>NgRx: Dispatch loadCategories action
    NgRx->>HttpClient: GET /api/categories
    HttpClient->>SpringBoot: Request categories
    SpringBoot-->>HttpClient: Categories data
    HttpClient-->>NgRx: Update state
    NgRx-->>Angular: State updated
    Angular-->>User: Display report creation form
    
    User->>Angular: Fill report form
    User->>Angular: Submit form
    Angular->>Angular: Validate form
    Angular->>NgRx: Dispatch createReport action
    NgRx->>ReportsService: createReport(reportData)
    ReportsService->>HttpClient: POST /api/reports
    HttpClient->>SpringBoot: Report data
    SpringBoot->>ReportController: createReport(reportDTO)
    ReportController->>ReportService: createReport(reportDTO)
    ReportService->>Database: Save report
    Database-->>ReportService: Success
    ReportService-->>ReportController: Report entity
    ReportController-->>HttpClient: Report response
    HttpClient-->>NgRx: Action success
    NgRx-->>NgRx: Update reports state
    NgRx-->>Angular: State updated
    
    User->>Angular: View report
    Angular->>NgRx: Dispatch loadReportData action
    NgRx->>ReportsService: getReportData(reportId)
    ReportsService->>HttpClient: GET /api/reports/{id}/data
    HttpClient->>SpringBoot: Request report data
    SpringBoot->>ReportService: generateReportData(reportId)
    ReportService->>TransactionService: getTransactionsForPeriod()
    TransactionService->>Database: Query transactions
    Database-->>TransactionService: Transaction data
    TransactionService-->>ReportService: Transactions
    ReportService->>ReportService: Process data
    ReportService-->>SpringBoot: Report data
    SpringBoot-->>HttpClient: JSON data
    HttpClient-->>NgRx: Update report data state
    NgRx-->>Angular: State updated
    Angular->>Angular: Process data for charts
    Angular-->>User: Display report with charts
```

### 4. Category Management Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant CategoryService
    participant NgRx
    participant HttpClient
    participant SpringBoot
    participant CategoryController
    participant CategoryService
    participant Database
    
    User->>Angular: Navigate to categories page
    Angular->>NgRx: Dispatch loadCategories action
    NgRx->>CategoryService: getCategories()
    CategoryService->>HttpClient: GET /api/categories
    HttpClient->>SpringBoot: Request categories
    SpringBoot->>CategoryController: getCategories()
    CategoryController->>CategoryService: findByUser(userId)
    CategoryService->>Database: Query categories
    Database-->>CategoryService: Category data
    CategoryService-->>CategoryController: Category entities
    CategoryController-->>HttpClient: Categories JSON
    HttpClient-->>NgRx: Update categories state
    NgRx-->>Angular: State updated
    Angular->>Angular: Process hierarchy for tree view
    Angular-->>User: Display category tree
    
    User->>Angular: Request to create new category
    Angular->>Angular: Show category form
    
    User->>Angular: Fill category form
    User->>Angular: Submit form
    Angular->>Angular: Validate form
    Angular->>NgRx: Dispatch createCategory action
    NgRx->>CategoryService: createCategory(categoryData)
    CategoryService->>HttpClient: POST /api/categories
    HttpClient->>SpringBoot: Category data
    SpringBoot->>CategoryController: createCategory(categoryDTO)
    CategoryController->>CategoryService: save(categoryDTO)
    CategoryService->>Database: Save category
    Database-->>CategoryService: Success
    CategoryService-->>CategoryController: Category entity
    CategoryController-->>HttpClient: Category response
    HttpClient-->>NgRx: Action success
    NgRx-->>NgRx: Update categories state
    NgRx-->>Angular: State updated
    Angular-->>User: Update category tree
```

### 5. Goal Setting Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant GoalService
    participant NgRx
    participant HttpClient
    participant SpringBoot
    participant GoalController
    participant GoalService
    participant CategoryService
    participant Database
    
    User->>Angular: Navigate to goals page
    Angular->>NgRx: Dispatch loadGoals action
    Angular->>NgRx: Dispatch loadCategories action
    NgRx->>HttpClient: Multiple API requests
    HttpClient->>SpringBoot: GET /api/goals
    HttpClient->>SpringBoot: GET /api/categories
    SpringBoot->>GoalService: findByUser(userId)
    SpringBoot->>CategoryService: findByUser(userId)
    SpringBoot->>Database: Query goals/categories
    Database-->>SpringBoot: Data
    SpringBoot-->>HttpClient: JSON responses
    HttpClient-->>NgRx: Update store
    NgRx-->>Angular: State updated
    Angular-->>User: Display goals list
    
    User->>Angular: Request to create new goal
    Angular->>Angular: Display goal creation form
    
    User->>Angular: Fill goal form
    User->>Angular: Submit form
    Angular->>Angular: Validate form
    Angular->>NgRx: Dispatch createGoal action
    NgRx->>GoalService: createGoal(goalData)
    GoalService->>HttpClient: POST /api/goals
    HttpClient->>SpringBoot: Goal data
    SpringBoot->>GoalController: createGoal(goalDTO)
    GoalController->>GoalService: save(goalDTO)
    GoalService->>Database: Save goal
    Database-->>GoalService: Success
    GoalService-->>GoalController: Goal entity
    GoalController-->>HttpClient: Goal response
    HttpClient-->>NgRx: Action success
    NgRx-->>NgRx: Update goals state
    NgRx-->>Angular: State updated
    Angular-->>User: Display success and update goals list
```

These sequence diagrams illustrate the key user flows in the target Angular/Spring Boot application, showing the interactions between the user, frontend, state management, API layer, services, and database.