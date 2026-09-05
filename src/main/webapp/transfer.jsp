<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.bank.model.Account" %>

<%
List<Account> accounts =
        (List<Account>) request.getAttribute("accounts");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Fund Transfer</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
          rel="stylesheet">
</head>

<body>

<div class="container mt-5">

    <h3>Fund Transfer</h3>

    <form method="post"
          action="${pageContext.request.contextPath}/transfer">

        <div class="mb-3">

            <label>From Account</label>

            <select name="fromAccountId"
                    class="form-select"
                    required>

                <% for(Account account : accounts){ %>

                    <option value="<%=account.getId()%>">
                        <%=account.getAccountNumber()%>
                        -
                        Balance:
                        <%=account.getBalance()%>
                    </option>

                <% } %>

            </select>

        </div>

        <div class="mb-3">

            <label>To Account</label>

            <select name="toAccountId"
                    class="form-select"
                    required>

                <% for(Account account : accounts){ %>

                    <option value="<%=account.getId()%>">
                        <%=account.getAccountNumber()%>
                    </option>

                <% } %>

            </select>

        </div>

        <div class="mb-3">

            <label>Amount</label>

            <input type="number"
                   step="0.01"
                   min="1"
                   name="amount"
                   class="form-control"
                   required>

        </div>

        <div class="mb-3">

            <label>Remarks</label>

            <input type="text"
                   name="remarks"
                   class="form-control">

        </div>

        <button class="btn btn-success">
            Transfer
        </button>

    </form>

</div>

</body>
</html>
