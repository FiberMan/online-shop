package com.filk.web.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Controller
public class UtilController {
    @Value("${db.jdbc.createDbScript}")
    String ddlScript;
    @Value("${db.jdbc.insertDataScript}")
    String dmlScript;

    JdbcTemplate jdbcTemplate;

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    @RequestMapping(method = RequestMethod.GET, path = "/db")
    public void dbRefresh(HttpServletResponse response) {
        InputStream resourceAsStream1 = getClass().getClassLoader().getResourceAsStream(ddlScript);
        InputStream resourceAsStream2 = getClass().getClassLoader().getResourceAsStream(dmlScript);

        // refresh database
        try {
            String sqlStructure = getFileContent(resourceAsStream1);
            String sqlData = getFileContent(resourceAsStream2);

            String sql = sqlStructure + sqlData;
            for (String s : sql.split(";")) {
                if (!s.isEmpty()) {
                    jdbcTemplate.update(s);
                }
            }

            response.getWriter().println("Database refreshed");
        } catch (IOException e) {
            System.out.println("Can't refresh database.");
            e.printStackTrace();
        }
    }

    private String getFileContent(InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }
}
