<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>403 - Access Denied</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container text-center mt-5">

    <h1 class="display-1 text-danger">403</h1>

    <h3>Access Denied</h3>

    <p>You don't have permission to access this page.</p>

    <a href="<%=request.getContextPath()%>/dashboard"
       class="btn btn-primary">
        Back to Dashboard
    </a>

</div>

</body>
</html>
