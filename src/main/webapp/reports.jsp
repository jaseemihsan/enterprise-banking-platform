<%@ page contentType="text/html;charset=UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="com.bank.model.Transaction" %>
<%@ page import="com.bank.model.Customer"%>
<%@ page import="com.bank.model.Account" %>

<%
List<Transaction> transactions =
        (List<Transaction>) request.getAttribute("transactions");

List<Customer> customers =
        (List<Customer>) request.getAttribute("customers");

List<Account> accounts =
        (List<Account>) request.getAttribute("accounts");

String title = (String) request.getAttribute("title");
%>

<%@ include file="includes/header.jsp" %>

<div class="container mt-4">

<h2 class="mb-4">Enterprise Reports</h2>

<div class="row">

    <!-- Daily Transactions -->
    <div class="col-lg-6 mb-4">
        <div class="card shadow h-100 border-0">

            <div class="card-header bg-primary text-white">

                <h5 class=class="mb-0">
                    📅 Daily Transactions
                </h5>
                
		</div>

                <div class="card-body">
                <p class="text-muted">
                    View today's banking transactions.
                </p>

                <a href="reports?type=daily"
                   class="btn btn-primary btn-sm">
                    View
                </a>

                <a href="reports?type=daily&format=pdf"
                   class="btn btn-danger btn-sm">
                    PDF
                </a>

		<a href="reports?type=daily&format=excel"
class="btn btn-success btn-sm">
Excel
</a>

		 </div>

            </div>

        </div>
    </div>

    <!-- Customer Report -->
    <div class="col-lg-6 mb-4">

        <div class="card shadow h-100 border-0">

            <div class="card-header bg-primary text-white">

                <h5 class="mb-0">
                    👥 Customer Report
                </h5>
                 
                 </div>

    <div class="card-body">

                <p class="text-muted">
                    View all registered customers.
                </p>

                <a href="reports?type=customer"
                   class="btn btn-primary btn-sm">
                    View
                </a>

                <a href="reports?type=customer&format=pdf"
                   class="btn btn-danger btn-sm">
                    PDF
                </a>

		<a href="reports?type=customer&format=excel"
                    class="btn btn-success btn-sm">
                    Excel
                </a>

            </div>

        </div>

    </div>

    <!-- Deposit Report -->

    <div class="col-lg-6 mb-4">

        <div class="card shadow h-100 border-0">

            <div class="card-header bg-primary text-white">

                <h5 class="mb-0">
                    💰 Deposit Report
                </h5>

		 </div>

    <div class="card-body">

                <p class="text-muted">
                    View all deposit transactions.
                </p>

                <a href="reports?type=deposit"
                   class="btn btn-primary btn-sm">
                    View
                </a>

		<a href="reports?type=deposit&format=excel"
class="btn btn-success btn-sm">
Excel
</a>

            </div>

        </div>

    </div>

    <!-- Withdrawal Report -->

    <div class="col-lg-6 mb-4">

        <div class="card shadow h-100 border-0">

            <div class="card-header bg-primary text-white">

                <h5 class="mb-0">
                    💸 Withdrawal Report
                </h5>

		 </div>

    <div class="card-body">

                <p class="text-muted">
                    View all withdrawal transactions.
                </p>

                <a href="reports?type=withdraw"
                   class="btn btn-primary btn-sm">
                    View
                </a>

		<a href="reports?type=withdraw&format=excel"
class="btn btn-success btn-sm">
Excel
</a>

            </div>

        </div>

    </div>

    <!-- Transfer Report -->

    <div class="col-lg-6 mb-4">

        <div class="card shadow h-100 border-0">

            <div class="card-header bg-primary text-white">

                <h5 class="mb-0">
                    🔄 Transfer Report
                </h5>

		 </div>

    <div class="card-body">

                <p class="text-muted">
                    View all transfer transactions.
                </p>

                <a href="reports?type=transfer"
                   class="btn btn-primary btn-sm">
                    View
                </a>

		<a href="reports?type=transfer&format=excel"
class="btn btn-success btn-sm">
Excel
</a>

            </div>

        </div>

    </div>

    <!-- Account Report -->

    <div class="col-lg-6 mb-4">

        <div class="card shadow h-100 border-0">

            <div class="card-header bg-primary text-white">

                <h5 class="mb-0">
                    🏦 Account Report
                </h5>

		 </div>

    <div class="card-body">

                <p class="text-muted">
                    View all bank accounts.
                </p>

                <a href="reports?type=account"
                   class="btn btn-primary btn-sm">
                    View
                </a>

		<a href="reports?type=account&format=excel"
class="btn btn-success btn-sm">
Excel
</a>

            </div>

        </div>

    </div>

</div>

<hr class="my-4">

<!-- ========================= TRANSACTION REPORT ========================= -->

<%
if (transactions != null) {
%>

<div class="alert alert-info">

    <strong><%= title %></strong>

    <br>

    Total Records :

    <strong><%= transactions.size() %></strong>

</div>

<table class="table table-striped table-hover table-bordered">

<thead class="table-dark text-center">

<tr>

    <th>#</th>
    <th>Date</th>
    <th>Account ID</th>
    <th>Type</th>
    <th>Amount</th>
    <th>Reference</th>

</tr>

</thead>

<tbody class="align-middle">

<%

int serialNo = 1;

for(Transaction t : transactions){

%>

<tr>

<td class="text-center">
<%= serialNo++ %>
</td>

<td>
<%= t.getTransactionTime() %>
</td>

<td class="text-center">
<%= t.getAccountId() %>
</td>

<td>
<%= t.getTransactionType() %>
</td>

<td class="text-end">

AED <%= t.getAmount() %>

</td>

<td>
<%= t.getReferenceNo() %>
</td>

</tr>

<%
}
%>

</tbody>

</table>

<%
}
%>

<!-- ========================= CUSTOMER REPORT ========================= -->

<%
if (customers != null) {
%>

<a href="reports?type=customer&format=pdf"
   class="btn btn-danger btn-sm mb-3">
    Export PDF
</a>

<div class="alert alert-info">

    <strong><%= title %></strong>

    <br>

    Total Customers :

    <strong><%= customers.size() %></strong>

</div>

<table class="table table-striped table-hover table-bordered">

<thead class="table-dark text-center">

<tr>

    <th>ID</th>
    <th>First Name</th>
    <th>Last Name</th>
    <th>Email</th>
    <th>Phone</th>

</tr>

</thead>

<tbody class="align-middle">

<%
for (Customer c : customers) {
%>

<tr>

<td class="text-center">
<%= c.getId() %>
</td>

<td>
<%= c.getFirstName() %>
</td>

<td>
<%= c.getLastName() %>
</td>

<td>
<%= c.getEmail() %>
</td>

<td>
<%= c.getPhone() %>
</td>

</tr>

<%
}
%>

</tbody>

</table>

<%
}
%>

<!-- ========================= ACCOUNT REPORT ========================= -->

<%
if (accounts != null) {
%>

<div class="alert alert-info">

    <strong><%= title %></strong>

    <br>

    Total Accounts :

    <strong><%= accounts.size() %></strong>

</div>

<table class="table table-striped table-hover table-bordered">

<thead class="table-dark text-center">

<tr>

    <th>ID</th>
    <th>Account No</th>
    <th>Customer</th>
    <th>Email</th>
    <th>Type</th>
    <th>Balance</th>
    <th>Status</th>

</tr>

</thead>

<tbody class="align-middle">

<%
for (Account account : accounts) {
%>

<tr>

<td class="text-center">
<%= account.getId() %>
</td>

<td>
<%= account.getAccountNumber() %>
</td>

<td>
<%= account.getCustomerName() %>
</td>

<td>
<%= account.getCustomerEmail() %>
</td>

<td class="text-center">
<%= account.getAccountType() %>
</td>

<td class="text-end">

AED <%= account.getBalance() %>

</td>

<td class="text-center">

<%
if ("ACTIVE".equalsIgnoreCase(account.getStatus())) {
%>

<span class="badge bg-success">
    ACTIVE
</span>

<%
} else {
%>

<span class="badge bg-danger">
    <%= account.getStatus() %>
</span>

<%
}
%>

</td>

</tr>

<%
}
%>

</tbody>

</table>

<%
}
%>


<%@ include file="includes/footer.jsp" %>
