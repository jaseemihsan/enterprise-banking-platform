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

        String username =
                (String) request.getSession()
                        .getAttribute("username");

        try {

            /*
             * Daily Transaction Report
             */
            if ("daily".equals(type)) {

                if ("excel".equalsIgnoreCase(format)) {

                    logger.info(
                            "User {} generated Daily Transaction Excel Report",
                            username);

                    TransactionReportExcel.generate(
                            reportService.getTodayTransactions(),
                            "DAILY TRANSACTION REPORT",
                            "daily-report.xlsx",
                            response);

                    return;

                } else if (format != null) {

                    logger.warn(
                            "Unsupported report format. type={}, format={}, user={}",
                            type,
                            format,
                            username);

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST);

                    return;
                }

                request.setAttribute(
                        "transactions",
                        reportService.getTodayTransactions());

            }

            /*
             * Deposit Report
             */
            else if ("deposit".equals(type)) {

                if ("excel".equalsIgnoreCase(format)) {

                    logger.info(
                            "User {} generated Deposit Excel Report",
                            username);

                    TransactionReportExcel.generate(
                            reportService.getDepositTransactions(),
                            "DEPOSIT REPORT",
                            "deposit-report.xlsx",
                            response);

                    return;

                } else if (format != null) {

                    logger.warn(
                            "Unsupported report format. type={}, format={}, user={}",
                            type,
                            format,
                            username);

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST);

                    return;
                }

                request.setAttribute(
                        "transactions",
                        reportService.getDepositTransactions());

                request.setAttribute(
                        "title",
                        "Deposit Report");
            }

            /*
             * Withdrawal Report
             */
            else if ("withdraw".equals(type)) {

                if ("excel".equalsIgnoreCase(format)) {

                    logger.info(
                            "User {} generated Withdrawal Excel Report",
                            username);

                    TransactionReportExcel.generate(
                            reportService.getWithdrawTransactions(),
                            "WITHDRAWAL REPORT",
                            "withdraw-report.xlsx",
                            response);

                    return;

                } else if (format != null) {

                    logger.warn(
                            "Unsupported report format. type={}, format={}, user={}",
                            type,
                            format,
                            username);

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST);

                    return;
                }

                request.setAttribute(
                        "transactions",
                        reportService.getWithdrawTransactions());

                request.setAttribute(
                        "title",
                        "Withdrawal Report");
            }

            /*
             * Transfer Report
             */
            else if ("transfer".equals(type)) {

                if ("excel".equalsIgnoreCase(format)) {

                    logger.info(
                            "User {} generated Transfer Excel Report",
                            username);

                    TransactionReportExcel.generate(
                            reportService.getTransferTransactions(),
                            "TRANSFER REPORT",
                            "transfer-report.xlsx",
                            response);

                    return;

                } else if (format != null) {

                    logger.warn(
                            "Unsupported report format. type={}, format={}, user={}",
                            type,
                            format,
                            username);

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST);

                    return;
                }

                request.setAttribute(
                        "transactions",
                        reportService.getTransferTransactions());

                request.setAttribute(
                        "title",
                        "Transfer Report");
            }

            /*
             * Customer Report
             */
            else if ("customer".equals(type)) {

                if ("pdf".equalsIgnoreCase(format)) {

                    response.setContentType("application/pdf");

                    response.setHeader(
                            "Content-Disposition",
                            "attachment; filename=customer-report.pdf");

                    logger.info(
                            "User {} generated Customer PDF Report",
                            username);

                 logger.info(
        "User {} generated Customer PDF Report",
        username);

new CustomerReportPdf().export(
        reportService.getCustomerReport(),
        response);

return;

                } else if ("excel".equalsIgnoreCase(format)) {

                    logger.info(
                            "User {} generated Customer Excel Report",
                            username);

                    CustomerReportExcel.generate(
                            reportService.getCustomerReport(),
                            response);

                    return;

                } else if (format != null) {

                    logger.warn(
                            "Unsupported report format. type={}, format={}, user={}",
                            type,
                            format,
                            username);

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST);

                    return;
                }

                request.setAttribute(
                        "customers",
                        reportService.getCustomerReport());

                request.setAttribute(
                        "title",
                        "Customer Report");
            }

	                /*
             * Account Report
             */
            else if ("account".equals(type)) {

                if ("excel".equalsIgnoreCase(format)) {

                    logger.info(
                            "User {} generated Account Excel Report",
                            username);

                    AccountReportExcel.generate(
                            reportService.getAccountReport(),
                            response);

                    return;

                } else if (format != null) {

                    logger.warn(
                            "Unsupported report format. type={}, format={}, user={}",
                            type,
                            format,
                            username);

                    response.sendError(
                            HttpServletResponse.SC_BAD_REQUEST);

                    return;
                }

                request.setAttribute(
                        "accounts",
                        reportService.getAccountReport());

                request.setAttribute(
                        "title",
                        "Account Report");
            }

            /*
             * Unknown Report
             */
            else {

                logger.warn(
                        "Unknown report requested. type={}, user={}",
                        type,
                        username);

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND);

                return;
            }

            /*
             * Opening Report Page
             */
            if (format == null) {

                logger.info(
                        "User {} opened {} report page",
                        username,
                        type);
            }

            request.getRequestDispatcher("/reports.jsp")
                    .forward(request, response);

        } catch (DocumentException e) {

            logger.error(
                    "PDF generation failed. type={}, user={}",
                    type,
                    username,
                    e);

            throw new ServletException(e);

        } catch (IOException e) {

            logger.error(
                    "Excel generation failed. type={}, user={}",
                    type,
                    username,
                    e);

            throw e;

        } catch (Exception e) {

            logger.error(
                    "Unexpected error while generating report. type={}, format={}, user={}",
                    type,
                    format,
                    username,
                    e);

            throw new ServletException(e);
        }
    }
}
