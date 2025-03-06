export interface Category {
  id?: number;
  name: string;
  parentId?: number;
  description?: string;
  isActive: boolean;
}