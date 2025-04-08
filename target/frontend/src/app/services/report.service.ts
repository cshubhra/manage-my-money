import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Report, ReportDTO } from '../models/report.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private apiUrl = `${environment.apiUrl}/reports`;

  constructor(private http: HttpClient) { }

  generateReport(reportDTO: ReportDTO): Observable<Report> {
    return this.http.post<Report>(`${this.apiUrl}/generate`, reportDTO);
  }

  getAllReports(): Observable<Report[]> {
    return this.http.get<Report[]>(this.apiUrl);
  }

  getReport(id: number): Observable<Report> {
    return this.http.get<Report>(`${this.apiUrl}/${id}`);
  }

  downloadReport(id: number): Observable<Blob> {
    const headers = new HttpHeaders({
      'Accept': 'application/pdf, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    });
    return this.http.get(`${this.apiUrl}/${id}/download`, {
      headers: headers,
      responseType: 'blob'
    });
  }
}