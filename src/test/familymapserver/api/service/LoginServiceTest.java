package com.jasoncarloscox.familymapserver.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.jasoncarloscox.familymapserver.api.request.LoginRequest;
import com.jasoncarloscox.familymapserver.api.result.LoginResult;
import com.jasoncarloscox.familymapserver.api.service.LoginService;
import com.jasoncarloscox.familymapserver.data.access.AuthTokenAccess;
import com.jasoncarloscox.familymapserver.data.access.DBException;
import com.jasoncarloscox.familymapserver.data.access.Database;
import com.jasoncarloscox.familymapserver.data.access.UserAccess;
import com.jasoncarloscox.familymapserver.data.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jasoncarloscox.familymapserver.data.access.DatabaseTest;

public class LoginServiceTest {

    private Database db;
    private UserAccess userAccess;
    private AuthTokenAccess authTokenAccess;
    private User user;

    @Before
    public void setup() throws DBException {
        Database.testDBPath = DatabaseTest.TEST_DB;
        db = new Database();
        db.init();
        db.clear();
        db.commit();

        userAccess = new UserAccess(db);
        authTokenAccess = new AuthTokenAccess(db);

        user = new User("uname", "password");
        user.setEmail("email@email.com");
        user.setFirstName("f");
        user.setLastName("l");
        user.setGender("m");
        user.setPersonId("pid");
        userAccess.add(user);
        db.commit();
    }

    @After
    public void cleanup() throws DBException {
        db.close();
    }

    @Test
    public void loginSucceedsIfPasswordMatches() throws DBException {
        LoginRequest request = new LoginRequest(user.getUsername(), 
                                                user.getPassword());
        LoginResult result = LoginService.login(request);

        assertTrue(result.getMessage(), result.isSuccess());
        assertEquals(user.getUsername(), result.getUserName());
        assertEquals(user.getPersonId(), result.getPersonID());
        assertNotNull(authTokenAccess.getUsername(result.getAuthToken()));
    }

    @Test
    public void loginFailsIfUserNotFound() {
        LoginRequest request = new LoginRequest("doesntexist", 
                                                user.getPassword());
        LoginResult result = LoginService.login(request);

        assertFalse(result.isSuccess());
    }

    @Test
    public void loginFailsIfPasswordNotMatches() {
        LoginRequest request = new LoginRequest(user.getUsername(), 
                                                "wrongpassword");
        LoginResult result = LoginService.login(request);

        assertFalse(result.isSuccess());
    }
}