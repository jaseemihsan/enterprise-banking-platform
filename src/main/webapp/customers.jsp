<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.bank.model.Customer" %>

<%@ include file="includes/header.jsp" %>
<%@ include file="includes/navbar.jsp" %>

<%
String sessionError = (String) session.getAttribute("error");

if (sessionError != null) {
%>

<div class="alert alert-danger">
    <%= sessionError %>
</div>

<%
    session.removeAttribute("error");
}
%>


<h2>ABC Bank - Customer Registration</h2>

<form action="<%= request.getContextPath() %>/customers/add" method="post">

    <label>First Name</label><br>
    <input type="text" name="firstName" required><br><br>

    <label>Last Name</label><br>
    <input type="text" name="lastName" required><br><br>

    <label>Email</label><br>
    <input type="email" name="email" required><br><br>

    <label>Phone</label><br>
    <input type="text" name="phone" required><br><br>

    <button type="submit">
        Save Customer
    </button>

</form>

<hr>

<h2>Customer List</h2>

<hr>

<h3>Search Customer</h3>

<form action="${pageContext.request.contextPath}/customers" method="get">

    <input type="text"
           name="search"
           placeholder="Search by Name or Email">

    <button type="submit">
        Search
    </button>

</form>

<br>

<table border="1" cellpadding="8">

<tr>
    <th scope="col">ID</th>
    <th scope="col">First Name</th>
    <th scope="col">Last Name</th>
    <th scope="col">Email</th>
    <th scope="col">Phone</th>
    <th>Action</th>
</tr>

<%
List<Customer> customers =
    (List<Customer>) request.getAttribute("customers");

if (customers != null) {

    for (Customer customer : customers) {
%>

<tr>

<td><%= customer.getId() %></td>

<td><%= customer.getFirstName() %></td>

<td><%= customer.getLastName() %></td>

<td><%= customer.getEmail() %></td>

<td><%= customer.getPhone() %></td>

<td>
    <a href="<%=request.getContextPath()%>/customers/edit?id=<%=customer.getId()%>">
        Edit
    </a>

    |

    <a href="<%=request.getContextPath()%>/customers/delete?id=<%=customer.getId()%>"
       onclick="return confirm('Delete this customer?');">
         Delete
</a>


</td>

</tr>

<%
    }
}
%>

</table>

<%
String success = request.getParameter("success");
String requestError = request.getParameter("error");

if(success != null){
%>
<p style="color:green"><%=success%></p>
<%
}

if(requestError != null){
%>
<p style="color:red"><%=requestError%></p>
<%
}
%>


<%@ include file="includes/footer.jsp" %>
