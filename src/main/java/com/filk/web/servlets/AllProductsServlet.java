package com.filk.web.servlets;

import com.filk.entity.Session;
import com.filk.service.ProductService;
import com.filk.service.SecurityService;
import com.filk.service.impl.DefaultProductService;
import com.filk.web.utils.PageGenerator;
import org.eclipse.jetty.servlet.Source;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AllProductsServlet extends HttpServlet {
    private ProductService productService;
    private SecurityService securityService;

    public AllProductsServlet(ProductService productService, SecurityService securityService) {
        this.productService = productService;
        this.securityService = securityService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Session session = securityService.getValidSession(request.getCookies());
        boolean isLoggedin = session != null;
        boolean isAdmin = session != null; // TODO: check user

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("title", "List of products");
        pageVariables.put("products", productService.getAll());
        pageVariables.put("nav_state_products", "active");
        pageVariables.put("nav_state_product_add", isAdmin ? "" : "disabled");
        pageVariables.put("nav_state_login", isLoggedin ? "disabled" : "");
        pageVariables.put("nav_state_logout", isLoggedin ? "" : "disabled");
        pageVariables.put("is_admin", isAdmin);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("products.html", pageVariables));
    }
}
