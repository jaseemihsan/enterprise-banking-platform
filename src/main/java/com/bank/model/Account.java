package com.bank.model;

import java.math.BigDecimal;

public class Account {

    private int id;
    private int customerId;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private String status;
    private String customerName;
    private String customerEmail;

    public Account() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerName() {
    return customerName;
    }

    public void setCustomerName(String customerName) {
         this.customerName = customerName;
    }

    public String getCustomerEmail() {
    return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
          this.customerEmail = customerEmail;
    } 
}
