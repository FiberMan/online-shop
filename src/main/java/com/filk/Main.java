package com.filk;

import com.filk.dao.ProductDao;
import com.filk.dao.UserDao;
import com.filk.dao.jdbc.JdbcProductDao;
import com.filk.dao.jdbc.JdbcUserDao;
import com.filk.dao.jdbc.JdbcUtils;
import com.filk.service.SecurityService;
import com.filk.service.impl.DefaultProductService;
import com.filk.service.impl.DefaultUserService;
import com.filk.web.servlets.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import java.io.FileInputStream;
import java.util.EnumSet;
import java.util.Properties;

public class Main {

    // +TODO: prepareStatement
    // +TODO: data, mapper, service,
    // TODO: presentation layer
    // +TODO: bootstrap
    // TODO: login
    // TODO: add servlet filters
    // +TODO: connection through DataSource
    // TODO: store login/pass -> hash
    // TODO: read: Optional, DispatcherType

    public static void main(String[] args) throws Exception {
        // config
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/db.properties")) {
            properties.load(fileInputStream);
        }

//        printSaltHash("password");
//        User: Filk
//        Pass: password
//        Salt: 95cKMbb0LCI0srpDhnJYHA==
//        Hash: BpLblmouLG7W6skmw3zF9w==

//        User: test
//        Pass: password
//        Salt: kegiMoJIhOayTS82BhFZTw==
//        Hash: tuK/ZiROmB7PcWw6F/N1eg==

        // dao
        //DataSource dataSource = createDataSource(properties);
        JdbcUtils jdbcUtils = new JdbcUtils(properties);

        UserDao userDao = new JdbcUserDao(jdbcUtils);
        ProductDao productDao = new JdbcProductDao(jdbcUtils);


        // services
        DefaultUserService userService = new DefaultUserService(userDao);
        DefaultProductService productService = new DefaultProductService(productDao);
        SecurityService securityService = new SecurityService(userService);

        // servlets
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new AllProductsServlet(productService, securityService)), "/products");
        context.addServlet(new ServletHolder(new AddProductServlet(productService)), "/product/add");
        context.addServlet(new ServletHolder(new EditProductServlet(productService)), "/product/edit");
        context.addServlet(new ServletHolder(new DeleteProductServlet(productService)), "/product/delete");
        context.addServlet(new ServletHolder(new LoginServlet(securityService)), "/login");
        context.addServlet(new ServletHolder(new LogoutServlet(securityService)), "/logout");
        context.addServlet(new ServletHolder(new AllRequestsServlet()), "/*");

        // filters
        context.addFilter(new FilterHolder(new AuthFilter(securityService)), "/product/*",
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));

        Server server = new Server(8080);
        server.setHandler(context);
        server.start();
    }
}
