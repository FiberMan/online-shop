package com.filk.service;

import com.filk.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User getByLogin(String login);
}
