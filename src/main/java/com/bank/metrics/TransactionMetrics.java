package com.bank.metrics;

import io.micrometer.core.instrument.Counter;

public class TransactionMetrics {

    private static final Counter deposits =
            Counter.builder("banking_deposits_total")
                    .description("Total deposits")
                    .register(MetricsRegistry.getRegistry());

    private static final Counter withdrawals =
            Counter.builder("banking_withdrawals_total")
                    .description("Total withdrawals")
                    .register(MetricsRegistry.getRegistry());

    private static final Counter transfers =
            Counter.builder("banking_transfers_total")
                    .description("Total fund transfers")
                    .register(MetricsRegistry.getRegistry());

    public static void incrementDeposits() {
        deposits.increment();
    }

    public static void incrementWithdrawals() {
        withdrawals.increment();
    }

    public static void incrementTransfers() {
        transfers.increment();
    }
}
