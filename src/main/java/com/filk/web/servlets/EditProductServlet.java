package com.filk.web.servlets;

import com.filk.entity.Product;
import com.filk.entity.Session;
import com.filk.entity.UserRole;
import com.filk.service.ProductService;
import com.filk.service.SecurityService;
import com.filk.web.utils.PageGenerator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditProductServlet extends HttpServlet {
    private ProductService productService;
    private SecurityService securityService;

    public EditProductServlet(ProductService productService, SecurityService securityService) {
        this.productService = productService;
        this.securityService = securityService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Session session = securityService.getValidSession(request.getCookies());
        boolean isLoggedIn = session != null;
        //boolean isUser = isLoggedIn && session.getUser().getUserRole() == UserRole.USER;
        boolean isAdmin = isLoggedIn && session.getUser().getUserRole() == UserRole.ADMIN;

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
            pageVariables.put("price", String.valueOf(product.getPrice()));
            pageVariables.put("nav_state_products", "");
            pageVariables.put("nav_state_product_add", "");
            pageVariables.put("nav_state_users", isAdmin ? "" : "disabled");
            pageVariables.put("nav_state_login", isLoggedIn ? "disabled" : "");
            pageVariables.put("nav_state_logout", isLoggedIn ? "" : "disabled");
            pageVariables.put("can_edit", isAdmin);
            pageVariables.put("user_name", isLoggedIn ? session.getUser().getName() : "");

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
        Session session = securityService.getValidSession(request.getCookies());
        boolean isLoggedIn = session != null;
        //boolean isUser = isLoggedIn && session.getUser().getUserRole() == UserRole.USER;
        boolean isAdmin = isLoggedIn && session.getUser().getUserRole() == UserRole.ADMIN;

        Product product = new Product();
        product.setId(Integer.parseInt(request.getParameter("product_id")));
        product.setName(request.getParameter("name"));
        product.setDescription(request.getParameter("description"));
        product.setPrice(Double.parseDouble(request.getParameter("price")));

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("nav_state_products", "");
        pageVariables.put("nav_state_product_add", "");
        pageVariables.put("nav_state_users", isAdmin ? "" : "disabled");
        pageVariables.put("nav_state_login", isLoggedIn ? "disabled" : "");
        pageVariables.put("nav_state_logout", isLoggedIn ? "" : "disabled");
        pageVariables.put("user_name", isLoggedIn ? session.getUser().getName() : "");
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
