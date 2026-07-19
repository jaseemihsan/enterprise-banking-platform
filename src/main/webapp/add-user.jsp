<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Add User</title>

    <style>
        body{
            font-family: Arial, sans-serif;
            background:#f4f4f4;
        }

        .container{
            width:500px;
            margin:40px auto;
            background:white;
            padding:25px;
            border-radius:8px;
            box-shadow:0 0 10px rgba(0,0,0,.15);
        }

        h2{
            text-align:center;
        }

        label{
            display:block;
            margin-top:15px;
            font-weight:bold;
        }

        input,select{
            width:100%;
            padding:10px;
            margin-top:5px;
        }

        .btn{
            margin-top:20px;
            width:100%;
            padding:12px;
            background:#0d6efd;
            color:white;
            border:none;
            cursor:pointer;
        }

        .btn:hover{
            background:#0b5ed7;
        }

        .error{
            color:red;
            text-align:center;
        }
    </style>

</head>

<body>

<div class="container">

    <h2>Add User</h2>

    <%
        String error=(String)request.getAttribute("error");

        if(error!=null){
    %>

        <p class="error"><%=error%></p>

    <%
        }
    %>

    <form action="${pageContext.request.contextPath}/users/add" method="post">

        <label>Username</label>
        <input
                type="text"
                name="username"
                required>

        <label>Password</label>
        <input
                type="password"
                name="password"
                required>

        <label>Role</label>

        <select name="roleId">

            <option value="1">ADMIN</option>
            <option value="2">MANAGER</option>
            <option value="3">CUSTOMER</option>
            <option value="4">AUDITOR</option>

        </select>

        <label>Status</label>

        <select name="status">

            <option value="ACTIVE">ACTIVE</option>
            <option value="INACTIVE">INACTIVE</option>

        </select>

        <button class="btn" type="submit">

            Save User

        </button>

    </form>

</div>

</body>
</html>
