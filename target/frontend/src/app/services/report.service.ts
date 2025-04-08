import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface ReportRequest {
  reportName: string;
  reportType: string;
  format: string;
  columns: string[];
  filters: string[];
}

@Injectable({
  providedIn: 'root'
})
export class ReportService {
  private apiUrl = `${environment.apiUrl}/api/reports`;

  constructor(private http: HttpClient) {}

  generateReport(request: ReportRequest): Observable<Blob> {
    return this.http.post(
      `${this.apiUrl}/generate`,
      request,
      { responseType: 'blob' }
    );
  }

  downloadReport(blob: Blob, filename: string): void {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename;
    link.click();
    window.URL.revokeObjectURL(url);
  }
}