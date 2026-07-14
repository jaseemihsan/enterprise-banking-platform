<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bank.model.Account" %>

<%
Account account = (Account) request.getAttribute("account");
%>

<%@ include file="includes/header.jsp" %>
<%@ include file="includes/navbar.jsp" %>

<div class="container mt-4">

<h2>Edit Account</h2>

<hr>

<form action="<%=request.getContextPath()%>/accounts/update"
      method="post">

<input
type="hidden"
name="id"
value="<%=account.getId()%>">

<div class="mb-3">

<label class="form-label">

Account Number

</label>

<input
class="form-control"
value="<%=account.getAccountNumber()%>"
readonly>

</div>

<div class="mb-3">

<label class="form-label">

Customer

</label>

<input
class="form-control"
value="<%=account.getCustomerName()%>"
readonly>

</div>

<div class="mb-3">

<label class="form-label">

Account Type

</label>

<select
class="form-select"
name="accountType">

<option
value="SAVINGS"
<%=account.getAccountType().equals("SAVINGS")?"selected":""%>>

SAVINGS

</option>

<option
value="CURRENT"
<%=account.getAccountType().equals("CURRENT")?"selected":""%>>

CURRENT

</option>

</select>

</div>

<div class="mb-3">

<label class="form-label">

Balance

</label>

<input
class="form-control"
value="<%=account.getBalance()%>"
readonly>

</div>

<div class="mb-3">

<label class="form-label">

Status

</label>

<select
class="form-select"
name="status">

<option
value="ACTIVE"
<%=account.getStatus().equals("ACTIVE")?"selected":""%>>

ACTIVE

</option>

<option
value="INACTIVE"
<%=account.getStatus().equals("INACTIVE")?"selected":""%>>

INACTIVE

</option>

<option
value="CLOSED"
<%=account.getStatus().equals("CLOSED")?"selected":""%>>

CLOSED

</option>

</select>

</div>

<button
class="btn btn-success">

Update Account

</button>

<a
href="<%=request.getContextPath()%>/accounts"
class="btn btn-secondary">

Cancel

</a>

</form>

</div>

<%@ include file="includes/footer.jsp" %>
