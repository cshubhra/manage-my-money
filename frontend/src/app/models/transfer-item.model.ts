/**
 * TransferItem model that represents an individual line item in a transfer/transaction.
 * Based on the original Ruby on Rails TransferItem model.
 */
export class TransferItem {
  id?: number;
  transferId?: number;
  categoryId: number;
  description: string;
  value: number;
  currencyId: number;
  createdAt?: Date;
  updatedAt?: Date;
  
  constructor(data: Partial<TransferItem> = {}) {
    Object.assign(this, data);
  }
  
  /**
   * Determines if this is an income item (positive value)
   */
  isIncome(): boolean {
    return this.value >= 0;
  }
  
  /**
   * Determines if this is an expense/outcome item (negative value)
   */
  isOutcome(): boolean {
    return this.value <= 0;
  }
}