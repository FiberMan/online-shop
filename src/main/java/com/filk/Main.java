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

    // TODO: read: Optional, DispatcherType

    // TODO: Migrate Online Shop on spring
    // TODO: configure filters
    // TODO: Migrate all services and daos to annotations (@Service, @Repository)
    // TODO: Separate context’s use Root and Servlet contexts (theory part 1)
    // TODO: Configure Spring with Thymeleaf (or migrate to freemarker view resolver)
    // TODO: Use Jdbc Template from spring-jdbc instead JDBC interfaces
    // TODO: Add logback logging


    public static void main(String[] args) throws Exception {
        // config
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("application.properties")) {
            properties.load(fileInputStream);
        }

        int port = Integer.parseInt(properties.getProperty("web.port"));
        String portEnv = System.getenv().get("PORT");
        if (portEnv != null) {
            port = Integer.parseInt(portEnv);
        }

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
        SecurityService securityService = new SecurityService(userService, properties);

        // servlets
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new IndexServlet()), "/index.html");
        context.addServlet(new ServletHolder(new AllProductsServlet(productService, securityService)), "/products");
        context.addServlet(new ServletHolder(new AddProductServlet(productService, securityService)), "/product/add");
        context.addServlet(new ServletHolder(new EditProductServlet(productService, securityService)), "/product/edit");
        context.addServlet(new ServletHolder(new DeleteProductServlet(productService, securityService)), "/product/delete");
        context.addServlet(new ServletHolder(new AllUsersServlet(userService, securityService)), "/users");
        context.addServlet(new ServletHolder(new LoginServlet(securityService)), "/login");
        context.addServlet(new ServletHolder(new LogoutServlet(securityService)), "/logout");
        context.addServlet(new ServletHolder(new ReCreateDatabaseServlet(jdbcUtils)), "/db");
        context.addServlet(new ServletHolder(new ResourcesServlet()), "/assets/*");
        context.addServlet(new ServletHolder(new NotFoundServlet()), "/*");

        // filters
        context.addFilter(new FilterHolder(new AuthFilter(securityService)), "/product/*",
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
        context.addFilter(new FilterHolder(new AuthFilter(securityService)), "/users",
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));
        context.addFilter(new FilterHolder(new AdminFilter(securityService)), "/users",
                EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD));

        Server server = new Server(port);
        server.setHandler(context);
        server.start();
    }
}
