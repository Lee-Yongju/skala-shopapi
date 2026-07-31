package com.sk.skala.shopapi.common;

import com.sk.skala.shopapi.data.dto.CustomerSession;
import com.sk.skala.shopapi.exception.Error;
import com.sk.skala.shopapi.exception.ResponseException;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;

@Component
public class SessionHandler {
    private static final String SESSION_KEY = "CUSTOMER_SESSION";

    public void login(HttpSession session, String customerId) {
        session.setAttribute(SESSION_KEY, new CustomerSession(customerId));
    }

    public void logout(HttpSession session) {
        session.removeAttribute(SESSION_KEY);
    }

    public CustomerSession getSession(HttpSession session) {
        CustomerSession customerSession = (CustomerSession) session.getAttribute(SESSION_KEY);
        if (customerSession == null) {
            throw new ResponseException(Error.UNAUTHORIZED);
        }
        return customerSession;
    }
}
