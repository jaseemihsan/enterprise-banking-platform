package com.bank.controller;

import com.bank.model.Customer;
import com.bank.service.CustomerService;
import com.bank.service.AuditLogService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import java.util.List;

@WebServlet({"/customers", "/customers/add",  "/customers/edit",
    "/customers/update", "/customers/delete"})

public class CustomerServlet extends HttpServlet {

    private final CustomerService customerService = new CustomerService();
    private final AuditLogService auditLogService = new AuditLogService();

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
		
	auditLogService.log(
        (String) request.getSession().getAttribute("username"),
        "CREATE",
        "Customer",
        "Created customer: " + customer.getEmail(),
        request.getRemoteAddr());

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

	auditLogService.log(
        (String) request.getSession().getAttribute("username"),
        "UPDATE",
        "Customer",
        "Updated customer: " + customer.getEmail(),
        request.getRemoteAddr());

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

    boolean deleted = customerService.deleteCustomer(id);

       if (deleted) {

	 auditLogService.log(
        (String) request.getSession().getAttribute("username"),
        "DELETE",
        "Customer",
        "Deleted customer ID: " + id,
        request.getRemoteAddr());

	 } else {

        request.getSession().setAttribute(
                "error",
                "Customer cannot be deleted because they have existing accounts.");

    }
         response.sendRedirect(request.getContextPath() + "/customers");

         return;
    }


String keyword = request.getParameter("search");


List<Customer> customers;

if (keyword != null && !keyword.isBlank()) {

    customers = customerService.searchCustomers(keyword);

} else {

    customers = customerService.getAllCustomers();

}


request.setAttribute("customers", customers);


request.getRequestDispatcher("/customers.jsp")
       .forward(request, response);

return;
}
}
