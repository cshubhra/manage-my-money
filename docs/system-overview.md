# Ruby on Rails Application - System Overview

## Introduction

The application appears to be a personal finance management system, similar to a budgeting or expense tracking application. It allows users to track their financial transactions, categorize expenses/income, manage currencies and exchange rates, generate reports, and plan financial goals.

## Core Features

1. **User Management**:
   - User registration and authentication
   - User profiles and preferences

2. **Categories Management**:
   - Hierarchical category structure (parent-child relationships)
   - Different category types: Assets, Income, Expenses, Loans, Balance
   - System categories as templates

3. **Transaction Management**:
   - Transfer creation with multiple items
   - Categories assignment
   - Transaction history

4. **Multi-Currency Support**:
   - Currency management
   - Exchange rates tracking
   - Currency conversion for transactions

5. **Financial Planning**:
   - Goals setting and tracking
   - Loans management (debtors and creditors)

6. **Reporting**:
   - Various report types (Share, Value, Flow)
   - Customizable date ranges
   - Category-based reports

7. **Data Import**:
   - Import functionality for bank statements
   - Support for different file formats

## Technology Stack

- **Framework**: Ruby on Rails
- **Database**: Not explicitly specified, but appears to be SQL-based (likely MySQL or PostgreSQL)
- **Frontend**: HTML, CSS, JavaScript (with Prototype.js framework)
- **Authentication**: Custom authentication system
- **Additional Libraries**:
  - BackgroundRB for background processing
  - Thinking Sphinx for search functionality
  - Various plugins for specific features

## Application Structure

### Models

The application follows the MVC (Model-View-Controller) pattern. Key models include:

1. **User**: Manages user accounts and preferences
2. **Category**: Hierarchical system for organizing transactions
3. **Transfer**: Core transaction model
4. **TransferItem**: Individual items within a transfer
5. **Currency**: Currency management
6. **Exchange**: Currency exchange rates
7. **Goal**: Financial planning goals
8. **Report**: Various report types

### Controllers

The application has controllers for each major feature set:

1. **UsersController**: Account management
2. **CategoriesController**: Category operations
3. **TransfersController**: Transaction management
4. **CurrenciesController**: Currency operations
5. **ExchangesController**: Exchange rate management
6. **GoalsController**: Financial goals
7. **ReportsController**: Report generation
8. **DebtorsController** & **CreditorsController**: Loan management
9. **ImportController**: Data import functionality

### Views

Views are organized by controller, with shared partials for common elements. The application uses ERB templates and includes:

1. Form templates for data entry
2. Display templates for viewing data
3. Reports and visualizations
4. Shared partials for common UI elements

## Database Schema

The database schema includes tables for:

1. **users**: User account information
2. **categories**: Hierarchical category structure
3. **transfers**: Core transaction records
4. **transfer_items**: Individual items in transactions
5. **currencies**: Currency information
6. **exchanges**: Exchange rate records
7. **goals**: Financial planning records
8. **reports**: Saved report configurations
9. Other supporting tables

## Key Functional Flows

1. **Transaction Recording**:
   - User creates a transfer
   - Assigns categories and currencies
   - System validates balance (income equals outcome)
   - Transaction is stored with related items

2. **Reporting**:
   - User selects report type
   - Configures parameters (categories, date range)
   - System generates the report with calculations
   - Results are displayed visually

3. **Financial Planning**:
   - User creates financial goals
   - System tracks progress against transactions
   - Provides status updates and projections

4. **Multi-Currency Operations**:
   - User records exchange rates
   - Transactions in different currencies are converted
   - Balance calculations use configurable algorithms for currency handling

## Integration Points

1. **Import Functionality**: Interfaces with bank statement formats
2. **Email Notifications**: Sends various system notifications
3. **Background Processing**: Handles report generation and recurrent tasks

## Security Considerations

1. **Authentication**: Custom user authentication system
2. **Authorization**: User data isolation
3. **Input Validation**: Form validation for all user inputs
4. **SSL Support**: Available for secure communications