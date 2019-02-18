package com.filk.web.servlets;

import com.filk.entity.Session;
import com.filk.service.SecurityService;
import com.filk.web.utils.PageGenerator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {
    private SecurityService securityService;

    public LoginServlet(SecurityService securityService) {
        this.securityService = securityService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Session session = securityService.getValidSession(request.getCookies());

        // User already logged in
        if(session != null) {
            response.sendRedirect("/products");
        }

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("title", "Login page");
        pageVariables.put("nav_state_products", "");
        pageVariables.put("nav_state_product_add", "");
        pageVariables.put("nav_state_login", "active");
        pageVariables.put("nav_state_logout", "disabled");
        pageVariables.put("message", "");

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("login.html", pageVariables));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Session session = securityService.getValidSession(request.getCookies());

        // User already logged in
        if(session != null) {
            response.sendRedirect("/products");
        }

        session = securityService.login(request.getParameter("login"), request.getParameter("password"));
        if(session != null) {
            response.addCookie(session.getCookie());
            response.sendRedirect("/products");
        } else {
            Map<String, Object> pageVariables = new HashMap<>();
            pageVariables.put("title", "Product added");
            pageVariables.put("nav_state_products", "");
            pageVariables.put("nav_state_product_add", "");
            pageVariables.put("nav_state_login", "active");
            pageVariables.put("nav_state_logout", "disabled");
            pageVariables.put("message", "Wrong User or Password.");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().println(PageGenerator.instance().getPage("login.html", pageVariables));
        }
    }


}
