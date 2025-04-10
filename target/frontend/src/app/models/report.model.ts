export enum ReportType {
    SALES = 'SALES',
    INVENTORY = 'INVENTORY',
    FINANCIAL = 'FINANCIAL',
    CUSTOMER = 'CUSTOMER',
    ANALYTICS = 'ANALYTICS'
}

export enum FileType {
    PDF = 'PDF',
    EXCEL = 'EXCEL',
    CSV = 'CSV',
    HTML = 'HTML'
}

export interface Report {
    id?: number;
    reportName: string;
    reportType: ReportType;
    format: FileType;
    generatedBy: string;
    generatedAt?: Date;
    filePath?: string;
    parameters?: string;
}

export interface ReportDTO {
    reportName: string;
    reportType: ReportType;
    format: FileType;
    generatedBy: string;
    parameters?: string;
    dateRange?: {
        startDate: Date;
        endDate: Date;
    };
    filters?: {
        [key: string]: any;
    };
}