<%
String username=(String)session.getAttribute("username");

String role=(String)session.getAttribute("role");
%>


<nav class="navbar navbar-expand-lg navbar-dark bg-primary">

<div class="container-fluid">

<a class="navbar-brand"
   href="${pageContext.request.contextPath}/dashboard">

<strong>ABC Bank</strong>

</a>

<button class="navbar-toggler"
        type="button"
        data-bs-toggle="collapse"
        data-bs-target="#navbarNav">

<span class="navbar-toggler-icon"></span>

</button>

<div class="collapse navbar-collapse"
     id="navbarNav">

<ul class="navbar-nav">

    <!-- Everyone -->
    <li class="nav-item">
        <a class="nav-link"
           href="<%=request.getContextPath()%>/dashboard">
            Dashboard
        </a>
    </li>

    <!-- ADMIN & MANAGER -->
    <% if ("ADMIN".equals(role) || "MANAGER".equals(role)) { %>

    <li class="nav-item">
        <a class="nav-link"
           href="<%=request.getContextPath()%>/customers">
            Customers
        </a>
    </li>

    <li class="nav-item">
        <a class="nav-link"
           href="<%=request.getContextPath()%>/accounts">
            Accounts
        </a>
    </li>

    <li class="nav-item">
        <a class="nav-link"
           href="<%=request.getContextPath()%>/transactions">
            Transactions
        </a>
    </li>

    <li class="nav-item">
        <a class="nav-link"
           href="<%=request.getContextPath()%>/deposit">
            Deposit
        </a>
    </li>

    <li class="nav-item">
        <a class="nav-link"
           href="<%=request.getContextPath()%>/withdraw">
            Withdraw
        </a>
    </li>

    <% } %>

    <!-- ADMIN ONLY -->
    <% if ("ADMIN".equals(role)) { %>

    <li class="nav-item">
        <a class="nav-link"
           href="<%=request.getContextPath()%>/users">
            User Management
        </a>
    </li>

    <li class="nav-item">
        <a class="nav-link"
           href="<%=request.getContextPath()%>/reports">
            Reports
        </a>
    </li>

    <% } %>

</ul>

 <div class="ms-auto text-white">

        Welcome,
        <strong><%=username%></strong>
        (<%=role%>)

        <a class="btn btn-light btn-sm ms-3"
           href="<%=request.getContextPath()%>/logout">
            Logout
        </a>

    </div>

</div>

</div>

</nav>
