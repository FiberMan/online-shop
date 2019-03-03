package com.filk.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UtilController {

    @RequestMapping(method = RequestMethod.GET, path = "/index.html")
    public String index() {
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public String indexRoot() {
        return "forward:/index.html";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/404.html")
    public String notFound() {
        return "404";
    }

//    @ExceptionHandler(ResourceNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public String handleResourceNotFoundException() {
//        return "notfound";
//    }

//    public String dbRefresh() {
//        String pageContent;
//        try {
//            jdbcUtils.refreshDatabase();
//            pageContent = "Database refreshed";
//        } catch (IOException e) {
//            pageContent = "Can't refresh database.\n" + e.getMessage();
//            System.out.println(pageContent);
//            e.printStackTrace();
//        }
//
//        response.getWriter().println(pageContent);
//    }
}
