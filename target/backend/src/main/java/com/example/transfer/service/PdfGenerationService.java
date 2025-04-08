package com.example.transfer.service;

import com.example.transfer.dto.TransferDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@Slf4j
public class PdfGenerationService {

    public byte[] generateTransferSummary(List<TransferDTO> transfers, List<String> columns) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, baos);
            document.open();

            addTitle(document, "Transfer Summary Report");
            addTransferSummaryTable(document, transfers, columns);
            
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Error generating PDF", e);
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }

    public byte[] generateTransferDetail(List<TransferDTO> transfers, List<String> columns) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, baos);
            document.open();

            addTitle(document, "Transfer Detail Report");
            addTransferDetailTable(document, transfers, columns);
            
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Error generating PDF", e);
            throw new RuntimeException("Failed to generate PDF report", e);
        }
    }

    private void addTitle(Document document, String title) throws DocumentException {
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Paragraph titleParagraph = new Paragraph(title, titleFont);
        titleParagraph.setAlignment(Element.ALIGN_CENTER);
        titleParagraph.setSpacingAfter(20);
        document.add(titleParagraph);
    }

    private void addTransferSummaryTable(Document document, List<TransferDTO> transfers, List<String> columns) 
            throws DocumentException {
        PdfPTable table = new PdfPTable(columns.size());
        table.setWidthPercentage(100);

        // Add headers
        for (String column : columns) {
            PdfPCell cell = new PdfPCell(new Phrase(column));
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }

        // Add data
        for (TransferDTO transfer : transfers) {
            for (String column : columns) {
                String value = getTransferValue(transfer, column);
                table.addCell(new Phrase(value));
            }
        }

        document.add(table);
    }

    private void addTransferDetailTable(Document document, List<TransferDTO> transfers, List<String> columns) 
            throws DocumentException {
        for (TransferDTO transfer : transfers) {
            // Add transfer header
            Paragraph transferHeader = new Paragraph("Transfer ID: " + transfer.getId());
            transferHeader.setSpacingBefore(15);
            document.add(transferHeader);

            // Add transfer details table
            PdfPTable table = new PdfPTable(columns.size());
            table.setWidthPercentage(100);

            // Add headers and data similar to summary table
            // Additional implementation details...
        }
    }

    private String getTransferValue(TransferDTO transfer, String column) {
        switch (column.toLowerCase()) {
            case "id":
                return transfer.getId().toString();
            case "date":
                return transfer.getDate().toString();
            case "status":
                return transfer.getStatus();
            case "amount":
                return transfer.getAmount().toString();
            default:
                return "";
        }
    }
}