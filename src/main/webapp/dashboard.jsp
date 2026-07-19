<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bank.model.Dashboard" %>
<%@ page import="java.util.List" %>
<%@ page import="com.bank.model.Customer" %>


<%
Dashboard dashboard = (Dashboard) request.getAttribute("dashboard");

List<Customer> recentCustomers =
        (List<Customer>) request.getAttribute("recentCustomers");
%>

<%@ include file="includes/header.jsp" %>
<%@ include file="includes/navbar.jsp" %>

<div class="container mt-4">

    <h2 class="mb-4">Enterprise Banking Dashboard</h2>

    <!-- Dashboard Cards -->
    <div class="row">

        <div class="col-lg-3 col-md-6 mb-3">

            <div class="card shadow border-0">

                <div class="card-body text-center">

                    <h5 class="card-title">👥 Customers</h5>

                    <h1 class="display-5 text-primary">
                        <%= dashboard.getTotalCustomers() %>
                    </h1>

                </div>

            </div>

        </div>

        <div class="col-lg-3 col-md-6 mb-3">

            <div class="card shadow border-0">

                <div class="card-body text-center">

                    <h5 class="card-title">🏦 Accounts</h5>

                    <h1 class="display-5 text-success">
                        <%= dashboard.getTotalAccounts() %>
                    </h1>

                </div>

            </div>

        </div>

        <div class="col-lg-3 col-md-6 mb-3">

            <div class="card shadow border-0">

                <div class="card-body text-center">

                    <h5 class="card-title">💰 Transactions</h5>

                    <h1 class="display-5 text-danger">
                        <%= dashboard.getTotalTransactions() %>
                    </h1>

                </div>

            </div>

        </div>

    <div class="col-lg-3 col-md-6 mb-3">

    <div class="card border-0 shadow text-white bg-warning">

        <div class="card-body text-center">

            <h5>Total Balance</h5>

            <h2>
                AED <%= dashboard.getTotalBalance() %>
            </h2>

        </div>

    </div>

</div>

    </div>

<hr class="my-4">

<h3>Recent Customers</h3>

<table class="table table-striped table-bordered">

    <thead class="table-dark">

    <tr>

        <th>ID</th>
        <th>First Name</th>
        <th>Last Name</th>
        <th>Email</th>
        <th>Phone</th>

    </tr>

    </thead>

    <tbody>

    <%
    if(recentCustomers != null){

        for(Customer customer : recentCustomers){
    %>

    <tr>

        <td><%=customer.getId()%></td>

        <td><%=customer.getFirstName()%></td>

        <td><%=customer.getLastName()%></td>

        <td><%=customer.getEmail()%></td>

        <td><%=customer.getPhone()%></td>

    </tr>

    <%
        }
    }
    %>

    </tbody>

</table>

<hr class="my-4">

<h3>Quick Navigation</h3>

    <div class="row">

        <div class="col-md-3 mb-3">

            <div class="card shadow-sm">

                <div class="card-body text-center">

                    <h5>Customer Management</h5>

                    <a href="<%=request.getContextPath()%>/customers"
                       class="btn btn-primary mt-2">
                        Open
                    </a>

                </div>

            </div>

        </div>

        <div class="col-md-3 mb-3">

            <div class="card shadow-sm">

                <div class="card-body text-center">

                    <h5>Accounts</h5>

                    <a href="<%=request.getContextPath()%>/accounts"
   class="btn btn-success">
Open
</a>

                </div>

            </div>

        </div>

        <div class="col-md-3 mb-3">

            <div class="card shadow-sm">

                <div class="card-body text-center">

                    <h5>Transactions</h5>

                    <a href="<%=request.getContextPath()%>/transactions"
   class="btn btn-success">
Open
</a>

                </div>

            </div>

        </div>

        <div class="col-md-3 mb-3">

            <div class="card shadow-sm">

                <div class="card-body text-center">

                    <h5>Reports</h5>

                    <a href="<%=request.getContextPath()%>/reports"
   class="btn btn-warning">
Open
</a>

                </div>

            </div>

        </div>

    </div>

</div>

<%@ include file="includes/footer.jsp" %>
