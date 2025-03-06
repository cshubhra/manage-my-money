# Current State Sequence Diagram

This document illustrates the key sequences and flows in the current Ruby on Rails application.

## Authentication Flow

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant SessionsController
    participant User_Model
    participant Mailer
    
    User->>Browser: Access application
    Browser->>SessionsController: GET /login
    SessionsController->>Browser: Display login form
    
    User->>Browser: Enter credentials
    Browser->>SessionsController: POST /session
    SessionsController->>User_Model: authenticate(login, password)
    
    alt Valid credentials
        User_Model->>SessionsController: Return user object
        SessionsController->>Browser: Set session cookie & redirect to home
    else Invalid credentials
        User_Model->>SessionsController: Return nil
        SessionsController->>Browser: Show error message
    end
    
    User->>Browser: Click "Sign up"
    Browser->>UsersController: GET /signup
    UsersController->>Browser: Display registration form
    
    User->>Browser: Complete registration form
    Browser->>UsersController: POST /users
    UsersController->>User_Model: Create user
    UsersController->>Mailer: Send activation email
    Mailer->>User: Send email with activation link
    UsersController->>Browser: Display "check your email" message
    
    User->>Browser: Click activation link in email
    Browser->>UsersController: GET /activate/:code
    UsersController->>User_Model: activate!
    UsersController->>Browser: Redirect to login
```

## Transfer Creation Flow

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant TransfersController
    participant Transfer_Model
    participant TransferItem_Model
    participant Category_Model
    participant Currency_Model
    
    User->>Browser: Navigate to Transfers
    Browser->>TransfersController: GET /transfers
    TransfersController->>Transfer_Model: Get newest transfers
    TransfersController->>Browser: Display transfers list and empty form
    
    User->>Browser: Fill transfer form (description, date, items)
    Browser->>TransfersController: POST /transfers
    
    TransfersController->>Transfer_Model: new(params)
    TransfersController->>Transfer_Model: Set user_id
    
    loop For each transfer item
        Transfer_Model->>TransferItem_Model: build(params)
        TransferItem_Model->>Category_Model: Validate category belongs to user
        TransferItem_Model->>Currency_Model: Validate currency
    end
    
    Transfer_Model->>Transfer_Model: validate
    
    alt Valid transfer
        Transfer_Model->>Transfer_Model: save
        TransfersController->>Browser: Return success & updated transfer list
    else Invalid transfer
        Transfer_Model->>TransfersController: Return errors
        TransfersController->>Browser: Display error messages
    end
```

## Category Management Flow

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant CategoriesController
    participant Category_Model
    participant Transfer_Model
    
    User->>Browser: Navigate to Categories
    Browser->>CategoriesController: GET /categories
    CategoriesController->>Category_Model: Get user categories
    CategoriesController->>Category_Model: Calculate saldos
    CategoriesController->>Browser: Display categories list
    
    User->>Browser: Click on category
    Browser->>CategoriesController: GET /categories/:id
    CategoriesController->>Category_Model: Find category
    CategoriesController->>Transfer_Model: Get transfers for category
    CategoriesController->>Browser: Show category details & transfers
    
    User->>Browser: Create new category
    Browser->>CategoriesController: GET /categories/new
    CategoriesController->>Browser: Display category form
    
    User->>Browser: Fill category form
    Browser->>CategoriesController: POST /categories
    
    CategoriesController->>Category_Model: new(params)
    CategoriesController->>Category_Model: Set user_id
    CategoriesController->>Category_Model: save_with_subcategories
    
    alt Valid category
        CategoriesController->>Browser: Redirect to categories list
    else Invalid category
        CategoriesController->>Browser: Display error messages
    end
```

## Report Generation Flow

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant ReportsController
    participant Report_Model
    participant Category_Model
    participant Transfer_Model
    participant GraphBuilder
    
    User->>Browser: Navigate to Reports
    Browser->>ReportsController: GET /reports
    ReportsController->>Report_Model: Get user reports
    ReportsController->>Browser: Display reports list
    
    User->>Browser: Create new report
    Browser->>ReportsController: GET /reports/new
    ReportsController->>ReportsController: Determine report type
    ReportsController->>Browser: Display report form
    
    User->>Browser: Fill report form (type, date range, categories)
    Browser->>ReportsController: POST /reports
    ReportsController->>Report_Model: Create report
    
    alt Valid report
        ReportsController->>Browser: Redirect to report
    else Invalid report
        ReportsController->>Browser: Display error messages
    end
    
    User->>Browser: View report
    Browser->>ReportsController: GET /reports/:id
    ReportsController->>Report_Model: Find report
    
    alt Graph report
        Report_Model->>Category_Model: Get category data
        Category_Model->>Transfer_Model: Get transfer data
        Report_Model->>GraphBuilder: Generate graph
        ReportsController->>Browser: Display report with graph
    else Text report
        Report_Model->>Category_Model: Get category data
        Category_Model->>Transfer_Model: Get transfer data
        ReportsController->>Browser: Display report with data table
    end
```

## Import Flow

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant ImportController
    participant Parser
    participant Transfer_Model
    participant TransferItem_Model
    
    User->>Browser: Navigate to Import
    Browser->>ImportController: GET /import
    ImportController->>Browser: Display upload form
    
    User->>Browser: Upload bank statement file
    Browser->>ImportController: POST /import
    ImportController->>Parser: Parse file
    Parser->>ImportController: Return structured data
    ImportController->>Browser: Display parsed data for confirmation
    
    User->>Browser: Confirm import
    Browser->>ImportController: POST /import/parse
    
    loop For each transaction
        ImportController->>Transfer_Model: Create transfer
        Transfer_Model->>TransferItem_Model: Create transfer items
    end
    
    ImportController->>Browser: Display import results
```

## Currency Management Flow

```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant CurrenciesController
    participant Currency_Model
    participant ExchangesController
    participant Exchange_Model
    
    User->>Browser: Navigate to Currencies
    Browser->>CurrenciesController: GET /currencies
    CurrenciesController->>Currency_Model: Get user currencies
    CurrenciesController->>Browser: Display currencies list
    
    User->>Browser: Create new currency
    Browser->>CurrenciesController: GET /currencies/new
    CurrenciesController->>Browser: Display currency form
    
    User->>Browser: Fill currency form
    Browser->>CurrenciesController: POST /currencies
    CurrenciesController->>Currency_Model: Create currency
    
    alt Valid currency
        CurrenciesController->>Browser: Redirect to currencies list
    else Invalid currency
        CurrenciesController->>Browser: Display error messages
    end
    
    User->>Browser: Navigate to Exchanges
    Browser->>ExchangesController: GET /exchanges
    ExchangesController->>Exchange_Model: Get user exchanges
    ExchangesController->>Browser: Display exchanges list
    
    User->>Browser: Create new exchange rate
    Browser->>ExchangesController: GET /exchanges/new
    ExchangesController->>Browser: Display exchange form
    
    User->>Browser: Fill exchange form
    Browser->>ExchangesController: POST /exchanges
    ExchangesController->>Exchange_Model: Create exchange
    
    alt Valid exchange
        ExchangesController->>Browser: Redirect to exchanges list
    else Invalid exchange
        ExchangesController->>Browser: Display error messages
    end
```

These sequence diagrams illustrate the primary flows in the current Ruby on Rails application, highlighting the interactions between the user interface, controllers, models, and other components of the system.