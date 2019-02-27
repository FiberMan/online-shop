package com.filk.service;

import com.filk.entity.Session;
import com.filk.entity.User;

import javax.servlet.http.Cookie;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

public class SecurityService {
    private static final String TOKEN_NAME = "user-token";

    private UserService userService;
    private Properties properties;
    private List<Session> sessions = Collections.synchronizedList(new ArrayList<>());

    public SecurityService(UserService userService, Properties properties) {
        this.userService = userService;
        this.properties = properties;
    }

    public Session login(String login, String password) {
        User user = userService.getByLogin(login);
        if (user == null || !user.getLoginHash().equals(getHash(password + user.getLoginSalt()))) {
            return null;
        }

        Session session = new Session();
        session.setTokenName(TOKEN_NAME);
        session.setToken(UUID.randomUUID().toString());
        session.setExpireDate(getExpireDate());
        session.setUser(user);

        sessions.add(session);

        return session;
    }

    // TODO: use token instead of Cookie
    public Cookie logout(Cookie[] cookies) {
        Session session = getValidSession(cookies);

        if (session != null) {
            sessions.remove(session);
        }

        Cookie cookie = new Cookie(TOKEN_NAME, "");
        cookie.setMaxAge(0);
        return cookie;
    }

    public Session getValidSession(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TOKEN_NAME.equals(cookie.getName())) {
                    for (Session session : sessions) {
                        if (session.getToken().equals(cookie.getValue())) {
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
            }
        }
        return null;
    }


    public boolean isTokenValid(Cookie[] cookies) {
        return getValidSession(cookies) != null;
    }

    private LocalDateTime getExpireDate() {
        return LocalDateTime.now().plusMinutes(Long.parseLong(properties.getProperty("web.sessionLifetimeMinutes")));
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
