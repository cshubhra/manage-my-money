import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Currency } from '../models/currency.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CurrencyService {
  private apiUrl = `${environment.apiUrl}/currencies`;
  
  constructor(private http: HttpClient) { }
  
  /**
   * Get all currencies
   */
  getCurrencies(): Observable<Currency[]> {
    return this.http.get<any[]>(this.apiUrl)
      .pipe(
        map(currencies => currencies.map(currency => new Currency(currency)))
      );
  }
  
  /**
   * Get a single currency by ID
   */
  getCurrency(id: number): Observable<Currency> {
    return this.http.get<any>(`${this.apiUrl}/${id}`)
      .pipe(
        map(currency => new Currency(currency))
      );
  }
  
  /**
   * Create a new currency
   */
  createCurrency(currency: Currency): Observable<Currency> {
    return this.http.post<any>(this.apiUrl, currency)
      .pipe(
        map(createdCurrency => new Currency(createdCurrency))
      );
  }
  
  /**
   * Update an existing currency
   */
  updateCurrency(id: number, currency: Partial<Currency>): Observable<Currency> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, currency)
      .pipe(
        map(updatedCurrency => new Currency(updatedCurrency))
      );
  }
  
  /**
   * Delete a currency
   */
  deleteCurrency(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  
  /**
   * Get the default currency for current user
   */
  getDefaultCurrency(): Observable<Currency> {
    return this.http.get<any>(`${this.apiUrl}/default`)
      .pipe(
        map(currency => new Currency(currency))
      );
  }
  
  /**
   * Set a currency as the default for current user
   */
  setDefaultCurrency(id: number): Observable<Currency> {
    return this.http.post<any>(`${this.apiUrl}/${id}/default`, {})
      .pipe(
        map(currency => new Currency(currency))
      );
  }
}