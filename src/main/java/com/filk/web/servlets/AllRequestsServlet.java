package com.filk.web.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class AllRequestsServlet extends HttpServlet {
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException {
        String resourceURI = request.getRequestURI().substring(1);
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(resourceURI);
        if(resourceAsStream == null) {
            System.out.println("Can't find requested resource: " + resourceURI);
        }

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = resourceAsStream.read(buffer)) != -1) {
            response.getOutputStream().write(buffer, 0, bytesRead);
        }
    }
}