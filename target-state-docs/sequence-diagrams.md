# Target State Sequence Diagrams

This document contains key sequence diagrams showing how the Angular frontend will interact with the Node.js backend in the target architecture.

## User Authentication Sequence

```mermaid
sequenceDiagram
    participant User
    participant Angular
    participant AuthService
    participant API
    participant Database
    
    User->>Angular: Enter login credentials
    Angular->>AuthService: login(username, password)
    AuthService->>API: POST /api/auth/login
    API->>Database: Validate credentials
    Database-->>API: Return user if valid
    API->>API: Generate JWT token
    API-->>AuthService: Return JWT token
    AuthService->>AuthService: Store token in storage
    AuthService-->>Angular: Return authentication result
    Angular-->>User: Display success/failure message
    
    Note over Angular,API: Subsequent authenticated requests
    
    Angular->>API: Request with Authorization header
    API->>API: Validate JWT token
    API-->>Angular: Return protected resource
```

## Transfer Creation Sequence

```mermaid
sequenceDiagram
    participant User
    participant Angular
    participant TransferService
    participant API
    participant Database
    
    User->>Angular: Fill transfer form
    User->>Angular: Add transfer items
    User->>Angular: Submit form
    Angular->>TransferService: createTransfer(transferData)
    
    opt Currency conversion needed
        Angular->>TransferService: getExchangeRate(fromCurrency, toCurrency)
        TransferService->>API: GET /api/exchanges?from={id}&to={id}&date={date}
        API->>Database: Query exchange rates
        Database-->>API: Return exchange rates
        API-->>TransferService: Return exchange rate data
        TransferService-->>Angular: Display exchange rates
        User->>Angular: Confirm exchange rates
    end
    
    TransferService->>API: POST /api/transfers
    API->>API: Validate transfer data
    API->>API: Calculate and validate balancing
    API->>Database: Begin transaction
    API->>Database: Insert transfer record
    
    loop For each transfer item
        API->>Database: Insert transfer item
    end
    
    loop For each currency conversion
        API->>Database: Insert conversion record
    end
    
    API->>Database: Commit transaction
    Database-->>API: Confirm transaction
    API-->>TransferService: Return created transfer
    TransferService-->>Angular: Update UI state
    Angular-->>User: Display success message and redirect
```

## Report Generation Sequence

```mermaid
sequenceDiagram
    participant User
    participant Angular
    participant ReportService
    participant API
    participant Database
    
    User->>Angular: Navigate to reports page
    Angular->>ReportService: getReportTypes()
    ReportService->>API: GET /api/reports/types
    API-->>ReportService: Return report types
    ReportService-->>Angular: Display report types
    
    User->>Angular: Select report type and parameters
    Angular->>ReportService: createReport(reportConfig)
    ReportService->>API: POST /api/reports
    API->>Database: Save report configuration
    Database-->>API: Return saved report
    
    API->>API: Process report data
    
    alt Share Report
        API->>Database: Query category distribution data
    else Flow Report
        API->>Database: Query cash flow data
    else Value Report
        API->>Database: Query value changes over time
    end
    
    Database-->>API: Return report data
    API-->>ReportService: Return processed report
    ReportService-->>Angular: Update report state
    Angular->>Angular: Render visualization
    Angular-->>User: Display report visualization
    
    opt Export Report
        User->>Angular: Request export
        Angular->>ReportService: exportReport(format)
        ReportService->>API: GET /api/reports/{id}/export?format={format}
        API->>API: Generate export file
        API-->>ReportService: Return export data
        ReportService-->>Angular: Generate download
        Angular-->>User: Download starts
    end
```

## Category Management Sequence

```mermaid
sequenceDiagram
    participant User
    participant Angular
    participant CategoryService
    participant API
    participant Database
    
    User->>Angular: Navigate to categories page
    Angular->>CategoryService: getCategories()
    CategoryService->>API: GET /api/categories
    API->>Database: Query categories with hierarchy
    Database-->>API: Return category data
    API-->>CategoryService: Return processed categories
    CategoryService-->>Angular: Update category state
    Angular-->>User: Display category hierarchy
    
    User->>Angular: Create new category
    Angular->>CategoryService: createCategory(categoryData)
    CategoryService->>API: POST /api/categories
    API->>Database: Save new category
    API->>API: Update nested set values
    Database-->>API: Return saved category
    API-->>CategoryService: Return new category
    CategoryService-->>Angular: Update category state
    Angular-->>User: Show updated hierarchy
    
    User->>Angular: Move category (drag and drop)
    Angular->>CategoryService: moveCategory(id, newParentId, position)
    CategoryService->>API: PUT /api/categories/{id}/move
    API->>Database: Begin transaction
    API->>API: Recalculate nested set values
    API->>Database: Update affected categories
    API->>Database: Commit transaction
    Database-->>API: Confirm transaction
    API-->>CategoryService: Return updated hierarchy
    CategoryService-->>Angular: Update category state
    Angular-->>User: Show updated hierarchy
```

## Goal Tracking Sequence

```mermaid
sequenceDiagram
    participant User
    participant Angular
    participant GoalService
    participant API
    participant Database
    
    User->>Angular: Navigate to goals page
    Angular->>GoalService: getGoals()
    GoalService->>API: GET /api/goals
    API->>Database: Query goals
    Database-->>API: Return goals
    API-->>GoalService: Return goals
    GoalService-->>Angular: Update goals state
    Angular-->>User: Display goals
    
    User->>Angular: Create new goal
    Angular->>GoalService: createGoal(goalData)
    GoalService->>API: POST /api/goals
    API->>Database: Save new goal
    Database-->>API: Return saved goal
    API-->>GoalService: Return new goal
    GoalService-->>Angular: Update goals state
    Angular-->>User: Display updated goals
    
    User->>Angular: View goal progress
    Angular->>GoalService: getGoalProgress(goalId)
    GoalService->>API: GET /api/goals/{id}/progress
    API->>Database: Query transactions for goal's category
    Database-->>API: Return transactions
    API->>API: Calculate progress
    API-->>GoalService: Return progress data
    GoalService-->>Angular: Update goal progress state
    Angular-->>User: Display goal progress visualization
```

## Currency Exchange Management Sequence

```mermaid
sequenceDiagram
    participant User
    participant Angular
    participant CurrencyService
    participant API
    participant Database
    
    User->>Angular: Navigate to currencies page
    Angular->>CurrencyService: getCurrencies()
    CurrencyService->>API: GET /api/currencies
    API->>Database: Query currencies
    Database-->>API: Return currencies
    API-->>CurrencyService: Return currencies
    CurrencyService-->>Angular: Update currencies state
    Angular-->>User: Display currencies
    
    User->>Angular: Create new exchange rate
    Angular->>CurrencyService: createExchange(exchangeData)
    CurrencyService->>API: POST /api/exchanges
    API->>Database: Save new exchange rate
    Database-->>API: Return saved exchange
    API-->>CurrencyService: Return new exchange
    CurrencyService-->>Angular: Update exchanges state
    Angular-->>User: Display updated exchange rates
    
    User->>Angular: Request currency conversion
    Angular->>CurrencyService: convertCurrency(amount, fromCurrency, toCurrency, date)
    CurrencyService->>API: GET /api/exchanges/convert?amount={amount}&from={id}&to={id}&date={date}
    API->>Database: Query relevant exchange rates
    Database-->>API: Return exchange rates
    API->>API: Calculate conversion
    API-->>CurrencyService: Return converted amount
    CurrencyService-->>Angular: Update conversion result
    Angular-->>User: Display converted amount
```

## Financial Dashboard Sequence

```mermaid
sequenceDiagram
    participant User
    participant Angular
    participant DashboardService
    participant API
    participant Database
    
    User->>Angular: Navigate to dashboard
    Angular->>DashboardService: getDashboardData()
    
    par Get recent transactions
        DashboardService->>API: GET /api/transfers?limit=5
        API->>Database: Query recent transfers
        Database-->>API: Return transfers
        API-->>DashboardService: Return recent transfers
    and Get balance summary
        DashboardService->>API: GET /api/categories/summary
        API->>Database: Calculate category balances
        Database-->>API: Return balances
        API-->>DashboardService: Return balance summary
    and Get active goals
        DashboardService->>API: GET /api/goals?status=active
        API->>Database: Query active goals
        Database-->>API: Return goals
        API-->>DashboardService: Return active goals
    end
    
    DashboardService-->>Angular: Update dashboard state
    Angular->>Angular: Render dashboard components
    Angular-->>User: Display dashboard
    
    User->>Angular: Select time range filter
    Angular->>DashboardService: updateTimeRange(start, end)
    DashboardService->>API: GET /api/dashboard?start={start}&end={end}
    API->>Database: Query filtered dashboard data
    Database-->>API: Return filtered data
    API-->>DashboardService: Return updated dashboard data
    DashboardService-->>Angular: Update dashboard state
    Angular-->>User: Display updated dashboard
```

## Multi-Currency Balance Calculation Sequence

```mermaid
sequenceDiagram
    participant User
    participant Angular
    participant BalanceService
    participant API
    participant Database
    
    User->>Angular: Navigate to balance view
    Angular->>BalanceService: getBalances()
    BalanceService->>API: GET /api/balance
    API->>API: Determine calculation algorithm
    
    alt Use latest exchange rates
        API->>Database: Query latest exchange rates
        Database-->>API: Return latest rates
    else Use transaction-date exchange rates
        API->>Database: Query historical rates at transaction dates
        Database-->>API: Return historical rates
    else Don't convert (show all currencies)
        API->>Database: Query balances by currency
        Database-->>API: Return multi-currency balances
    end
    
    API->>Database: Query category balances
    Database-->>API: Return category balances
    
    API->>API: Apply conversion algorithm
    API-->>BalanceService: Return processed balances
    BalanceService-->>Angular: Update balances state
    Angular-->>User: Display balances
    
    User->>Angular: Change calculation algorithm
    Angular->>BalanceService: updateAlgorithm(algorithm)
    BalanceService->>API: PUT /api/users/settings
    API->>Database: Update user settings
    Database-->>API: Confirm update
    BalanceService->>BalanceService: getBalances() (refresh with new algorithm)
    Angular-->>User: Display recalculated balances
```