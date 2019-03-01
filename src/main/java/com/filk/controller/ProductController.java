package com.filk.controller;

import com.filk.entity.Session;
import com.filk.entity.UserRole;
import com.filk.service.ProductService;
import com.filk.web.utils.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ProductController {
    private ProductService productService;
//    private SecurityService SecurityService;

    @Autowired
    public ProductController(ProductService productService/*, SecurityService SecurityService*/) {
        this.productService = productService;
//        this.SecurityService = SecurityService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/products")
    @ResponseBody
    public String getProducts(HttpServletResponse httpServletResponse) throws IOException {
//        Session session = defaultSecurityService.getValidSession(request.getCookies());
        boolean isLoggedIn = true; // session != null;
        boolean isUser = false; //isLoggedIn && session.getUser().getUserRole() == UserRole.USER;
        boolean isAdmin = true; //isLoggedIn && session.getUser().getUserRole() == UserRole.ADMIN;

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("title", "List of products");
        pageVariables.put("products", productService.getAll());
        pageVariables.put("nav_state_products", "active");
        pageVariables.put("nav_state_product_add", isUser || isAdmin? "" : "disabled");
        pageVariables.put("nav_state_users", isAdmin ? "" : "disabled");
        pageVariables.put("nav_state_login", isLoggedIn ? "disabled" : "");
        pageVariables.put("nav_state_logout", isLoggedIn ? "" : "disabled");
        pageVariables.put("can_edit", isUser || isAdmin);
        pageVariables.put("user_name", isLoggedIn ? /*session.getUser().getName()*/"Super Admin" : "");

//        response.setStatus(HttpServletResponse.SC_OK);
//        response.setContentType("text/html;charset=utf-8");
//        response.getWriter().println(PageGenerator.instance().getPage("products.html", pageVariables));

        return PageGenerator.instance().getPage("products.html", pageVariables);

    }
}
