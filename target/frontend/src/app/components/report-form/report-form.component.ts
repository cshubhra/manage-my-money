import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ReportService } from '../../services/report.service';
import { Report, ReportType, FileType } from '../../models/report.model';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
    selector: 'app-report-form',
    templateUrl: './report-form.component.html',
    styleUrls: ['./report-form.component.scss']
})
export class ReportFormComponent implements OnInit {
    reportForm: FormGroup;
    isEditMode = false;
    reportId: number | null = null;
    loading = false;
    reportTypes = Object.values(ReportType);
    fileTypes = Object.values(FileType);

    constructor(
        private fb: FormBuilder,
        private reportService: ReportService,
        private router: Router,
        private route: ActivatedRoute,
        private snackBar: MatSnackBar
    ) {
        this.reportForm = this.fb.group({
            name: ['', [Validators.required, Validators.minLength(3)]],
            type: ['', Validators.required],
            startDate: ['', Validators.required],
            endDate: ['', Validators.required],
            parameters: [''],
            fileType: ['', Validators.required]
        });
    }

    ngOnInit(): void {
        this.route.params.subscribe(params => {
            if (params['id']) {
                this.isEditMode = true;
                this.reportId = +params['id'];
                this.loadReport(this.reportId);
            }
        });
    }

    loadReport(id: number): void {
        this.loading = true;
        this.reportService.getReport(id)
            .subscribe({
                next: (report) => {
                    this.reportForm.patchValue({
                        name: report.name,
                        type: report.type,
                        startDate: report.startDate,
                        endDate: report.endDate,
                        parameters: report.parameters,
                        fileType: report.fileType
                    });
                    this.loading = false;
                },
                error: (error) => {
                    this.snackBar.open('Error loading report', 'Close', { duration: 3000 });
                    this.loading = false;
                }
            });
    }

    onSubmit(): void {
        if (this.reportForm.valid) {
            this.loading = true;
            const report: Report = this.reportForm.value;

            const request = this.isEditMode && this.reportId
                ? this.reportService.updateReport(this.reportId, report)
                : this.reportService.createReport(report);

            request.subscribe({
                next: () => {
                    this.snackBar.open(
                        `Report ${this.isEditMode ? 'updated' : 'created'} successfully`,
                        'Close',
                        { duration: 3000 }
                    );
                    this.router.navigate(['/reports']);
                },
                error: (error) => {
                    this.snackBar.open(
                        `Error ${this.isEditMode ? 'updating' : 'creating'} report`,
                        'Close',
                        { duration: 3000 }
                    );
                    this.loading = false;
                }
            });
        }
    }
}