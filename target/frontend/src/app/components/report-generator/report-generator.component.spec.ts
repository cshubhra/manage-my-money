import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { of, throwError } from 'rxjs';

import { ReportGeneratorComponent } from './report-generator.component';
import { ReportService } from '../../services/report.service';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressBarModule } from '@angular/material/progress-bar';

describe('ReportGeneratorComponent', () => {
  let component: ReportGeneratorComponent;
  let fixture: ComponentFixture<ReportGeneratorComponent>;
  let reportService: jasmine.SpyObj<ReportService>;
  let snackBar: jasmine.SpyObj<MatSnackBar>;

  beforeEach(async () => {
    const reportServiceSpy = jasmine.createSpyObj('ReportService', ['generateReport', 'downloadReport']);
    const snackBarSpy = jasmine.createSpyObj('MatSnackBar', ['open']);

    await TestBed.configureTestingModule({
      declarations: [ ReportGeneratorComponent ],
      imports: [
        ReactiveFormsModule,
        BrowserAnimationsModule,
        MatCardModule,
        MatFormFieldModule,
        MatSelectModule,
        MatInputModule,
        MatButtonModule,
        MatIconModule,
        MatProgressBarModule
      ],
      providers: [
        { provide: ReportService, useValue: reportServiceSpy },
        { provide: MatSnackBar, useValue: snackBarSpy }
      ]
    })
    .compileComponents();

    reportService = TestBed.inject(ReportService) as jasmine.SpyObj<ReportService>;
    snackBar = TestBed.inject(MatSnackBar) as jasmine.SpyObj<MatSnackBar>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportGeneratorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with empty form', () => {
    expect(component.reportForm.get('reportName')?.value).toBe('');
    expect(component.reportForm.get('reportType')?.value).toBe('');
    expect(component.reportForm.get('format')?.value).toBe('');
    expect(component.reportForm.get('columns')?.value).toEqual([]);
    expect(component.reportForm.get('filters')?.value).toEqual([]);
  });

  it('should validate required fields', () => {
    const form = component.reportForm;
    expect(form.valid).toBeFalse();

    form.patchValue({
      reportName: 'Test Report',
      reportType: 'TRANSFER_SUMMARY',
      format: 'PDF',
      columns: ['id', 'date']
    });

    expect(form.valid).toBeTrue();
  });

  it('should generate report successfully', () => {
    const mockBlob = new Blob(['test'], { type: 'application/pdf' });
    reportService.generateReport.and.returnValue(of(mockBlob));

    component.reportForm.patchValue({
      reportName: 'Test Report',
      reportType: 'TRANSFER_SUMMARY',
      format: 'PDF',
      columns: ['id', 'date']
    });

    component.onSubmit();

    expect(reportService.generateReport).toHaveBeenCalled();
    expect(snackBar.open).toHaveBeenCalledWith(
      'Report generated successfully',
      'Close',
      jasmine.any(Object)
    );
  });

  it('should handle error when generating report', () => {
    reportService.generateReport.and.returnValue(throwError(() => new Error('Test error')));

    component.reportForm.patchValue({
      reportName: 'Test Report',
      reportType: 'TRANSFER_SUMMARY',
      format: 'PDF',
      columns: ['id', 'date']
    });

    component.onSubmit();

    expect(reportService.generateReport).toHaveBeenCalled();
    expect(snackBar.open).toHaveBeenCalledWith(
      'Failed to generate report',
      'Close',
      jasmine.any(Object)
    );
  });
});