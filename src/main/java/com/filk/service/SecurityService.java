package com.filk.service;

import com.filk.entity.Session;

public interface SecurityService {
    Session login(String login, String password);

    void logout(String token);

    Session getValidSession(String token);
}
