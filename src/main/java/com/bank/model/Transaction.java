package com.bank.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {

    private int id;
    private int accountId;
    private String accountNumber;
    private String transactionType;
    private BigDecimal amount;
    private String referenceNo;
    private String remarks;
    private Timestamp transactionTime;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;

    public Transaction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
    return accountNumber;
}

public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
}

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Timestamp getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Timestamp transactionTime) {
        this.transactionTime = transactionTime;
    }

    public BigDecimal getBalanceBefore() {
    return balanceBefore;
}

public void setBalanceBefore(BigDecimal balanceBefore) {
    this.balanceBefore = balanceBefore;
}

public BigDecimal getBalanceAfter() {
    return balanceAfter;
}

public void setBalanceAfter(BigDecimal balanceAfter) {
    this.balanceAfter = balanceAfter;
}

}
