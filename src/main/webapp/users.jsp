<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.bank.model.User" %>

<%@ include file="includes/header.jsp" %>
<%@ include file="includes/navbar.jsp" %>

<!DOCTYPE html>
<html lang="en">
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

<caption>Users List</caption>


        <thead class="table-dark">

        <tr>
            <th scope="col">ID</th>
            <th scope="col">Username</th>
            <th scope="col">Role</th>
            <th scope="col">Status</th>
	    <th scope="col">Created</th>
            <th scope="col">Actions</th>
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

    <a href="${pageContext.request.contextPath}/users/edit?id=<%=user.getId()%>"
       class="btn btn-warning btn-sm">
        Edit
    </a>

    <a href="${pageContext.request.contextPath}/users/reset?id=<%=user.getId()%>"
   class="btn btn-secondary btn-sm">
    Reset Password
</a>

    <% if ("ACTIVE".equals(user.getStatus())) { %>

        <a href="${pageContext.request.contextPath}/users/status?id=<%=user.getId()%>&status=INACTIVE"
           class="btn btn-danger btn-sm">
            Disable
        </a>

    <% } else { %>

        <a href="${pageContext.request.contextPath}/users/status?id=<%=user.getId()%>&status=ACTIVE"
           class="btn btn-success btn-sm">
            Activate
        </a>

    <% } %>

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
