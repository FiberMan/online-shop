package com.filk.web.servlets;

import com.filk.service.ProductService;
import com.filk.web.utils.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DeleteProductServlet extends HttpServlet {
    private ProductService productService;

    public DeleteProductServlet(ProductService productService) {
        this.productService = productService;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String productId = request.getParameter("product_id");

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("nav_state_products", "");
        pageVariables.put("nav_state_product_add", "");
        pageVariables.put("nav_state_login", "disabled");
        pageVariables.put("nav_state_logout", "");
        if(productService.deleteById(Integer.parseInt(productId))) {
            pageVariables.put("title", "Product deleted");
            pageVariables.put("message", "Product has been deleted. ID: " + productId);
        } else {
            pageVariables.put("title", "Failed to delete product");
            pageVariables.put("message", "Failed to delete product ID: " + productId);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("message.html", pageVariables));
    }
}
