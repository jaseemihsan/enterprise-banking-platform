package com.bank.excel;

import com.bank.model.Transaction;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionReportExcel {

    public static void generate(List<Transaction> transactions,
                                String title,
                                String fileName,
                                HttpServletResponse response)
            throws IOException {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet("Transactions");

        Row titleRow = sheet.createRow(0);

        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title);

        CellStyle titleStyle = workbook.createCellStyle();

        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short)16);

        titleStyle.setFont(titleFont);
        titleCell.setCellStyle(titleStyle);

        sheet.addMergedRegion(
                new CellRangeAddress(0,0,0,6));

        Row infoRow = sheet.createRow(1);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a");

        infoRow.createCell(0).setCellValue(
                "Generated : " +
                        LocalDateTime.now().format(formatter));

        infoRow.createCell(4).setCellValue(
                "Total Transactions : " + transactions.size());

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

        String[] headers = {
                "ID",
                "Account No",
                "Type",
                "Amount",
                "Reference",
                "Remarks",
                "Transaction Time"
        };

        for(int i=0;i<headers.length;i++){

            Cell cell = header.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);

        }

        int rowNum = 4;

        for(Transaction t : transactions){

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(t.getId());
            row.createCell(1).setCellValue(t.getAccountNumber());
            row.createCell(2).setCellValue(t.getTransactionType());

            if(t.getAmount()!=null){
                row.createCell(3).setCellValue(
                        t.getAmount().doubleValue());
            }

            row.createCell(4).setCellValue(t.getReferenceNo());
            row.createCell(5).setCellValue(t.getRemarks());

            if(t.getTransactionTime()!=null){
                row.createCell(6).setCellValue(
                        t.getTransactionTime().toString());
            }

        }

        sheet.createFreezePane(0,4);

        sheet.setAutoFilter(
                new CellRangeAddress(
                        3,
                        rowNum-1,
                        0,
                        6));

        for(int i=0;i<7;i++){
            sheet.autoSizeColumn(i);
        }

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        response.setHeader(
                "Content-Disposition",
                "attachment; filename=" + fileName);

        workbook.write(response.getOutputStream());

        workbook.close();

    }

}
