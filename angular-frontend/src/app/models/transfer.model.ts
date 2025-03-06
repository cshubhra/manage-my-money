import { Category } from './category.model';
import { Currency } from './currency.model';

export interface Transfer {
  id?: number;
  description: string;
  day: string; // Format: YYYY-MM-DD
  userId?: number;
  items: TransferItem[];
  conversions?: Conversion[];
}

export interface TransferItem {
  id?: number;
  description: string;
  value: number;
  transferId?: number;
  categoryId: number;
  currencyId: number;
  category?: Category;
  currency?: Currency;
}

export interface Conversion {
  id?: number;
  transferId?: number;
  exchangeId: number;
}