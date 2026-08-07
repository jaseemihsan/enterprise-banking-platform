package com.bank.metrics;

public class MetricsInitializer {

    static {

        // Register all business metrics

        new CustomerMetrics();

        // Later we'll add:
        // new AccountMetrics();
        // new TransactionMetrics();
        // new LoginMetrics();
	
	new AccountMetrics();

        new TransactionMetrics();


    }

    public static void initialize() {
        // Trigger static initialization
    }
}
