package com.filk.entity;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;

public class Session {
    private String tokenName;
    private String token;
    private User user;
    private LocalDateTime expireDate;

    public Cookie getCookie() {
        return new Cookie(tokenName, token);
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }
}
