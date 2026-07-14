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

<li class="nav-item">
<a class="nav-link"
   href="${pageContext.request.contextPath}/dashboard">
Dashboard
</a>
</li>

<li class="nav-item">
<a class="nav-link"
   href="${pageContext.request.contextPath}/customers">
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
<a class="nav-link" href="#">
Transactions
</a>
</li>

<li class="nav-item">
<a class="nav-link" href="#">
Reports
</a>
</li>

<li class="nav-item">

<a
class="nav-link"
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

<li class="nav-item">
    <a class="nav-link"
       href="<%=request.getContextPath()%>/transactions">
        Transactions
    </a>
</li>

</ul>

</div>

</div>

</nav>
