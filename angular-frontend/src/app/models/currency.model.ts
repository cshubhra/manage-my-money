export interface Currency {
  id?: number;
  symbol: string;
  longSymbol: string;
  name: string;
  longName: string;
  userId?: number;
}

export interface Exchange {
  id?: number;
  leftCurrencyId: number;
  rightCurrencyId: number;
  rate: number;
  day: string; // Format: YYYY-MM-DD
  leftCurrency?: Currency;
  rightCurrency?: Currency;
}