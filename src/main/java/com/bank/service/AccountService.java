package com.bank.service;

import com.bank.dao.AccountDAO;
import com.bank.model.Account;

import java.util.List;

public class AccountService {

    private final AccountDAO accountDAO = new AccountDAO();

    /*
     * Create Account
     */
    public boolean createAccount(Account account) {

        return accountDAO.saveAccount(account);

    }

    /*
     * List Accounts
     */
    public List<Account> getAllAccounts() {

        return accountDAO.getAllAccounts();

    }

    /*
     * Search Accounts
     */
    public List<Account> searchAccounts(String keyword) {

        return accountDAO.searchAccounts(keyword);

    }

    /*
     * Get Account By ID
     */
    public Account getAccountById(int id) {

        return accountDAO.getAccountById(id);

    }

    /*
     * Update Account
     */
    public boolean updateAccount(Account account) {

        return accountDAO.updateAccount(account);

    }

    /*
     * Close Account
     */
    public boolean closeAccount(int id) {

        return accountDAO.closeAccount(id);

    }

    /*
     * Active Account
     */
    public List<Account> getActiveAccounts() {

        return accountDAO.getActiveAccounts();

    }
    
}
