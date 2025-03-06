import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Transfer } from '../models/transfer.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TransferService {
  private apiUrl = `${environment.apiUrl}/transfers`;
  
  constructor(private http: HttpClient) { }
  
  /**
   * Get all transfers
   */
  getTransfers(): Observable<Transfer[]> {
    return this.http.get<any[]>(this.apiUrl)
      .pipe(
        map(transfers => transfers.map(transfer => new Transfer(transfer)))
      );
  }
  
  /**
   * Get a single transfer by ID
   */
  getTransfer(id: number): Observable<Transfer> {
    return this.http.get<any>(`${this.apiUrl}/${id}`)
      .pipe(
        map(transfer => new Transfer(transfer))
      );
  }
  
  /**
   * Create a new transfer
   */
  createTransfer(transfer: Transfer): Observable<Transfer> {
    return this.http.post<any>(this.apiUrl, transfer)
      .pipe(
        map(createdTransfer => new Transfer(createdTransfer))
      );
  }
  
  /**
   * Update an existing transfer
   */
  updateTransfer(id: number, transfer: Partial<Transfer>): Observable<Transfer> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, transfer)
      .pipe(
        map(updatedTransfer => new Transfer(updatedTransfer))
      );
  }
  
  /**
   * Delete a transfer
   */
  deleteTransfer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  
  /**
   * Get newest transfers based on user preferences
   */
  getNewestTransfers(): Observable<Transfer[]> {
    return this.http.get<any[]>(`${this.apiUrl}/newest`)
      .pipe(
        map(transfers => transfers.map(transfer => new Transfer(transfer)))
      );
  }
  
  /**
   * Search transfers by date range
   */
  searchTransfers(startDate: Date, endDate: Date): Observable<Transfer[]> {
    const params = { 
      startDate: startDate.toISOString().split('T')[0],
      endDate: endDate.toISOString().split('T')[0]
    };
    
    return this.http.get<any[]>(`${this.apiUrl}/search`, { params })
      .pipe(
        map(transfers => transfers.map(transfer => new Transfer(transfer)))
      );
  }
  
  /**
   * Create a quick transfer (simplified transfer creation)
   */
  createQuickTransfer(data: any): Observable<Transfer> {
    return this.http.post<any>(`${this.apiUrl}/quick`, data)
      .pipe(
        map(transfer => new Transfer(transfer))
      );
  }
}