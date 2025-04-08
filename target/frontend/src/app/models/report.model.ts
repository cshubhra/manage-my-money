export interface Report {
    id?: number;
    reportName: string;
    reportType: string;
    format: string;
    generatedBy: string;
    generatedAt?: Date;
    filePath?: string;
    parameters?: string;
}

export interface ReportDTO {
    reportName: string;
    reportType: string;
    format: string;
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