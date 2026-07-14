<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bank.model.Customer" %>

<%
Customer customer = (Customer) request.getAttribute("customer");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Customer</title>
</head>
<body>

<h2>Edit Customer</h2>

<form action="${pageContext.request.contextPath}/customers/update" method="post">

    <input type="hidden" name="id"
           value="<%=customer.getId()%>">

    First Name<br>
    <input type="text"
           name="firstName"
           value="<%=customer.getFirstName()%>"><br><br>

    Last Name<br>
    <input type="text"
           name="lastName"
           value="<%=customer.getLastName()%>"><br><br>

    Email<br>
    <input type="email"
           name="email"
           value="<%=customer.getEmail()%>"><br><br>

    Phone<br>
    <input type="text"
           name="phone"
           value="<%=customer.getPhone()%>"><br><br>

    <button type="submit">
        Update Customer
    </button>

</form>

</body>
</html>
