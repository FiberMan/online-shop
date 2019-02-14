package com.filk.dao;

import com.filk.entity.User;

public interface UserDao {
    User getByLogin(String login);
}
