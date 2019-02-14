package com.filk;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {

    // TODO: prepareStatement
    // TODO: data, mapper, service, presentation layers
    // TODO: bootstrap
    // TODO: login
    // TODO: add servlet filters
    // TODO: connection through DataSource
    // TODO: store login/pass -> hash
    // TODO: read: Optional, DispatcherType

    public static void main(String[] args) throws IOException {
        // config
        Properties properties = new Properties();
        try(FileInputStream fileInputStream = new FileInputStream("src/main/resources/jdbc.properties")) {
            properties.load(fileInputStream);
        }

        // dao
        DataSource dataSource = createDataSource(properties);

        // services


        // servlets



    }

    private static DataSource createDataSource(Properties properties) {
        // TODO: is it really a connection pool?
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUser(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));
        return dataSource;
    }
}
