import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Exchange } from '../models/exchange.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ExchangeService {
  private apiUrl = `${environment.apiUrl}/exchanges`;
  
  constructor(private http: HttpClient) { }
  
  /**
   * Get all exchanges
   */
  getExchanges(): Observable<Exchange[]> {
    return this.http.get<any[]>(this.apiUrl)
      .pipe(
        map(exchanges => exchanges.map(exchange => new Exchange(exchange)))
      );
  }
  
  /**
   * Get a single exchange by ID
   */
  getExchange(id: number): Observable<Exchange> {
    return this.http.get<any>(`${this.apiUrl}/${id}`)
      .pipe(
        map(exchange => new Exchange(exchange))
      );
  }
  
  /**
   * Create a new exchange
   */
  createExchange(exchange: Exchange): Observable<Exchange> {
    return this.http.post<any>(this.apiUrl, exchange)
      .pipe(
        map(createdExchange => new Exchange(createdExchange))
      );
  }
  
  /**
   * Update an existing exchange
   */
  updateExchange(id: number, exchange: Partial<Exchange>): Observable<Exchange> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, exchange)
      .pipe(
        map(updatedExchange => new Exchange(updatedExchange))
      );
  }
  
  /**
   * Delete an exchange
   */
  deleteExchange(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  
  /**
   * Get exchanges for specific currency pair
   */
  getExchangesForCurrencyPair(leftCurrencyId: number, rightCurrencyId: number): Observable<Exchange[]> {
    return this.http.get<any[]>(`${this.apiUrl}/list/${leftCurrencyId}/${rightCurrencyId}`)
      .pipe(
        map(exchanges => exchanges.map(exchange => new Exchange(exchange)))
      );
  }
  
  /**
   * Get the newest exchange rate for a specific currency pair
   */
  getNewestExchangeForCurrencyPair(leftCurrencyId: number, rightCurrencyId: number): Observable<Exchange> {
    return this.http.get<any>(`${this.apiUrl}/newest/${leftCurrencyId}/${rightCurrencyId}`)
      .pipe(
        map(exchange => new Exchange(exchange))
      );
  }
}