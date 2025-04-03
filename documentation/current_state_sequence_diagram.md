# Current State Sequence Diagram

## Key User Flows

This document illustrates the primary user interaction flows in the current Ruby on Rails application.

### 1. User Authentication Flow

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant SessionsController
    participant UsersController
    participant UserModel
    participant Database
    
    User->>Browser: Navigate to login page
    Browser->>SessionsController: GET /login
    SessionsController->>Browser: Return login form
    
    User->>Browser: Enter credentials
    Browser->>SessionsController: POST /session
    SessionsController->>UserModel: authenticate(login, password)
    UserModel->>Database: Query user
    Database-->>UserModel: Return user record
    UserModel-->>SessionsController: Authentication result
    
    alt Authentication successful
        SessionsController->>Browser: Redirect to homepage
    else Authentication failed
        SessionsController->>Browser: Return login form with errors
    end
```

### 2. Transfer Creation Flow

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant TransfersController
    participant CategoriesController 
    participant CurrenciesController
    participant TransferModel
    participant TransferItemModel
    participant CategoryModel
    participant CurrencyModel
    participant Database
    
    User->>Browser: Navigate to transfers page
    Browser->>TransfersController: GET /transfers
    TransfersController->>CategoryModel: Find user's categories
    CategoryModel->>Database: Query categories
    Database-->>CategoryModel: Return categories
    TransfersController->>CurrencyModel: Find user's currencies
    CurrencyModel->>Database: Query currencies
    Database-->>CurrencyModel: Return currencies
    TransfersController->>Browser: Return transfer list and form
    
    User->>Browser: Fill transfer form
    Browser->>TransfersController: POST /transfers
    TransfersController->>TransferModel: Create new transfer
    TransferModel->>TransferItemModel: Create transfer items
    TransferItemModel->>Database: Save transfer items
    TransferModel->>Database: Save transfer
    Database-->>TransferModel: Confirmation
    TransfersController->>Browser: Render updated transfer list
```

### 3. Report Generation Flow

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant ReportsController
    participant ReportModel
    participant CategoryModel
    participant TransferModel
    participant Database
    
    User->>Browser: Navigate to reports page
    Browser->>ReportsController: GET /reports
    ReportsController->>ReportModel: Get user's reports
    ReportModel->>Database: Query reports
    Database-->>ReportModel: Return reports
    ReportsController->>Browser: Display reports list
    
    User->>Browser: Request to create new report
    Browser->>ReportsController: GET /reports/new
    ReportsController->>CategoryModel: Get user's categories
    CategoryModel->>Database: Query categories
    Database-->>CategoryModel: Return categories
    ReportsController->>Browser: Display report creation form
    
    User->>Browser: Fill report form
    Browser->>ReportsController: POST /reports
    ReportsController->>ReportModel: Create new report
    ReportModel->>Database: Save report
    Database-->>ReportModel: Confirmation
    
    User->>Browser: View report
    Browser->>ReportsController: GET /reports/:id
    ReportsController->>ReportModel: Find report
    ReportModel->>Database: Query report
    Database-->>ReportModel: Return report
    ReportsController->>TransferModel: Get transfers for report
    TransferModel->>Database: Query transfers
    Database-->>TransferModel: Return transfers
    ReportsController->>Browser: Display report with data/charts
```

### 4. Category Management Flow

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant CategoriesController
    participant CategoryModel
    participant Database
    
    User->>Browser: Navigate to categories page
    Browser->>CategoriesController: GET /categories
    CategoriesController->>CategoryModel: Get user's categories
    CategoryModel->>Database: Query categories
    Database-->>CategoryModel: Return categories
    CategoriesController->>Browser: Display categories list
    
    User->>Browser: Request to create new category
    Browser->>CategoriesController: GET /categories/new
    CategoriesController->>CategoryModel: Get parent categories
    CategoryModel->>Database: Query parent categories
    Database-->>CategoryModel: Return parent categories
    CategoriesController->>Browser: Display category creation form
    
    User->>Browser: Fill category form
    Browser->>CategoriesController: POST /categories
    CategoriesController->>CategoryModel: Create new category
    CategoryModel->>Database: Save category
    Database-->>CategoryModel: Confirmation
    CategoriesController->>Browser: Redirect to categories list
```

### 5. Goal Setting Flow

```mermaid
sequenceDiagram
    actor User
    participant Browser
    participant GoalsController
    participant GoalModel
    participant CategoryModel
    participant CurrencyModel
    participant Database
    
    User->>Browser: Navigate to goals page
    Browser->>GoalsController: GET /goals
    GoalsController->>GoalModel: Get user's goals
    GoalModel->>Database: Query goals
    Database-->>GoalModel: Return goals
    GoalsController->>Browser: Display goals list
    
    User->>Browser: Request to create new goal
    Browser->>GoalsController: GET /goals/new
    GoalsController->>CategoryModel: Get user's categories
    CategoryModel->>Database: Query categories
    Database-->>CategoryModel: Return categories
    GoalsController->>CurrencyModel: Get user's currencies
    CurrencyModel->>Database: Query currencies
    Database-->>CurrencyModel: Return currencies
    GoalsController->>Browser: Display goal creation form
    
    User->>Browser: Fill goal form
    Browser->>GoalsController: POST /goals
    GoalsController->>GoalModel: Create new goal
    GoalModel->>Database: Save goal
    Database-->>GoalModel: Confirmation
    GoalsController->>Browser: Redirect to goals list
```

These sequence diagrams illustrate the key user flows in the current Ruby on Rails application, showing the interactions between the user, browser, controllers, models, and database.