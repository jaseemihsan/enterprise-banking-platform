package com.bank.excel;

import com.bank.model.Customer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import org.apache.poi.ss.util.CellRangeAddress;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class CustomerReportExcel {

    public static void generate(List<Customer> customers,
                                HttpServletResponse response)
            throws IOException {

       try ( Workbook workbook = new XSSFWorkbook()) {

        Sheet sheet = workbook.createSheet("Customers");

        Row titleRow = sheet.createRow(0);

Cell titleCell = titleRow.createCell(0);
titleCell.setCellValue("CUSTOMER REPORT");

CellStyle titleStyle = workbook.createCellStyle();

Font titleFont = workbook.createFont();
titleFont.setBold(true);
titleFont.setFontHeightInPoints((short)16);

titleStyle.setFont(titleFont);
titleStyle.setVerticalAlignment(
        VerticalAlignment.CENTER);

titleCell.setCellStyle(titleStyle);

sheet.addMergedRegion(
        new CellRangeAddress(0, 0, 0, 4));

        Row infoRow = sheet.createRow(1);

DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a");

infoRow.createCell(0).setCellValue(
        "Generated : " +
        LocalDateTime.now().format(formatter));

infoRow.createCell(3).setCellValue(
        "Total Customers : " + customers.size());
       

        CellStyle headerStyle = workbook.createCellStyle();

Font font = workbook.createFont();
font.setBold(true);
font.setColor(IndexedColors.WHITE.getIndex());

headerStyle.setFont(font);
headerStyle.setFillForegroundColor(
        IndexedColors.DARK_BLUE.getIndex());
headerStyle.setFillPattern(
        FillPatternType.SOLID_FOREGROUND);

// Header Row
Row header = sheet.createRow(3);

Cell cell;

cell = header.createCell(0);
cell.setCellValue("ID");
cell.setCellStyle(headerStyle);

cell = header.createCell(1);
cell.setCellValue("First Name");
cell.setCellStyle(headerStyle);

cell = header.createCell(2);
cell.setCellValue("Last Name");
cell.setCellStyle(headerStyle);

cell = header.createCell(3);
cell.setCellValue("Email");
cell.setCellStyle(headerStyle);

cell = header.createCell(4);
cell.setCellValue("Phone");
cell.setCellStyle(headerStyle);

// Customer data starts after the header
int rowNum = 4;

        for (Customer c : customers) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(c.getId());
            row.createCell(1).setCellValue(c.getFirstName());
            row.createCell(2).setCellValue(c.getLastName());
            row.createCell(3).setCellValue(c.getEmail());
            row.createCell(4).setCellValue(c.getPhone());
        }

	sheet.createFreezePane(0, 4);

sheet.setAutoFilter(
        new CellRangeAddress(
                3,
                rowNum - 1,
                0,
                4));

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=customer-report.xlsx");

        workbook.write(response.getOutputStream());

       }
    }
}
