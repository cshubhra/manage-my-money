# Ruby on Rails Application Architecture

This document describes the architecture of the existing Ruby on Rails application, which will serve as the foundation for our Angular and Spring Boot migration.

## Overall Architecture

The application follows the Model-View-Controller (MVC) architecture pattern of Ruby on Rails:

1. **Models** - Located in `app/models/` - Represent the data structures and business logic
2. **Views** - Located in `app/views/` - Generate HTML representations of data
3. **Controllers** - Located in `app/controllers/` - Handle user requests and coordinate models and views

## Key Components

### Authentication System
- User registration, activation, and authentication
- Session management
- Uses the RestfulAuthentication plugin

### Financial Management
- Categories (hierarchical structure)
- Transfers and transfer items
- Multi-currency support with exchange rates
- Financial reporting

### Core Models

- **User**: Authentication and user preferences
- **Category**: Hierarchical structure for organizing financial data
- **Transfer**: Financial transaction records
- **TransferItem**: Individual components of transfers
- **Currency**: Monetary units
- **Exchange**: Currency exchange rates
- **Goal**: Financial planning targets
- **Report**: Financial reports (Share, Value, Flow)

### Controllers

- **ApplicationController**: Base controller with common functionality
- **CategoriesController**: Category management
- **TransfersController**: Transfer management
- **ReportsController**: Report generation and display
- **SessionsController**: Authentication management
- **UsersController**: User management
- **ExchangesController**: Exchange rate management
- **CurrenciesController**: Currency management
- **GoalsController**: Goal management
- **ImportController**: Import functionality

### Views

The views are organized by controller with various partials for reuse across different pages. Main layout is in `app/views/layouts/application.html.erb`.

### Plugins and Extensions

The application uses various plugins:
- RestfulAuthentication for user authentication
- AwesomeNestedSet for hierarchical category management
- BackgroundRB for background jobs
- ThinkingSphinx for search functionality
- ValidatesEquality for model validation

## Data Flow

1. User requests arrive at controllers
2. Controllers load appropriate models
3. Models handle business logic and database operations
4. Views render HTML based on models
5. Controllers return rendered views to users

## Key Functionality

1. **Financial Management**
   - Categorization of income and expenses
   - Transfer recording and tracking
   - Multi-currency support
   - Exchange rate management

2. **Reporting**
   - Different report types (Share, Value, Flow)
   - Customizable periods and categories
   - Visual representation through graphs

3. **Goals and Planning**
   - Setting financial goals
   - Tracking progress
   - Cyclic/recurring goals

4. **Import/Export**
   - Support for importing from bank formats
   - GnuCash import support