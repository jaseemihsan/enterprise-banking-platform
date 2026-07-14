<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.bank.model.Account" %>

<%
List<Account> accounts =
        (List<Account>) request.getAttribute("accounts");
%>

<%@ include file="includes/header.jsp" %>
<%@ include file="includes/navbar.jsp" %>


<div class="container mt-4">

    <h2>Deposit Money</h2>

    <form action="<%=request.getContextPath()%>/deposit"
          method="post">

        <div class="mb-3">

            <label class="form-label">
                Account
            </label>

            <select class="form-select"
                    name="accountId"
                    required>

                <option value="">
                    Select Account
                </option>

                <%
                if(accounts != null){

                    for(Account account : accounts){
                %>

                <option value="<%=account.getId()%>">

                    <%=account.getAccountNumber()%>
                    -
                    <%=account.getCustomerName()%>

                </option>

                <%
                    }
                }
                %>

            </select>

        </div>

        <div class="mb-3">

            <label class="form-label">

                Deposit Amount

            </label>

            <input
                class="form-control"
                type="number"
                step="0.01"
                min="0.01"
                name="amount"
                required>

        </div>

        <button
            class="btn btn-success">

            Deposit

        </button>

    </form>

</div>

<%@ include file="includes/footer.jsp" %>
