package com.filk.dao.jdbc;

import com.filk.entity.User;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JdbcUserDaoTest {
    @Test
    public void getByLoginTest() throws IOException {
        Properties properties = new Properties();
        try(FileInputStream fileInputStream = new FileInputStream("src/main/resources/db.properties")) {
            properties.load(fileInputStream);
        }
        JdbcUtils jdbcUtils = new JdbcUtils(properties);
        JdbcUserDao jdbcUserDao = new JdbcUserDao(jdbcUtils);

        User user = jdbcUserDao.getByLogin("filk");

        assertEquals(1, user.getId());
        assertEquals("Y F", user.getName());
        assertEquals("Filk", user.getLogin());
        assertEquals("BpLblmouLG7W6skmw3zF9w==", user.getLoginHash());
        assertEquals("95cKMbb0LCI0srpDhnJYHA==", user.getLoginSalt());
        assertEquals("ADMIN", user.getUserRole().getName());

        user = jdbcUserDao.getByLogin("TEST");

        assertEquals(2, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("test", user.getLogin());
        assertEquals("tuK/ZiROmB7PcWw6F/N1eg==", user.getLoginHash());
        assertEquals("kegiMoJIhOayTS82BhFZTw==", user.getLoginSalt());
        assertEquals("USER", user.getUserRole().getName());

        user = jdbcUserDao.getByLogin("not_exists");
        assertNull(user);
    }
}
