# Code Inventory

This document provides a tabular inventory of all key files in the current Ruby on Rails application, along with their purpose and dependencies. This inventory will help guide the migration to the Angular and Node.js architecture.

## Models

| File | Purpose | Upstream Dependencies | Downstream Dependencies |
|------|---------|----------------------|------------------------|
| `app/models/user.rb` | User account model with authentication and settings | Authentication gems | Category, Transfer, Goal, Report |
| `app/models/category.rb` | Hierarchical category structure using nested set | awesome_nested_set gem | TransferItem, Goal, Report |
| `app/models/transfer.rb` | Core transaction model representing financial movements | - | TransferItem, Conversion |
| `app/models/transfer_item.rb` | Individual items within a transfer | Transfer, Category, Currency | - |
| `app/models/currency.rb` | Currency representation with symbol and name | - | TransferItem, Exchange, Goal |
| `app/models/exchange.rb` | Currency exchange rates | Currency | Conversion |
| `app/models/conversion.rb` | Links transfers to exchange rates | Transfer, Exchange | - |
| `app/models/goal.rb` | Financial goal tracking | User, Category, Currency | - |
| `app/models/report.rb` | Base class for financial reports | User | CategoryReportOption |
| `app/models/share_report.rb` | Category distribution reports | Report | - |
| `app/models/flow_report.rb` | Cash flow reports | Report | - |
| `app/models/value_report.rb` | Value change over time reports | Report | - |
| `app/models/multiple_category_report.rb` | Reports with multiple categories | Report | CategoryReportOption |
| `app/models/category_report_option.rb` | Options for category inclusion in reports | Category, Report | - |
| `app/models/money.rb` | Value object for money amounts | - | Multiple models |

## Controllers

| File | Purpose | Upstream Dependencies | Downstream Dependencies |
|------|---------|----------------------|------------------------|
| `app/controllers/application_controller.rb` | Base controller with shared functionality | Rails | All controllers |
| `app/controllers/users_controller.rb` | User account management | ApplicationController | User model |
| `app/controllers/categories_controller.rb` | Category CRUD operations | ApplicationController | Category model |
| `app/controllers/transfers_controller.rb` | Transfer management | ApplicationController | Transfer, TransferItem models |
| `app/controllers/reports_controller.rb` | Report generation and display | ApplicationController | Report models |
| `app/controllers/goals_controller.rb` | Goal management | ApplicationController | Goal model |
| `app/controllers/currencies_controller.rb` | Currency management | ApplicationController | Currency model |
| `app/controllers/exchanges_controller.rb` | Exchange rate management | ApplicationController | Exchange model |
| `app/controllers/sessions_controller.rb` | Authentication sessions | ApplicationController | User model |

## Views

| File | Purpose | Upstream Dependencies | Downstream Dependencies |
|------|---------|----------------------|------------------------|
| `app/views/layouts/application.html.erb` | Main application layout | - | All views |
| `app/views/transfers/index.html.erb` | List of transfers | TransfersController | Partials |
| `app/views/transfers/new.html.erb` | Transfer creation form | TransfersController | Partials |
| `app/views/transfers/edit.html.erb` | Transfer editing form | TransfersController | Partials |
| `app/views/transfers/_form.html.erb` | Transfer form partial | - | - |
| `app/views/categories/index.html.erb` | Category hierarchy display | CategoriesController | Partials |
| `app/views/categories/show.html.erb` | Category details and transactions | CategoriesController | Partials |
| `app/views/reports/index.html.erb` | List of reports | ReportsController | Partials |
| `app/views/reports/show.html.erb` | Report display | ReportsController | Type-specific partials |
| `app/views/reports/show_share_report.html.erb` | Share report display | ReportsController | - |
| `app/views/reports/show_flow_report.html.erb` | Flow report display | ReportsController | - |
| `app/views/reports/show_value_report.html.erb` | Value report display | ReportsController | - |
| `app/views/goals/index.html.erb` | Goals list and progress | GoalsController | Partials |
| `app/views/goals/_form.html.erb` | Goal form partial | - | - |
| `app/views/users/show.html.erb` | User profile | UsersController | - |
| `app/views/users/edit.html.erb` | Edit user settings | UsersController | - |
| `app/views/sessions/new.html.erb` | Login form | SessionsController | - |

## Migrations

| File | Purpose | Upstream Dependencies | Downstream Dependencies |
|------|---------|----------------------|------------------------|
| `db/migrate/001_create_users.rb` | Create users table | - | All other migrations |
| `db/migrate/002_create_categories.rb` | Create categories table | 001 | 003, 004 |
| `db/migrate/003_create_transfers.rb` | Create transfers table | 001 | 004 |
| `db/migrate/004_create_transfer_items.rb` | Create transfer_items table | 002, 003, 007 | - |
| `db/migrate/007_create_currencies.rb` | Create currencies table | 001 | 004, 008 |
| `db/migrate/008_create_exchanges.rb` | Create exchanges table | 001, 007 | 009 |
| `db/migrate/009_create_conversions.rb` | Create conversions table | 003, 008 | - |
| `db/migrate/20081110145518_create_goals.rb` | Create goals table | 001, 002, 007 | - |
| `db/migrate/20081208212007_create_reports.rb` | Create reports table | 001, 002 | 20081208215053 |
| `db/migrate/20081208215053_create_category_report_options.rb` | Create category_report_options table | 002, 20081208212007 | - |
| `db/migrate/20090104123107_add_default_currency_column.rb` | Add default currency to users | 001, 007 | - |

## Helpers and Modules

| File | Purpose | Upstream Dependencies | Downstream Dependencies |
|------|---------|----------------------|------------------------|
| `app/helpers/application_helper.rb` | Common view helpers | - | All views |
| `app/helpers/transfers_helper.rb` | Transfer-specific helpers | ApplicationHelper | Transfer views |
| `app/helpers/categories_helper.rb` | Category-specific helpers | ApplicationHelper | Category views |
| `app/helpers/reports_helper.rb` | Report-specific helpers | ApplicationHelper | Report views |
| `lib/hash_enums.rb` | Enumeration support for models | - | Multiple models |
| `lib/periodable.rb` | Period calculation for date ranges | - | Report models |
| `lib/money_extensions.rb` | Extensions for money handling | - | Money model |

## JavaScript and CSS

| File | Purpose | Upstream Dependencies | Downstream Dependencies |
|------|---------|----------------------|------------------------|
| `public/javascripts/application.js` | Main application JS | jQuery | All pages |
| `public/javascripts/transfers.js` | Transfer form handling | jQuery, application.js | Transfer views |
| `public/javascripts/categories.js` | Category tree handling | jQuery, application.js | Category views |
| `public/javascripts/reports.js` | Report visualization | jQuery, application.js, Highcharts | Report views |
| `public/stylesheets/application.css` | Main application styles | - | All pages |
| `public/stylesheets/transfers.css` | Transfer-specific styles | application.css | Transfer views |
| `public/stylesheets/categories.css` | Category-specific styles | application.css | Category views |
| `public/stylesheets/reports.css` | Report-specific styles | application.css | Report views |

## Configuration and Initializers

| File | Purpose | Upstream Dependencies | Downstream Dependencies |
|------|---------|----------------------|------------------------|
| `config/routes.rb` | Application routing | Rails | All controllers |
| `config/database.yml` | Database configuration | - | All models |
| `config/environment.rb` | Environment configuration | - | Application |
| `config/initializers/inflections.rb` | Custom inflection rules | - | Models |
| `config/initializers/new_rails_defaults.rb` | Rails defaults | - | Application |
| `config/locales/en.yml` | English translations | - | Views |

## Tests

| File | Purpose | Upstream Dependencies | Downstream Dependencies |
|------|---------|----------------------|------------------------|
| `test/unit/user_test.rb` | User model tests | User model | - |
| `test/unit/category_test.rb` | Category model tests | Category model | - |
| `test/unit/transfer_test.rb` | Transfer model tests | Transfer model | - |
| `test/unit/currency_test.rb` | Currency model tests | Currency model | - |
| `test/functional/transfers_controller_test.rb` | Transfers controller tests | TransfersController | - |
| `test/functional/categories_controller_test.rb` | Categories controller tests | CategoriesController | - |
| `test/functional/reports_controller_test.rb` | Reports controller tests | ReportsController | - |
| `test/integration/category_flows_test.rb` | Category integration tests | Categories feature | - |
| `test/integration/transfer_flows_test.rb` | Transfer integration tests | Transfers feature | - |

## External Dependencies

| Dependency | Purpose | Uses |
|------------|---------|------|
| Ruby on Rails | Web framework | Core application |
| awesome_nested_set | Tree structure for categories | Category model |
| authlogic | Authentication | User model |
| will_paginate | Pagination | List views |
| thinking-sphinx | Full text search | Search functionality |
| factory_girl | Test data generation | Tests |
| highcharts | Chart visualization | Reports |
| jQuery | JavaScript library | Front-end interactivity |

## Key Business Logic Components

| Component | Location | Purpose | Key Functions |
|-----------|----------|---------|--------------|
| Category Balance Calculation | `app/models/category.rb` | Calculate category balances | `saldo`, `saldo_for_period` |
| Transfer Validation | `app/models/transfer.rb` | Ensure transfer items balance | `validate`, `different_income_outcome?` |
| Currency Conversion | `app/models/exchange.rb` | Convert between currencies | `exchange`, `exchange_value` |
| Report Generation | `app/models/report.rb` and subclasses | Generate financial reports | Various calculation methods |
| Goal Progress Tracking | `app/models/goal.rb` | Track progress toward goals | `current_value`, `percentage` |
| Nested Category Management | `app/controllers/categories_controller.rb` | Manage category hierarchy | `move`, `create`, `update` |

## Data Flow Paths

### Transfer Creation Flow
1. `TransfersController#new` → 
2. `app/views/transfers/new.html.erb` →
3. `app/views/transfers/_form.html.erb` →
4. `TransfersController#create` →
5. `Transfer.create` →
6. `Transfer#validate` →
7. `TransferItem.create` (multiple) →
8. `Conversion.create` (if needed)

### Category Balance Calculation Flow
1. `CategoriesController#show` →
2. `Category#saldo_for_period` →
3. `Category.build_saldo_for_period_sql` →
4. SQL query execution →
5. `app/views/categories/show.html.erb`

### Report Generation Flow
1. `ReportsController#show` →
2. `Report.find` →
3. Report subclass methods (e.g., `ShareReport#calculate_share_values`) →
4. `app/views/reports/show_*.html.erb` →
5. JavaScript for visualization

## Integration Points

| Integration Point | Purpose | Related Files |
|------------------|---------|---------------|
| Authentication System | User login and session management | `app/controllers/sessions_controller.rb`, `app/models/user.rb` |
| Search Functionality | Full-text search across models | `config/initializers/thinking_sphinx.rb`, model indexes |
| Reporting System | Generate and display financial reports | Report models, `app/controllers/reports_controller.rb` |
| Currency Exchange | Convert between different currencies | `app/models/exchange.rb`, `app/models/conversion.rb` |
| Category Hierarchy | Nested set model for categories | `app/models/category.rb`, awesome_nested_set |
| Import/Export | Data import and export functionality | `lib/importers/*.rb`, `lib/exporters/*.rb` |

## Summary of Technical Debt

| Area | Issues | Impact |
|------|--------|--------|
| Models | Some large models with too many responsibilities | Maintenance complexity |
| Controllers | Some fat controllers with business logic | Difficult testing |
| Tests | Incomplete test coverage | Regression risks |
| JavaScript | Outdated jQuery patterns | Maintainability issues |
| Performance | Some N+1 query issues | Slow page loads |
| UI/UX | Inconsistent styling | User experience issues |
| Documentation | Incomplete code documentation | Knowledge transfer challenges |