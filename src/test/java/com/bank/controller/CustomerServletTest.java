package com.bank.controller;

import com.bank.model.Customer;
import com.bank.service.AuditLogService;
import com.bank.service.CustomerService;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServletTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private AuditLogService auditLogService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher dispatcher;

    private CustomerServlet servlet;

    @BeforeEach
    void setUp() {
        servlet = new CustomerServlet(
                customerService,
                auditLogService);
    }

    @Test
    void addCustomer_shouldRedirect_whenSuccessful()
            throws Exception {

        when(request.getServletPath())
                .thenReturn("/customers/add");

        when(request.getParameter("firstName"))
                .thenReturn("Test");

        when(request.getParameter("lastName"))
                .thenReturn("Customer");

        when(request.getParameter("email"))
                .thenReturn("test@example.com");

        when(request.getParameter("phone"))
                .thenReturn("0500000000");

        when(request.getSession())
                .thenReturn(session);

        when(session.getAttribute("username"))
                .thenReturn("admin");

        when(request.getContextPath())
                .thenReturn("/banking-app");

        when(request.getRemoteAddr())
                .thenReturn("127.0.0.1");

        when(customerService.addCustomer(any(Customer.class)))
                .thenReturn(true);

        servlet.doPost(request, response);

        verify(customerService)
                .addCustomer(any(Customer.class));

        verify(auditLogService).log(
                eq("admin"),
                eq("CREATE"),
                eq("Customer"),
                contains("test@example.com"),
                eq("127.0.0.1"));

        verify(response)
                .sendRedirect("/banking-app/customers");
    }

    @Test
    void addCustomer_shouldRedirectWithError_whenFailed()
            throws Exception {

        when(request.getServletPath())
                .thenReturn("/customers/add");

        when(request.getParameter("firstName"))
                .thenReturn("Test");

        when(request.getParameter("lastName"))
                .thenReturn("Customer");

        when(request.getParameter("email"))
                .thenReturn("test@example.com");

        when(request.getParameter("phone"))
                .thenReturn("0500000000");

        when(request.getContextPath())
                .thenReturn("/banking-app");

        when(customerService.addCustomer(any(Customer.class)))
                .thenReturn(false);

        servlet.doPost(request, response);

        verify(customerService)
                .addCustomer(any(Customer.class));

        verify(response)
                .sendRedirect(
                        "/banking-app/customers.jsp?error=Unable+to+Add+Customer");

        verify(auditLogService, never()).log(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString());
    }

    @Test
    void updateCustomer_shouldRedirect_whenSuccessful()
            throws Exception {

        when(request.getServletPath())
                .thenReturn("/customers/update");

        when(request.getParameter("id"))
                .thenReturn("1");

        when(request.getParameter("firstName"))
                .thenReturn("Updated");

        when(request.getParameter("lastName"))
                .thenReturn("Customer");

        when(request.getParameter("email"))
                .thenReturn("updated@example.com");

        when(request.getParameter("phone"))
                .thenReturn("0501234567");

        when(request.getSession())
                .thenReturn(session);

        when(session.getAttribute("username"))
                .thenReturn("admin");

        when(request.getContextPath())
                .thenReturn("/banking-app");

        when(request.getRemoteAddr())
                .thenReturn("127.0.0.1");

        when(customerService.updateCustomer(any(Customer.class)))
                .thenReturn(true);

        servlet.doPost(request, response);

        verify(customerService)
                .updateCustomer(any(Customer.class));

        verify(auditLogService).log(
                eq("admin"),
                eq("UPDATE"),
                eq("Customer"),
                contains("updated@example.com"),
                eq("127.0.0.1"));

        verify(response)
                .sendRedirect("/banking-app/customers");
    }

    @Test
    void updateCustomer_shouldRedirectWithError_whenFailed()
            throws Exception {

        when(request.getServletPath())
                .thenReturn("/customers/update");

        when(request.getParameter("id"))
                .thenReturn("1");

        when(request.getParameter("firstName"))
                .thenReturn("Updated");

        when(request.getParameter("lastName"))
                .thenReturn("Customer");

        when(request.getParameter("email"))
                .thenReturn("updated@example.com");

        when(request.getParameter("phone"))
                .thenReturn("0501234567");

        when(request.getContextPath())
                .thenReturn("/banking-app");

        when(customerService.updateCustomer(any(Customer.class)))
                .thenReturn(false);

        servlet.doPost(request, response);

        verify(customerService)
                .updateCustomer(any(Customer.class));

        verify(response)
                .sendRedirect(
                        "/banking-app/customers?error=UpdateFailed");

        verify(auditLogService, never()).log(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString());
    }

    @Test
    void editCustomer_shouldForwardToEditPage()
            throws Exception {

        when(request.getServletPath())
                .thenReturn("/customers/edit");

        when(request.getParameter("id"))
                .thenReturn("1");

        Customer customer = new Customer();

        customer.setId(1);
        customer.setFirstName("Test");
        customer.setLastName("Customer");

        when(customerService.getCustomerById(1))
                .thenReturn(customer);

        when(request.getRequestDispatcher(
                "/edit-customer.jsp"))
                .thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(request)
                .setAttribute("customer", customer);

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void deleteCustomer_shouldAuditAndRedirect_whenSuccessful()
            throws Exception {

        when(request.getServletPath())
                .thenReturn("/customers/delete");

        when(request.getParameter("id"))
                .thenReturn("1");

        when(request.getSession())
                .thenReturn(session);

        when(session.getAttribute("username"))
                .thenReturn("admin");

        when(request.getContextPath())
                .thenReturn("/banking-app");

        when(request.getRemoteAddr())
                .thenReturn("127.0.0.1");

        when(customerService.deleteCustomer(1))
                .thenReturn(true);

        servlet.doGet(request, response);

        verify(customerService)
                .deleteCustomer(1);

        verify(auditLogService).log(
                eq("admin"),
                eq("DELETE"),
                eq("Customer"),
                contains("1"),
                eq("127.0.0.1"));

        verify(response)
                .sendRedirect("/banking-app/customers");
    }

    @Test
    void deleteCustomer_shouldSetError_whenFailed()
            throws Exception {

        when(request.getServletPath())
                .thenReturn("/customers/delete");

        when(request.getParameter("id"))
                .thenReturn("1");

        when(request.getSession())
                .thenReturn(session);

        when(request.getContextPath())
                .thenReturn("/banking-app");

        when(customerService.deleteCustomer(1))
                .thenReturn(false);

        servlet.doGet(request, response);

        verify(session).setAttribute(
                eq("error"),
                contains("Customer cannot be deleted"));

        verify(auditLogService, never()).log(
                anyString(),
                anyString(),
                anyString(),
                anyString(),
                anyString());

        verify(response)
                .sendRedirect("/banking-app/customers");
    }

    @Test
    void searchCustomers_shouldReturnSearchResults()
            throws Exception {

        when(request.getServletPath())
                .thenReturn("/customers");

        when(request.getParameter("search"))
                .thenReturn("Test");

        when(customerService.searchCustomers("Test"))
                .thenReturn(Collections.emptyList());

        when(request.getRequestDispatcher(
                "/customers.jsp"))
                .thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(customerService)
                .searchCustomers("Test");

        verify(request)
                .setAttribute(
                        eq("customers"),
                        anyList());

        verify(dispatcher)
                .forward(request, response);
    }

    @Test
    void customers_shouldReturnAllCustomers_whenNoSearch()
            throws Exception {

        when(request.getServletPath())
                .thenReturn("/customers");

        when(request.getParameter("search"))
                .thenReturn(null);

        when(customerService.getAllCustomers())
                .thenReturn(Collections.emptyList());

        when(request.getRequestDispatcher(
                "/customers.jsp"))
                .thenReturn(dispatcher);

        servlet.doGet(request, response);

        verify(customerService)
                .getAllCustomers();

        verify(request)
                .setAttribute(
                        eq("customers"),
                        anyList());

        verify(dispatcher)
                .forward(request, response);
    }
}
