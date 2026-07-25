<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bank.model.Dashboard" %>
<%@ page import="java.util.List" %>
<%@ page import="com.bank.model.Customer" %>
<%@ page import="com.bank.dto.DashboardStatistics" %>
<%@ page import="com.bank.dto.TransactionTrend" %>


<%
Dashboard dashboard = (Dashboard) request.getAttribute("dashboard");

List<Customer> recentCustomers =
        (List<Customer>) request.getAttribute("recentCustomers");
%>

<%
DashboardStatistics statistics =
    (DashboardStatistics) request.getAttribute("statistics");
%>

<%
List<TransactionTrend> transactionTrend =
        (List<TransactionTrend>) request.getAttribute("transactionTrend");
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

<div class="row">

    <div class="col-lg-8">

        <div class="card shadow">

            <div class="card-header bg-primary text-white">

                <h5 class="mb-0">
                    Transaction Analytics
                </h5>

            </div>

            <div class="card-body">

                <canvas id="transactionChart"></canvas>

            </div>

        </div>

    </div>

    <div class="col-lg-4">

        <div class="card shadow">

            <div class="card-header bg-success text-white">

                <h5 class="mb-0">
                    Bank Analytics
                </h5>

            </div>

            <div class="card-body">

                <table class="table table-borderless">

                    <tr>
                        <th>Customers</th>
                        <td><%=statistics.getCustomerCount()%></td>
                    </tr>

                    <tr>
                        <th>Accounts</th>
                        <td><%=statistics.getAccountCount()%></td>
                    </tr>

                    <tr>
                        <th>Transactions</th>
                        <td><%=statistics.getTransactionCount()%></td>
                    </tr>

                    <tr>
                        <th>Deposits</th>
                        <td>AED <%=statistics.getTotalDeposits()%></td>
                    </tr>

                    <tr>
                        <th>Withdrawals</th>
                        <td>AED <%=statistics.getTotalWithdrawals()%></td>
                    </tr>

                    <tr>
                        <th>Transfers</th>
                        <td>AED <%=statistics.getTotalTransfers()%></td>
                    </tr>

                </table>

            </div>

        </div>

    </div>

</div>

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

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<script>

const deposits =
    <%= statistics.getTotalDeposits() %>;

const withdrawals =
    <%= statistics.getTotalWithdrawals() %>;

const transfers =
    <%= statistics.getTotalTransfers() %>;

const ctx = document.getElementById("transactionChart");

new Chart(ctx, {

    type: "pie",

    data: {

        labels: [
            "Deposits",
            "Withdrawals",
            "Transfers"
        ],

        datasets: [{

            label: "Transaction Summary",

            data: [
                deposits,
                withdrawals,
                transfers
            ],

            backgroundColor: [
                "#0d6efd",
                "#dc3545",
                "#ffc107"
            ],

            borderWidth: 2

        }]

    }

});

</script>

<h3 class="mt-4">System Statistics</h3>

<canvas id="systemChart"></canvas>

<script>

const customers =
    <%= statistics.getCustomerCount() %>;

const accounts =
    <%= statistics.getAccountCount() %>;

const transactions =
    <%= statistics.getTransactionCount() %>;

</script>

<script>

const ctx2 =
document.getElementById("systemChart");

new Chart(ctx2, {

    type: "bar",

    data: {

        labels: [
            "Customers",
            "Accounts",
            "Transactions"
        ],

        datasets: [{

            label: "System Statistics",

            data: [
                customers,
                accounts,
                transactions
            ],

            backgroundColor: [
                "#0d6efd",
                "#198754",
                "#dc3545"
            ]

        }]

    }

});

</script>

<div class="card shadow mt-4">

    <div class="card-header bg-info text-white">

        <h5 class="mb-0">
            Transaction Trend
        </h5>

    </div>

    <div class="card-body">

        <canvas id="trendChart"></canvas>

    </div>

</div>

<script>

const trendLabels = [

<%
for(int i = 0; i < transactionTrend.size(); i++){

    TransactionTrend trend =
            transactionTrend.get(i);
%>

"<%=trend.getDate()%>"

<%
if(i < transactionTrend.size()-1){
%>
,
<%
}
}
%>

];

const trendData = [

<%
for(int i = 0; i < transactionTrend.size(); i++){

    TransactionTrend trend =
            transactionTrend.get(i);
%>

<%=trend.getCount()%>

<%
if(i < transactionTrend.size()-1){
%>
,
<%
}
}
%>

];

	const trendCtx =
document.getElementById("trendChart");

new Chart(trendCtx, {

    type: "line",

    data: {

        labels: trendLabels,

        datasets: [{

            label: "Transactions",

            data: trendData,

            borderColor: "#0d6efd",

            backgroundColor: "#0d6efd",

            fill: false,

            tension: 0.3

        }]

    }

});

</script>
<%@ include file="includes/footer.jsp" %>

