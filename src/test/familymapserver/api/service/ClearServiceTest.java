package com.jasoncarloscox.familymapserver.api.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import com.jasoncarloscox.familymapserver.api.result.ClearResult;
import com.jasoncarloscox.familymapserver.api.service.ClearService;
import com.jasoncarloscox.familymapserver.data.access.AuthTokenAccess;
import com.jasoncarloscox.familymapserver.data.access.DBException;
import com.jasoncarloscox.familymapserver.data.access.Database;
import com.jasoncarloscox.familymapserver.data.access.EventAccess;
import com.jasoncarloscox.familymapserver.data.access.PersonAccess;
import com.jasoncarloscox.familymapserver.data.access.UserAccess;
import com.jasoncarloscox.familymapserver.data.model.AuthToken;
import com.jasoncarloscox.familymapserver.data.model.Person;
import com.jasoncarloscox.familymapserver.data.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jasoncarloscox.familymapserver.data.access.DatabaseTest;

public class ClearServiceTest {

    private Database db;
    private UserAccess userAccess;
    private PersonAccess personAccess;
    private EventAccess eventAccess;
    private AuthTokenAccess authTokenAccess;

    @Before
    public void setup() throws DBException {
        Database.testDBPath = DatabaseTest.TEST_DB;
        db = new Database();
        db.init();
        db.clear();
        db.commit();

        userAccess = new UserAccess(db);
        personAccess = new PersonAccess(db);
        eventAccess = new EventAccess(db);
        authTokenAccess = new AuthTokenAccess(db);
    }

    @After
    public void cleanup() throws DBException {
        db.close();
    }

    @Test
    public void clearDeletesAllUsers() throws DBException {
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User u = new User("uname" + i, "password");
            u.setEmail("email@email.com");
            u.setFirstName("f");
            u.setLastName("l");
            u.setGender("m");
            u.setPersonId("pid"  + i);

            users.add(u);
            userAccess.add(u);
        }

        db.commit();

        ClearResult result = ClearService.clear();

        assertTrue(result.getMessage(), result.isSuccess());

        for (User u : users) {
            assertNull(userAccess.get(u.getUsername()));
        }
    }

    @Test
    public void clearDeletesAllPersons() throws DBException {
        ArrayList<Person> persons = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Person p = new Person("pid" + i, "uname" + i);
            p.setFirstName("f");
            p.setLastName("l");
            p.setGender("m");
            p.setFather("fid" + i);
            p.setMother("mid" + i);
            p.setSpouse("sid" + i);

            persons.add(p);
            personAccess.add(p);
        }

        db.commit();

        ClearResult result = ClearService.clear();

        assertTrue(result.getMessage(), result.isSuccess());

        for (Person p : persons) {
            assertNull(personAccess.get(p.getId()));
        }
    }

    @Test
    public void clearDeletesAllEvents() throws DBException {
        ArrayList<Event> events = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Event e = new Event("eid" + i, "uname" + i);
            e.setPersonId("pid"  + i);
            e.setLatitude(i);
            e.setLongitude(i + 1);
            e.setCountry("country");
            e.setCity("city");
            e.setType("type");
            e.setYear(2000 + i);

            events.add(e);
            eventAccess.add(e);
        }

        db.commit();

        ClearResult result = ClearService.clear();

        assertTrue(result.getMessage(), result.isSuccess());

        for (Event e : events) {
            assertNull(eventAccess.get(e.getId()));
        }
    }

    @Test
    public void clearDeletesAllAuthTokens() throws DBException {
        ArrayList<AuthToken> authTokens = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            AuthToken at = new AuthToken("token" + i, "uname" + i);

            authTokens.add(at);
            authTokenAccess.add(at);
        }

        db.commit();

        ClearResult result = ClearService.clear();

        assertTrue(result.getMessage(), result.isSuccess());

        for (AuthToken at : authTokens) {
            assertNull(authTokenAccess.getUsername(at.getToken()));
        }
    }

    @Test
    public void clearDeletesAllData() throws DBException {
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User u = new User("uname" + i, "password");
            u.setEmail("email@email.com");
            u.setFirstName("f");
            u.setLastName("l");
            u.setGender("m");
            u.setPersonId("pid"  + i);

            users.add(u);
            userAccess.add(u);
        }

        ArrayList<Person> persons = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Person p = new Person("pid" + i, "uname" + i);
            p.setFirstName("f");
            p.setLastName("l");
            p.setGender("m");
            p.setFather("fid" + i);
            p.setMother("mid" + i);
            p.setSpouse("sid" + i);

            persons.add(p);
            personAccess.add(p);
        }

        ArrayList<Event> events = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Event e = new Event("eid" + i, "uname" + i);
            e.setPersonId("pid"  + i);
            e.setLatitude(i);
            e.setLongitude(i + 1);
            e.setCountry("country");
            e.setCity("city");
            e.setType("type");
            e.setYear(2000 + i);

            events.add(e);
            eventAccess.add(e);
        }

        ArrayList<AuthToken> authTokens = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            AuthToken at = new AuthToken("token" + i, "uname" + i);

            authTokens.add(at);
            authTokenAccess.add(at);
        }

        db.commit();

        ClearResult result = ClearService.clear();

        assertTrue(result.getMessage(), result.isSuccess());

        for (User u : users) {
            assertNull(userAccess.get(u.getUsername()));
        }

        for (Person p : persons) {
            assertNull(personAccess.get(p.getId()));
        }

        for (Event e : events) {
            assertNull(eventAccess.get(e.getId()));
        }

        for (AuthToken at : authTokens) {
            assertNull(authTokenAccess.getUsername(at.getToken()));
        }
    }
}