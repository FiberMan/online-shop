package com.filk.web.servlets;

import com.filk.entity.Product;
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

public class AddProductServlet extends HttpServlet {
    private ProductService productService;

    public AddProductServlet(ProductService productService) {
        this.productService = productService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("title", "Add new product");
        pageVariables.put("action", "add");
        pageVariables.put("button_value", "Add");
        pageVariables.put("product_id", "");
        pageVariables.put("name", "");
        pageVariables.put("description", "");
        pageVariables.put("price", "");
        pageVariables.put("nav_state_products", "");
        pageVariables.put("nav_state_product_add", "active");
        pageVariables.put("nav_state_login", "");
        pageVariables.put("nav_state_logout", "disabled");

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("product_form.html", pageVariables));
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        Product product = new Product();
        product.setName(request.getParameter("name"));
        product.setDescription(request.getParameter("description"));
        product.setPrice(Double.parseDouble(request.getParameter("price")));

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("nav_state_products", "");
        pageVariables.put("nav_state_product_add", "active");
        pageVariables.put("nav_state_login", "");
        pageVariables.put("nav_state_logout", "disabled");
        if(productService.insert(product)) {
            pageVariables.put("title", "User added");
            pageVariables.put("update_resolution", "New user has been added: " + product.getName());
        } else {
            pageVariables.put("title", "Failed to add user");
            pageVariables.put("update_resolution", "Failed to add new user: " + product.getName());
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("product_updated.html", pageVariables));

    }
}
