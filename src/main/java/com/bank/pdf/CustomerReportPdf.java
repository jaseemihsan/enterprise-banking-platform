package com.bank.pdf;

import com.bank.model.Customer;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class CustomerReportPdf {

    public void export(List<Customer> customers,
                       HttpServletResponse response)
            throws IOException, DocumentException {

        try (Document document = new Document(PageSize.A4)) {

        PdfWriter.getInstance(
                document,
                response.getOutputStream());

        document.open();

        Font titleFont =
                FontFactory.getFont(
                        FontFactory.HELVETICA_BOLD,
                        18);

        Paragraph title =
                new Paragraph(
                        "Customer Report",
                        titleFont);

        title.setAlignment(Element.ALIGN_CENTER);

        document.add(title);

        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);

        table.setWidthPercentage(100);

        table.addCell("ID");
        table.addCell("First Name");
        table.addCell("Last Name");
        table.addCell("Email");
        table.addCell("Phone");

        for (Customer customer : customers) {

            table.addCell(String.valueOf(customer.getId()));
            table.addCell(customer.getFirstName());
            table.addCell(customer.getLastName());
            table.addCell(customer.getEmail());
            table.addCell(customer.getPhone());

        }

        document.add(table);
      }
    }
}
