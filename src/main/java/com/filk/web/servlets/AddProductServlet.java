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

public class AddProductServlet extends HttpServlet {
    private ProductService productService;
    private SecurityService securityService;

    public AddProductServlet(ProductService productService, SecurityService securityService) {
        this.productService = productService;
        this.securityService = securityService;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Session session = securityService.getValidSession(request.getCookies());
        boolean isLoggedIn = session != null;
        boolean isUser = isLoggedIn && session.getUser().getUserRole() == UserRole.USER;
        boolean isAdmin = isLoggedIn && session.getUser().getUserRole() == UserRole.ADMIN;

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("title", "Add new product");
        pageVariables.put("action", "add");
        pageVariables.put("button_value", "Add");
        pageVariables.put("product_id", "");
        pageVariables.put("name", "");
        pageVariables.put("description", "");
        pageVariables.put("price", 0);
        pageVariables.put("nav_state_products", "");
        pageVariables.put("nav_state_product_add", "active");
        pageVariables.put("nav_state_users", isAdmin ? "" : "disabled");
        pageVariables.put("nav_state_login", isLoggedIn ? "disabled" : "");
        pageVariables.put("nav_state_logout", isLoggedIn ? "" : "disabled");
        pageVariables.put("can_edit", isAdmin);
        pageVariables.put("user_name", isLoggedIn ? session.getUser().getName() : "");

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("product_form.html", pageVariables));
    }

    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        Session session = securityService.getValidSession(request.getCookies());
        boolean isLoggedIn = session != null;
        //boolean isUser = isLoggedIn && session.getUser().getUserRole() == UserRole.USER;
        boolean isAdmin = isLoggedIn && session.getUser().getUserRole() == UserRole.ADMIN;

        Product product = new Product();
        product.setName(request.getParameter("name"));
        product.setDescription(request.getParameter("description"));
        product.setPrice(Double.parseDouble(request.getParameter("price")));

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("nav_state_products", "");
        pageVariables.put("nav_state_product_add", "active");
        pageVariables.put("nav_state_users", isAdmin ? "" : "disabled");
        pageVariables.put("nav_state_login", isLoggedIn ? "disabled" : "");
        pageVariables.put("nav_state_logout", isLoggedIn ? "" : "disabled");
        pageVariables.put("user_name", isLoggedIn ? session.getUser().getName() : "");
        if(productService.insert(product)) {
            pageVariables.put("title", "Product added");
            pageVariables.put("message", "New product has been added: " + product.getName());
        } else {
            pageVariables.put("title", "Failed to add product");
            pageVariables.put("message", "Failed to add new product: " + product.getName());
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().println(PageGenerator.instance().getPage("message.html", pageVariables));
    }
}
