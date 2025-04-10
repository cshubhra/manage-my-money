package com.epam.managemymoney.service;

import com.epam.managemymoney.model.Report;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.csv.CSVFormat;
//import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CsvGeneratorService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

//    public byte[] generateReportCsv(Report report, Map<String, Object> reportData) {
////        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
////             OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
////             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
////
////            // Print report header
////            printReportHeader(csvPrinter, report);
////
////            // Print empty line as separator
////            csvPrinter.println();
////
////            // Print summary section if exists
////            if (reportData.containsKey("summary")) {
////                printSummarySection(csvPrinter, (Map<String, Object>) reportData.get("summary"));
////                csvPrinter.println();
////            }
////
////            // Print details section if exists
////            if (reportData.containsKey("details")) {
////                printDetailsSection(csvPrinter, (List<Map<String, Object>>) reportData.get("details"));
////            }
////
////            csvPrinter.flush();
////            return baos.toByteArray();
////
////        } catch (IOException e) {
////            log.error("Error generating CSV report", e);
////            throw new RuntimeException("Failed to generate CSV report", e);
////        }
//    }

//    private void printReportHeader(CSVPrinter csvPrinter, Report report) throws IOException {
//        csvPrinter.printRecord("Financial Report - " + report.getReportType());
//        csvPrinter.printRecord("Report Type", report.getReportType());
//        csvPrinter.printRecord("Period",
//                report.getStartDate().format(DATE_FORMATTER) + " to " +
//                        report.getEndDate().format(DATE_FORMATTER));
//        csvPrinter.printRecord("Currency", report.getCurrencyCode());
//        csvPrinter.printRecord("Generated On", report.getCreatedDate().format(DATE_FORMATTER));
//    }

//    private void printSummarySection(CSVPrinter csvPrinter, Map<String, Object> summary)
//            throws IOException {
//        csvPrinter.printRecord("Summary");
//        csvPrinter.println();
//
//        for (Map.Entry<String, Object> entry : summary.entrySet()) {
//            String value = formatValue(entry.getValue());
//            csvPrinter.printRecord(entry.getKey(), value);
//        }
//    }

//    private void printDetailsSection(CSVPrinter csvPrinter, List<Map<String, Object>> details)
//            throws IOException {
//        if (details.isEmpty()) {
//            csvPrinter.printRecord("No details available");
//            return;
//        }

//        csvPrinter.printRecord("Details");
//        csvPrinter.println();
//
//        // Print headers
//        List<String> headers = new ArrayList<>(details.get(0).keySet());
//        csvPrinter.printRecord(headers);
//
//        // Print data rows
//        for (Map<String, Object> row : details) {
//            List<String> rowData = new ArrayList<>();
//            for (String header : headers) {
//                rowData.add(formatValue(row.get(header)));
//            }
//            csvPrinter.printRecord(rowData);
//        }
//    }

    private String formatValue(Object value) {
        if (value == null) {
            return "";
        }

        if (value instanceof java.time.LocalDate) {
            return ((java.time.LocalDate) value).format(DATE_FORMATTER);
        }

        if (value instanceof java.time.LocalDateTime) {
            return ((java.time.LocalDateTime) value).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        if (value instanceof Number) {
            // Format numbers with 2 decimal places if they're floating point
            if (value instanceof Double || value instanceof Float) {
                return String.format("%.2f", ((Number) value).doubleValue());
            }
        }

        return value.toString();
    }

//    public byte[] generateSimpleCsv(List<String> headers, List<List<String>> data) {
//        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
//             OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
//             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
//
//            if (headers != null && !headers.isEmpty()) {
//                csvPrinter.printRecord(headers);
//            }
//
//            for (List<String> row : data) {
//                csvPrinter.printRecord(row);
//            }
//
//            csvPrinter.flush();
//            return baos.toByteArray();
//
//        } catch (IOException e) {
//            log.error("Error generating simple CSV", e);
//            throw new RuntimeException("Failed to generate simple CSV", e);
//        }
//    }

//    public byte[] generateMapBasedCsv(List<Map<String, Object>> data) {
//        if (data.isEmpty()) {
//            return new byte[0];
//        }
//
//        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
//             OutputStreamWriter writer = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
//             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
//
//            // Extract headers from the first row
//            List<String> headers = new ArrayList<>(data.get(0).keySet());
//            csvPrinter.printRecord(headers);
//
//            // Print data rows
//            for (Map<String, Object> row : data) {
//                List<String> rowData = new ArrayList<>();
//                for (String header : headers) {
//                    rowData.add(formatValue(row.get(header)));
//                }
//                csvPrinter.printRecord(rowData);
//            }
//
//            csvPrinter.flush();
//            return baos.toByteArray();
//
//        } catch (IOException e) {
//            log.error("Error generating map-based CSV", e);
//            throw new RuntimeException("Failed to generate map-based CSV", e);
//        }
//    }
}