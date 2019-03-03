package com.filk.dao.jdbc.mapper;

import com.filk.entity.User;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserRowMapperTest {
    @Test
    public void mapTest() throws SQLException {
        ResultSet resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getInt("user_id")).thenReturn(1);
        when(resultSetMock.getString("name")).thenReturn("Y F");
        when(resultSetMock.getString("login")).thenReturn("Filk");
        when(resultSetMock.getString("login_hash")).thenReturn("BpLblmouLG7W6skmw3zF9w==");
        when(resultSetMock.getString("login_salt")).thenReturn("95cKMbb0LCI0srpDhnJYHA==");
        when(resultSetMock.getString("user_role")).thenReturn("ADMIN");

        User user = UserRowMapper.map(resultSetMock);

        assertEquals(1, user.getId());
        assertEquals("Y F", user.getName());
        assertEquals("Filk", user.getLogin());
        assertEquals("BpLblmouLG7W6skmw3zF9w==", user.getLoginHash());
        assertEquals("95cKMbb0LCI0srpDhnJYHA==", user.getLoginSalt());
        assertEquals("ADMIN", user.getUserRole().getName());

    }
}
