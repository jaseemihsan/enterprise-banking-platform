package com.bank.metrics;

import com.bank.dao.CustomerDAO;
import io.micrometer.core.instrument.Gauge;

public class CustomerMetrics {

    private static final CustomerDAO customerDAO =
            new CustomerDAO();

    static {

        Gauge.builder(
                "banking_customers_total",
                customerDAO,
                dao -> dao.countCustomers())
                .description("Total number of customers")
                .register(MetricsRegistry.getRegistry());

    }

}
