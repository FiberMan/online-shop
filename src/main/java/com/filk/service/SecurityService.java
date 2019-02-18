package com.filk.service;

import com.filk.entity.Session;

import javax.servlet.http.Cookie;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

public class SecurityService {
    private final int SESSION_LIFETIME_HOURS = 2;
    private final String TOKEN_NAME = "user-token";

    private UserService userService;
    private List<Session> sessions = Collections.synchronizedList(new ArrayList<>());

    public SecurityService(UserService userService) {
        this.userService = userService;
    }

    public Session login(String login, String password) {

        // check user

        // add session
        Session session = new Session();
        session.setTokenName(TOKEN_NAME);
        session.setToken(UUID.randomUUID().toString());
        session.setExpireDate(LocalDateTime.now().plusHours(SESSION_LIFETIME_HOURS));
        //session.setUser(user);

        // add to sessions
        sessions.add(session);

        return session;
    }

    public Cookie logout(Cookie[] cookies) {
        Session session = getValidSession(cookies);
        Cookie cookie;

        if (session != null) {
            sessions.remove(session);
            cookie = session.getCookie();
        } else {
            cookie = new Cookie(TOKEN_NAME, "");
        }

        cookie.setMaxAge(0);
        return cookie;
    }

    public Session getValidSession(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (TOKEN_NAME.equals(cookie.getName())) {
                    for (Session session : sessions) {
                        if (session.getToken().equals(cookie.getValue())) {
                            return session;
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

    private static void printSaltHash(String password) throws NoSuchAlgorithmException {
        String salt = getSalt();
        String hash = getHash(password + salt);
        System.out.println("Pass: " + password);
        System.out.println("Salt: " + salt);
        System.out.println("Hash: " + hash);
    }

    private static String getSalt() {
        Random RANDOM = new SecureRandom();
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private static String getHash(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        return Base64.getEncoder().encodeToString(digest);
    }
}
