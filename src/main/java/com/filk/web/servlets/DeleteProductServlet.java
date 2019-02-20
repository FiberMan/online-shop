package com.filk.web.servlets;

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

public class DeleteProductServlet extends HttpServlet {
    private ProductService productService;
    private SecurityService securityService;

    public DeleteProductServlet(ProductService productService, SecurityService securityService) {
        this.productService = productService;
        this.securityService = securityService;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Session session = securityService.getValidSession(request.getCookies());
        boolean isLoggedIn = session != null;
        //boolean isUser = isLoggedIn && session.getUser().getUserRole() == UserRole.USER;
        boolean isAdmin = isLoggedIn && session.getUser().getUserRole() == UserRole.ADMIN;

        String productId = request.getParameter("product_id");

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("nav_state_products", "");
        pageVariables.put("nav_state_product_add", "");
        pageVariables.put("nav_state_users", isAdmin ? "" : "disabled");
        pageVariables.put("nav_state_login", isLoggedIn ? "disabled" : "");
        pageVariables.put("nav_state_logout", isLoggedIn ? "" : "disabled");
        pageVariables.put("user_name", isLoggedIn ? session.getUser().getName() : "");
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
