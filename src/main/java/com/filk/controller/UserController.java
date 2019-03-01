package com.filk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class UserController {

    @RequestMapping(method = RequestMethod.GET, path = "/users")
    public void getUsers(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.getWriter().write("All users here!");
    }
}
