package com.filk.web.servlets;

import com.filk.entity.Session;
import com.filk.entity.UserRole;
import com.filk.service.ProductService;
import com.filk.service.impl.DefaultSecurityService;
import com.filk.web.utils.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AllProductsServlet extends HttpServlet {
    private ProductService productService;
    private DefaultSecurityService defaultSecurityService;

    public AllProductsServlet(ProductService productService, DefaultSecurityService defaultSecurityService) {
        this.productService = productService;
        this.defaultSecurityService = defaultSecurityService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Session session = defaultSecurityService.getValidSession(request.getCookies());
        boolean isLoggedIn = session != null;
        boolean isUser = isLoggedIn && session.getUser().getUserRole() == UserRole.USER;
        boolean isAdmin = isLoggedIn && session.getUser().getUserRole() == UserRole.ADMIN;

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("title", "List of products");
        pageVariables.put("products", productService.getAll());
        pageVariables.put("nav_state_products", "active");
        pageVariables.put("nav_state_product_add", isUser || isAdmin? "" : "disabled");
        pageVariables.put("nav_state_users", isAdmin ? "" : "disabled");
        pageVariables.put("nav_state_login", isLoggedIn ? "disabled" : "");
        pageVariables.put("nav_state_logout", isLoggedIn ? "" : "disabled");
        pageVariables.put("can_edit", isUser || isAdmin);
        pageVariables.put("user_name", isLoggedIn ? session.getUser().getName() : "");

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("products.html", pageVariables));
    }
}
