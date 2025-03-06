export interface Transfer {
  id?: number;
  amount: number;
  description?: string;
  date: Date;
  categoryId: number;
  fromAccountId?: number;
  toAccountId?: number;
}