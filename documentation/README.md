# Financial Management System Documentation

This documentation set provides a comprehensive overview of the current Ruby on Rails financial management application and the plan for reengineering it to an Angular and Node.js stack.

## Documentation Index

### Current State Documentation

1. **[System Overview](current_state.md)**  
   A comprehensive overview of the current Ruby on Rails application, including its models, controllers, and features.

2. **[Database Schema](database_schema.md)**  
   Detailed documentation of the database schema including tables, relationships, and indexes.

3. **[User Workflows](user_workflows.md)**  
   Documentation of the key user interactions and workflows within the application.

### Reengineering Documentation

1. **[Reengineering Architecture](reengineering_architecture.md)**  
   The proposed architecture for the new Angular and Node.js application, including component structure, API endpoints, and migration strategy.

## System Summary

The Financial Management System is a personal finance application that allows users to:

- Track financial transactions (transfers)
- Categorize income and expenses in a hierarchical structure
- Support multiple currencies with exchange rates
- Generate various financial reports
- Set and track financial goals
- View financial history with filtering options

## Key Features

1. **Financial Transaction Management**
   - Create, edit, and delete transfers
   - Quick transfer functionality
   - Multi-currency support
   - Transfer categorization

2. **Category Management**
   - Hierarchical category structure
   - Category balances
   - Opening balances
   - System categories

3. **Multi-Currency Support**
   - Currency management
   - Exchange rates
   - Currency conversion

4. **Reporting**
   - Share reports (pie charts)
   - Flow reports (cash flow)
   - Value reports (time series)
   - Custom reporting periods

5. **Goal Tracking**
   - Financial goals
   - Goal progress tracking
   - Cyclic goals

## Reengineering Strategy

The reengineering effort will transform the current Ruby on Rails monolithic application into a modern client-server architecture with:

1. **Angular Front-End**
   - Modern component-based UI
   - Responsive design
   - State management with NgRx
   - Enhanced visualizations

2. **Node.js Back-End**
   - RESTful API
   - Enhanced security
   - Optimized performance
   - Scalable architecture

3. **Enhanced Features**
   - Improved user experience
   - More robust reporting
   - Expanded visualization options
   - Mobile responsiveness

## Project Timeline

The reengineering project will follow these phases:

1. **Analysis & Documentation** - Understanding the current system
2. **Architecture Design** - Designing the new system
3. **Back-End Development** - Building the Node.js API
4. **Front-End Development** - Creating the Angular application
5. **Integration & Testing** - Ensuring everything works together
6. **Deployment & Transition** - Going live with the new system

Each phase will have specific deliverables and milestones to track progress.

## Contact Information

For questions or clarification regarding this documentation or the reengineering project, please contact the project lead.

---

This documentation was created to support the reengineering of the Ruby on Rails application to an Angular and Node.js stack while preserving all existing functionality and enhancing the user experience.