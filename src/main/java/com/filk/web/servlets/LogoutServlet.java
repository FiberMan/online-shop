package com.filk.web.servlets;

import com.filk.service.impl.DefaultSecurityService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    private DefaultSecurityService defaultSecurityService;

    public LogoutServlet(DefaultSecurityService defaultSecurityService) {
        this.defaultSecurityService = defaultSecurityService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.addCookie(defaultSecurityService.logout(request.getCookies()));
        response.sendRedirect("/login");
    }
}
