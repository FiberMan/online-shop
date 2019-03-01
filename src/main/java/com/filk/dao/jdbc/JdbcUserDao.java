package com.filk.dao.jdbc;

import com.filk.dao.UserDao;
import com.filk.dao.jdbc.mapper.UserMapper;
import com.filk.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDao implements UserDao {
    private final String SQL_GET_ALL_USERS = "SELECT * FROM \"user\"";
    private final String SQL_GET_USER_BY_LOGIN = "SELECT * FROM \"user\" WHERE lower(login) = lower(?)";
    private JdbcUtils jdbcUtils;

    public JdbcUserDao(JdbcUtils jdbcUtils) {
        this.jdbcUtils = jdbcUtils;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();

        try {
            ResultSet resultSet = jdbcUtils.select(SQL_GET_ALL_USERS);
            while (resultSet.next()) {
                users.add(UserMapper.map(resultSet));
            }
            resultSet.getStatement().getConnection().close();   // Closing: Connection -> Statement -> ResultSet
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception: Can't get all users.", e);
        }
        return users;
    }

    public User getByLogin(String login) {
        try {
            ResultSet resultSet = jdbcUtils.select(SQL_GET_USER_BY_LOGIN, login);
            User user = null;
            if (resultSet.next()) {
                user = UserMapper.map(resultSet);
            }
            resultSet.getStatement().getConnection().close();   // Closing: Connection -> Statement -> ResultSet
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("SQL Exception: Can't get User by Login. Login = " + login, e);
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
