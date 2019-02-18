package com.filk.web.servlets;

import com.filk.entity.Product;
import com.filk.service.ProductService;
import com.filk.web.utils.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProductServlet extends HttpServlet {
    private ProductService productService;

    public EditProductServlet(ProductService productService) {
        this.productService = productService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String productId = request.getParameter("product_id");
        Product product = productService.getById(Integer.parseInt(productId));
        Map<String, Object> pageVariables = new HashMap<>();
        String template;

        if(product != null) {
            pageVariables.put("title", "Edit product");
            pageVariables.put("action", "edit");
            pageVariables.put("button_value", "Edit");
            pageVariables.put("product_id", product.getId());
            pageVariables.put("name", product.getName());
            pageVariables.put("description", product.getDescription());
            pageVariables.put("price", product.getPrice());
            pageVariables.put("nav_state_products", "");
            pageVariables.put("nav_state_product_add", "");
            pageVariables.put("nav_state_login", "disabled");
            pageVariables.put("nav_state_logout", "");
            template = "product_form.html";
        } else {
            pageVariables.put("title", "No such product");
            pageVariables.put("message", "Can't find product with ID: " + productId);
            template = "message.html";
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage(template, pageVariables));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Product product = new Product();
        product.setId(Integer.parseInt(request.getParameter("product_id")));
        product.setName(request.getParameter("name"));
        product.setDescription(request.getParameter("description"));
        product.setPrice(Double.parseDouble(request.getParameter("price")));

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("nav_state_products", "");
        pageVariables.put("nav_state_product_add", "");
        pageVariables.put("nav_state_login", "disabled");
        pageVariables.put("nav_state_logout", "");
        if(productService.update(product)) {
            pageVariables.put("title", "Product updated");
            pageVariables.put("message", "Product has been updated: " + product.getName());
        } else {
            pageVariables.put("title", "Failed to update product");
            pageVariables.put("message", "Failed to update product ID: " + request.getParameter("product_id"));
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("message.html", pageVariables));
    }
}
