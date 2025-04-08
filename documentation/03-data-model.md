# Data Model Documentation

## Entity Relationship Diagram

```mermaid
erDiagram
    Users ||--o{ Categories : has
    Users ||--o{ Transfers : creates
    Users ||--o{ Goals : sets
    Users ||--o{ Reports : generates
    Users ||--o{ Currencies : manages
    Categories ||--o{ TransferItems : categorizes
    Transfers ||--o{ TransferItems : contains
    Categories ||--o{ Goals : tracks
    Categories }|--|| SystemCategories : belongs_to
    TransferItems }|--|| Currencies : uses
    Exchanges ||--o{ Conversions : provides
    Transfers ||--o{ Conversions : uses
    Reports ||--o{ CategoryReportOptions : configures
    Categories ||--o{ CategoryReportOptions : includes

    Users {
        integer id PK
        string login
        string name
        string email
        string crypted_password
        string salt
        datetime created_at
        datetime updated_at
        integer default_currency_id FK
        boolean invert_saldo_for_income
        integer transaction_amount_limit_type_int
    }

    Categories {
        integer id PK
        string name
        string description
        integer category_type_int
        integer user_id FK
        integer parent_id
        integer lft
        integer rgt
        boolean loan_category
        string email
        text bankinfo
        string bank_account_number
    }

    Transfers {
        integer id PK
        text description
        date day
        integer user_id FK
        string import_guid
    }

    TransferItems {
        integer id PK
        text description
        decimal value
        integer transfer_id FK
        integer category_id FK
        integer currency_id FK
        string import_guid
    }

    Currencies {
        integer id PK
        string symbol
        string long_symbol
        string name
        string long_name
        integer user_id FK
    }

    Goals {
        integer id PK
        string description
        boolean include_subcategories
        integer period_type_int
        integer goal_type_int
        float value
        integer category_id FK
        integer currency_id FK
        date period_start
        date period_end
        boolean is_cyclic
        boolean is_finished
    }

    Reports {
        integer id PK
        string type
        string name
        integer period_type_int
        date period_start
        date period_end
        integer report_view_type_int
        integer user_id FK
        integer depth
        integer category_id FK
        boolean temporary
    }

    Exchanges {
        integer id PK
        integer left_currency_id FK
        integer right_currency_id FK
        decimal left_to_right
        decimal right_to_left
        date day
        integer user_id FK
    }

    SystemCategories {
        integer id PK
        string name
        integer parent_id
        integer lft
        integer rgt
        string description
        integer category_type_int
        string name_with_path
    }

    CategoryReportOptions {
        integer id PK
        integer inclusion_type_int
        integer report_id FK
        integer category_id FK
    }

    Conversions {
        integer id PK
        integer exchange_id FK
        integer transfer_id FK
    }
```

## Table Descriptions

### Users
- Primary table for user management
- Stores authentication details and user preferences
- Controls currency and transaction settings

### Categories
- Hierarchical structure for financial categorization
- Supports nested categories (using lft/rgt for tree structure)
- Links to system categories for standardization
- Can be configured as loan categories

### Transfers
- Records financial transactions
- Links to transfer items for detailed breakdown
- Maintains user ownership and timing information

### TransferItems
- Detailed transaction entries
- Links to categories and currencies
- Stores individual monetary values

### Currencies
- Manages supported currencies
- Can be system-defined or user-defined
- Used in transfers and exchanges

### Goals
- Financial goal tracking
- Period-based targeting
- Supports cyclic and one-time goals
- Category-specific tracking

### Reports
- Customizable financial reporting
- Multiple report types supported
- Period-based analysis
- Category-specific reporting

### Exchanges
- Currency exchange rate management
- Bidirectional rate tracking
- Daily rate history
- User-specific rates possible

### SystemCategories
- Pre-defined category hierarchy
- Standardized categorization
- Supports type-based categorization

### CategoryReportOptions
- Report customization settings
- Controls category inclusion in reports
- Configures report parameters

### Conversions
- Links transfers to exchange rates
- Manages multi-currency transactions

## Key Relationships

1. **User-centric Design**
   - Users own their categories, transfers, and goals
   - User-specific currency preferences
   - Customizable reporting per user

2. **Hierarchical Categories**
   - Nested category structure
   - System and user-defined categories
   - Category-based goal tracking

3. **Financial Transactions**
   - Transfers contain multiple items
   - Category classification
   - Multi-currency support

4. **Reporting System**
   - Flexible report configuration
   - Category-based filtering
   - Period-based analysis

## Database Indexes

The schema includes optimized indexes for:
- User lookups
- Category tree traversal
- Transfer queries
- Currency operations
- Report generation

## Data Integrity

1. **Foreign Key Constraints**
   - Category relationships
   - Transfer item associations
   - Currency references

2. **Required Fields**
   - User authentication data
   - Transaction details
   - Category structure

3. **Default Values**
   - Currency settings
   - Report configurations
   - Goal parameters

This data model provides a robust foundation for financial tracking, reporting, and goal management while maintaining data integrity and supporting multi-currency operations.