<%@ page contentType="text/html;charset=UTF-8" language="java"%>

<%@ include file="includes/header.jsp"%>
<%@ include file="includes/navbar.jsp"%>

<%
String success=request.getParameter("success");
String error=request.getParameter("error");
%>

<div class="container mt-4">

<h2>

Deposit Money

</h2>

<hr>

<% if(success!=null){ %>

<div class="alert alert-success">

<%=success%>

</div>

<% } %>

<% if(error!=null){ %>

<div class="alert alert-danger">

<%=error%>

</div>

<% } %>

<form
action="<%=request.getContextPath()%>/deposit"
method="post">

<div class="mb-3">

<label class="form-label">

Account ID

</label>

<%@ page import="java.util.List"%>
<%@ page import="com.bank.model.Account"%>

<%
List<Account> accounts =
(List<Account>)request.getAttribute("accounts");
%>

<select
class="form-select"
name="accountId"
required>

<option value="">

Select Account

</option>

<%

for(Account account : accounts){

%>

<option value="<%=account.getId()%>">

<%=account.getAccountNumber()%>

|

<%=account.getCustomerName()%>

|

<%=account.getAccountType()%>

|

AED <%=account.getBalance()%>

</option>

<%

}

%>

</select>

</div>

<div class="mb-3">

<label class="form-label">

Amount

</label>

<input
class="form-control"
type="number"
step="0.01"
name="amount"
required>

</div>

<div class="mb-3">

<label class="form-label">

Remarks

</label>

<textarea
class="form-control"
name="remarks"></textarea>

</div>

<button
class="btn btn-success">

Deposit

</button>

</form>

</div>

<%@ include file="includes/footer.jsp"%>
