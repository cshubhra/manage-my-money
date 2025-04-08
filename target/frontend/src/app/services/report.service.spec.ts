import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ReportService, ReportRequest } from './report.service';
import { environment } from '../../environments/environment';

describe('ReportService', () => {
  let service: ReportService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ReportService]
    });

    service = TestBed.inject(ReportService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should generate report', () => {
    const mockRequest: ReportRequest = {
      reportName: 'Test Report',
      reportType: 'TRANSFER_SUMMARY',
      format: 'PDF',
      columns: ['id', 'date'],
      filters: []
    };

    const mockBlob = new Blob(['test'], { type: 'application/pdf' });

    service.generateReport(mockRequest).subscribe(response => {
      expect(response).toEqual(mockBlob);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/api/reports/generate`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockRequest);
    req.flush(mockBlob);
  });

  it('should download report', () => {
    const mockBlob = new Blob(['test'], { type: 'application/pdf' });
    const mockFilename = 'test.pdf';

    // Mock necessary DOM APIs
    const mockUrl = 'blob:test';
    const mockLink = {
      href: '',
      download: '',
      click: jasmine.createSpy('click'),
    };

    spyOn(window.URL, 'createObjectURL').and.returnValue(mockUrl);
    spyOn(window.URL, 'revokeObjectURL');
    spyOn(document, 'createElement').and.returnValue(mockLink);

    service.downloadReport(mockBlob, mockFilename);

    expect(window.URL.createObjectURL).toHaveBeenCalledWith(mockBlob);
    expect(mockLink.href).toBe(mockUrl);
    expect(mockLink.download).toBe(mockFilename);
    expect(mockLink.click).toHaveBeenCalled();
    expect(window.URL.revokeObjectURL).toHaveBeenCalledWith(mockUrl);
  });
});