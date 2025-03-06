/**
 * Exchange model that represents a currency exchange rate.
 * Based on the original Ruby on Rails Exchange model.
 */
export class Exchange {
  id: number;
  leftCurrencyId: number;
  rightCurrencyId: number;
  rate: number;
  day: Date;
  userId: number;
  createdAt?: Date;
  updatedAt?: Date;
  
  constructor(data: Partial<Exchange> = {}) {
    Object.assign(this, data);
  }
  
  /**
   * Converts an amount from one currency to another based on this exchange rate
   * @param amount The amount to convert
   * @param targetCurrency The target currency ID
   * @returns The converted amount
   */
  convert(amount: number, targetCurrencyId: number): number {
    if (targetCurrencyId === this.leftCurrencyId) {
      return amount / this.rate; // Convert from right to left
    } else if (targetCurrencyId === this.rightCurrencyId) {
      return amount * this.rate; // Convert from left to right
    }
    throw new Error('Target currency not part of this exchange rate');
  }
}