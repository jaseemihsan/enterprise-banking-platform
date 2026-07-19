<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.bank.model.User" %>

<%@ include file="includes/header.jsp" %>
<%@ include file="includes/navbar.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>User Management</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>

<div class="container mt-4">

    <h2>User Management</h2>

    <div class="mb-3">
    <a href="${pageContext.request.contextPath}/users/add"
       class="btn btn-primary">
        + Add User
    </a>
</div>

    <table class="table table-bordered table-striped">

        <thead class="table-dark">

        <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Role</th>
            <th>Status</th>
	    <th>Created</th>
            <th>Actions</th>
        </tr>

        </thead>

        <tbody>

<%
List<User> users = (List<User>) request.getAttribute("users");

if (users != null) {
for(User user : users){
%>

<tr>

    <td><%=user.getId()%></td>
    <td><%=user.getUsername()%></td>
    <td><%=user.getRoleName()%></td>
    <td><%=user.getStatus()%></td>
    <td><%= user.getCreatedAt() %></td>

<td>
    <a href="#" class="btn btn-warning btn-sm">Edit</a>

    <a href="#" class="btn btn-danger btn-sm">Disable</a>
</td>

</tr>

<%
  }
}
%>

        </tbody>

    </table>

</div>

</body>
</html>

<%@ include file="includes/footer.jsp" %>
