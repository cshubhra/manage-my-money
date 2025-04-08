import { Component, OnInit } from '@angular/core';
import { Report, FileType } from '../../models/report.model';
import { ReportService } from '../../services/report.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { saveAs } from 'file-saver';

@Component({
    selector: 'app-report-list',
    templateUrl: './report-list.component.html',
    styleUrls: ['./report-list.component.scss']
})
export class ReportListComponent implements OnInit {
    reports: Report[] = [];
    displayedColumns: string[] = ['name', 'type', 'startDate', 'endDate', 'fileType', 'actions'];
    loading = false;

    constructor(
        private reportService: ReportService,
        private snackBar: MatSnackBar
    ) { }

    ngOnInit(): void {
        this.loadReports();
    }

    loadReports(): void {
        this.loading = true;
        this.reportService.getReports()
            .subscribe({
                next: (data) => {
                    this.reports = data;
                    this.loading = false;
                },
                error: (error) => {
                    this.snackBar.open('Error loading reports', 'Close', { duration: 3000 });
                    this.loading = false;
                }
            });
    }

    downloadReport(report: Report): void {
        this.loading = true;
        this.reportService.downloadReport(report.id!)
            .subscribe({
                next: (response) => {
                    const fileName = this.getFileName(response);
                    const blob = new Blob([response.body!], { type: response.headers.get('content-type')! });
                    saveAs(blob, fileName);
                    this.loading = false;
                },
                error: (error) => {
                    this.snackBar.open('Error downloading report', 'Close', { duration: 3000 });
                    this.loading = false;
                }
            });
    }

    deleteReport(id: number): void {
        if (confirm('Are you sure you want to delete this report?')) {
            this.loading = true;
            this.reportService.deleteReport(id)
                .subscribe({
                    next: () => {
                        this.loadReports();
                        this.snackBar.open('Report deleted successfully', 'Close', { duration: 3000 });
                    },
                    error: (error) => {
                        this.snackBar.open('Error deleting report', 'Close', { duration: 3000 });
                        this.loading = false;
                    }
                });
        }
    }

    private getFileName(response: any): string {
        const contentDisposition = response.headers.get('content-disposition');
        if (contentDisposition) {
            const matches = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/.exec(contentDisposition);
            if (matches != null && matches[1]) {
                return matches[1].replace(/['"]/g, '');
            }
        }
        return 'report.pdf';
    }
}