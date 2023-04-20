package com.behrend.contestmanager.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

@Service
public class CookieServices {
    private static final int MAX_AGE = 72000;

    public void setCookie(HttpServletResponse response, String name, String value){

    }
}
