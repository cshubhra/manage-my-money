# Sequence Diagrams

This document contains sequence diagrams for key workflows in the application.

## User Registration and Authentication

```mermaid
sequenceDiagram
    actor User
    participant Browser as Browser
    participant UsersController as UsersController
    participant UserModel as User Model
    participant Mailer as ActionMailer
    
    User->>Browser: Fill registration form
    Browser->>UsersController: POST /users
    UsersController->>UserModel: .new(user_params)
    UserModel->>UserModel: make_activation_code
    UserModel->>UserModel: create_top_categories
    UserModel->>UserModel: save
    UsersController->>Mailer: Send activation email
    Mailer->>User: Email with activation link
    UsersController->>Browser: Redirect to login
    
    User->>Browser: Click activation link
    Browser->>UsersController: GET /activate/{code}
    UsersController->>UserModel: find_by_activation_code
    UserModel->>UserModel: activate!
    UsersController->>Browser: Redirect to login
    
    User->>Browser: Fill login form
    Browser->>UsersController: POST /session
    UsersController->>UserModel: authenticate(login, password)
    UserModel->>UsersController: Return authenticated user or nil
    UsersController->>Browser: Set session cookie and redirect to dashboard
```

## Creating a New Transfer

```mermaid
sequenceDiagram
    actor User
    participant Browser as Browser
    participant TransfersController as TransfersController
    participant TransferModel as Transfer Model
    participant TransferItemModel as TransferItem Model
    participant CategoryModel as Category Model
    participant CurrencyModel as Currency Model
    participant ConversionModel as Conversion Model
    participant ExchangeModel as Exchange Model
    
    User->>Browser: Navigate to new transfer page
    Browser->>TransfersController: GET /transfers/new
    TransfersController->>CurrencyModel: Currency.for_user(current_user)
    TransfersController->>CategoryModel: Category.for_user(current_user)
    TransfersController->>Browser: Render new transfer form
    
    User->>Browser: Fill in transfer details (multi-item)
    Browser->>TransfersController: POST /transfers
    TransfersController->>TransferModel: .new(transfer_params)
    TransferModel->>TransferItemModel: Build transfer items
    
    alt Is multi-currency
        TransferModel->>ConversionModel: Build conversions
        ConversionModel->>ExchangeModel: Associate exchange rates
    end
    
    TransferModel->>TransferModel: validate
    Note right of TransferModel: Check if income = outcome
    
    TransferModel->>TransferModel: save
    TransfersController->>Browser: Redirect to transfer show page
```

## Generating a Financial Report

```mermaid
sequenceDiagram
    actor User
    participant Browser as Browser
    participant ReportsController as ReportsController
    participant ReportModel as Report Model
    participant ShareReportModel as ShareReport Model
    participant FlowReportModel as FlowReport Model
    participant ValueReportModel as ValueReport Model
    participant CategoryModel as Category Model
    participant TransferModel as Transfer Model
    
    User->>Browser: Navigate to reports page
    Browser->>ReportsController: GET /reports
    ReportsController->>ReportModel: .find_all_by_user_id(current_user.id)
    ReportsController->>Browser: Display list of reports
    
    User->>Browser: Create new report
    Browser->>ReportsController: GET /reports/new
    ReportsController->>CategoryModel: .for_user(current_user)
    ReportsController->>Browser: Show report creation form
    
    User->>Browser: Select report type & parameters
    Browser->>ReportsController: POST /reports
    
    alt Report Type is ShareReport
        ReportsController->>ShareReportModel: .new(report_params)
    else Report Type is FlowReport
        ReportsController->>FlowReportModel: .new(report_params)
    else Report Type is ValueReport
        ReportsController->>ValueReportModel: .new(report_params)  
    end
    
    ReportModel->>ReportModel: save
    ReportsController->>Browser: Redirect to report show page
    
    User->>Browser: View report
    Browser->>ReportsController: GET /reports/{id}
    ReportsController->>ReportModel: .find(params[:id])
    ReportModel->>TransferModel: Query transfers for period
    ReportModel->>CategoryModel: Aggregate data by category
    
    alt Report Type is ShareReport
        ReportModel->>ReportModel: calculate_share_values
    else Report Type is FlowReport
        ReportModel->>ReportModel: calculate_flow_values
    else Report Type is ValueReport
        ReportModel->>ReportModel: calculate_value_series
    end
    
    ReportsController->>Browser: Render report with charts/tables
```

## Setting and Tracking Financial Goals

```mermaid
sequenceDiagram
    actor User
    participant Browser as Browser
    participant GoalsController as GoalsController
    participant GoalModel as Goal Model
    participant CategoryModel as Category Model
    participant CurrencyModel as Currency Model
    participant TransferModel as Transfer Model
    
    User->>Browser: Navigate to goals page
    Browser->>GoalsController: GET /goals
    GoalsController->>GoalModel: Goal.find_actual(current_user)
    GoalsController->>GoalModel: Goal.find_past(current_user)
    GoalsController->>Browser: Display goals
    
    User->>Browser: Create new goal
    Browser->>GoalsController: GET /goals/new
    GoalsController->>CategoryModel: .for_user(current_user)
    GoalsController->>CurrencyModel: .for_user(current_user)
    GoalsController->>Browser: Show goal creation form
    
    User->>Browser: Submit goal details
    Browser->>GoalsController: POST /goals
    GoalsController->>GoalModel: .new(goal_params)
    GoalModel->>GoalModel: validate
    GoalModel->>GoalModel: save
    
    alt Is cyclic goal
        GoalModel->>GoalModel: update_attribute :cycle_group, id
    end
    
    GoalsController->>Browser: Redirect to goals page
    
    User->>Browser: View goal progress
    Browser->>GoalsController: GET /goals/{id}
    GoalsController->>GoalModel: .find(params[:id])
    GoalModel->>CategoryModel: Calculate actual value
    
    alt Goal type is percent
        CategoryModel->>CategoryModel: percent_of_parent_category
    else Goal type is value
        CategoryModel->>CategoryModel: saldo_for_period
    end
    
    GoalsController->>Browser: Display goal with progress
```

## Currency Exchange Management

```mermaid
sequenceDiagram
    actor User
    participant Browser as Browser
    participant ExchangesController as ExchangesController
    participant ExchangeModel as Exchange Model
    participant CurrencyModel as Currency Model
    
    User->>Browser: Navigate to exchanges page
    Browser->>ExchangesController: GET /exchanges
    ExchangesController->>ExchangeModel: .daily.for_user(current_user)
    ExchangesController->>Browser: Display exchanges
    
    User->>Browser: Create new exchange rate
    Browser->>ExchangesController: GET /exchanges/new
    ExchangesController->>CurrencyModel: .for_user(current_user)
    ExchangesController->>Browser: Show exchange creation form
    
    User->>Browser: Submit exchange details
    Browser->>ExchangesController: POST /exchanges
    ExchangesController->>ExchangeModel: .new(exchange_params)
    ExchangeModel->>ExchangeModel: before_validation (ensure correct order)
    ExchangeModel->>ExchangeModel: validate
    ExchangeModel->>ExchangeModel: save
    ExchangesController->>Browser: Redirect to exchanges page
    
    alt When used in transfer
        Browser->>ExchangesController: GET /conversions/new
        ExchangesController->>ExchangeModel: .for_currencies(currency1, currency2)
        ExchangesController->>Browser: Show exchange selection
    end
```

## Category Management

```mermaid
sequenceDiagram
    actor User
    participant Browser as Browser
    participant CategoriesController as CategoriesController
    participant CategoryModel as Category Model
    participant SystemCategoryModel as SystemCategory Model
    
    User->>Browser: Navigate to categories page
    Browser->>CategoriesController: GET /categories
    CategoriesController->>CategoryModel: Category.roots.for_user(current_user)
    CategoriesController->>Browser: Display category tree
    
    User->>Browser: Create new category
    Browser->>CategoriesController: GET /categories/new
    CategoriesController->>CategoryModel: Category.for_user(current_user)
    CategoriesController->>SystemCategoryModel: SystemCategory.all
    CategoriesController->>Browser: Show category creation form
    
    User->>Browser: Submit category details
    Browser->>CategoriesController: POST /categories
    CategoriesController->>CategoryModel: .new(category_params)
    
    alt Based on system category
        CategoryModel->>SystemCategoryModel: Associate with system category
    end
    
    CategoryModel->>CategoryModel: validate
    CategoryModel->>CategoryModel: save
    CategoriesController->>Browser: Redirect to categories page
    
    User->>Browser: Move category in hierarchy
    Browser->>CategoriesController: PUT /categories/{id}/move
    CategoriesController->>CategoryModel: .find(params[:id])
    CategoryModel->>CategoryModel: move_to_child_of/move_to_right_of
    CategoriesController->>Browser: Refresh category tree
```