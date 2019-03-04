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

@Controller
public class CartController {
    private ProductService productService;
    private SecurityService securityService;

    @Autowired
    public CartController(ProductService productService, SecurityService securityService) {
        this.productService = productService;
        this.securityService = securityService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/cart")
    public String showCart(@CookieValue(name = "user-token", required = false) String token,
                           Model model) {
        Session session = securityService.getValidSession(token);
        boolean isLoggedIn = session != null;
        boolean isUser = isLoggedIn && session.getUser().getUserRole() == UserRole.USER;
        boolean isAdmin = isLoggedIn && session.getUser().getUserRole() == UserRole.ADMIN;

        model.addAttribute("title", "Product Cart");
        model.addAttribute("products", session.getCart());
        model.addAttribute("nav_state_products", "");
        model.addAttribute("nav_state_product_add", isAdmin? "" : "disabled");
        model.addAttribute("nav_state_users", isAdmin ? "" : "disabled");
        model.addAttribute("nav_state_cart", isLoggedIn ? "active" : "disabled");
        model.addAttribute("nav_state_login", isLoggedIn ? "disabled" : "");
        model.addAttribute("nav_state_logout", isLoggedIn ? "" : "disabled");
        model.addAttribute("show_action_column", isUser || isAdmin);
        model.addAttribute("can_edit", false);
        model.addAttribute("can_add_to_cart", false);
        model.addAttribute("can_remove_from_cart", isLoggedIn);
        model.addAttribute("user_name", isLoggedIn ? session.getUser().getName() : "");

        return "products";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/cart/add")
    public String addToCart(@RequestParam(name = "product_id") String productId,
                            @CookieValue(name = "user-token", required = false) String token) {
        Session session = securityService.getValidSession(token);
        Product product = productService.getById(Integer.parseInt(productId));

        if(session != null && product != null) {
            session.getCart().add(product);
        }

        return "redirect:/cart";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/cart/delete")
    public String removeFromCart(@RequestParam(name = "product_id") String productId,
                                 @CookieValue(name = "user-token", required = false) String token) {
        Session session = securityService.getValidSession(token);

        if(session != null) {
            session.removeFromCartById(Integer.parseInt(productId));
        }

        return "redirect:/cart";
    }
}
