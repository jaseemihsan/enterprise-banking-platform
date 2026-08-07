package com.bank.listener;

import com.bank.metrics.MetricsInitializer;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationStartupListener
        implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        MetricsInitializer.initialize();

        System.out.println("Business metrics initialized.");

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }

}
