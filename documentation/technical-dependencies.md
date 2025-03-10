# Technical Dependencies

This document outlines the technical dependencies between components in the Ruby on Rails application.

## Database Dependencies

```mermaid
graph TD
    User --> Categories
    User --> Transfers
    User --> Currencies
    User --> Exchanges
    User --> Goals
    User --> Reports
    
    Categories --> TransferItems
    Categories --> Goals
    
    Transfers --> TransferItems
    Transfers --> Conversions
    
    TransferItems --> Categories
    TransferItems --> Currencies
    
    Conversions --> Exchanges
    Conversions --> Transfers
    
    Exchanges --> LeftCurrency[Currency]
    Exchanges --> RightCurrency[Currency]
    
    Goals --> Categories
    Goals --> Currencies
    
    Reports --> Categories
    
    CategoryReportOptions --> Categories
    CategoryReportOptions --> MultipleCategoryReport[Multiple Category Report]
    
    ValueReport --> CategoryReportOptions
    FlowReport --> CategoryReportOptions
    ShareReport --> Categories
```

## Controller Dependencies

```mermaid
graph TD
    ApplicationController --> User
    
    CategoriesController --> Categories
    CategoriesController --> ApplicationController
    
    CreditorsController --> Categories
    CreditorsController --> ApplicationController
    
    CurrenciesController --> Currencies
    CurrenciesController --> ApplicationController
    
    ExchangesController --> Exchanges
    ExchangesController --> Currencies
    ExchangesController --> ApplicationController
    
    GoalsController --> Goals
    GoalsController --> Categories
    GoalsController --> Currencies
    GoalsController --> ApplicationController
    
    ReportsController --> Reports
    ReportsController --> Categories
    ReportsController --> ApplicationController
    
    TransfersController --> Transfers
    TransfersController --> TransferItems
    TransfersController --> Categories
    TransfersController --> Currencies
    TransfersController --> Exchanges
    TransfersController --> ApplicationController
    
    UsersController --> User
    UsersController --> ApplicationController
    
    SessionsController --> User
    SessionsController --> ApplicationController
    
    AutocompleteController --> Transfers
    AutocompleteController --> ApplicationController
```

## Key Process Flows

### User Authentication Flow

```mermaid
sequenceDiagram
    participant User as User (Browser)
    participant SC as SessionsController
    participant UserModel as User Model
    participant DB as Database
    
    User->>SC: GET /login
    SC->>User: Render login form
    User->>SC: POST /session (credentials)
    SC->>UserModel: authenticate(login, password)
    UserModel->>DB: Find user by login
    DB->>UserModel: Return user record
    UserModel->>UserModel: Check password
    UserModel->>SC: Return authenticated user or nil
    alt Authentication Success
        SC->>User: Set session, redirect to homepage
    else Authentication Failure
        SC->>User: Render login with error
    end
```

### Transfer Creation Flow

```mermaid
sequenceDiagram
    participant User as User (Browser)
    participant TC as TransfersController
    participant TM as Transfer Model
    participant TIM as TransferItem Model
    participant CM as Category Model
    participant CurrM as Currency Model
    participant EM as Exchange Model
    participant DB as Database
    
    User->>TC: GET /transfers/new
    TC->>User: Render transfer form
    User->>TC: POST /transfers (transfer data)
    TC->>TM: new(params[:transfer])
    TM->>TIM: build transfer items
    TIM->>CM: validate categories
    TIM->>CurrM: validate currencies
    alt Multiple Currencies
        TM->>EM: find appropriate exchanges
    end
    TM->>TM: validate (check balanced items)
    TC->>TM: save
    TM->>DB: insert records
    TC->>User: Redirect to transfer show
```

## Module Dependencies

```mermaid
graph TD
    User --> Authentication
    User --> AuthenticationByPassword
    User --> AuthenticationByCookieToken
    
    Report --> Periodable
    Goal --> Periodable
    
    User --> HashEnums
    Goal --> HashEnums
    Report --> HashEnums
    Category --> HashEnums
```

## External Dependencies (Gems)

```mermaid
graph LR
    Application --> Rails
    Application --> ActiveRecord
    Application --> ActionPack
    Application --> ActionMailer
    Application --> ActionController
    Application --> ActionView
    Application --> ActiveSupport
    Application --> ThinkingSphinx[Thinking Sphinx]
    Application --> WillPaginate
    Application --> JRails
    Application --> Prawn
    Application --> Haml
    Application --> NestedSet
```