package com.filk.web.controller;

import com.filk.entity.Product;
import com.filk.entity.Session;
import com.filk.entity.UserRole;
import com.filk.service.ProductService;
import com.filk.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProductController {
    private ProductService productService;
    private SecurityService securityService;

    @Autowired
    public ProductController(ProductService productService, SecurityService SecurityService) {
        this.productService = productService;
        this.securityService = SecurityService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/products")
    public String getProducts(@CookieValue(name = "user-token", required = false) String token,
                              Model model) {
        Session session = securityService.getValidSession(token);
        boolean isLoggedIn = session != null;
        boolean isUser = isLoggedIn && session.getUser().getUserRole() == UserRole.USER;
        boolean isAdmin = isLoggedIn && session.getUser().getUserRole() == UserRole.ADMIN;
        
        model.addAttribute("title", "List of products");
        model.addAttribute("products", productService.getAll());
        model.addAttribute("nav_state_products", "active");
        model.addAttribute("nav_state_product_add", isAdmin? "" : "disabled");
        model.addAttribute("nav_state_users", isAdmin ? "" : "disabled");
        model.addAttribute("nav_state_cart", isLoggedIn ? "" : "disabled");
        model.addAttribute("nav_state_login", isLoggedIn ? "disabled" : "");
        model.addAttribute("nav_state_logout", isLoggedIn ? "" : "disabled");
        model.addAttribute("can_edit", isAdmin);
        model.addAttribute("can_add_to_cart", isUser || isAdmin);
        model.addAttribute("show_action_column", isUser || isAdmin);
        model.addAttribute("user_name", isLoggedIn ? session.getUser().getName() : "");

        return "products";
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/product/edit")
    public String editProductGet(@RequestParam(name = "product_id") String productId,
                                 @CookieValue(name = "user-token", required = false) String token,
                                 Model model) {
        Session session = securityService.getValidSession(token);
        boolean isLoggedIn = session != null;
//        boolean isUser = isLoggedIn && session.getUser().getUserRole() == UserRole.USER;
        boolean isAdmin = isLoggedIn && session.getUser().getUserRole() == UserRole.ADMIN;
        
        Product product = productService.getById(Integer.parseInt(productId));
        String template;

        model.addAttribute("nav_state_products", "");
        model.addAttribute("nav_state_product_add", "");
        model.addAttribute("nav_state_users", isAdmin ? "" : "disabled");
        model.addAttribute("nav_state_cart",isLoggedIn ? "" : "disabled");
        model.addAttribute("nav_state_login", isLoggedIn ? "disabled" : "");
        model.addAttribute("nav_state_logout", isLoggedIn ? "" : "disabled");
        model.addAttribute("user_name", isLoggedIn ? session.getUser().getName() : "");

        if(product != null) {
            model.addAttribute("title", "Edit product");
            model.addAttribute("action", "edit");
            model.addAttribute("button_value", "Edit");
            model.addAttribute("product_id", product.getId());
            model.addAttribute("name", product.getName());
            model.addAttribute("description", product.getDescription());
            model.addAttribute("price", product.getPrice());
            model.addAttribute("can_edit", isAdmin);

            template = "product_form";
        } else {
            model.addAttribute("title", "No such product");
            model.addAttribute("message", "Can't find product with ID: " + productId);
            template = "message";
        }

        return template;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/product/edit", produces = "text/plain;charset=UTF-8")
    public String editProductPost(@RequestParam(name = "product_id") String productId,
                                  @RequestParam(name = "name") String name,
                                  @RequestParam(name = "description") String description,
                                  @RequestParam(name = "price") String price,
                                  @CookieValue(name = "user-token", required = false) String token,
                                  Model model,
                                  HttpServletRequest httpServletRequest) {
        Session session = securityService.getValidSession(token);
        boolean isLoggedIn = session != null;
        //boolean isUser = isLoggedIn && session.getUser().getUserRole() == UserRole.USER;
        boolean isAdmin = isLoggedIn && session.getUser().getUserRole() == UserRole.ADMIN;

        Product product = new Product();
        product.setId(Integer.parseInt(productId));
        product.setName(name);
        product.setDescription(description);
        product.setPrice(Double.parseDouble(price));
        
        
        model.addAttribute("nav_state_products", "");
        model.addAttribute("nav_state_product_add", "");
        model.addAttribute("nav_state_users", isAdmin ? "" : "disabled");
        model.addAttribute("nav_state_cart", isLoggedIn ? "" : "disabled");
        model.addAttribute("nav_state_login", isLoggedIn ? "disabled" : "");
        model.addAttribute("nav_state_logout", isLoggedIn ? "" : "disabled");
        model.addAttribute("user_name", isLoggedIn ? session.getUser().getName() : "");
        if(productService.update(product)) {
            model.addAttribute("title", "Product updated");
            model.addAttribute("message", "Product has been updated: " + product.getName());
        } else {
            model.addAttribute("title", "Failed to update product");
            model.addAttribute("message", "Failed to update product ID: " + productId);
        }

        return "message";
    }
    
    @RequestMapping(method = RequestMethod.GET, path = "/product/add")
    public String addProductGet(@CookieValue(name = "user-token", required = false) String token,
                                Model model) {
        Session session = securityService.getValidSession(token);
        boolean isLoggedIn = session != null;
        boolean isUser = isLoggedIn && session.getUser().getUserRole() == UserRole.USER;
        boolean isAdmin = isLoggedIn && session.getUser().getUserRole() == UserRole.ADMIN;

        model.addAttribute("title", "Add new product");
        model.addAttribute("action", "add");
        model.addAttribute("button_value", "Add");
        model.addAttribute("product_id", "");
        model.addAttribute("name", "");
        model.addAttribute("description", "");
        model.addAttribute("price", 0);
        model.addAttribute("nav_state_products", "");
        model.addAttribute("nav_state_product_add", "active");
        model.addAttribute("nav_state_users", isAdmin ? "" : "disabled");
        model.addAttribute("nav_state_cart", isLoggedIn ? "" : "disabled");
        model.addAttribute("nav_state_login", isLoggedIn ? "disabled" : "");
        model.addAttribute("nav_state_logout", isLoggedIn ? "" : "disabled");
        model.addAttribute("can_edit", isAdmin);
        model.addAttribute("user_name", isLoggedIn ? session.getUser().getName() : "");
        
        return "product_form";
    }
    
    @RequestMapping(method = RequestMethod.POST, path = "/product/add")
    public String addProductPost(@RequestParam(name = "name") String name,
                                 @RequestParam(name = "description") String description,
                                 @RequestParam(name = "price") String price,
                                 @CookieValue(name = "user-token", required = false) String token,
                                 Model model) {
        Session session = securityService.getValidSession(token);
        boolean isLoggedIn = session != null;
        //boolean isUser = isLoggedIn && session.getUser().getUserRole() == UserRole.USER;
        boolean isAdmin = isLoggedIn && session.getUser().getUserRole() == UserRole.ADMIN;

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(Double.parseDouble(price));

        model.addAttribute("nav_state_products", "");
        model.addAttribute("nav_state_product_add", "active");
        model.addAttribute("nav_state_users", isAdmin ? "" : "disabled");
        model.addAttribute("nav_state_cart", isLoggedIn ? "" : "disabled");
        model.addAttribute("nav_state_login", isLoggedIn ? "disabled" : "");
        model.addAttribute("nav_state_logout", isLoggedIn ? "" : "disabled");
        model.addAttribute("user_name", isLoggedIn ? session.getUser().getName() : "");
        if(productService.insert(product)) {
            model.addAttribute("title", "Product added");
            model.addAttribute("message", "New product has been added: " + product.getName());
        } else {
            model.addAttribute("title", "Failed to add product");
            model.addAttribute("message", "Failed to add new product: " + product.getName());
        }
        
        return "message";
    }
    
    @RequestMapping(method = RequestMethod.POST, path = "/product/delete")
    public String deleteProduct(@RequestParam(name = "product_id") String productId,
                                @CookieValue(name = "user-token", required = false) String token,
                                Model model) {
        Session session = securityService.getValidSession(token);
        boolean isLoggedIn = session != null;
        //boolean isUser = isLoggedIn && session.getUser().getUserRole() == UserRole.USER;
        boolean isAdmin = isLoggedIn && session.getUser().getUserRole() == UserRole.ADMIN;

        model.addAttribute("nav_state_products", "");
        model.addAttribute("nav_state_product_add", "");
        model.addAttribute("nav_state_users", isAdmin ? "" : "disabled");
        model.addAttribute("nav_state_cart", isLoggedIn ? "" : "disabled");
        model.addAttribute("nav_state_login", isLoggedIn ? "disabled" : "");
        model.addAttribute("nav_state_logout", isLoggedIn ? "" : "disabled");
        model.addAttribute("user_name", isLoggedIn ? session.getUser().getName() : "");
        if(productService.deleteById(Integer.parseInt(productId))) {
            model.addAttribute("title", "Product deleted");
            model.addAttribute("message", "Product has been deleted. ID: " + productId);
        } else {
            model.addAttribute("title", "Failed to delete product");
            model.addAttribute("message", "Failed to delete product ID: " + productId);
        }

        return "message";
    }
}
