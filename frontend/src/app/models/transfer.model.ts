import { TransferItem } from './transfer-item.model';
import { Conversion } from './conversion.model';

/**
 * Transfer model that represents a financial transaction.
 * Based on the original Ruby on Rails Transfer model.
 */
export class Transfer {
  id: number;
  description: string;
  day: Date;
  userId: number;
  importGuid?: string;
  transferItems: TransferItem[] = [];
  conversions: Conversion[] = [];
  createdAt?: Date;
  updatedAt?: Date;
  
  constructor(data: Partial<Transfer> = {}) {
    Object.assign(this, data);
    
    // Convert transfer items if they exist
    if (data.transferItems) {
      this.transferItems = data.transferItems.map(item => new TransferItem(item));
    }
    
    // Convert conversions if they exist
    if (data.conversions) {
      this.conversions = data.conversions.map(conv => new Conversion(conv));
    }
  }
  
  /**
   * Validates that the transfer has at least two items
   */
  isValid(): boolean {
    return this.transferItems && this.transferItems.length >= 2;
  }
  
  /**
   * Gets all unique currencies used in this transfer
   */
  getUniqueCurrencyIds(): number[] {
    return [...new Set(this.transferItems.map(item => item.currencyId))];
  }
}