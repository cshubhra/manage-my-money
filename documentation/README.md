# Current State Documentation

This folder contains comprehensive documentation of the current Ruby on Rails application that will be reengineered to an Angular and Node.js stack.

## Table of Contents

1. [Data Model Documentation](data-model.md) - Describes the entities, relationships, and data dictionary
2. [Architecture Documentation](architecture.md) - Describes the current system architecture and component structure
3. [Database Schema](database-schema.md) - Detailed database schema information
4. [Sequence Diagrams](sequence-diagrams.md) - Sequence diagrams for key application workflows

## System Overview

The current system is a Ruby on Rails application for personal finance management that allows users to:

1. Track financial transactions across multiple accounts and currencies
2. Categorize income and expenses in a hierarchical manner
3. Set and track financial goals
4. Generate financial reports and visualizations
5. Manage currency exchange rates

## Key Features

- **Multi-currency support**: Track finances in different currencies with support for exchange rates
- **Hierarchical categories**: Organize transactions in a nested category structure
- **Financial goals**: Set and track progress on financial goals
- **Reporting**: Generate various types of financial reports
- **Transfer system**: Record financial transactions with complex multi-item entries

## Domain Model Overview

The core entities in the system are:

- **User**: Application users with authentication and preferences
- **Category**: Hierarchical categories for organizing financial data
- **Transfer & TransferItem**: Financial transactions and their components
- **Currency & Exchange**: Currencies and their exchange rates
- **Goal**: Financial goals with tracking
- **Report**: Various financial reports and visualizations

## Migration Considerations

When reengineering to Angular and Node.js, the following aspects need special attention:

1. **Complex data model**: The data model includes complex relationships and inheritance patterns
2. **Multi-currency calculations**: Currency conversion and financial calculations are deeply integrated
3. **Hierarchical data**: The nested set model for categories needs a suitable replacement
4. **Authentication system**: The custom authentication system needs to be migrated
5. **Report generation**: The reporting system has complex aggregation and visualization logic