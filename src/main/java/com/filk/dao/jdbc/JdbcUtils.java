package com.filk.dao.jdbc;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {
    private DataSource dataSource;

    public JdbcUtils(Properties properties) {
        // TODO: is it really a connection pool?
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setUrl(properties.getProperty("jdbc.url"));
        mysqlDataSource.setUser(properties.getProperty("jdbc.username"));
        mysqlDataSource.setPassword(properties.getProperty("jdbc.password"));
        this.dataSource = mysqlDataSource;
    }

    private PreparedStatement prepareSql(String query, Object... conditions) {
        try {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    ResultSet select(String query, Object... conditions) throws SQLException {
        PreparedStatement preparedStatement = prepareSql(query, conditions);
        if (!preparedStatement.execute()) {
            throw new RuntimeException("Not a SELECT statement.");
        }
        return preparedStatement.getResultSet();

    }

//    PreparedStatement update(String query, Object... conditions) throws SQLException {
//        try (PreparedStatement preparedStatement = prepareSql(query, conditions)) {
//            if (preparedStatement.execute()) {
//                throw new RuntimeException("Looks like a SELECT statement.");
//            }
//            return preparedStatement;
//        }
//    }


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