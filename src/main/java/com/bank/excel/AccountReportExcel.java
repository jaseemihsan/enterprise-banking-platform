package com.bank.excel;

import com.bank.model.Account;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AccountReportExcel {

    public static void generate(List<Account> accounts,
                                HttpServletResponse response)
            throws IOException {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Accounts");

        Row titleRow = sheet.createRow(0);

        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("ACCOUNT REPORT");

        CellStyle titleStyle = workbook.createCellStyle();

        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 16);

        titleStyle.setFont(titleFont);
        titleStyle.setVerticalAlignment(
                VerticalAlignment.CENTER);

        titleCell.setCellStyle(titleStyle);

        sheet.addMergedRegion(
                new CellRangeAddress(0, 0, 0, 6));

        Row infoRow = sheet.createRow(1);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a");

        infoRow.createCell(0).setCellValue(
                "Generated : " +
                        LocalDateTime.now().format(formatter));

        infoRow.createCell(4).setCellValue(
                "Total Accounts : " + accounts.size());

        CellStyle headerStyle = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());

        headerStyle.setFont(font);
        headerStyle.setFillForegroundColor(
                IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(
                FillPatternType.SOLID_FOREGROUND);

        Row header = sheet.createRow(3);

        Cell cell;

        cell = header.createCell(0);
        cell.setCellValue("ID");
        cell.setCellStyle(headerStyle);

        cell = header.createCell(1);
        cell.setCellValue("Account No");
        cell.setCellStyle(headerStyle);

        cell = header.createCell(2);
        cell.setCellValue("Customer");
        cell.setCellStyle(headerStyle);

        cell = header.createCell(3);
        cell.setCellValue("Email");
        cell.setCellStyle(headerStyle);

        cell = header.createCell(4);
        cell.setCellValue("Type");
        cell.setCellStyle(headerStyle);

        cell = header.createCell(5);
        cell.setCellValue("Balance");
        cell.setCellStyle(headerStyle);

        cell = header.createCell(6);
        cell.setCellValue("Status");
        cell.setCellStyle(headerStyle);

        int rowNum = 4;

        for (Account account : accounts) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(account.getId());
            row.createCell(1).setCellValue(account.getAccountNumber());
            row.createCell(2).setCellValue(account.getCustomerName());
            row.createCell(3).setCellValue(account.getCustomerEmail());
            row.createCell(4).setCellValue(account.getAccountType());

            if (account.getBalance() != null) {
                row.createCell(5).setCellValue(
                        account.getBalance().doubleValue());
            }

            row.createCell(6).setCellValue(account.getStatus());
        }

        sheet.createFreezePane(0, 4);

        sheet.setAutoFilter(
                new CellRangeAddress(
                        3,
                        rowNum - 1,
                        0,
                        6));

        for (int i = 0; i < 7; i++) {
            sheet.autoSizeColumn(i);
        }

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=account-report.xlsx");

        workbook.write(response.getOutputStream());

        workbook.close();
    }
}
