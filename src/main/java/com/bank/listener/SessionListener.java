package com.bank.listener;

import com.bank.metrics.LoginMetrics;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {

        LoginMetrics.sessionCreated();

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {

        LoginMetrics.sessionDestroyed();

    }
}
