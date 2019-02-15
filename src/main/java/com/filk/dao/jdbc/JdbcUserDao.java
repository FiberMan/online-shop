package com.filk.dao.jdbc;

import com.filk.dao.UserDao;
import com.filk.dao.jdbc.mapper.UserMapper;
import com.filk.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUserDao implements UserDao {
    private final String SQL_GET_USER_BY_LOGIN = "SELECT * FROM user WHERE login = ?";
    private JdbcUtils jdbcUtils;

    public JdbcUserDao(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }

    public User getByLogin(String login) {
        try (ResultSet resultSet = jdbcUtils.select(SQL_GET_USER_BY_LOGIN, login)) {
            User user = new User();
            if (resultSet.next()) {
                user = UserMapper.map(resultSet);
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public User getByLogin(String login) {
//        try(Connection connection = dataSource.getConnection();
//            PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_USER_BY_LOGIN)) {
//            preparedStatement.setString(1, login.toLowerCase());
//            try(ResultSet resultSet = preparedStatement.executeQuery()) {
//                User user = new User();
//                if(resultSet.next()) {
//                    user = UserMapper.map(resultSet);
//                }
//                return user;
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
