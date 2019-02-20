package com.filk.dao;

import com.filk.entity.User;

import java.util.List;

public interface UserDao {
    List<User> getAll();

    User getByLogin(String login);
}
