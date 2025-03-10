# Application Workflow Documentation

## Overview
This document describes the key workflows and interactions in the Ruby on Rails financial management application. It provides an understanding of how users interact with the system and how the different components work together.

## User Registration and Setup

```mermaid
sequenceDiagram
    actor User
    participant System
    participant Categories
    participant Currencies
    
    User->>System: Register new account
    System->>User: Send activation email
    User->>System: Activate account
    System->>Categories: Create default categories
    Note over Categories: Assets, Income, Expense, Loans, Opening Balances
    System->>Currencies: Set default currency
    User->>System: Login
    User->>System: Configure preferences
```

## Managing Categories

```mermaid
graph TD
    A[User] -->|Creates| B[Top-Level Category]
    A -->|Creates| C[Sub-Category]
    B -->|Contains| C
    C -->|Contains| D[Further Sub-Categories]
    A -->|Assigns| E[Transfer Item]
    C -->|Receives| E
    D -->|Receives| E
    A -->|Sets| F[Financial Goal]
    C -->|Target of| F
    D -->|Target of| F
    A -->|Views| G[Category Report]
    B -->|Included in| G
    C -->|Included in| G
    D -->|Included in| G
```

## Recording Financial Transactions

```mermaid
sequenceDiagram
    actor User
    participant Transfer
    participant TransferItems
    participant Categories
    participant Currencies
    participant Conversions

    User->>Transfer: Create new transfer
    User->>TransferItems: Add income item
    TransferItems->>Categories: Associate with category
    TransferItems->>Currencies: Set currency
    User->>TransferItems: Add expense item
    TransferItems->>Categories: Associate with category
    TransferItems->>Currencies: Set currency

    alt Multiple currencies used
        User->>Conversions: Define currency conversion
        Conversions->>Transfer: Link to transfer
    end

    Transfer->>Transfer: Validate balance (sum=0)
    Transfer->>User: Confirm transaction recorded
```

## Multi-Currency Operations

```mermaid
graph TD
    A[User] -->|Creates| B[Currency]
    A -->|Defines| C[Exchange Rate]
    C -->|Between| B
    C -->|And| D[Another Currency]
    A -->|Records| E[Transfer]
    E -->|Contains| F[Transfer Item in Currency 1]
    E -->|Contains| G[Transfer Item in Currency 2]
    E -->|Uses| C
    A -->|Views| H[Multi-Currency Report]
    B -->|Shown in| H
    D -->|Shown in| H
    E -->|Included in| H
```

## Financial Reporting

```mermaid
graph TD
    A[User] -->|Creates| B[Report]
    B -->|Types| C[Share Report]
    B -->|Types| D[Flow Report]
    B -->|Types| E[Value Report]
    B -->|Types| F[Multiple Category Report]
    
    A -->|Configures| G[Report Period]
    G -->|Options| G1[Selected Dates]
    G -->|Options| G2[This Month]
    G -->|Options| G3[Last Month]
    G -->|Options| G4[This Year]
    
    A -->|Selects| H[Report View]
    H -->|Options| H1[Pie Chart]
    H -->|Options| H2[Linear Chart]
    H -->|Options| H3[Text]
    H -->|Options| H4[Bar Chart]
    
    A -->|Views| I[Report Results]
```

## Goal Tracking

```mermaid
sequenceDiagram
    actor User
    participant Goal
    participant Category
    participant Transfers
    
    User->>Goal: Create new goal
    User->>Goal: Define target (percentage or value)
    User->>Goal: Set time period
    User->>Goal: Link to category
    User->>Goal: Configure as cyclic or one-time
    
    loop During goal period
        User->>Transfers: Record financial activities
        Transfers->>Category: Update category balances
        Category->>Goal: Update progress
        Goal->>User: Show progress toward goal
    end
    
    alt Goal completed
        Goal->>User: Show completion status
        opt If cyclic
            Goal->>Goal: Create next period goal
        end
    end
```

## System Integration Points

```mermaid
graph TD
    A[Ruby on Rails App] -->|Provides| B[Web UI]
    A -->|Manages| C[Database]
    A -->|Generates| D[Reports]
    A -->|Sends| E[Emails]
    A -->|Processes| F[Financial Data]
    
    B -->|User Authentication| B1[Login/Register]
    B -->|Transaction Management| B2[Create/Edit Transfers]
    B -->|Category Management| B3[Manage Categories]
    B -->|Reporting| B4[Generate Reports]
    
    C -->|Stores| C1[User Data]
    C -->|Stores| C2[Financial Data]
    C -->|Stores| C3[System Settings]
    
    D -->|Chart Types| D1[Pie Charts]
    D -->|Chart Types| D2[Line Charts]
    D -->|Chart Types| D3[Bar Charts]
    
    E -->|Types| E1[Account Activation]
    E -->|Types| E2[Password Reset]
    
    F -->|Features| F1[Multi-Currency Support]
    F -->|Features| F2[Goal Tracking]
    F -->|Features| F3[Nested Categories]
```

This documentation provides a high-level understanding of the key workflows and interactions in the application. It shows how users interact with the system and how the different components work together to provide the application's functionality.