<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.bank.model.User" %>

<%
User user = (User) request.getAttribute("user");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Reset Password</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>

<div class="container mt-5">

    <h3>Reset Password</h3>

    <form method="post"
          action="${pageContext.request.contextPath}/users/reset-password">

        <input type="hidden"
               name="id"
               value="<%=user.getId()%>">

        <div class="mb-3">

            <label>Username</label>

            <input
                    class="form-control"
                    value="<%=user.getUsername()%>"
                    readonly>

        </div>

        <div class="mb-3">

            <label>New Password</label>

            <input
                    type="password"
                    name="password"
                    class="form-control"
                    required>

        </div>

        <button class="btn btn-primary">
            Reset Password
        </button>

    </form>

</div>

</body>
</html>
