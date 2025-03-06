import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private apiUrl = `${environment.apiUrl}/reports`;
  
  constructor(private http: HttpClient) { }
  
  /**
   * Get share report data (how categories share of total income/expenses)
   */
  getShareReport(params: any): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/share`, { params });
  }
  
  /**
   * Get value report data (category values over time)
   */
  getValueReport(params: any): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/value`, { params });
  }
  
  /**
   * Get cash flow report data (income vs expenses over time)
   */
  getFlowReport(params: any): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/flow`, { params });
  }
  
  /**
   * Get debtors report (who owes money to the user)
   */
  getDebtorsReport(): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/debtors`);
  }
  
  /**
   * Get creditors report (who the user owes money to)
   */
  getCreditorsReport(): Observable<any> {
    return this.http.get<any>(`${environment.apiUrl}/creditors`);
  }
}