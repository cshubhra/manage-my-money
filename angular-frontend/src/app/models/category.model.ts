export interface Category {
  id?: number;
  name: string;
  description?: string;
  categoryType: CategoryType;
  parentId?: number;
  level?: number;
  children?: Category[];
  transfers?: Transfer[];
}

export enum CategoryType {
  ASSET = 'ASSET',
  INCOME = 'INCOME',
  EXPENSE = 'EXPENSE',
  LOAN = 'LOAN',
  BALANCE = 'BALANCE'
}

// This interface is used when displaying categories in a tree structure
export interface CategoryNode {
  id: number;
  name: string;
  categoryType: CategoryType;
  level: number;
  children?: CategoryNode[];
  expandable: boolean;
}