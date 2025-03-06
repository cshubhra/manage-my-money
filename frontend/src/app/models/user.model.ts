/**
 * User model that represents a user of the application.
 * Based on the original Ruby on Rails User model.
 */
export class User {
  id: number;
  email: string;
  username: string;
  defaultCurrencyId: number;
  transactionAmountLimitType: string;
  transactionAmountLimitValue: number;
  includeTransactionsFromSubcategories: boolean;
  invertSaldoForIncome: boolean;
  createdAt?: Date;
  updatedAt?: Date;
  
  constructor(data: Partial<User> = {}) {
    Object.assign(this, data);
  }
}