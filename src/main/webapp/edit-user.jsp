<%@ page import="com.bank.model.User" %>

<%
User user = (User) request.getAttribute("user");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Edit User</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>

<div class="container mt-4">

<h2>Edit User</h2>

<form action="${pageContext.request.contextPath}/users/update" method="post">

<input type="hidden"
       name="id"
       value="<%=user.getId()%>">

<div class="mb-3">

<label>Username</label>

<input
class="form-control"
type="text"
name="username"
value="<%=user.getUsername()%>"
required>

</div>

<div class="mb-3">

<label>Role</label>

<select class="form-select" name="roleId">

<option value="1" <%=user.getRoleId()==1?"selected":""%>>ADMIN</option>

<option value="2" <%=user.getRoleId()==2?"selected":""%>>MANAGER</option>

<option value="3" <%=user.getRoleId()==3?"selected":""%>>CUSTOMER</option>

<option value="4" <%=user.getRoleId()==4?"selected":""%>>AUDITOR</option>

</select>

</div>

<div class="mb-3">

<label>Status</label>

<select class="form-select" name="status">

<option value="ACTIVE"
<%=user.getStatus().equals("ACTIVE")?"selected":""%>>

ACTIVE

</option>

<option value="INACTIVE"
<%=user.getStatus().equals("INACTIVE")?"selected":""%>>

INACTIVE

</option>

</select>

</div>

<button class="btn btn-success">

Update User

</button>

<a href="${pageContext.request.contextPath}/users"
class="btn btn-secondary">

Cancel

</a>

</form>

</div>

</body>
</html>
