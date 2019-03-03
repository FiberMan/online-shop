package com.filk.service.impl;

import com.filk.entity.Session;
import com.filk.entity.User;
import com.filk.service.SecurityService;
import com.filk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DefaultSecurityService implements SecurityService {
    @Value("${web.sessionLifetimeMinutes}")
    private String sessionLifetimeMinutes;

    private UserService userService;
    private List<Session> sessions = Collections.synchronizedList(new ArrayList<>());

    @Autowired
    public DefaultSecurityService(UserService userService) {
        this.userService = userService;
    }

    public Session login(String login, String password) {
        User user = userService.getByLogin(login);
        if (user == null || !user.getLoginHash().equals(getHash(password + user.getLoginSalt()))) {
            return null;
        }

        Session session = new Session();
        session.setToken(UUID.randomUUID().toString());
        session.setExpireDate(getExpireDate());
        session.setUser(user);

        sessions.add(session);

        return session;
    }

    // TODO: use token instead of Cookie
    public void logout(String token) {
        Session session = getValidSession(token);

        if (session != null) {
            sessions.remove(session);
        }
    }

    public Session getValidSession(String token) {
        if (token != null) {
            for (Session session : sessions) {
                if (session.getToken().equals(token)) {
                    if (session.isLive()) {
                        session.setExpireDate(getExpireDate()); // update expiration date
                        return session;
                    } else {
                        sessions.remove(session);
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public boolean isTokenValid(String token) {
        return getValidSession(token) != null;
    }

    private LocalDateTime getExpireDate() {
        return LocalDateTime.now().plusMinutes(Long.parseLong(sessionLifetimeMinutes));
    }

    public static String getHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getSalt() {
        Random RANDOM = new SecureRandom();
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private static void printSaltHash(String password) {
        String salt = getSalt();
        String hash = getHash(password + salt);
        System.out.println("Pass: " + password);
        System.out.println("Salt: " + salt);
        System.out.println("Hash: " + hash);
    }
}
