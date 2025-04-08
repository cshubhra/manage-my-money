import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transfer, TransferFormData } from '../models/transfer.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TransferService {
  private apiUrl = `${environment.apiUrl}/transfers`;

  constructor(private http: HttpClient) { }

  getTransfers(): Observable<Transfer[]> {
    return this.http.get<Transfer[]>(this.apiUrl);
  }

  getTransfer(id: number): Observable<Transfer> {
    return this.http.get<Transfer>(`${this.apiUrl}/${id}`);
  }

  createTransfer(transfer: TransferFormData): Observable<Transfer> {
    return this.http.post<Transfer>(this.apiUrl, transfer);
  }

  updateTransfer(id: number, transfer: TransferFormData): Observable<Transfer> {
    return this.http.put<Transfer>(`${this.apiUrl}/${id}`, transfer);
  }
}