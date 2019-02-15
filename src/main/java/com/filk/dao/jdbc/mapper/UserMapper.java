package com.filk.dao.jdbc.mapper;

import com.filk.entity.User;
import com.filk.entity.UserRole;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper {
    public static User map(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setName(resultSet.getString("name"));
        user.setLogin(resultSet.getString("login"));
        user.setLoginHash(resultSet.getString("login_hash"));
        user.setLoginSalt(resultSet.getString("login_salt"));
        user.setUserRole(UserRole.getByName(resultSet.getString("user_role")));
        return user;
    }
}