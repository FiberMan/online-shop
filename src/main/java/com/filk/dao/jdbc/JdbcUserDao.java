package com.filk.dao.jdbc;

import com.filk.dao.UserDao;
import com.filk.entity.User;

import java.sql.Connection;

public class JdbcUserDao implements UserDao {
    private Connection connection;

    public JdbcUserDao(Connection connection) {
        this.connection = connection;
    }

    public User getByLogin(String login) {

        return null;
    }


}
