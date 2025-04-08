import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ReportService } from '../../../services/report.service';
import { Report, ReportDTO } from '../../../models/report.model';
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-report-generator',
  templateUrl: './report-generator.component.html',
  styleUrls: ['./report-generator.component.scss']
})
export class ReportGeneratorComponent implements OnInit {
  reportForm: FormGroup;
  loading = false;
  reportTypes = [
    { value: 'TRANSFER_SUMMARY', label: 'Transfer Summary Report' },
    { value: 'TRANSFER_DETAIL', label: 'Transfer Detail Report' }
  ];
  formats = [
    { value: 'PDF', label: 'PDF' },
    { value: 'EXCEL', label: 'Excel' }
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
      reportName: ['', [Validators.required]],
      reportType: ['', [Validators.required]],
      format: ['PDF', [Validators.required]],
      dateRange: this.fb.group({
        startDate: ['', [Validators.required]],
        endDate: ['', [Validators.required]]
      }),
      filters: this.fb.group({
        status: [''],
        category: [''],
        amount: ['']
      })
    });
  }

  onSubmit(): void {
    if (this.reportForm.valid) {
      this.loading = true;
      const reportDTO: ReportDTO = {
        ...this.reportForm.value,
        generatedBy: 'current-user', // Replace with actual logged-in user
        parameters: JSON.stringify(this.reportForm.value.filters)
      };

      this.reportService.generateReport(reportDTO).subscribe({
        next: (report: Report) => {
          this.loading = false;
          this.snackBar.open('Report generated successfully', 'Close', { duration: 3000 });
          this.downloadReport(report.id);
        },
        error: (error) => {
          this.loading = false;
          this.snackBar.open('Error generating report', 'Close', { duration: 3000 });
        }
      });
    }
  }

  downloadReport(reportId: number): void {
    this.reportService.downloadReport(reportId).subscribe({
      next: (blob: Blob) => {
        const fileName = `${this.reportForm.get('reportName').value}_${new Date().getTime()}`;
        const extension = this.reportForm.get('format').value === 'PDF' ? '.pdf' : '.xlsx';
        saveAs(blob, fileName + extension);
      },
      error: (error) => {
        this.snackBar.open('Error downloading report', 'Close', { duration: 3000 });
      }
    });
  }

  resetForm(): void {
    this.reportForm.reset();
    this.reportForm.patchValue({
      format: 'PDF'
    });
  }
}