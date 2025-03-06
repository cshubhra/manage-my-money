# Sequence Diagrams

This document contains sequence diagrams illustrating the key flows in the Financial Management System.

## User Authentication Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant AuthService
    participant API as Spring Boot API
    participant AuthController
    participant JwtTokenProvider
    participant UserService
    participant DB as Database

    User->>Angular: Enter credentials
    Angular->>AuthService: login(email, password)
    AuthService->>API: POST /api/auth/login
    API->>AuthController: login(LoginRequest)
    AuthController->>UserService: authenticateUser(email, password)
    UserService->>DB: Find user by email
    DB-->>UserService: User details
    UserService->>UserService: Verify password
    UserService-->>AuthController: Authentication result
    AuthController->>JwtTokenProvider: generateToken(userId)
    JwtTokenProvider-->>AuthController: JWT token
    AuthController-->>API: LoginResponse (token)
    API-->>AuthService: LoginResponse (token)
    AuthService->>AuthService: Store token
    AuthService-->>Angular: Authentication success
    Angular->>Angular: Navigate to dashboard
```

## Create Transfer Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant TransferService
    participant API as Spring Boot API
    participant TransferController
    participant TransferValidator
    participant TransferManager
    participant DB as Database

    User->>Angular: Fill transfer form
    Angular->>Angular: Validate form data
    Angular->>TransferService: createTransfer(transferData)
    
    TransferService->>API: POST /api/transfers
    API->>TransferController: createTransfer(TransferDTO)
    
    TransferController->>TransferValidator: validate(transferDTO)
    TransferValidator->>TransferValidator: Check balance
    TransferValidator->>TransferValidator: Validate categories
    TransferValidator->>TransferValidator: Validate currencies
    TransferValidator-->>TransferController: Validation result
    
    alt Invalid transfer
        TransferController-->>API: Error response
        API-->>TransferService: Error response
        TransferService-->>Angular: Error details
        Angular->>User: Show validation errors
    else Valid transfer
        TransferController->>TransferManager: createTransfer(transferDTO)
        TransferManager->>DB: Begin transaction
        TransferManager->>DB: Save transfer
        TransferManager->>DB: Save transfer items
        
        opt Multi-currency transfer
            TransferManager->>DB: Save currency conversions
        end
        
        TransferManager->>DB: Commit transaction
        TransferManager-->>TransferController: Created transfer
        TransferController-->>API: Transfer response
        API-->>TransferService: Transfer data
        TransferService-->>Angular: Update UI
        Angular->>User: Show success message
    end
```

## Category Balance Calculation Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant CategoryService
    participant API as Spring Boot API
    participant CategoryController
    participant BalanceCalculator
    participant DB as Database

    User->>Angular: Select category and view balance
    Angular->>CategoryService: getCategoryBalance(categoryId, includeSubcategories)
    CategoryService->>API: GET /api/categories/{id}/balance?includeSubcategories={bool}
    API->>CategoryController: getCategoryBalance(id, includeSubcategories)
    
    CategoryController->>BalanceCalculator: calculateBalance(categoryId, includeSubcategories)
    
    alt Include subcategories
        BalanceCalculator->>DB: Get category with descendants
    else Category only
        BalanceCalculator->>DB: Get single category
    end
    
    DB-->>BalanceCalculator: Category data
    
    BalanceCalculator->>DB: Get transfer items for categories
    DB-->>BalanceCalculator: Transfer items
    
    alt Multi-currency
        BalanceCalculator->>DB: Get relevant exchange rates
        DB-->>BalanceCalculator: Exchange rates
        BalanceCalculator->>BalanceCalculator: Apply currency algorithm
    end
    
    BalanceCalculator->>BalanceCalculator: Sum values by currency
    BalanceCalculator-->>CategoryController: Balance per currency
    
    CategoryController-->>API: Balance response
    API-->>CategoryService: Balance data
    CategoryService-->>Angular: Update UI
    Angular->>User: Display balance information
```

## Goals Progress Tracking Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant GoalService
    participant API as Spring Boot API
    participant GoalController
    participant GoalTracker
    participant CategoryService
    participant DB as Database

    User->>Angular: View goal progress
    Angular->>GoalService: getGoalWithProgress(goalId)
    GoalService->>API: GET /api/goals/{id}
    API->>GoalController: getGoal(id)
    GoalController->>DB: Find goal by id
    DB-->>GoalController: Goal data
    
    GoalController->>GoalTracker: calculateProgress(goal)
    GoalTracker->>CategoryService: getCategoryBalance(goal.categoryId, goal.includeSubcategories)
    
    CategoryService->>DB: Get category data
    DB-->>CategoryService: Category data
    CategoryService->>DB: Get transfer items
    DB-->>CategoryService: Transfer items
    
    CategoryService->>CategoryService: Calculate balance
    CategoryService-->>GoalTracker: Current balance
    
    GoalTracker->>GoalTracker: Compare with goal target
    GoalTracker->>GoalTracker: Calculate percentage progress
    GoalTracker-->>GoalController: Goal with progress data
    
    GoalController-->>API: Goal response
    API-->>GoalService: Goal with progress
    GoalService-->>Angular: Update UI
    Angular->>User: Display goal progress
```

## Report Generation Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant ReportService
    participant API as Spring Boot API
    participant ReportController
    participant ReportGenerator
    participant CategoryService
    participant DB as Database

    User->>Angular: Configure and execute report
    Angular->>ReportService: executeReport(reportConfig)
    ReportService->>API: GET /api/reports/{id}/execute
    API->>ReportController: executeReport(id)
    
    ReportController->>DB: Get report configuration
    DB-->>ReportController: Report configuration
    
    ReportController->>ReportGenerator: generateReport(reportConfig)
    
    alt Flow Report
        ReportGenerator->>CategoryService: getFlowForCategories(categoryIds, period)
    else Share Report
        ReportGenerator->>CategoryService: getCategoryShares(categoryId, depth, period)
    else Value Report
        ReportGenerator->>CategoryService: getCategoryValuesOverTime(categoryId, period, intervals)
    end
    
    CategoryService->>DB: Query transactions
    DB-->>CategoryService: Transaction data
    
    CategoryService->>CategoryService: Process raw data
    CategoryService-->>ReportGenerator: Processed data
    
    ReportGenerator->>ReportGenerator: Apply formatting and calculations
    ReportGenerator-->>ReportController: Report results
    
    opt Save report
        ReportController->>DB: Save report results
        DB-->>ReportController: Confirmation
    end
    
    ReportController-->>API: Report data
    API-->>ReportService: Report results
    ReportService-->>Angular: Update UI
    Angular->>Angular: Generate charts/visualizations
    Angular->>User: Display report
```

## Multi-Currency Transfer with Conversion Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant TransferService
    participant API as Spring Boot API
    participant TransferController
    participant ExchangeService
    participant DB as Database

    User->>Angular: Create multi-currency transfer
    Angular->>Angular: Select different currencies for items
    Angular->>TransferService: getLatestExchangeRate(fromCurrency, toCurrency)
    TransferService->>API: GET /api/exchanges/latest/{fromId}/{toId}
    API->>ExchangeService: getLatestExchangeRate(fromId, toId)
    ExchangeService->>DB: Query latest exchange rate
    DB-->>ExchangeService: Exchange rate data
    ExchangeService-->>API: Exchange rate
    API-->>TransferService: Exchange rate
    TransferService-->>Angular: Display conversion rate
    
    User->>Angular: Adjust values using conversion
    Angular->>Angular: Calculate and balance items
    
    User->>Angular: Submit transfer
    Angular->>TransferService: createTransfer(transferData)
    TransferService->>API: POST /api/transfers
    API->>TransferController: createTransfer(transferDTO)
    
    TransferController->>TransferController: Extract conversion data
    TransferController->>ExchangeService: recordConversion(transferId, exchangeId)
    ExchangeService->>DB: Save conversion record
    DB-->>ExchangeService: Confirmation
    
    TransferController->>DB: Save transfer and items
    DB-->>TransferController: Saved transfer
    
    TransferController-->>API: Transfer with conversions
    API-->>TransferService: Complete transfer data
    TransferService-->>Angular: Update UI
    Angular->>User: Display success message
```

## User Registration and Account Activation Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant AuthService
    participant API as Spring Boot API
    participant UserController
    participant UserService
    participant EmailService
    participant DB as Database

    User->>Angular: Fill registration form
    Angular->>Angular: Validate form data
    Angular->>AuthService: register(userData)
    AuthService->>API: POST /api/auth/register
    API->>UserController: register(RegistrationRequest)
    
    UserController->>UserService: registerUser(userData)
    UserService->>DB: Check if email exists
    DB-->>UserService: Email status
    
    alt Email already exists
        UserService-->>UserController: Error response
        UserController-->>API: Error details
        API-->>AuthService: Error response
        AuthService-->>Angular: Show error
        Angular->>User: Display error message
    else Email available
        UserService->>UserService: Generate activation token
        UserService->>DB: Save new user with activation token
        DB-->>UserService: Saved user
        
        UserService->>EmailService: sendActivationEmail(user, token)
        EmailService-->>User: Email with activation link
        
        UserService-->>UserController: Registration success
        UserController-->>API: Success response
        API-->>AuthService: Registration success
        AuthService-->>Angular: Update UI
        Angular->>User: Show registration confirmation
        
        User->>User: Receive email and click activation link
        User->>Angular: Navigate to activation URL
        Angular->>AuthService: activateAccount(token)
        AuthService->>API: POST /api/auth/activate/{token}
        API->>UserController: activateAccount(token)
        UserController->>UserService: activateUser(token)
        UserService->>DB: Find user by token
        DB-->>UserService: User data
        UserService->>DB: Update user as activated
        DB-->>UserService: Confirmation
        UserService-->>UserController: Activation success
        UserController-->>API: Success response
        API-->>AuthService: Activation complete
        AuthService-->>Angular: Update UI
        Angular->>User: Show activation success
    end
```

## Category Tree Management Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant CategoryService
    participant API as Spring Boot API
    participant CategoryController
    participant TreeService
    participant DB as Database

    User->>Angular: Request to move category
    Angular->>CategoryService: moveCategory(categoryId, newParentId, position)
    CategoryService->>API: PUT /api/categories/{id}/move
    API->>CategoryController: moveCategory(id, MoveRequest)
    
    CategoryController->>TreeService: moveNode(categoryId, newParentId, position)
    TreeService->>DB: Begin transaction
    
    TreeService->>DB: Get category and new parent
    DB-->>TreeService: Category data
    
    TreeService->>TreeService: Validate move (circular reference, etc.)
    
    TreeService->>DB: Update nested set values
    DB-->>TreeService: Confirmation
    
    TreeService->>DB: Commit transaction
    DB-->>TreeService: Transaction complete
    
    TreeService-->>CategoryController: Move success
    CategoryController-->>API: Updated category
    API-->>CategoryService: Category data
    CategoryService-->>Angular: Update tree view
    Angular->>User: Display updated category hierarchy
```

## Export Data Flow

```mermaid
sequenceDiagram
    actor User
    participant Angular
    participant ExportService
    participant API as Spring Boot API
    participant ExportController
    participant DataExporter
    participant DB as Database

    User->>Angular: Request data export
    Angular->>Angular: Configure export options
    Angular->>ExportService: exportData(exportOptions)
    ExportService->>API: POST /api/export
    API->>ExportController: exportData(ExportRequest)
    
    ExportController->>DataExporter: createExport(format, dateRange, options)
    
    alt Format = CSV
        DataExporter->>DataExporter: Prepare CSV generation
    else Format = Excel
        DataExporter->>DataExporter: Prepare Excel generation
    else Format = PDF
        DataExporter->>DataExporter: Prepare PDF generation
    end
    
    DataExporter->>DB: Query required data
    DB-->>DataExporter: Data to export
    
    DataExporter->>DataExporter: Format data for export
    DataExporter->>DataExporter: Generate export file
    DataExporter-->>ExportController: Export file
    
    ExportController-->>API: File download response
    API-->>ExportService: Export file
    ExportService-->>Angular: Initiate file download
    Angular->>User: Download export file
```