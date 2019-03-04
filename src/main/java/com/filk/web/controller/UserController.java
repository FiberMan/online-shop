package com.filk.web.controller;

import com.filk.entity.Session;
import com.filk.entity.UserRole;
import com.filk.service.SecurityService;
import com.filk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    private UserService userService;
    private SecurityService securityService;

    @Autowired
    public UserController(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/users")
    public String getUsers(@CookieValue(name = "user-token", required = false) String token,
                         Model model) {
        Session session = securityService.getValidSession(token);
        boolean isLoggedIn = session != null;
        //boolean isUser = isLoggedIn && session.getUser().getUserRole() == UserRole.USER;
        boolean isAdmin = isLoggedIn && session.getUser().getUserRole() == UserRole.ADMIN;

        model.addAttribute("title", "List of users");
        model.addAttribute("users", userService.getAll());
        model.addAttribute("nav_state_products", "");
        model.addAttribute("nav_state_product_add", "");
        model.addAttribute("nav_state_users", isAdmin ? "" : "disabled");
        model.addAttribute("nav_state_cart", isLoggedIn ? "" : "disabled");
        model.addAttribute("nav_state_login", isLoggedIn ? "disabled" : "");
        model.addAttribute("nav_state_logout", isLoggedIn ? "" : "disabled");
        //model.addAttribute("can_edit", isAdmin);
        model.addAttribute("user_name", isLoggedIn ? session.getUser().getName() : "");

        return "users";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/login")
    public String loginGet(@CookieValue(name = "user-token", required = false) String token,
                           Model model) {

        Session session = securityService.getValidSession(token);
        boolean isLoggedIn = session != null;

        // User already logged in
        if (isLoggedIn) {
            return "redirect:/products";
        }

        model.addAttribute("title", "Login page");
        model.addAttribute("nav_state_products", "");
        model.addAttribute("nav_state_product_add", "disabled");
        model.addAttribute("nav_state_users", "disabled");
        model.addAttribute("nav_state_cart", "disabled");
        model.addAttribute("nav_state_login", "active");
        model.addAttribute("nav_state_logout", "disabled");
        model.addAttribute("user_name", "");
        model.addAttribute("message", "");

        return "login";
    }

    @RequestMapping(method = RequestMethod.POST, path = "/login")
    public String loginPost(HttpServletResponse response,
                            @RequestParam(name = "login") String login,
                            @RequestParam(name = "password") String password,
                            @CookieValue(name = "user-token", required = false) String token,
                            Model model) {
        Session session = securityService.getValidSession(token);
        boolean isLoggedIn = session != null;

        // User already logged in
        if (isLoggedIn) {
            return "redirect:/products";
        }

        session = securityService.login(login, password);
        if (session != null) {
            response.addCookie(new Cookie("user-token", session.getToken()));
            return "redirect:/products";
        } else {
            model.addAttribute("title", "Login page");
            model.addAttribute("nav_state_products", "");
            model.addAttribute("nav_state_product_add", "disabled");
            model.addAttribute("nav_state_users", "disabled");
            model.addAttribute("nav_state_cart", "disabled");
            model.addAttribute("nav_state_login", "active");
            model.addAttribute("nav_state_logout", "disabled");
            model.addAttribute("user_name", "");
            model.addAttribute("message", "Wrong User or Password.");

            return "login";
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/logout")
    public String logout(HttpServletResponse response,
                         @CookieValue(name = "user-token", required = false) String token) {

        securityService.logout(token);

        Cookie cookie = new Cookie("user-token", "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/login";
    }
}
