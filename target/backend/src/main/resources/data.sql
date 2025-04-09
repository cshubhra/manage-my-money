-- Insert sample users
INSERT INTO users (login, name, email, password, created_at, updated_at, default_currency_id)
VALUES ('admin', 'Administrator', 'admin@example.com', '$2a$10$rGxCSx9JnByAoqQ3kJwZou.RyGt.DWy8rA8QQhvAqJM7AG9FZPxFi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1),
       ('user1', 'John Doe', 'john@example.com', '$2a$10$rGxCSx9JnByAoqQ3kJwZou.RyGt.DWy8rA8QQhvAqJM7AG9FZPxFi', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1);

-- Insert default currencies
INSERT INTO currencies (symbol, long_symbol, name, long_name)
VALUES ('USD', 'US$', 'Dollar', 'United States Dollar'),
       ('EUR', '€', 'Euro', 'European Euro'),
       ('GBP', '£', 'Pound', 'British Pound Sterling');

-- Insert system categories
INSERT INTO system_categories (name, description, category_type_int, lft, rgt, cached_level)
VALUES ('Income', 'All income categories', 1, 1, 2, 0),
       ('Expense', 'All expense categories', 2, 3, 4, 0),
       ('Asset', 'Asset categories', 3, 5, 6, 0),
       ('Liability', 'Liability categories', 4, 7, 8, 0);

-- Insert sample categories for user1
INSERT INTO categories (name, description, category_type_int, user_id, lft, rgt, created_at, updated_at)
VALUES ('Salary', 'Monthly salary income', 1, 2, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Groceries', 'Food and household items', 2, 2, 3, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Rent', 'Monthly rent payment', 2, 2, 5, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Savings', 'Savings account', 3, 2, 7, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Credit Card', 'Credit card payments', 4, 2, 9, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample exchanges
INSERT INTO exchanges (left_currency_id, right_currency_id, left_to_right, right_to_left, day, user_id)
VALUES (1, 2, 0.85, 1.18, CURRENT_DATE, 2),
       (1, 3, 0.73, 1.37, CURRENT_DATE, 2),
       (2, 3, 0.86, 1.16, CURRENT_DATE, 2);

-- Insert sample transfers
INSERT INTO transfers (description, day, user_id)
VALUES ('Monthly salary payment', CURRENT_DATE, 2),
       ('Grocery shopping', CURRENT_DATE, 2),
       ('Rent payment', CURRENT_DATE, 2);

-- Insert sample transfer items
INSERT INTO transfer_items (description, value, transfer_id, category_id, currency_id)
VALUES ('Salary deposit', 5000.00, 1, 1, 1),
       ('Grocery store purchase', -150.00, 2, 2, 1),
       ('Monthly rent', -1200.00, 3, 3, 1);

-- Insert sample goals
INSERT INTO goals (description, include_subcategories, period_type_int, goal_type_int, value, category_id, currency_id, period_start, period_end, user_id)
VALUES ('Monthly savings target', true, 1, 1, 1000.00, 4, 1, CURRENT_DATE, DATEADD('MONTH', 1, CURRENT_DATE), 2),
       ('Reduce grocery spending', true, 1, 2, 500.00, 2, 1, CURRENT_DATE, DATEADD('MONTH', 1, CURRENT_DATE), 2);

-- Insert sample reports
INSERT INTO reports (type, name, period_type_int, report_view_type_int, user_id, category_id, created_at, updated_at)
VALUES ('ExpenseReport', 'Monthly Expense Report', 1, 1, 2, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('IncomeReport', 'Monthly Income Report', 1, 1, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample category report options
INSERT INTO category_report_options (inclusion_type_int, report_id, category_id, created_at, updated_at)
VALUES (1, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (1, 2, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);