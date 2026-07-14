<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.bank.model.Account" %>
<%@ page import="com.bank.model.Customer" %>

<%
List<Account> accounts =
        (List<Account>) request.getAttribute("accounts");

List<Customer> customers =
        (List<Customer>) request.getAttribute("customers");

String success = request.getParameter("success");
String error = request.getParameter("error");
%>

<%@ include file="includes/header.jsp" %>
<%@ include file="includes/navbar.jsp" %>

<div class="container mt-4">

<h2 class="mb-4">

Account Management

</h2>

<% if(success != null){ %>

<div class="alert alert-success">

<%=success%>

</div>

<% } %>

<% if(error != null){ %>

<div class="alert alert-danger">

<%=error%>

</div>

<% } %>

<hr>

<h3>Create Account</h3>

<form action="<%=request.getContextPath()%>/accounts"
      method="post">

<div class="row">

<div class="col-md-4">

<label class="form-label">

Customer

</label>

<select
class="form-select"
name="customerId"
required>

<option value="">Select Customer</option>

<%
if(customers != null){

for(Customer customer : customers){
%>

<option value="<%=customer.getId()%>">

<%=customer.getFirstName()%>
<%=customer.getLastName()%>

</option>

<%
}
}
%>

</select>

</div>

<div class="col-md-3">

<label class="form-label">

Account Type

</label>

<select
class="form-select"
name="accountType">

<option>SAVINGS</option>

<option>CURRENT</option>

</select>

</div>

<div class="col-md-3">

<label class="form-label">

Opening Balance

</label>

<input
class="form-control"
type="number"
step="0.01"
name="balance"
required>

</div>

<div class="col-md-2 d-flex align-items-end">

<button
class="btn btn-success w-100">

Create

</button>

</div>

</div>

</form>

<hr class="my-4">

<h3>Search Account</h3>

<form action="<%=request.getContextPath()%>/accounts"
method="get">

<div class="row">

<div class="col-md-10">

<input
class="form-control"
type="text"
name="search"
placeholder="Search Account Number or Customer Name">

</div>

<div class="col-md-2">

<button
class="btn btn-primary w-100">

Search

</button>

</div>

</div>

</form>

<hr class="my-4">

<h3>Account List</h3>

<table class="table table-striped table-bordered">

<thead class="table-dark">

<tr>

<th>ID</th>

<th>Account Number</th>

<th>Customer</th>

<th>Type</th>

<th>Balance</th>

<th>Status</th>

<th width="180">

Action

</th>

</tr>

</thead>

<tbody>

<%
if(accounts != null){

for(Account account : accounts){
%>

<tr>

<td>

<%=account.getId()%>

</td>

<td>

<%=account.getAccountNumber()%>

</td>

<td>

<%=account.getCustomerName()%>

</td>

<td>

<%=account.getAccountType()%>

</td>

<td>

AED <%=account.getBalance()%>

</td>

<td>

<span class="badge bg-success">

<%=account.getStatus()%>

</span>

</td>

<td>

<a
class="btn btn-warning btn-sm"
href="<%=request.getContextPath()%>/accounts/edit?id=<%=account.getId()%>">

Edit

</a>

<a
class="btn btn-danger btn-sm"
onclick="return confirm('Close this account?')"
href="<%=request.getContextPath()%>/accounts/close?id=<%=account.getId()%>">

Close

</a>

</td>

</tr>

<%
}
}
%>

</tbody>

</table>

</div>

<%@ include file="includes/footer.jsp" %>
