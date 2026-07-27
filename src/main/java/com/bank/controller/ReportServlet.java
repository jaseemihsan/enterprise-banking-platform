package com.bank.controller;

import com.bank.service.ReportService;
import com.bank.pdf.CustomerReportPdf;
import com.lowagie.text.DocumentException;
import com.bank.excel.CustomerReportExcel;

import com.bank.excel.AccountReportExcel;
import com.bank.excel.TransactionReportExcel;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/reports")
public class ReportServlet extends HttpServlet {


    private static final Logger logger =
            LoggerFactory.getLogger(ReportServlet.class);

      private final ReportService reportService =
        new ReportService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

 	    String type = request.getParameter("type");
            String format = request.getParameter("format");

if ("daily".equals(type)) {

	  if ("excel".equalsIgnoreCase(format)) {
logger.info("Generating Daily Transaction Excel Report");

        TransactionReportExcel.generate(
                reportService.getTodayTransactions(),
                "DAILY TRANSACTION REPORT",
                "daily-report.xlsx",
                response);

        return;
    }

    request.setAttribute(
            "transactions",
            reportService.getTodayTransactions());

    } else if ("deposit".equals(type)) {

              if ("excel".equalsIgnoreCase(format)) {
logger.info("Generating Deposit Excel Report");

        TransactionReportExcel.generate(
                reportService.getDepositTransactions(),
                "DEPOSIT REPORT",
                "deposit-report.xlsx",
                response);

        return;
    }

        request.setAttribute(
                "transactions",
                reportService.getDepositTransactions());

        request.setAttribute("title", "Deposit Report");

    } else if ("withdraw".equals(type)) {

	      if ("excel".equalsIgnoreCase(format)) {
logger.info("Generating Withdrawal Excel Report");

        TransactionReportExcel.generate(
                reportService.getWithdrawTransactions(),
                "WITHDRAWAL REPORT",
                "withdraw-report.xlsx",
                response);

        return;
    }
        request.setAttribute(
                "transactions",
                reportService.getWithdrawTransactions());

        request.setAttribute("title", "Withdrawal Report");

    } else if ("transfer".equals(type)) {

	     if ("excel".equalsIgnoreCase(format)) {
logger.info("Generating Transfer Excel Report");

        TransactionReportExcel.generate(
                reportService.getTransferTransactions(),
                "TRANSFER REPORT",
                "transfer-report.xlsx",
                response);

        return;
    }
        request.setAttribute(
                "transactions",
                reportService.getTransferTransactions());

        request.setAttribute("title", "Transfer Report");
    } else if ("customer".equals(type)) {

if ("pdf".equalsIgnoreCase(format)) {
        response.setContentType("application/pdf");

        response.setHeader(
                "Content-Disposition",
		"attachment; filename=customer-report.pdf");

	logger.info("Generating Customer PDF Report");
        try {

            new CustomerReportPdf().export(
                    reportService.getCustomerReport(),
                    response);

        } catch (DocumentException e) {
logger.error("Error generating Customer PDF Report", e);
            throw new ServletException(e);

        }

        return;

    } else if ("excel".equalsIgnoreCase(format)) {
String username = (String) request.getSession().getAttribute("username");
logger.info("User {} generated Customer Excel Report", username);
        try {

            CustomerReportExcel.generate(
                    reportService.getCustomerReport(),
                    response);
        } catch (IOException e) {
    logger.error("Error generating Customer Excel Report", e);

            throw new ServletException(e);

        }

        return;
    }

request.setAttribute(
            "customers",
            reportService.getCustomerReport());

    request.setAttribute(
            "title",
            "Customer Report");

} else if ("account".equals(type)) {

	if ("excel".equalsIgnoreCase(format)) {
logger.info("Generating Account Excel Report");

    AccountReportExcel.generate(
            reportService.getAccountReport(),
            response);

    return;
}
    request.setAttribute(
            "accounts",
            reportService.getAccountReport());

    request.setAttribute(
            "title",
            "Account Report");
}
if (type != null && format == null) {
logger.info("Opening {} report page", type);
}
        request.getRequestDispatcher("/reports.jsp")
	.forward(request, response);

    }

}
