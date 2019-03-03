package com.filk.service.impl;

import com.filk.dao.UserDao;
import com.filk.entity.User;
import com.filk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultUserService implements UserService {
    private UserDao userDao;

    @Autowired
    public DefaultUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public User getByLogin(String login) {
        return userDao.getByLogin(login);
    }
}
