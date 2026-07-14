package com.bank.service;

import com.bank.config.DBConnection;
import com.bank.dao.AccountDAO;
import com.bank.dao.TransactionDAO;

import java.sql.Connection;

public class TransferService {

    private final AccountDAO accountDAO =
            new AccountDAO();

    private final TransactionDAO transactionDAO =
            new TransactionDAO();

    public boolean transfer(
            int fromAccountId,
            int toAccountId,
            java.math.BigDecimal amount,
            String remarks) {

        Connection connection = null;

        try {

            connection = DBConnection.getConnection();

            connection.setAutoCommit(false);

            /*
             * Transfer Logic
             */

            connection.commit();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            try {

                if(connection != null){

                    connection.rollback();

                }

            } catch (Exception ignored){}

        } finally {

            try {

                if(connection != null){

                    connection.setAutoCommit(true);

                    connection.close();

                }

            } catch (Exception ignored){}

        }

        return false;

    }

}
