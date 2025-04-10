package com.epam.managemymoney.service;

import com.epam.managemymoney.model.Report;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PdfGeneratorService {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public byte[] generateReportPdf(Report report, Map<String, Object> reportData) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, baos);
            document.open();

            addMetadata(document, report);
            addTitle(document, report);
            addReportHeader(document, report);
            addReportContent(document, reportData);
            addCharts(document, reportData);
            addFooter(writer, report);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Error generating PDF report", e);
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }

    private void addMetadata(Document document, Report report) {
        document.addTitle("Financial Report - " + report.getReportType());
        document.addAuthor("Manage My Money Application");
        document.addCreator("Manage My Money Application");
        document.addSubject("Financial Report for period: " +
                report.getStartDate().format(DATE_FORMATTER) + " to " +
                report.getEndDate().format(DATE_FORMATTER));
    }

    private void addTitle(Document document, Report report) throws DocumentException {
        Paragraph title = new Paragraph("Financial Report", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        document.add(title);
    }

    private void addReportHeader(Document document, Report report) throws DocumentException {
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);

        addHeaderCell(headerTable, "Report Type:", String.valueOf(report.getReportType()));
        addHeaderCell(headerTable, "Period:",
                report.getStartDate().format(DATE_FORMATTER) + " to " +
                        report.getEndDate().format(DATE_FORMATTER));
        //@todo hardcoded
        addHeaderCell(headerTable, "Currency:", "USD");
        addHeaderCell(headerTable, "Generated On:",
                report.getCreatedAt().format(DATE_FORMATTER));

        document.add(headerTable);
        document.add(Chunk.NEWLINE);
    }

    private void addHeaderCell(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, HEADER_FONT));
        labelCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, NORMAL_FONT));
        valueCell.setBorder(Rectangle.NO_BORDER);
        table.addCell(valueCell);
    }

    private void addReportContent(Document document, Map<String, Object> reportData) throws DocumentException {
        if (reportData.containsKey("summary")) {
            addSummarySection(document, (Map<String, Object>) reportData.get("summary"));
        }

        if (reportData.containsKey("details")) {
            addDetailsSection(document, (List<Map<String, Object>>) reportData.get("details"));
        }
    }

    private void addSummarySection(Document document, Map<String, Object> summary) throws DocumentException {
        Paragraph summaryTitle = new Paragraph("Summary", HEADER_FONT);
        summaryTitle.setSpacingBefore(15f);
        summaryTitle.setSpacingAfter(10f);
        document.add(summaryTitle);

        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(100);

        summary.forEach((key, value) -> {
            try {
                addHeaderCell(summaryTable, key + ":", value.toString());
            } catch (Exception e) {
                log.error("Error adding summary cell", e);
            }
        });

        document.add(summaryTable);
    }

    private void addDetailsSection(Document document, List<Map<String, Object>> details) throws DocumentException {
        Paragraph detailsTitle = new Paragraph("Details", HEADER_FONT);
        detailsTitle.setSpacingBefore(15f);
        detailsTitle.setSpacingAfter(10f);
        document.add(detailsTitle);

        if (details.isEmpty()) {
            document.add(new Paragraph("No details available", NORMAL_FONT));
            return;
        }

        PdfPTable detailsTable = new PdfPTable(details.get(0).size());
        detailsTable.setWidthPercentage(100);

        // Add headers
        details.get(0).keySet().forEach(header -> {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            detailsTable.addCell(cell);
        });

        // Add data
        details.forEach(row ->
                row.values().forEach(value ->
                        detailsTable.addCell(new Phrase(value.toString(), NORMAL_FONT))
                )
        );

        document.add(detailsTable);
    }

    private void addCharts(Document document, Map<String, Object> reportData) throws DocumentException, IOException {
        if (reportData.containsKey("charts")) {
            Paragraph chartsTitle = new Paragraph("Charts", HEADER_FONT);
            chartsTitle.setSpacingBefore(15f);
            chartsTitle.setSpacingAfter(10f);
            document.add(chartsTitle);

            List<byte[]> charts = (List<byte[]>) reportData.get("charts");
            for (byte[] chartData : charts) {
                Image chart = Image.getInstance(chartData);
                chart.setAlignment(Element.ALIGN_CENTER);
                chart.scaleToFit(500, 300);
                document.add(chart);
            }
        }
    }

    private void addFooter(PdfWriter writer, Report report) {
        writer.setPageEvent(new PdfPageEventHelper() {
            @Override
            public void onEndPage(PdfWriter writer, Document document) {
                PdfContentByte cb = writer.getDirectContent();
                Phrase footer = new Phrase("Page " + writer.getPageNumber(), NORMAL_FONT);
                ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                        footer,
                        (document.right() - document.left()) / 2 + document.leftMargin(),
                        document.bottom() - 10,
                        0);
            }
        });
    }
}