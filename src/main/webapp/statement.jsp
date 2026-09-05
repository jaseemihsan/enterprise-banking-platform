<%@ page contentType="text/html;charset=UTF-8" %>

<%@ page import="java.util.List" %>
<%@ page import="com.bank.model.Transaction" %>
<%@ page import="com.bank.model.Account" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DecimalFormat" %>

<%
DecimalFormat df =
        new DecimalFormat("#,##0.00");
%>

<%
SimpleDateFormat sdf =
        new SimpleDateFormat("dd-MMM-yyyy HH:mm");
%>


<%
String selectedAccountId = request.getParameter("accountId");
%>

<%
Account selectedAccount =
(Account)request.getAttribute("selectedAccount");
%>

<%
List<Account> accounts =
(List<Account>)request.getAttribute("accounts");

List<Transaction> transactions =
(List<Transaction>)request.getAttribute("transactions");
%>

<h2>Account Statement</h2>

<form method="get"
      action="${pageContext.request.contextPath}/statement">

<select name="accountId">

<%
for(Account account : accounts){
%>

<option value="<%=account.getId()%>"
<%= (selectedAccountId != null &&
     selectedAccountId.equals(String.valueOf(account.getId())))
     ? "selected" : "" %>>
    <%=account.getAccountNumber()%>
</option>

<%
}
%>

</select>

<button type="submit">
Search
</button>

</form>


<%
if(selectedAccount != null){
%>

<table border="1" cellpadding="8">
    <caption>Account Details</caption>

    <tr>
        <th scope="row">Account Number</th>
        <td><%=selectedAccount.getAccountNumber()%></td>
    </tr>

    <tr>
        <th scope="row">Customer</th>
        <td><%=selectedAccount.getCustomerName()%></td>
    </tr>

    <tr>
        <th scope="row">Account Type</th>
        <td><%=selectedAccount.getAccountType()%></td>
    </tr>

    <tr>
        <th scope="row">Status</th>
        <td><%=selectedAccount.getStatus()%></td>
    </tr>

    <tr>
        <th scope="row">Current Balance</th>
        <td><%=df.format(selectedAccount.getBalance())%></td>
    </tr>

</table>

<br>

<%
}
%>

<%
if(transactions != null){
%>

<p>

<b>Total Transactions :</b>

<%=transactions.size()%>

</p>

<%
}
%>

<%
if(transactions != null){

    if(transactions.isEmpty()){
%>

<p><b>No transactions found.</b></p>

<%
    } else {

        int serialNo = 1;
%>

<h3>Transaction History</h3>

<table border="1" cellpadding="5" cellspacing="0" width="100%">

<caption>Transaction History</caption>

<thead>

<tr>
    <th scope="col" align="center">#</th>
    <th scope="col" align="center">Date</th>
    <th scope="col" align="center">Type</th>
    <th scope="col" align="center">Amount</th>
    <th scope="col" align="center">Balance Before</th>
    <th scope="col" align="center">Balance After</th>
    <th scope="col" align="center">Reference</th>
    <th scope="col" align="center">Remarks</th>
</tr>

</thead> 

<tbody>

<%
for(Transaction t : transactions){
%>

<tr>
    <td align="center"><%= serialNo++ %></td>
    <td align="center"><%= sdf.format(t.getTransactionTime()) %></td>
    <td align="center"><%= t.getTransactionType() %></td>
    <td align="right"><%= df.format(t.getAmount()) %></td>
    <td align="right"><%= df.format(t.getBalanceBefore()) %></td>
    <td align="right"><%= df.format(t.getBalanceAfter()) %></td>
    <td align="center"><%= t.getReferenceNo() %></td>
    <td align="left"><%= t.getRemarks() %></td>
</tr>

<%
}
%>

</tbody>
</table>

<%
    }
}
%>
