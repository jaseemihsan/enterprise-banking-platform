package com.bank.metrics;

import com.bank.dao.AccountDAO;
import io.micrometer.core.instrument.Gauge;

public class AccountMetrics {

    private static final AccountDAO accountDAO =
            new AccountDAO();

    static {

        Gauge.builder(
                "banking_accounts_total",
                accountDAO,
                dao -> dao.countAccounts())
                .description("Total number of accounts")
                .register(MetricsRegistry.getRegistry());

    }

}
