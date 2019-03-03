package com.filk.web.filter;

import com.filk.entity.Session;
import com.filk.entity.UserRole;
import com.filk.service.SecurityService;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class RoleFilter implements Filter {
    private SecurityService securityService;
    private UserRole userRole;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Session session = securityService.getValidSession(getToken(httpServletRequest.getCookies()));
        if(session != null && session.getUser().getUserRole() == userRole) {
            chain.doFilter(request, response);
        } else {
            httpServletResponse.sendRedirect("/login");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        //WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        WebApplicationContext webApplicationContext = (WebApplicationContext) servletContext.getAttribute("org.springframework.web.context.WebApplicationContext.ROOT");
        securityService = webApplicationContext.getBean(SecurityService.class);
    }

    @Override
    public void destroy() {
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    private String getToken(Cookie[] cookies) {
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if("user-token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
