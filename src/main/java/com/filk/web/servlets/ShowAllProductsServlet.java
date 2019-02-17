package com.filk.web.servlets;

import com.filk.service.ProductService;
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

public class ShowAllProductsServlet extends HttpServlet {
    private ProductService productService;

    public ShowAllProductsServlet(ProductService productService) {
        this.productService = productService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("title", "List of products");
        pageVariables.put("products", productService.getAll());
        pageVariables.put("nav_state_products", "active");
        pageVariables.put("nav_state_product_add", "");
        pageVariables.put("nav_state_login", "");
        pageVariables.put("nav_state_logout", "disabled");

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("products.html", pageVariables));
    }
}
