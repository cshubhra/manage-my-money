import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReportService } from '../../services/report.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-report-generator',
  templateUrl: './report-generator.component.html',
  styleUrls: ['./report-generator.component.scss']
})
export class ReportGeneratorComponent implements OnInit {
  reportForm: FormGroup;
  loading = false;

  reportTypes = [
    { value: 'TRANSFER_SUMMARY', label: 'Transfer Summary' },
    { value: 'TRANSFER_DETAIL', label: 'Transfer Detail' }
  ];

  formats = [
    { value: 'PDF', label: 'PDF' },
    { value: 'EXCEL', label: 'Excel' }
  ];

  availableColumns = [
    { value: 'id', label: 'ID' },
    { value: 'date', label: 'Date' },
    { value: 'status', label: 'Status' },
    { value: 'amount', label: 'Amount' }
  ];

  constructor(
    private fb: FormBuilder,
    private reportService: ReportService,
    private snackBar: MatSnackBar
  ) {
    this.createForm();
  }

  ngOnInit(): void {}

  private createForm(): void {
    this.reportForm = this.fb.group({
      reportName: ['', Validators.required],
      reportType: ['', Validators.required],
      format: ['', Validators.required],
      columns: [[], Validators.required],
      filters: [[]]
    });
  }

  onSubmit(): void {
    if (this.reportForm.valid) {
      this.loading = true;
      const request = this.reportForm.value;
      
      this.reportService.generateReport(request).subscribe({
        next: (blob: Blob) => {
          const filename = this.generateFileName(request);
          this.reportService.downloadReport(blob, filename);
          this.showSuccess('Report generated successfully');
        },
        error: (error) => {
          console.error('Error generating report:', error);
          this.showError('Failed to generate report');
        },
        complete: () => {
          this.loading = false;
        }
      });
    }
  }

  private generateFileName(request: any): string {
    const timestamp = new Date().toISOString().replace(/[:.]/g, '-');
    const extension = request.format.toLowerCase();
    return `${request.reportType.toLowerCase()}_${timestamp}.${extension}`;
  }

  private showSuccess(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      panelClass: ['success-snackbar']
    });
  }

  private showError(message: string): void {
    this.snackBar.open(message, 'Close', {
      duration: 5000,
      panelClass: ['error-snackbar']
    });
  }
}