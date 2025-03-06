export interface User {
  id?: number;
  username: string;
  name: string;
  email: string;
  transactionAmountLimitType?: TransactionAmountLimitType;
  transactionAmountLimitValue?: number;  
  includeTransactionsFromSubcategories?: boolean;
  multiCurrencyBalanceCalculatingAlgorithm?: MultiCurrencyBalanceCalculatingAlgorithm;
  defaultCurrencyId?: number;
  invertSaldoForIncome?: boolean;
}

export enum TransactionAmountLimitType {
  TRANSACTION_COUNT = 'TRANSACTION_COUNT',
  WEEK_COUNT = 'WEEK_COUNT',
  THIS_MONTH = 'THIS_MONTH',
  THIS_AND_LAST_MONTH = 'THIS_AND_LAST_MONTH'
}

export enum MultiCurrencyBalanceCalculatingAlgorithm {
  SHOW_ALL_CURRENCIES = 'SHOW_ALL_CURRENCIES',
  CALCULATE_WITH_NEWEST_EXCHANGES = 'CALCULATE_WITH_NEWEST_EXCHANGES',
  CALCULATE_WITH_EXCHANGES_CLOSEST_TO_TRANSACTION = 'CALCULATE_WITH_EXCHANGES_CLOSEST_TO_TRANSACTION',
  CALCULATE_WITH_NEWEST_EXCHANGES_BUT = 'CALCULATE_WITH_NEWEST_EXCHANGES_BUT',
  CALCULATE_WITH_EXCHANGES_CLOSEST_TO_TRANSACTION_BUT = 'CALCULATE_WITH_EXCHANGES_CLOSEST_TO_TRANSACTION_BUT'
}

export interface AuthResponse {
  token: string;
  user: User;
}