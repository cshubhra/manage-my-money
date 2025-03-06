import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transfer } from '../models/transfer.model';

@Injectable({
  providedIn: 'root'
})
export class TransferService {
  private apiUrl = 'http://localhost:8080/api/transfers';

  constructor(private http: HttpClient) { }

  getTransfers(): Observable<Transfer[]> {
    return this.http.get<Transfer[]>(this.apiUrl);
  }

  getTransfer(id: number): Observable<Transfer> {
    return this.http.get<Transfer>(`${this.apiUrl}/${id}`);
  }

  createTransfer(transfer: Transfer): Observable<Transfer> {
    return this.http.post<Transfer>(this.apiUrl, transfer);
  }

  updateTransfer(id: number, transfer: Transfer): Observable<Transfer> {
    return this.http.put<Transfer>(`${this.apiUrl}/${id}`, transfer);
  }

  deleteTransfer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}