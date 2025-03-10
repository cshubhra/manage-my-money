# UI Component Structure

This document outlines the Angular component structure and organization for the target state application.

## Component Organization

The application UI is organized into modules following Angular best practices, with feature modules containing domain-specific components and shared modules for reusable components.

```mermaid
graph TD
    subgraph "App Module"
        App[App Component]
        App --> Nav[Navigation Component]
        App --> Footer[Footer Component]
        App --> Router[Router Outlet]
        
        Router --> |Routes| Dashboard[Dashboard Module]
        Router --> |Routes| Transfers[Transfers Module]
        Router --> |Routes| Categories[Categories Module]
        Router --> |Routes| Reports[Reports Module]
        Router --> |Routes| Goals[Goals Module]
        Router --> |Routes| Currencies[Currencies Module]
        Router --> |Routes| Settings[Settings Module]
        Router --> |Routes| Auth[Auth Module]
    end
```

## Feature Modules

### Dashboard Module

```mermaid
graph TD
    Dashboard[Dashboard Module]
    Dashboard --> DashboardComponent[Dashboard Component]
    DashboardComponent --> RecentTransactions[Recent Transactions Component]
    DashboardComponent --> AccountBalances[Account Balances Component]
    DashboardComponent --> SpendingOverview[Spending Overview Component]
    DashboardComponent --> GoalProgress[Goal Progress Component]
```

### Transfers Module

```mermaid
graph TD
    Transfers[Transfers Module]
    Transfers --> TransferList[Transfer List Component]
    Transfers --> TransferDetail[Transfer Detail Component]
    Transfers --> TransferForm[Transfer Form Component]
    TransferForm --> TransferItemForm[Transfer Item Form Component]
    TransferForm --> CurrencySelector[Currency Selector Component]
    TransferForm --> CategorySelector[Category Selector Component]
    TransferForm --> DatePicker[Date Picker Component]
```

### Categories Module

```mermaid
graph TD
    Categories[Categories Module]
    Categories --> CategoryTree[Category Tree Component]
    Categories --> CategoryDetail[Category Detail Component]
    Categories --> CategoryForm[Category Form Component]
    CategoryTree --> CategoryNode[Category Node Component]
    CategoryDetail --> CategoryTransactions[Category Transactions Component]
    CategoryDetail --> CategoryBalance[Category Balance Component]
```

### Reports Module

```mermaid
graph TD
    Reports[Reports Module]
    Reports --> ReportList[Report List Component]
    Reports --> ReportDetail[Report Detail Component]
    Reports --> ReportForm[Report Form Component]
    
    ReportDetail --> ShareReport[Share Report Component]
    ReportDetail --> FlowReport[Flow Report Component]
    ReportDetail --> ValueReport[Value Report Component]
    
    ShareReport --> PieChart[Pie Chart Component]
    FlowReport --> FlowData[Flow Data Component]
    ValueReport --> LineChart[Line Chart Component]
    ValueReport --> BarChart[Bar Chart Component]
```

### Goals Module

```mermaid
graph TD
    Goals[Goals Module]
    Goals --> GoalList[Goal List Component]
    Goals --> GoalDetail[Goal Detail Component]
    Goals --> GoalForm[Goal Form Component]
    GoalDetail --> GoalProgress[Goal Progress Component]
    GoalDetail --> GoalTimeline[Goal Timeline Component]
```

### Currencies Module

```mermaid
graph TD
    Currencies[Currencies Module]
    Currencies --> CurrencyList[Currency List Component]
    Currencies --> CurrencyDetail[Currency Detail Component]
    Currencies --> CurrencyForm[Currency Form Component]
    Currencies --> ExchangeList[Exchange List Component]
    Currencies --> ExchangeForm[Exchange Form Component]
    Currencies --> CurrencyConverter[Currency Converter Component]
```

### Settings Module

```mermaid
graph TD
    Settings[Settings Module]
    Settings --> UserProfile[User Profile Component]
    Settings --> SecuritySettings[Security Settings Component]
    Settings --> DisplaySettings[Display Settings Component]
    Settings --> CurrencySettings[Currency Settings Component]
    Settings --> NotificationSettings[Notification Settings Component]
    Settings --> DataImportExport[Data Import/Export Component]
```

### Auth Module

```mermaid
graph TD
    Auth[Auth Module]
    Auth --> Login[Login Component]
    Auth --> Register[Register Component]
    Auth --> ForgotPassword[Forgot Password Component]
    Auth --> ResetPassword[Reset Password Component]
    Auth --> ChangePassword[Change Password Component]
```

## Shared Components

```mermaid
graph TD
    Shared[Shared Module]
    Shared --> DataTable[Data Table Component]
    Shared --> Pagination[Pagination Component]
    Shared --> SearchBox[Search Box Component]
    Shared --> DateRangePicker[Date Range Picker Component]
    Shared --> ConfirmDialog[Confirmation Dialog Component]
    Shared --> AlertBanner[Alert Banner Component]
    Shared --> Loader[Loader Component]
    Shared --> CurrencyDisplay[Currency Display Component]
    Shared --> FileUpload[File Upload Component]
    Shared --> ChartBase[Chart Base Component]
```

## Core Module

```mermaid
graph TD
    Core[Core Module]
    Core --> Guards[Route Guards]
    Core --> Interceptors[HTTP Interceptors]
    Core --> Services[Core Services]
    
    Guards --> AuthGuard[Auth Guard]
    Guards --> UnsavedChangesGuard[Unsaved Changes Guard]
    
    Interceptors --> AuthInterceptor[Auth Interceptor]
    Interceptors --> ErrorInterceptor[Error Interceptor]
    Interceptors --> LoadingInterceptor[Loading Interceptor]
    
    Services --> AuthService[Auth Service]
    Services --> NotificationService[Notification Service]
    Services --> LoggingService[Logging Service]
    Services --> ApiService[API Service]
    Services --> StorageService[Storage Service]
    Services --> ErrorHandlingService[Error Handling Service]
```

## UI Screens and Components

### Dashboard Screen

The dashboard provides an overview of financial status and recent activities:

```mermaid
graph TD
    Dashboard[Dashboard Screen]
    Dashboard --> Header[Dashboard Header]
    Dashboard --> Balance[Total Balance Card]
    Dashboard --> ExpenseIncome[Expense/Income Card]
    Dashboard --> RecentTransactions[Recent Transactions Table]
    Dashboard --> TopCategories[Top Categories Chart]
    Dashboard --> GoalTrackers[Goal Trackers]
```

### Transfer List Screen

```mermaid
graph TD
    TransferList[Transfer List Screen]
    TransferList --> Header[Transfers Header]
    TransferList --> Filters[Filter Panel]
    TransferList --> AddButton[Add Transfer Button]
    TransferList --> TransferTable[Transfer Table]
    TransferTable --> ActionButtons[Action Buttons]
    Filters --> DateRange[Date Range Filter]
    Filters --> CategoryFilter[Category Filter]
    Filters --> AmountFilter[Amount Filter]
    Filters --> SearchInput[Search Input]
```

### Transfer Form Screen

```mermaid
graph TD
    TransferForm[Transfer Form Screen]
    TransferForm --> Header[Transfer Form Header]
    TransferForm --> BasicInfo[Basic Info Section]
    TransferForm --> ItemsSection[Transfer Items Section]
    TransferForm --> ConversionSection[Currency Conversion Section]
    TransferForm --> Actions[Form Actions]
    
    BasicInfo --> DatePicker[Date Picker]
    BasicInfo --> Description[Description Field]
    
    ItemsSection --> AddItemButton[Add Item Button]
    ItemsSection --> ItemsList[Transfer Items List]
    ItemsList --> ItemRow[Transfer Item Row]
    ItemRow --> Value[Value Input]
    ItemRow --> CategorySelect[Category Selector]
    ItemRow --> CurrencySelect[Currency Selector]
    ItemRow --> DeleteItem[Delete Item Button]
    
    ConversionSection --> ConversionRow[Conversion Row]
    ConversionRow --> CurrencyPair[Currency Pair]
    ConversionRow --> ExchangeRate[Exchange Rate]
    
    Actions --> SaveButton[Save Button]
    Actions --> CancelButton[Cancel Button]
```

### Categories Screen

```mermaid
graph TD
    Categories[Categories Screen]
    Categories --> Header[Categories Header]
    Categories --> TreeView[Category Tree View]
    Categories --> CategoryDetail[Category Detail Panel]
    
    TreeView --> CategoryNode[Category Node]
    CategoryNode --> Expand[Expand/Collapse]
    CategoryNode --> DragHandle[Drag Handle]
    CategoryNode --> Actions[Node Actions]
    
    CategoryDetail --> Info[Category Info]
    CategoryDetail --> Balance[Category Balance]
    CategoryDetail --> Chart[Transaction History Chart]
    CategoryDetail --> Transactions[Recent Transactions]
```

### Reports Screen

```mermaid
graph TD
    Reports[Reports Screen]
    Reports --> Header[Reports Header]
    Reports --> ReportsList[Reports List]
    Reports --> CreateButton[Create Report Button]
    
    ReportsList --> ReportCard[Report Card]
    ReportCard --> ReportTitle[Report Title]
    ReportCard --> ReportType[Report Type]
    ReportCard --> ReportPreview[Report Preview]
    ReportCard --> Actions[Card Actions]
```

### Report Detail Screen

```mermaid
graph TD
    ReportDetail[Report Detail Screen]
    ReportDetail --> Header[Report Header]
    ReportDetail --> Filters[Report Filters]
    ReportDetail --> Visualization[Report Visualization]
    ReportDetail --> DataTable[Report Data Table]
    ReportDetail --> ExportOptions[Export Options]
    
    Visualization --> ChartArea[Chart Area]
    Visualization --> ChartControls[Chart Controls]
    
    DataTable --> TableHeaders[Table Headers]
    DataTable --> TableRows[Table Rows]
    DataTable --> TablePagination[Table Pagination]
```

### Goals Screen

```mermaid
graph TD
    Goals[Goals Screen]
    Goals --> Header[Goals Header]
    Goals --> CreateButton[Create Goal Button]
    Goals --> GoalCards[Goal Cards]
    
    GoalCards --> GoalCard[Goal Card]
    GoalCard --> GoalTitle[Goal Title]
    GoalCard --> ProgressBar[Progress Bar]
    GoalCard --> GoalAmount[Goal Amount]
    GoalCard --> TimeRemaining[Time Remaining]
    GoalCard --> Actions[Card Actions]
```

### Currency Management Screen

```mermaid
graph TD
    Currencies[Currency Management Screen]
    Currencies --> Header[Currency Header]
    Currencies --> Tabs[Currency Tabs]
    
    Tabs --> CurrencyList[Currency List Tab]
    Tabs --> ExchangeRates[Exchange Rates Tab]
    Tabs --> CurrencyConversion[Currency Conversion Tab]
    
    CurrencyList --> AddButton[Add Currency Button]
    CurrencyList --> Table[Currency Table]
    
    ExchangeRates --> AddRateButton[Add Rate Button]
    ExchangeRates --> RatesTable[Exchange Rates Table]
    
    CurrencyConversion --> ConversionForm[Conversion Form]
    CurrencyConversion --> ConversionResult[Conversion Result]
```

## Component Design Guidelines

### Component Communication Patterns

1. **Parent-Child Communication**
   - Inputs (`@Input()`) for passing data from parent to child
   - Outputs (`@Output()`) for events from child to parent

2. **Service-based Communication**
   - Shared services for components that need to communicate but aren't directly related
   - BehaviorSubject/Observable pattern for reactive updates

3. **State Management**
   - NgRx store for global application state
   - Component-level state for UI-specific concerns

### Component Patterns

1. **Container/Presenter Pattern**
   - Container components connect to services and manage state
   - Presenter components are pure and focus on UI rendering

2. **Smart/Dumb Component Pattern**
   - Smart components contain business logic and connect to services
   - Dumb components are presentational and receive data through inputs

3. **Component Lifecycle Management**
   - Proper use of lifecycle hooks
   - Subscription management and cleanup in `ngOnDestroy()`

4. **Lazy Loading**
   - Feature modules are lazy loaded for improved performance
   - Core and shared modules are eagerly loaded

## UI/UX Guidelines

### Responsive Design

All components must be responsive and work well on:
- Desktop (1200px+)
- Tablet (768px - 1199px)
- Mobile (< 768px)

#### Responsive Strategies
- Use Angular Material's grid system and breakpoints
- Implement mobile-first design approach
- Collapse complex tables into cards on mobile
- Use responsive typography

### Accessibility

- Ensure all components meet WCAG 2.1 AA standards
- Include proper ARIA attributes
- Support keyboard navigation
- Test with screen readers
- Maintain sufficient color contrast

### Theming

- Use Angular Material's theming system
- Support light and dark modes
- Provide high-contrast mode for accessibility
- Allow customizable accent colors

### Error Handling and Feedback

- Show clear error messages close to the relevant input
- Implement field-level validation feedback
- Use toast notifications for system messages
- Include loading indicators for async operations

## Implementation Plan

The UI implementation will be phased:

1. **Phase 1: Core Infrastructure**
   - Authentication module
   - Navigation and layout components
   - Core services and interceptors
   - Shared components library

2. **Phase 2: Key Features**
   - Dashboard module
   - Transfers module
   - Categories module
   - Settings module

3. **Phase 3: Advanced Features**
   - Reports module
   - Goals module
   - Currency module
   - Data import/export functionality

4. **Phase 4: Refinement**
   - Performance optimization
   - Comprehensive testing
   - Accessibility improvements
   - Visual polish