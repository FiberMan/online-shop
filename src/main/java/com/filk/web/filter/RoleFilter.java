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
import java.util.EnumSet;

public abstract class RoleFilter implements Filter {
    private SecurityService securityService;
    private EnumSet<UserRole> userRoles = EnumSet.noneOf(UserRole.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        Session session = securityService.getValidSession(getToken(httpServletRequest.getCookies()));
        if (session != null && userRoles.contains(session.getUser().getUserRole())) {
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

    public void setUserRole(EnumSet<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    private String getToken(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("user-token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
