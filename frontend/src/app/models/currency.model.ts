/**
 * Currency model that represents a monetary unit.
 * Based on the original Ruby on Rails Currency model.
 */
export class Currency {
  id: number;
  name: string;
  symbol: string;
  longSymbol: string;
  userId?: number;
  isDefault?: boolean;
  createdAt?: Date;
  updatedAt?: Date;
  
  constructor(data: Partial<Currency> = {}) {
    Object.assign(this, data);
  }
}