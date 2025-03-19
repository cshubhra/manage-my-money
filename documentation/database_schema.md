# Database Schema Documentation

This document provides a comprehensive overview of the database schema for the financial management application, detailing tables, fields, relationships, and indexes.

## Core Tables

### Users

```mermaid
erDiagram
    USERS {
        integer id PK
        string login
        string name
        string email
        string crypted_password
        string salt
        datetime created_at
        datetime updated_at
        string remember_token
        datetime remember_token_expires_at
        string activation_code
        datetime activated_at
        integer transaction_amount_limit_type_int
        integer transaction_amount_limit_value
        boolean include_transactions_from_subcategories
        integer multi_currency_balance_calculating_algorithm_int
        integer default_currency_id FK
        boolean invert_saldo_for_income
    }
```

The Users table stores user account information, preferences, and authentication details.

### Categories

```mermaid
erDiagram
    CATEGORIES {
        integer id PK
        string name
        text description
        integer parent_id FK
        integer category_type_int
        integer user_id FK
        integer lft
        integer rgt
        decimal opening_balance
        integer currency_id FK
        integer system_category_id FK
        string bank_account_number
        datetime created_at
        datetime updated_at
    }
```

Categories form a hierarchical tree structure using nested set model (lft/rgt columns) for efficient tree operations.

### Transfers

```mermaid
erDiagram
    TRANSFERS {
        integer id PK
        text description
        date day
        integer user_id FK
        string import_guid
    }
```

Transfers represent financial transactions with at least two sides (debit and credit).

### Transfer_Items

```mermaid
erDiagram
    TRANSFER_ITEMS {
        integer id PK
        decimal value
        text description
        integer category_id FK
        integer transfer_id FK
        integer currency_id FK
    }
```

Transfer items are the individual entries that make up a transfer (transaction).

### Currencies

```mermaid
erDiagram
    CURRENCIES {
        integer id PK
        string symbol
        string long_symbol
        string name
        string long_name
        integer user_id FK
    }
```

Currencies represent monetary units used in the system.

### Exchanges

```mermaid
erDiagram
    EXCHANGES {
        integer id PK
        decimal left_to_right_rate
        date day
        integer user_id FK
        integer left_currency_id FK
        integer right_currency_id FK
    }
```

Exchanges store currency exchange rates.

### Conversions

```mermaid
erDiagram
    CONVERSIONS {
        integer id PK
        integer transfer_id FK
        integer exchange_id FK
    }
```

Conversions link transfers with the exchange rates used for currency conversion.

### Reports

```mermaid
erDiagram
    REPORTS {
        integer id PK
        string type
        string name
        integer period_type_int
        date period_start
        date period_end
        integer report_view_type_int
        integer user_id FK
        datetime created_at
        datetime updated_at
        integer depth
        integer max_categories_values_count
        integer category_id FK
        integer period_division_int
        boolean temporary
        boolean relative_period
    }
```

Reports store configurations for financial reports.

### Category_Report_Options

```mermaid
erDiagram
    CATEGORY_REPORT_OPTIONS {
        integer id PK
        integer category_id FK
        integer multiple_category_report_id FK
        integer inclusion_type_int
    }
```

Links categories with reports and specifies how they should be included.

### Goals

```mermaid
erDiagram
    GOALS {
        integer id PK
        string name
        text description
        decimal target_balance
        date end_date
        integer category_id FK
        integer created_at
        integer updated_at
        integer user_id FK
        integer currency_id FK
        boolean cyclic
        integer period_type_int
        integer period_quantity
    }
```

Goals track financial objectives with target balances.

### System_Categories

```mermaid
erDiagram
    SYSTEM_CATEGORIES {
        integer id PK
        string name
        text description
        integer category_type_int
        integer level
    }
```

System-defined categories that serve as templates.

## Table Relationships

```mermaid
erDiagram
    USERS ||--o{ CATEGORIES : "has many"
    USERS ||--o{ TRANSFERS : "has many"  
    USERS ||--o{ CURRENCIES : "has many"
    USERS ||--o{ EXCHANGES : "has many"
    USERS ||--o{ REPORTS : "has many"
    USERS ||--o{ GOALS : "has many"
    USERS ||--o| CURRENCIES : "default currency"
    
    CATEGORIES ||--o{ TRANSFER_ITEMS : "has many"
    CATEGORIES ||--o{ GOALS : "has many"
    CATEGORIES |o--|| SYSTEM_CATEGORIES : "based on"
    CATEGORIES |o--|| CURRENCIES : "opening balance currency"
    CATEGORIES |o--|| CATEGORIES : "parent"
    
    TRANSFERS ||--o{ TRANSFER_ITEMS : "has many"
    TRANSFERS ||--o{ CONVERSIONS : "has many"
    TRANSFERS |o--|| USERS : "belongs to"
    
    TRANSFER_ITEMS |o--|| CATEGORIES : "belongs to"
    TRANSFER_ITEMS |o--|| TRANSFERS : "belongs to"
    TRANSFER_ITEMS |o--|| CURRENCIES : "belongs to"
    
    CURRENCIES |o--|| USERS : "belongs to"
    
    EXCHANGES ||--o{ CONVERSIONS : "has many"
    EXCHANGES |o--|| CURRENCIES : "left currency"
    EXCHANGES |o--|| CURRENCIES : "right currency"
    EXCHANGES |o--|| USERS : "belongs to"
    
    CONVERSIONS |o--|| TRANSFERS : "belongs to"
    CONVERSIONS |o--|| EXCHANGES : "belongs to"
    
    REPORTS |o--|| USERS : "belongs to"
    REPORTS |o--o| CATEGORIES : "primary category"
    REPORTS |o--o{ CATEGORY_REPORT_OPTIONS : "has many"
    
    CATEGORY_REPORT_OPTIONS |o--|| CATEGORIES : "belongs to"
    CATEGORY_REPORT_OPTIONS |o--|| REPORTS : "belongs to"
    
    GOALS |o--|| CATEGORIES : "belongs to"
    GOALS |o--|| USERS : "belongs to"
    GOALS |o--|| CURRENCIES : "belongs to"
```

## Key Indexes

- **categories**: user_id, lft, rgt, parent_id
- **transfers**: user_id, day
- **transfer_items**: transfer_id, category_id, currency_id
- **currencies**: user_id
- **exchanges**: user_id, left_currency_id, right_currency_id, day
- **reports**: user_id, type
- **goals**: user_id, category_id

## Database Constraints

- User deletion cascades to all user-owned data
- Category deletion prevented if it has transfers
- Currency deletion prevented if it's used in transfers
- Transfer balance must be zero (sum of items equals zero)
- Exchange rates must be positive
- Categories must maintain a valid nested set structure

This schema documentation provides a comprehensive overview of the database structure, which will serve as a reference for the reengineering effort to convert the application to an Angular and Node.js stack.