# Sequence Diagrams

This document contains sequence diagrams for the main workflows in the application.

## User Authentication

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant SessionController
    participant UserModel
    participant Database
    
    User->>Browser: Enter login credentials
    Browser->>SessionController: POST /session (login, password)
    SessionController->>UserModel: authenticate(login, password)
    UserModel->>Database: Query user record
    Database-->>UserModel: User data
    UserModel-->>SessionController: Authentication result
    
    alt Authentication Successful
        SessionController->>Browser: Set session cookie
        SessionController-->>Browser: Redirect to dashboard
        Browser-->>User: Show dashboard
    else Authentication Failed
        SessionController-->>Browser: Show login page with error
        Browser-->>User: Display error message
    end
```

## Creating a New Transfer

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant TransfersController
    participant TransferModel
    participant CategoryModel
    participant CurrencyModel
    participant Database
    
    User->>Browser: Fill in transfer data
    User->>Browser: Click "Save"
    Browser->>TransfersController: POST /transfers (transfer data)
    
    TransfersController->>TransferModel: new Transfer(params)
    TransferModel->>TransferModel: build_transfer_items
    
    loop For each transfer item
        TransferModel->>CategoryModel: verify category ownership
        CategoryModel->>Database: Query category
        Database-->>CategoryModel: Category data
        CategoryModel-->>TransferModel: Category verification
        
        TransferModel->>CurrencyModel: verify currency
        CurrencyModel->>Database: Query currency
        Database-->>CurrencyModel: Currency data
        CurrencyModel-->>TransferModel: Currency verification
    end
    
    TransferModel->>TransferModel: validate balancing (income = outcome)
    TransferModel->>Database: Save transfer and items
    Database-->>TransferModel: Save result
    TransferModel-->>TransfersController: Save result
    
    alt Save Successful
        TransfersController-->>Browser: Return success response
        Browser-->>User: Show success message, update transfer list
    else Save Failed
        TransfersController-->>Browser: Return error details
        Browser-->>User: Display validation errors
    end
```

## Viewing Category Transactions

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant CategoriesController
    participant CategoryModel
    participant TransferModel
    participant Database
    
    User->>Browser: Click on category
    Browser->>CategoriesController: GET /categories/:id
    
    CategoriesController->>CategoryModel: find(id)
    CategoryModel->>Database: Query category
    Database-->>CategoryModel: Category data
    CategoryModel-->>CategoriesController: Category object
    
    CategoriesController->>CategoryModel: get_transfers(date_range)
    CategoryModel->>TransferModel: Query transfers
    TransferModel->>Database: Query transfer data
    Database-->>TransferModel: Transfer data
    TransferModel-->>CategoryModel: Transfer objects
    CategoryModel-->>CategoriesController: Transfer collection
    
    CategoriesController->>CategoriesController: Calculate saldo
    CategoriesController-->>Browser: Render category view with transfers
    Browser-->>User: Display category details and transactions
```

## Creating a Report

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant ReportsController
    participant ReportModel
    participant CategoryModel
    participant Database
    
    User->>Browser: Fill report parameters
    User->>Browser: Click "Create"
    Browser->>ReportsController: POST /reports (report parameters)
    
    ReportsController->>ReportModel: new Report(params)
    ReportModel->>ReportModel: set_period
    
    loop For each selected category
        ReportModel->>CategoryModel: verify category
        CategoryModel->>Database: Query category
        Database-->>CategoryModel: Category data
        CategoryModel-->>ReportModel: Category verification
    end
    
    ReportModel->>Database: Save report
    Database-->>ReportModel: Save result
    ReportModel-->>ReportsController: Report object
    
    alt Save Successful
        ReportsController-->>Browser: Redirect to report view
        Browser->>ReportsController: GET /reports/:id
        ReportsController->>ReportModel: find(id)
        ReportModel->>Database: Query report data
        Database-->>ReportModel: Report data
        
        alt Report Type = ValueReport
            ReportsController->>ReportsController: prepare_graph_report_variables
        else Report Type = FlowReport
            ReportsController->>ReportsController: prepare_flow_report_variables
        else Report Type = ShareReport
            ReportsController->>ReportsController: prepare_graph_report_variables
        end
        
        ReportsController-->>Browser: Render report view
        Browser-->>User: Display report with data visualization
    else Save Failed
        ReportsController-->>Browser: Render form with errors
        Browser-->>User: Display validation errors
    end
```

## Adding a Goal

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant GoalsController
    participant GoalModel
    participant CategoryModel
    participant CurrencyModel
    participant Database
    
    User->>Browser: Fill goal form
    User->>Browser: Click "Save"
    Browser->>GoalsController: POST /goals (goal data)
    
    GoalsController->>GoalModel: new Goal(params)
    
    GoalModel->>CategoryModel: verify category
    CategoryModel->>Database: Query category
    Database-->>CategoryModel: Category data
    CategoryModel-->>GoalModel: Category verification
    
    alt Goal Type = Value
        GoalModel->>CurrencyModel: verify currency
        CurrencyModel->>Database: Query currency
        Database-->>CurrencyModel: Currency data
        CurrencyModel-->>GoalModel: Currency verification
    end
    
    GoalModel->>Database: Save goal
    Database-->>GoalModel: Save result
    GoalModel-->>GoalsController: Goal object
    
    alt Save Successful
        GoalsController-->>Browser: Redirect to goals list
        Browser-->>User: Show goals list with new goal
    else Save Failed
        GoalsController-->>Browser: Render form with errors
        Browser-->>User: Display validation errors
    end
```

## Currency Exchange Workflow

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant ExchangesController
    participant ExchangeModel
    participant CurrencyModel
    participant Database
    
    User->>Browser: Fill exchange rate form
    User->>Browser: Click "Save"
    Browser->>ExchangesController: POST /exchanges (exchange data)
    
    ExchangesController->>CurrencyModel: find currencies
    CurrencyModel->>Database: Query currencies
    Database-->>CurrencyModel: Currency data
    CurrencyModel-->>ExchangesController: Currency objects
    
    ExchangesController->>ExchangeModel: new Exchange(params)
    ExchangeModel->>Database: Save exchange rate
    Database-->>ExchangeModel: Save result
    ExchangeModel-->>ExchangesController: Exchange object
    
    alt Save Successful
        ExchangesController-->>Browser: Redirect to exchanges list
        Browser-->>User: Show exchanges list with new rate
    else Save Failed
        ExchangesController-->>Browser: Render form with errors
        Browser-->>User: Display validation errors
    end
```

## Multi-Currency Transfer

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant TransfersController
    participant TransferModel
    participant ExchangeModel
    participant ConversionModel
    participant Database
    
    User->>Browser: Create transfer with multiple currencies
    User->>Browser: Add conversion rate
    User->>Browser: Click "Save"
    
    Browser->>TransfersController: POST /transfers (transfer data with conversions)
    
    TransfersController->>TransferModel: new Transfer(params)
    TransferModel->>TransferModel: build_transfer_items
    
    loop For each transfer item
        TransferModel->>TransferModel: Set currency
    end
    
    loop For each conversion
        TransferModel->>ExchangeModel: build or find Exchange
        ExchangeModel->>Database: Query or prepare exchange
        Database-->>ExchangeModel: Exchange data
        
        TransferModel->>ConversionModel: build Conversion
        ConversionModel->>ConversionModel: Link exchange and transfer
    end
    
    TransferModel->>TransferModel: validate multi-currency balance
    TransferModel->>Database: Save transfer, items, and conversions
    Database-->>TransferModel: Save result
    TransferModel-->>TransfersController: Save result
    
    alt Save Successful
        TransfersController-->>Browser: Return success response
        Browser-->>User: Show success message, update transfer list
    else Save Failed
        TransfersController-->>Browser: Return error details
        Browser-->>User: Display validation errors
    end
```