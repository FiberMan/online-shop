package com.filk.dao.jdbc;

import com.filk.utils.AppUtils;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private DataSource dataSource;

    public JdbcUtils(Properties properties) {
        // TODO: is it really a connection pool?
        String herokuDatabaseUrl = getHerokuDatabaseUrl();
        PGSimpleDataSource dataSource = new PGSimpleDataSource();

        if(herokuDatabaseUrl == null) {
            dataSource.setUrl(properties.getProperty("db.jdbc.url"));
            dataSource.setUser(properties.getProperty("db.jdbc.username"));
            dataSource.setPassword(properties.getProperty("db.jdbc.password"));
        } else {
            dataSource.setUrl(herokuDatabaseUrl + "&currentSchema=onlineshop");
        }
        this.dataSource = dataSource;
    }

    public ResultSet select(String query, Object... conditions) throws SQLException {
        PreparedStatement preparedStatement = prepareSql(query, conditions);
        if (!preparedStatement.execute()) {
            throw new RuntimeException("Not a SELECT statement.");
        }
        return preparedStatement.getResultSet();

    }

    public int update(String query, Object... conditions) throws SQLException {
        try (PreparedStatement preparedStatement = prepareSql(query, conditions)) {
            if (preparedStatement.execute()) {
                throw new RuntimeException("Looks like a SELECT statement.");
            }
            return preparedStatement.getUpdateCount();
        }
    }

    private PreparedStatement prepareSql(String query, Object... conditions) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        for (int i = 0; i < conditions.length; i++) {
            Object condition = conditions[i];
            if (condition instanceof String) {
                preparedStatement.setString(i + 1, (String) condition);
            } else if (condition instanceof Integer) {
                preparedStatement.setInt(i + 1, (Integer) condition);
            } else if (condition instanceof Double) {
                preparedStatement.setDouble(i + 1, (Double) condition);
            } else {
                throw new IllegalArgumentException("Not supported object type: " + condition.getClass().getName());
            }
        }
        return preparedStatement;
    }

    public void refreshDatabase() throws IOException {
        InputStream resourceAsStream1 = getClass().getClassLoader().getResourceAsStream("sql/pg_create_tables.sql");
        InputStream resourceAsStream2 = getClass().getClassLoader().getResourceAsStream("sql/insert_data.sql");

        String sqlStructure = AppUtils.getFileContent(resourceAsStream1);
        String sqlData = AppUtils.getFileContent(resourceAsStream2);

        // refresh database
        try {
            String sql = sqlStructure + sqlData;
            for (String s : sql.split(";")) {
                if (!s.isEmpty()) {
                    update(s);
                }
            }
        } catch (SQLException e) {
            System.out.println("Can't refresh database.");
            e.printStackTrace();
        }
    }

    private String getHerokuDatabaseUrl() {
        return System.getenv("JDBC_DATABASE_URL");
    }

//    public User getByLogin(String login) {
//        try (Connection connection = dataSource.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_USER_BY_LOGIN)) {
//            preparedStatement.setString(1, login.toLowerCase());
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                User user = new User();
//                if (resultSet.next()) {
//                    user = UserMapper.map(resultSet);
//                }
//                return user;
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
}