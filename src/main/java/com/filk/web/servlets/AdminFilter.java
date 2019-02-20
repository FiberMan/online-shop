package com.filk.web.servlets;

import com.filk.entity.Session;
import com.filk.entity.UserRole;
import com.filk.service.SecurityService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminFilter implements Filter {
    SecurityService securityService;

    public AdminFilter(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Session session = securityService.getValidSession(httpServletRequest.getCookies());
        if(session != null && session.getUser().getUserRole() == UserRole.ADMIN) {
            chain.doFilter(request, response);
        } else {
            httpServletResponse.sendRedirect("/products");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
