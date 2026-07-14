package com.bank.controller;

import com.bank.model.Customer;
import com.bank.service.CustomerService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet({"/customers", "/customers/add",  "/customers/edit",
    "/customers/update", "/customers/delete"})

public class CustomerServlet extends HttpServlet {

    private final CustomerService customerService = new CustomerService();

    @Override
protected void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws ServletException, IOException {

    String servletPath = request.getServletPath();

    if ("/customers/add".equals(servletPath)) {

        Customer customer = new Customer();

        customer.setFirstName(request.getParameter("firstName"));
        customer.setLastName(request.getParameter("lastName"));
        customer.setEmail(request.getParameter("email"));
        customer.setPhone(request.getParameter("phone"));

        boolean saved = customerService.addCustomer(customer);

        if (saved) {
            response.sendRedirect(request.getContextPath() + "/customers");
        } else {
            response.sendRedirect(request.getContextPath()
                    + "/customers.jsp?error=Unable+to+Add+Customer");
        }

        return;
    }

    if ("/customers/update".equals(servletPath)) {

        Customer customer = new Customer();

        customer.setId(
                Integer.parseInt(request.getParameter("id")));

        customer.setFirstName(request.getParameter("firstName"));
        customer.setLastName(request.getParameter("lastName"));
        customer.setEmail(request.getParameter("email"));
        customer.setPhone(request.getParameter("phone"));

        boolean updated =
                customerService.updateCustomer(customer);

        if (updated) {

            response.sendRedirect(
                    request.getContextPath() + "/customers");

        } else {

            response.sendRedirect(
                    request.getContextPath()
                            + "/customers?error=UpdateFailed");

        }

    }

}

    @Override
protected void doGet(HttpServletRequest request,
                     HttpServletResponse response)
        throws ServletException, IOException {

    String servletPath = request.getServletPath();

    if ("/customers/edit".equals(servletPath)) {

        int id = Integer.parseInt(request.getParameter("id"));

        Customer customer = customerService.getCustomerById(id);

        request.setAttribute("customer", customer);

        request.getRequestDispatcher("/edit-customer.jsp")
                .forward(request, response);

        return;
    }

    if ("/customers/delete".equals(servletPath)) {

         int id = Integer.parseInt(request.getParameter("id"));

         customerService.deleteCustomer(id);

         response.sendRedirect(request.getContextPath() + "/customers");

         return;
    }

    String keyword = request.getParameter("search");

    if (keyword != null && !keyword.isBlank()) {

        request.setAttribute(
                "customers",
                customerService.searchCustomers(keyword));

    } else {

        request.setAttribute(
                "customers",
                customerService.getAllCustomers());

    }

    request.getRequestDispatcher("/customers.jsp")
            .forward(request, response);
  }
}

