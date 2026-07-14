package com.bank.controller;

import com.bank.model.Account;
import com.bank.service.AccountService;
import com.bank.service.CustomerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet({
        "/accounts",
        "/accounts/edit",
        "/accounts/update",
        "/accounts/close"
})
public class AccountServlet extends HttpServlet {

    private final AccountService accountService = new AccountService();
    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();

        switch (servletPath) {

            case "/accounts/edit":

                editAccount(request, response);
                return;

            case "/accounts/close":

                closeAccount(request, response);
                return;

            default:

                listAccounts(request, response);

        }

    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String servletPath = request.getServletPath();

        switch (servletPath) {

            case "/accounts":

                createAccount(request, response);
                break;

            case "/accounts/update":

                updateAccount(request, response);
                break;

        }

    }

    /*
     * List Accounts
     */
    private void listAccounts(HttpServletRequest request,
                              HttpServletResponse response)
            throws ServletException, IOException {

        String keyword = request.getParameter("search");

        request.setAttribute(
                "customers",
                customerService.getAllCustomers());

        if (keyword != null && !keyword.trim().isEmpty()) {

            request.setAttribute(
                    "accounts",
                    accountService.searchAccounts(keyword));

        } else {

            request.setAttribute(
                    "accounts",
                    accountService.getAllAccounts());

        }

        request.getRequestDispatcher("/accounts.jsp")
                .forward(request, response);

    }

    /*
     * Create Account
     */
    private void createAccount(HttpServletRequest request,
                               HttpServletResponse response)
            throws IOException {

        Account account = new Account();

        account.setCustomerId(
                Integer.parseInt(request.getParameter("customerId")));

        account.setAccountType(
                request.getParameter("accountType"));

        account.setBalance(
                new BigDecimal(request.getParameter("balance")));

        account.setStatus("ACTIVE");

        account.setAccountNumber(generateAccountNumber());

        boolean saved =
                accountService.createAccount(account);

        if (saved) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/accounts?success=Account+Created");

        } else {

            response.sendRedirect(
                    request.getContextPath()
                            + "/accounts?error=Unable+to+Create+Account");

        }

    }

    /*
     * Edit Account
     */
    private void editAccount(HttpServletRequest request,
                             HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(
                request.getParameter("id"));

        request.setAttribute(
                "account",
                accountService.getAccountById(id));

        request.getRequestDispatcher("/edit-account.jsp")
                .forward(request, response);

    }

    /*
     * Update Account
     */
    private void updateAccount(HttpServletRequest request,
                               HttpServletResponse response)
            throws IOException {

        Account account = new Account();

        account.setId(
                Integer.parseInt(request.getParameter("id")));

        account.setAccountType(
                request.getParameter("accountType"));

        account.setStatus(
                request.getParameter("status"));

        boolean updated =
                accountService.updateAccount(account);

        if (updated) {

            response.sendRedirect(
                    request.getContextPath()
                            + "/accounts?success=Account+Updated");

        } else {

            response.sendRedirect(
                    request.getContextPath()
                            + "/accounts?error=Update+Failed");

        }

    }

    /*
     * Close Account
     */
    private void closeAccount(HttpServletRequest request,
                              HttpServletResponse response)
            throws IOException {

        int id = Integer.parseInt(
                request.getParameter("id"));

        accountService.closeAccount(id);

        response.sendRedirect(
                request.getContextPath()
                        + "/accounts?success=Account+Closed");

    }

    /*
     * Temporary Account Number Generator
     */
    private String generateAccountNumber() {

        long time = System.currentTimeMillis();

        return "AC" + String.valueOf(time).substring(5);

    }

}
