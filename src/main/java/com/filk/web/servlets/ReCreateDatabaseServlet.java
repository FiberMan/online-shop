package com.filk.web.servlets;

import com.filk.dao.jdbc.JdbcUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class ReCreateDatabaseServlet extends HttpServlet {
    private JdbcUtils jdbcUtils;

    public ReCreateDatabaseServlet(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pageContent;
        try {
            jdbcUtils.refreshDatabase();
            pageContent = "Database refreshed";
        } catch (IOException e) {
            pageContent = "Can't refresh database";
            System.out.println(pageContent);
            e.printStackTrace();
        }

        response.getWriter().println(pageContent);
    }

}
