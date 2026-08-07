package com.bank.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;

import java.util.concurrent.atomic.AtomicInteger;

public class LoginMetrics {

    private static final Counter logins =
            Counter.builder("banking_logins_total")
                    .description("Total successful logins")
                    .register(MetricsRegistry.getRegistry());

    private static final Counter failedLogins =
            Counter.builder("banking_failed_logins_total")
                    .description("Total failed logins")
                    .register(MetricsRegistry.getRegistry());

    private static final AtomicInteger activeSessions =
            new AtomicInteger(0);

    static {

        Gauge.builder(
                "banking_active_sessions",
                activeSessions,
                AtomicInteger::get)
                .description("Current active sessions")
                .register(MetricsRegistry.getRegistry());

    }

    public static void incrementLogins() {
        logins.increment();
    }

    public static void incrementFailedLogins() {
        failedLogins.increment();
    }

    public static void sessionCreated() {
        activeSessions.incrementAndGet();
    }

    public static void sessionDestroyed() {
        activeSessions.decrementAndGet();
    }
}
