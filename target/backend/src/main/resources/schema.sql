-- Drop tables if they exist
DROP TABLE IF EXISTS conversions;
DROP TABLE IF EXISTS transfer_items;
DROP TABLE IF EXISTS transfers;
DROP TABLE IF EXISTS category_report_options;
DROP TABLE IF EXISTS reports;
DROP TABLE IF EXISTS goals;
DROP TABLE IF EXISTS categories_system_categories;
DROP TABLE IF EXISTS system_categories;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS exchanges;
DROP TABLE IF EXISTS currencies;
DROP TABLE IF EXISTS users;

-- Create Users table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login VARCHAR(40) UNIQUE,
    name VARCHAR(100) DEFAULT '',
    email VARCHAR(100),
    password VARCHAR(60),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    remember_token VARCHAR(40),
    remember_token_expires_at TIMESTAMP,
    activation_code VARCHAR(40),
    activated_at TIMESTAMP,
    transaction_amount_limit_type_int INT DEFAULT 2 NOT NULL,
    transaction_amount_limit_value INT,
    include_transactions_from_subcategories BOOLEAN DEFAULT FALSE,
    multi_currency_balance_calculating_algorithm_int INT DEFAULT 0,
    default_currency_id BIGINT DEFAULT 1,
    invert_saldo_for_income BOOLEAN DEFAULT TRUE
);

-- Create Currencies table
CREATE TABLE currencies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR(10) NOT NULL,
    long_symbol VARCHAR(20) NOT NULL,
    name VARCHAR(50) NOT NULL,
    long_name VARCHAR(100) NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create Categories table
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    category_type_int INT,
    user_id BIGINT,
    parent_id BIGINT,
    lft INT,
    rgt INT,
    import_guid VARCHAR(255),
    imported BOOLEAN DEFAULT FALSE,
    email VARCHAR(255),
    bankinfo TEXT,
    bank_account_number VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    loan_category BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (parent_id) REFERENCES categories(id)
);

-- Create System Categories table
CREATE TABLE system_categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    parent_id BIGINT,
    lft INT,
    rgt INT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    description VARCHAR(500),
    category_type_int INT,
    cached_level INT,
    name_with_path VARCHAR(500),
    FOREIGN KEY (parent_id) REFERENCES system_categories(id)
);

-- Create Categories System Categories join table
CREATE TABLE categories_system_categories (
    category_id BIGINT NOT NULL,
    system_category_id BIGINT NOT NULL,
    PRIMARY KEY (category_id, system_category_id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (system_category_id) REFERENCES system_categories(id)
);

-- Create Exchanges table
CREATE TABLE exchanges (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    left_currency_id BIGINT NOT NULL,
    right_currency_id BIGINT NOT NULL,
    left_to_right DECIMAL(8,4) NOT NULL,
    right_to_left DECIMAL(8,4) NOT NULL,
    day DATE,
    user_id BIGINT,
    FOREIGN KEY (left_currency_id) REFERENCES currencies(id),
    FOREIGN KEY (right_currency_id) REFERENCES currencies(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create Transfers table
CREATE TABLE transfers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description TEXT NOT NULL,
    day DATE NOT NULL,
    user_id BIGINT NOT NULL,
    import_guid VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create Transfer Items table
CREATE TABLE transfer_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description TEXT NOT NULL,
    value DECIMAL(12,2) NOT NULL,
    transfer_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    currency_id BIGINT NOT NULL DEFAULT 3,
    import_guid VARCHAR(255),
    FOREIGN KEY (transfer_id) REFERENCES transfers(id),
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (currency_id) REFERENCES currencies(id)
);

-- Create Goals table
CREATE TABLE goals (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(500),
    include_subcategories BOOLEAN,
    period_type_int INT,
    goal_type_int INT DEFAULT 0,
    goal_completion_condition_int INT DEFAULT 0,
    value FLOAT,
    category_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    currency_id BIGINT,
    period_start DATE,
    period_end DATE,
    is_cyclic BOOLEAN DEFAULT FALSE,
    is_finished BOOLEAN DEFAULT FALSE,
    cycle_group INT,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(id),
    FOREIGN KEY (currency_id) REFERENCES currencies(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create Reports table
CREATE TABLE reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50),
    name VARCHAR(255) NOT NULL,
    period_type_int INT NOT NULL,
    period_start DATE,
    period_end DATE,
    report_view_type_int INT NOT NULL,
    user_id BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    depth INT DEFAULT 0,
    max_categories_values_count INT DEFAULT 0,
    category_id BIGINT,
    period_division_int INT DEFAULT 5,
    temporary BOOLEAN DEFAULT FALSE,
    relative_period BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Create Category Report Options table
CREATE TABLE category_report_options (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    inclusion_type_int INT DEFAULT 0 NOT NULL,
    report_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (report_id) REFERENCES reports(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Create Conversions table
CREATE TABLE conversions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    exchange_id BIGINT NOT NULL,
    transfer_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (exchange_id) REFERENCES exchanges(id),
    FOREIGN KEY (transfer_id) REFERENCES transfers(id)
);

-- Create indexes
CREATE INDEX idx_categories_type_user ON categories(category_type_int, id, user_id);
CREATE INDEX idx_categories_tree ON categories(lft, rgt);
CREATE INDEX idx_categories_rgt ON categories(rgt);
CREATE INDEX idx_category_report_options ON category_report_options(category_id, report_id);
CREATE INDEX idx_conversions ON conversions(exchange_id, transfer_id);
CREATE INDEX idx_currencies_user ON currencies(id, user_id);
CREATE INDEX idx_exchanges_composite ON exchanges(day, left_currency_id, right_currency_id, user_id);
CREATE INDEX idx_exchanges_day ON exchanges(day);
CREATE INDEX idx_goals_category ON goals(category_id);
CREATE INDEX idx_reports_category ON reports(category_id);
CREATE INDEX idx_reports_user ON reports(user_id);
CREATE INDEX idx_transfer_items_category ON transfer_items(category_id);
CREATE INDEX idx_transfer_items_currency ON transfer_items(currency_id);
CREATE INDEX idx_transfer_items_transfer ON transfer_items(transfer_id);
CREATE INDEX idx_transfers_day ON transfers(day);
CREATE INDEX idx_transfers_user ON transfers(user_id);