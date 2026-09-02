package com.bank.controller;

import com.bank.model.Account;
import com.bank.model.Customer;
import com.bank.model.Transaction;

import org.junit.jupiter.api.Test;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.Collections;

import static org.mockito.Mockito.*;

class ReportServletTest {

    private ReportServlet servlet = new ReportServlet();

    private HttpServletRequest request() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("username")).thenReturn("testuser");

        return request;
    }

    private HttpServletResponse response() {
        return mock(HttpServletResponse.class);
    }

    private void mockDispatcher(HttpServletRequest request) {

        RequestDispatcher dispatcher =
                mock(RequestDispatcher.class);

        when(request.getRequestDispatcher("/reports.jsp"))
                .thenReturn(dispatcher);
    }

    @Test
    void unknownReport_shouldReturn404() throws Exception {

        HttpServletRequest request = request();
        HttpServletResponse response = response();

        when(request.getParameter("type"))
                .thenReturn("unknown");

        when(request.getParameter("format"))
                .thenReturn(null);

        servlet.doGet(request, response);

        verify(response)
                .sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void dailyReport_shouldForward() throws Exception {

        HttpServletRequest request = request();
        HttpServletResponse response = response();

        mockDispatcher(request);

        when(request.getParameter("type"))
                .thenReturn("daily");

        when(request.getParameter("format"))
                .thenReturn(null);

        servlet.doGet(request, response);

        verify(request)
                .setAttribute(eq("transactions"), any());

        verify(request.getRequestDispatcher("/reports.jsp"))
                .forward(request, response);
    }

    @Test
    void depositReport_shouldForward() throws Exception {

        HttpServletRequest request = request();
        HttpServletResponse response = response();

        mockDispatcher(request);

        when(request.getParameter("type"))
                .thenReturn("deposit");

        when(request.getParameter("format"))
                .thenReturn(null);

        servlet.doGet(request, response);

        verify(request)
                .setAttribute(eq("transactions"), any());

        verify(request)
                .setAttribute("title", "Deposit Report");

        verify(request.getRequestDispatcher("/reports.jsp"))
                .forward(request, response);
    }

    @Test
    void withdrawReport_shouldForward() throws Exception {

        HttpServletRequest request = request();
        HttpServletResponse response = response();

        mockDispatcher(request);

        when(request.getParameter("type"))
                .thenReturn("withdraw");

        when(request.getParameter("format"))
                .thenReturn(null);

        servlet.doGet(request, response);

        verify(request)
                .setAttribute(eq("transactions"), any());

        verify(request)
                .setAttribute("title", "Withdrawal Report");

        verify(request.getRequestDispatcher("/reports.jsp"))
                .forward(request, response);
    }

    @Test
    void transferReport_shouldForward() throws Exception {

        HttpServletRequest request = request();
        HttpServletResponse response = response();

        mockDispatcher(request);

        when(request.getParameter("type"))
                .thenReturn("transfer");

        when(request.getParameter("format"))
                .thenReturn(null);

        servlet.doGet(request, response);

        verify(request)
                .setAttribute(eq("transactions"), any());

        verify(request)
                .setAttribute("title", "Transfer Report");

        verify(request.getRequestDispatcher("/reports.jsp"))
                .forward(request, response);
    }

    @Test
    void customerReport_shouldForward() throws Exception {

        HttpServletRequest request = request();
        HttpServletResponse response = response();

        mockDispatcher(request);

        when(request.getParameter("type"))
                .thenReturn("customer");

        when(request.getParameter("format"))
                .thenReturn(null);

        servlet.doGet(request, response);

        verify(request)
                .setAttribute(eq("customers"), any());

        verify(request)
                .setAttribute("title", "Customer Report");

        verify(request.getRequestDispatcher("/reports.jsp"))
                .forward(request, response);
    }

    @Test
    void accountReport_shouldForward() throws Exception {

        HttpServletRequest request = request();
        HttpServletResponse response = response();

        mockDispatcher(request);

        when(request.getParameter("type"))
                .thenReturn("account");

        when(request.getParameter("format"))
                .thenReturn(null);

        servlet.doGet(request, response);

        verify(request)
                .setAttribute(eq("accounts"), any());

        verify(request)
                .setAttribute("title", "Account Report");

        verify(request.getRequestDispatcher("/reports.jsp"))
                .forward(request, response);
    }

    @Test
    void dailyInvalidFormat_shouldReturn400() throws Exception {

        HttpServletRequest request = request();
        HttpServletResponse response = response();

        when(request.getParameter("type"))
                .thenReturn("daily");

        when(request.getParameter("format"))
                .thenReturn("pdf");

        servlet.doGet(request, response);

        verify(response)
                .sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void depositInvalidFormat_shouldReturn400() throws Exception {

        HttpServletRequest request = request();
        HttpServletResponse response = response();

        when(request.getParameter("type"))
                .thenReturn("deposit");

        when(request.getParameter("format"))
                .thenReturn("pdf");

        servlet.doGet(request, response);

        verify(response)
                .sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void withdrawInvalidFormat_shouldReturn400() throws Exception {

        HttpServletRequest request = request();
        HttpServletResponse response = response();

        when(request.getParameter("type"))
                .thenReturn("withdraw");

        when(request.getParameter("format"))
                .thenReturn("pdf");

        servlet.doGet(request, response);

        verify(response)
                .sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void transferInvalidFormat_shouldReturn400() throws Exception {

        HttpServletRequest request = request();
        HttpServletResponse response = response();

        when(request.getParameter("type"))
                .thenReturn("transfer");

        when(request.getParameter("format"))
                .thenReturn("pdf");

        servlet.doGet(request, response);

        verify(response)
                .sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void customerInvalidFormat_shouldReturn400() throws Exception {

        HttpServletRequest request = request();
        HttpServletResponse response = response();

        when(request.getParameter("type"))
                .thenReturn("customer");

        when(request.getParameter("format"))
                .thenReturn("excelx");

        servlet.doGet(request, response);

        verify(response)
                .sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void accountInvalidFormat_shouldReturn400() throws Exception {

        HttpServletRequest request = request();
        HttpServletResponse response = response();

        when(request.getParameter("type"))
                .thenReturn("account");

        when(request.getParameter("format"))
                .thenReturn("pdf");

        servlet.doGet(request, response);

        verify(response)
                .sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}
