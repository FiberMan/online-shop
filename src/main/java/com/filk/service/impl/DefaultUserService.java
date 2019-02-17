package com.filk.service.impl;

import com.filk.dao.UserDao;
import com.filk.entity.User;
import com.filk.service.UserService;

public class DefaultUserService implements UserService {
    private UserDao userDao;

    public DefaultUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getByLogin(String login) {
        return userDao.getByLogin(login);
    }
}
