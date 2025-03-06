# Target State Sequence Diagram

This document illustrates the key sequences and flows in the target Angular frontend and Spring Boot backend architecture.

## Authentication Flow

```mermaid
sequenceDiagram
    participant User
    participant AngularApp
    participant AuthService
    participant HttpInterceptor
    participant SpringBoot
    participant JwtFilter
    participant UserService
    participant DB as Database
    
    User->>AngularApp: Access application
    AngularApp->>AngularApp: Redirect to login
    
    User->>AngularApp: Enter credentials
    AngularApp->>AuthService: login(username, password)
    AuthService->>SpringBoot: POST /api/auth/login
    SpringBoot->>UserService: authenticate(username, password)
    UserService->>DB: Find user
    DB->>UserService: Return user data
    
    alt Valid credentials
        UserService->>SpringBoot: Generate JWT token
        SpringBoot->>AuthService: Return JWT token
        AuthService->>AuthService: Store token in local storage
        AuthService->>AngularApp: Return authentication status
        AngularApp->>AngularApp: Navigate to dashboard
    else Invalid credentials
        UserService->>SpringBoot: Authentication failed
        SpringBoot->>AuthService: Return error
        AuthService->>AngularApp: Show error message
    end
    
    User->>AngularApp: Access protected resource
    AngularApp->>HttpInterceptor: Intercept request
    HttpInterceptor->>HttpInterceptor: Add Authorization header with JWT
    HttpInterceptor->>SpringBoot: Request with JWT
    SpringBoot->>JwtFilter: Process request
    JwtFilter->>JwtFilter: Validate JWT token
    
    alt Valid token
        JwtFilter->>SpringBoot: Set authentication context
        SpringBoot->>SpringBoot: Process request
        SpringBoot->>AngularApp: Return requested data
    else Invalid token
        JwtFilter->>SpringBoot: Authentication failed
        SpringBoot->>AngularApp: Return 401 Unauthorized
        AngularApp->>AuthService: Handle unauthorized error
        AuthService->>AngularApp: Redirect to login
    end
```

## Transfer Creation Flow

```mermaid
sequenceDiagram
    participant User
    participant AngularApp
    participant TransferService
    participant NgRxStore
    participant SpringBoot
    participant TransferController
    participant TransferService as BackendTransferService
    participant Database
    
    User->>AngularApp: Navigate to Transfers
    AngularApp->>NgRxStore: Dispatch loadTransfers action
    NgRxStore->>TransferService: getTransfers()
    TransferService->>SpringBoot: GET /api/transfers
    SpringBoot->>TransferController: getTransfers()
    TransferController->>BackendTransferService: findAllByUser()
    BackendTransferService->>Database: Query transfers
    Database->>BackendTransferService: Return transfers data
    BackendTransferService->>TransferController: Transfers data
    TransferController->>SpringBoot: Convert to DTOs
    SpringBoot->>TransferService: Return transfers
    TransferService->>NgRxStore: Dispatch loadTransfersSuccess action
    NgRxStore->>AngularApp: Update transfers state
    AngularApp->>User: Display transfers list
    
    User->>AngularApp: Fill transfer form
    AngularApp->>AngularApp: Validate form
    
    alt Form valid
        User->>AngularApp: Submit form
        AngularApp->>NgRxStore: Dispatch createTransfer action
        NgRxStore->>TransferService: createTransfer(transferData)
        TransferService->>SpringBoot: POST /api/transfers
        SpringBoot->>TransferController: createTransfer(transferDTO)
        TransferController->>BackendTransferService: create(transfer)
        BackendTransferService->>BackendTransferService: Validate transaction balance
        
        alt Valid transaction
            BackendTransferService->>Database: Save transfer
            Database->>BackendTransferService: Return saved transfer
            BackendTransferService->>TransferController: Transfer data
            TransferController->>SpringBoot: Convert to DTO
            SpringBoot->>TransferService: Return created transfer
            TransferService->>NgRxStore: Dispatch createTransferSuccess action
            NgRxStore->>AngularApp: Update transfers state
            AngularApp->>User: Show success message
        else Invalid transaction
            BackendTransferService->>TransferController: Throw validation exception
            TransferController->>SpringBoot: Handle exception
            SpringBoot->>TransferService: Return error
            TransferService->>NgRxStore: Dispatch createTransferFailure action
            NgRxStore->>AngularApp: Update error state
            AngularApp->>User: Display error message
        end
    else Form invalid
        AngularApp->>User: Show validation errors
    end
```

## Category Management Flow

```mermaid
sequenceDiagram
    participant User
    participant AngularApp
    participant CategoryService
    participant NgRxStore
    participant SpringBoot
    participant CategoryController
    participant CategoryService as BackendCategoryService
    participant Database
    
    User->>AngularApp: Navigate to Categories
    AngularApp->>NgRxStore: Dispatch loadCategories action
    NgRxStore->>CategoryService: getCategories()
    CategoryService->>SpringBoot: GET /api/categories
    SpringBoot->>CategoryController: getCategories()
    CategoryController->>BackendCategoryService: findAllByUser()
    BackendCategoryService->>Database: Query categories
    Database->>BackendCategoryService: Return categories data
    BackendCategoryService->>CategoryController: Categories data
    CategoryController->>SpringBoot: Convert to DTOs
    SpringBoot->>CategoryService: Return categories
    CategoryService->>NgRxStore: Dispatch loadCategoriesSuccess action
    NgRxStore->>AngularApp: Update categories state
    AngularApp->>User: Display categories list
    
    User->>AngularApp: Select category
    AngularApp->>NgRxStore: Dispatch loadCategory action
    NgRxStore->>CategoryService: getCategory(id)
    CategoryService->>SpringBoot: GET /api/categories/{id}
    SpringBoot->>CategoryController: getCategory(id)
    CategoryController->>BackendCategoryService: findById(id)
    BackendCategoryService->>Database: Query category
    Database->>BackendCategoryService: Return category data
    BackendCategoryService->>CategoryController: Category data
    CategoryController->>SpringBoot: Convert to DTO
    SpringBoot->>CategoryService: Return category
    CategoryService->>NgRxStore: Dispatch loadCategorySuccess action
    NgRxStore->>AngularApp: Update selected category state
    AngularApp->>User: Display category details
    
    User->>AngularApp: Create new category form
    User->>AngularApp: Fill category form
    User->>AngularApp: Submit form
    AngularApp->>NgRxStore: Dispatch createCategory action
    NgRxStore->>CategoryService: createCategory(categoryData)
    CategoryService->>SpringBoot: POST /api/categories
    SpringBoot->>CategoryController: createCategory(categoryDTO)
    CategoryController->>BackendCategoryService: create(category)
    BackendCategoryService->>Database: Save category
    Database->>BackendCategoryService: Return saved category
    BackendCategoryService->>CategoryController: Category data
    CategoryController->>SpringBoot: Convert to DTO
    SpringBoot->>CategoryService: Return created category
    CategoryService->>NgRxStore: Dispatch createCategorySuccess action
    NgRxStore->>AngularApp: Update categories state
    AngularApp->>User: Show success message
```

## Report Generation Flow

```mermaid
sequenceDiagram
    participant User
    participant AngularApp
    participant ReportService
    participant NgRxStore
    participant SpringBoot
    participant ReportController
    participant ReportService as BackendReportService
    participant Database
    
    User->>AngularApp: Navigate to Reports
    AngularApp->>NgRxStore: Dispatch loadReports action
    NgRxStore->>ReportService: getReports()
    ReportService->>SpringBoot: GET /api/reports
    SpringBoot->>ReportController: getReports()
    ReportController->>BackendReportService: findAllByUser()
    BackendReportService->>Database: Query reports
    Database->>BackendReportService: Return reports data
    BackendReportService->>ReportController: Reports data
    ReportController->>SpringBoot: Convert to DTOs
    SpringBoot->>ReportService: Return reports
    ReportService->>NgRxStore: Dispatch loadReportsSuccess action
    NgRxStore->>AngularApp: Update reports state
    AngularApp->>User: Display reports list
    
    User->>AngularApp: Create new report
    AngularApp->>AngularApp: Show report form
    User->>AngularApp: Select report type
    User->>AngularApp: Select time period
    User->>AngularApp: Select categories
    User->>AngularApp: Submit form
    AngularApp->>NgRxStore: Dispatch createReport action
    NgRxStore->>ReportService: createReport(reportData)
    ReportService->>SpringBoot: POST /api/reports
    SpringBoot->>ReportController: createReport(reportDTO)
    ReportController->>BackendReportService: create(report)
    BackendReportService->>Database: Save report
    Database->>BackendReportService: Return saved report
    BackendReportService->>ReportController: Report data
    ReportController->>SpringBoot: Convert to DTO
    SpringBoot->>ReportService: Return created report
    ReportService->>NgRxStore: Dispatch createReportSuccess action
    NgRxStore->>AngularApp: Update reports state
    
    User->>AngularApp: View report
    AngularApp->>NgRxStore: Dispatch loadReportData action
    NgRxStore->>ReportService: getReportData(id)
    ReportService->>SpringBoot: GET /api/reports/{id}/data
    SpringBoot->>ReportController: getReportData(id)
    ReportController->>BackendReportService: generateReportData(id)
    BackendReportService->>Database: Query necessary data
    Database->>BackendReportService: Return data
    BackendReportService->>BackendReportService: Process data
    BackendReportService->>ReportController: Report results
    ReportController->>SpringBoot: Convert to DTOs
    SpringBoot->>ReportService: Return report data
    ReportService->>NgRxStore: Dispatch loadReportDataSuccess action
    NgRxStore->>AngularApp: Update report data state
    AngularApp->>AngularApp: Generate chart/visualization
    AngularApp->>User: Display report with visualization
```

## Currency Exchange Management Flow

```mermaid
sequenceDiagram
    participant User
    participant AngularApp
    participant CurrencyService
    participant ExchangeService
    participant NgRxStore
    participant SpringBoot
    participant CurrencyController
    participant ExchangeController
    participant BackendServices
    participant Database
    
    User->>AngularApp: Navigate to Currencies
    AngularApp->>NgRxStore: Dispatch loadCurrencies action
    NgRxStore->>CurrencyService: getCurrencies()
    CurrencyService->>SpringBoot: GET /api/currencies
    SpringBoot->>CurrencyController: getCurrencies()
    CurrencyController->>BackendServices: findAllCurrencies()
    BackendServices->>Database: Query currencies
    Database->>BackendServices: Return currencies data
    BackendServices->>CurrencyController: Currencies data
    CurrencyController->>SpringBoot: Convert to DTOs
    SpringBoot->>CurrencyService: Return currencies
    CurrencyService->>NgRxStore: Dispatch loadCurrenciesSuccess action
    NgRxStore->>AngularApp: Update currencies state
    AngularApp->>User: Display currencies list
    
    User->>AngularApp: Navigate to Exchange Rates
    AngularApp->>NgRxStore: Dispatch loadExchanges action
    NgRxStore->>ExchangeService: getExchanges()
    ExchangeService->>SpringBoot: GET /api/exchanges
    SpringBoot->>ExchangeController: getExchanges()
    ExchangeController->>BackendServices: findAllExchanges()
    BackendServices->>Database: Query exchanges
    Database->>BackendServices: Return exchanges data
    BackendServices->>ExchangeController: Exchanges data
    ExchangeController->>SpringBoot: Convert to DTOs
    SpringBoot->>ExchangeService: Return exchanges
    ExchangeService->>NgRxStore: Dispatch loadExchangesSuccess action
    NgRxStore->>AngularApp: Update exchanges state
    AngularApp->>User: Display exchange rates
    
    User->>AngularApp: Create new exchange rate
    AngularApp->>User: Show exchange form
    User->>AngularApp: Select currencies
    User->>AngularApp: Enter rates
    User->>AngularApp: Submit form
    AngularApp->>NgRxStore: Dispatch createExchange action
    NgRxStore->>ExchangeService: createExchange(exchangeData)
    ExchangeService->>SpringBoot: POST /api/exchanges
    SpringBoot->>ExchangeController: createExchange(exchangeDTO)
    ExchangeController->>BackendServices: createExchange(exchange)
    BackendServices->>Database: Save exchange
    Database->>BackendServices: Return saved exchange
    BackendServices->>ExchangeController: Exchange data
    ExchangeController->>SpringBoot: Convert to DTO
    SpringBoot->>ExchangeService: Return created exchange
    ExchangeService->>NgRxStore: Dispatch createExchangeSuccess action
    NgRxStore->>AngularApp: Update exchanges state
    AngularApp->>User: Show success message
```

## Import Bank Statement Flow

```mermaid
sequenceDiagram
    participant User
    participant AngularApp
    participant ImportService
    participant NgRxStore
    participant SpringBoot
    participant ImportController
    participant ImportService as BackendImportService
    participant TransferService
    participant Database
    
    User->>AngularApp: Navigate to Import
    AngularApp->>User: Display upload form
    
    User->>AngularApp: Upload bank statement file
    AngularApp->>NgRxStore: Dispatch uploadBankStatement action
    NgRxStore->>ImportService: uploadFile(file)
    ImportService->>SpringBoot: POST /api/import/upload
    SpringBoot->>ImportController: uploadFile(file)
    ImportController->>BackendImportService: parseFile(file)
    BackendImportService->>BackendImportService: Parse file content
    BackendImportService->>ImportController: Parsed transactions
    ImportController->>SpringBoot: Return parsed data
    SpringBoot->>ImportService: Return preview data
    ImportService->>NgRxStore: Dispatch uploadSuccess action
    NgRxStore->>AngularApp: Update import preview state
    AngularApp->>User: Display parsed transactions for confirmation
    
    User->>AngularApp: Modify category mappings
    User->>AngularApp: Confirm import
    AngularApp->>NgRxStore: Dispatch confirmImport action
    NgRxStore->>ImportService: confirmImport(importData)
    ImportService->>SpringBoot: POST /api/import/confirm
    SpringBoot->>ImportController: confirmImport(importData)
    ImportController->>BackendImportService: processImport(importData)
    
    loop For each transaction
        BackendImportService->>TransferService: createTransfer(transferData)
        TransferService->>Database: Save transfer and items
    end
    
    BackendImportService->>ImportController: Import results
    ImportController->>SpringBoot: Return import summary
    SpringBoot->>ImportService: Return import results
    ImportService->>NgRxStore: Dispatch importSuccess action
    NgRxStore->>AngularApp: Update import results state
    AngularApp->>User: Display import summary
```

These sequence diagrams illustrate the primary flows in the target architecture, highlighting the interactions between the Angular frontend, NgRx state management, Spring Boot backend, and database. They demonstrate how the modernized application will handle key user interactions while maintaining a clean separation of concerns between frontend and backend components.