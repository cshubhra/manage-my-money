# Code Inventory

This document provides a tabular inventory of the Ruby on Rails application code base, including the purpose and dependencies of each file.

## Models

| File | Purpose | Upstream Dependencies | Downstream Dependencies |
|------|---------|----------------------|------------------------|
| `app/models/user.rb` | User authentication and profile management. Stores user preferences for currency, transaction limits, etc. | None | Categories, Transfers, Currencies, Exchanges, Goals, Reports |
| `app/models/category.rb` | Represents financial categories in a hierarchical structure using nested set model. | User | Transfer_Items, Goals |
| `app/models/transfer.rb` | Core transaction model representing money movement between categories. | User | Transfer_Items, Conversions |
| `app/models/transfer_item.rb` | Individual line items within transfers with specific categories and currencies. | Transfer, Category, Currency | None |
| `app/models/currency.rb` | Manages currency information including symbols and conversion rates. | User (optional) | Transfer_Items, Exchanges, Goals |
| `app/models/exchange.rb` | Stores currency exchange rates between currency pairs. | Left_Currency, Right_Currency, User | Conversions |
| `app/models/conversion.rb` | Links transfers with exchange rates for multi-currency transfers. | Transfer, Exchange | None |
| `app/models/goal.rb` | Financial goals with target values, periods, and tracking. | User, Category, Currency | None |
| `app/models/report.rb` | Base report class with common functionality for all report types. | User | None |
| `app/models/share_report.rb` | Reports showing category distribution (pie charts). | Report, Category | None |
| `app/models/flow_report.rb` | Reports showing cash flow over time. | Report, CategoryReportOption | None |
| `app/models/value_report.rb` | Reports showing value changes across categories. | Report, CategoryReportOption | None |
| `app/models/money.rb` | Value object representing money with multi-currency support. | Currency | Used throughout for monetary calculations |

## Controllers

| File | Purpose | Model Dependencies | View Dependencies |
|------|---------|-------------------|-------------------|
| `app/controllers/application_controller.rb` | Base controller with common methods and authentication. | User | Layouts |
| `app/controllers/categories_controller.rb` | CRUD operations for categories, hierarchy management. | Category | Category views |
| `app/controllers/creditors_controller.rb` | Manages creditors (loan accounts). | Category (LOAN type) | Creditor views |
| `app/controllers/currencies_controller.rb` | CRUD operations for currencies. | Currency | Currency views |
| `app/controllers/exchanges_controller.rb` | Manages exchange rates between currencies. | Exchange, Currency | Exchange views |
| `app/controllers/goals_controller.rb` | CRUD operations for financial goals. | Goal, Category | Goal views |
| `app/controllers/reports_controller.rb` | Manages report creation and display. | Report (and subclasses) | Report views |
| `app/controllers/transfers_controller.rb` | Core transaction management. | Transfer, TransferItem | Transfer views |
| `app/controllers/users_controller.rb` | User registration, profile management. | User | User views |
| `app/controllers/sessions_controller.rb` | Authentication management. | User | Session views |
| `app/controllers/autocomplete_controller.rb` | Provides autocomplete functionality. | Transfer, TransferItem | AJAX responses |

## Config Files

| File | Purpose | Dependencies |
|------|---------|-------------|
| `config/routes.rb` | URL routing configuration. | All controllers |
| `config/environment.rb` | Environment configuration, gem dependencies. | All application |
| `db/schema.rb` | Database schema definition. | All models |

## Libraries and Modules

| File | Purpose | Used By |
|------|---------|---------|
| `lib/authentication.rb` | User authentication functionality. | User model |
| `lib/hash_enums.rb` | Provides enum-like functionality for models. | Multiple models (User, Goal, etc.) |
| `lib/periodable.rb` | Date period handling functionality. | Report, Goal |

## JavaScript Files

| File | Purpose | Dependencies |
|------|---------|-------------|
| `public/javascripts/application.js` | Core application JavaScript. | Multiple views |
| `public/javascripts/transfer_form.js` | Transfer form handling. | Transfer views |
| `public/javascripts/category_form.js` | Category form functionality. | Category views |

## Key Design Patterns Used

1. **MVC (Model-View-Controller)** - Standard Rails architecture
2. **STI (Single Table Inheritance)** - Used for Report types
3. **Nested Set Model** - Used for hierarchical categories
4. **Value Object** - Money class encapsulates monetary values
5. **Observer Pattern** - ActiveRecord callbacks
6. **Decorator** - Extended functionality through modules like Periodable
7. **Builder Pattern** - Report generation