package com.bank.service;

import com.bank.dao.AccountDAO;
import com.bank.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountDAO accountDAO;

    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {

        accountService = new AccountService(accountDAO);

        account = new Account();

        account.setId(1);
        account.setCustomerId(1);
        account.setAccountNumber("ACC10001");
        account.setAccountType("SAVINGS");
        account.setStatus("ACTIVE");
    }

    @Test
    void createAccount_shouldReturnTrue_whenDaoSucceeds() {

        when(accountDAO.saveAccount(account))
                .thenReturn(true);

        boolean result =
                accountService.createAccount(account);

        assertTrue(result);

        verify(accountDAO)
                .saveAccount(account);
    }

    @Test
    void createAccount_shouldReturnFalse_whenDaoFails() {

        when(accountDAO.saveAccount(account))
                .thenReturn(false);

        boolean result =
                accountService.createAccount(account);

        assertFalse(result);

        verify(accountDAO)
                .saveAccount(account);
    }

    @Test
    void getAllAccounts_shouldReturnAccountsFromDao() {

        List<Account> accounts =
                Arrays.asList(account);

        when(accountDAO.getAllAccounts())
                .thenReturn(accounts);

        List<Account> result =
                accountService.getAllAccounts();

        assertEquals(accounts, result);

        verify(accountDAO)
                .getAllAccounts();
    }

    @Test
    void getAllAccounts_shouldReturnEmptyList_whenDaoReturnsEmpty() {

        when(accountDAO.getAllAccounts())
                .thenReturn(Collections.emptyList());

        List<Account> result =
                accountService.getAllAccounts();

        assertTrue(result.isEmpty());

        verify(accountDAO)
                .getAllAccounts();
    }

    @Test
    void searchAccounts_shouldReturnMatchingAccounts() {

        List<Account> accounts =
                Arrays.asList(account);

        when(accountDAO.searchAccounts("ACC10001"))
                .thenReturn(accounts);

        List<Account> result =
                accountService.searchAccounts("ACC10001");

        assertEquals(1, result.size());
        assertEquals("ACC10001",
                result.get(0).getAccountNumber());

        verify(accountDAO)
                .searchAccounts("ACC10001");
    }

    @Test
    void getAccountById_shouldReturnAccount() {

        when(accountDAO.getAccountById(1))
                .thenReturn(account);

        Account result =
                accountService.getAccountById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("ACC10001",
                result.getAccountNumber());

        verify(accountDAO)
                .getAccountById(1);
    }

    @Test
    void getAccountById_shouldReturnNull_whenAccountDoesNotExist() {

        when(accountDAO.getAccountById(999))
                .thenReturn(null);

        Account result =
                accountService.getAccountById(999);

        assertNull(result);

        verify(accountDAO)
                .getAccountById(999);
    }

    @Test
    void updateAccount_shouldReturnTrue_whenDaoSucceeds() {

        when(accountDAO.updateAccount(account))
                .thenReturn(true);

        boolean result =
                accountService.updateAccount(account);

        assertTrue(result);

        verify(accountDAO)
                .updateAccount(account);
    }

    @Test
    void updateAccount_shouldReturnFalse_whenDaoFails() {

        when(accountDAO.updateAccount(account))
                .thenReturn(false);

        boolean result =
                accountService.updateAccount(account);

        assertFalse(result);

        verify(accountDAO)
                .updateAccount(account);
    }

    @Test
    void closeAccount_shouldReturnTrue_whenDaoSucceeds() {

        when(accountDAO.closeAccount(1))
                .thenReturn(true);

        boolean result =
                accountService.closeAccount(1);

        assertTrue(result);

        verify(accountDAO)
                .closeAccount(1);
    }

    @Test
    void getActiveAccounts_shouldReturnActiveAccounts() {

        List<Account> accounts =
                Arrays.asList(account);

        when(accountDAO.getActiveAccounts())
                .thenReturn(accounts);

        List<Account> result =
                accountService.getActiveAccounts();

        assertEquals(1, result.size());
        assertEquals("ACTIVE",
                result.get(0).getStatus());

        verify(accountDAO)
                .getActiveAccounts();
    }
}
