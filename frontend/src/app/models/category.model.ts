/**
 * Category model that represents categories for income, expense, or other transaction types.
 * Based on the original Ruby on Rails Category model.
 */
export class Category {
  id: number;
  name: string;
  description?: string;
  categoryType: CategoryType;
  parentId?: number;
  userId: number;
  level?: number;
  openingBalance?: number;
  createdAt?: Date;
  updatedAt?: Date;
  
  constructor(data: Partial<Category> = {}) {
    Object.assign(this, data);
  }
}

/**
 * Enum representing different category types
 */
export enum CategoryType {
  INCOME = 'INCOME',
  EXPENSE = 'EXPENSE',
  ASSET = 'ASSET',
  LIABILITY = 'LIABILITY'
}