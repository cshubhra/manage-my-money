/**
 * Conversion model that associates an Exchange rate with a Transfer.
 * Based on the original Ruby on Rails Conversion model.
 */
export class Conversion {
  id?: number;
  transferId: number;
  exchangeId: number;
  createdAt?: Date;
  updatedAt?: Date;
  
  constructor(data: Partial<Conversion> = {}) {
    Object.assign(this, data);
  }
}