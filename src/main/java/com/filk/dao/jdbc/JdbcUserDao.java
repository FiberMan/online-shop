package com.filk.dao.jdbc;

import com.filk.dao.UserDao;
import com.filk.dao.jdbc.mapper.UserRowMapper;
import com.filk.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Repository
public class JdbcUserDao implements UserDao {
    private final String GET_ALL_USERS_SQL = "SELECT * FROM \"user\"";
    private final String GET_USER_BY_LOGIN_SQL = "SELECT * FROM \"user\" WHERE lower(login) = lower(?)";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(GET_ALL_USERS_SQL, new UserRowMapper());
    }

    public User getByLogin(String login) {
        List<User> users = jdbcTemplate.query(GET_USER_BY_LOGIN_SQL, new UserRowMapper(), login);
        if(users.size() == 0) {
            return null;
        }
        return users.get(0);
    }
}
