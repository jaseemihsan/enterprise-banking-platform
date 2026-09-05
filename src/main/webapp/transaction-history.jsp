<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List"%>
<%@ page import="com.bank.model.Transaction"%>

<%
List<Transaction> transactions =
(List<Transaction>)request.getAttribute("transactions");
%>

<%@ include file="includes/header.jsp"%>
<%@ include file="includes/navbar.jsp"%>

<div class="container mt-4">

<h2>Transaction History</h2>

<table class="table table-bordered table-striped">

<caption>Customer Tansactions</caption>


<thead class="table-dark">

<tr>

<th scope="col">Reference</th>

<th scope="col">Account</th>

<th scope="col">Type</th>

<th scope="col">Amount</th>

<th scope="col">Before</th>

<th scope="col">After</th>

<th scope="col">Date</th>

</tr>

</thead>

<tbody>

<%
if(transactions != null){

for(Transaction t : transactions){
%>

<tr>

<td><%=t.getReferenceNo()%></td>

<td><%=t.getAccountNumber()%></td>

<td><%=t.getTransactionType()%></td>

<td>AED <%=t.getAmount()%></td>

<td>AED <%=t.getBalanceBefore()%></td>

<td>AED <%=t.getBalanceAfter()%></td>

<td><%=t.getTransactionTime()%></td>

</tr>

<%
}
}
%>

</tbody>

</table>

</div>

<%@ include file="includes/footer.jsp"%>
