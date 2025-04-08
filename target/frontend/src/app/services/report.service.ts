import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
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

  getAllReports(page: number = 0, size: number = 10, sort?: string): Observable<Report[]> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    if (sort) {
      params = params.set('sort', sort);
    }

    return this.http.get<Report[]>(this.apiUrl, { params });
  }

  getReport(id: number): Observable<Report> {
    return this.http.get<Report>(`${this.apiUrl}/${id}`);
  }

  downloadReport(id: number, format: 'pdf' | 'xlsx' = 'pdf'): Observable<Blob> {
    const headers = new HttpHeaders({
      'Accept': format === 'pdf' 
        ? 'application/pdf' 
        : 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
    });

    const params = new HttpParams().set('format', format);

    return this.http.get(`${this.apiUrl}/${id}/download`, {
      headers,
      params,
      responseType: 'blob'
    });
  }

  deleteReport(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateReport(id: number, reportDTO: ReportDTO): Observable<Report> {
    return this.http.put<Report>(`${this.apiUrl}/${id}`, reportDTO);
  }
}