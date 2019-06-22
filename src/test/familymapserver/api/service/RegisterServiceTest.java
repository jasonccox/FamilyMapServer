package com.jasoncarloscox.familymapserver.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.jasoncarloscox.familymapserver.api.request.RegisterRequest;
import com.jasoncarloscox.familymapserver.api.result.LoginResult;
import com.jasoncarloscox.familymapserver.api.service.RegisterService;
import com.jasoncarloscox.familymapserver.data.access.AuthTokenAccess;
import com.jasoncarloscox.familymapserver.data.access.DBException;
import com.jasoncarloscox.familymapserver.data.access.Database;
import com.jasoncarloscox.familymapserver.data.access.EventAccess;
import com.jasoncarloscox.familymapserver.data.access.PersonAccess;
import com.jasoncarloscox.familymapserver.data.access.UserAccess;
import com.jasoncarloscox.familymapserver.data.model.Person;
import com.jasoncarloscox.familymapserver.data.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jasoncarloscox.familymapserver.data.access.DatabaseTest;

public class RegisterServiceTest {

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
        authTokenAccess = new AuthTokenAccess(db);
        personAccess = new PersonAccess(db);
        eventAccess = new EventAccess(db);
    }

    @After
    public void cleanup() throws DBException {
        db.close();
    }

    @Test
    public void registerSucceedsIfUserDataValid() throws DBException {
        User validUser = new User("uname", "password");
        validUser.setEmail("email@email.com");
        validUser.setFirstName("f");
        validUser.setLastName("l");
        validUser.setGender("m");

        LoginResult result = RegisterService.register(new RegisterRequest(validUser));

        assertTrue(result.getMessage(), result.isSuccess());

        // register should have created a person representing the user

        Person person = personAccess.get(result.getPersonID());
        assertEquals(validUser.getUsername(), person.getAssociatedUsername());
        assertEquals(validUser.getFirstName(), person.getFirstName());
        assertEquals(validUser.getLastName(), person.getLastName());
        assertEquals(validUser.getGender(), person.getGender());

        // register should have created four generations of ancestor data
        // this means 31 people, each with at least three events

        Collection<Person> persons = personAccess.getAll(validUser.getUsername());
        assertEquals(31, persons.size());

        Collection<Event> events = eventAccess.getAll(validUser.getUsername());
        assertTrue(events.size() >= 31*3);

        Map<String, Integer> eventsPerPerson = new HashMap<>();
        for (Event e : events) {
            if (!eventsPerPerson.containsKey(e.getPersonId())) {
                eventsPerPerson.put(e.getPersonId(), 1);
            } else {
                eventsPerPerson.put(e.getPersonId(), 
                                    eventsPerPerson.get(e.getPersonId()) + 1);
            }
        }

        assertEquals(persons.size(), eventsPerPerson.size());

        for(String personId : eventsPerPerson.keySet()) {
            assertTrue(eventsPerPerson.get(personId) >= 3);
        }

        // register should have created an auth token for the new user
        assertNotNull(result.getAuthToken());
        assertEquals(validUser.getUsername(), 
                     authTokenAccess.getUsername(result.getAuthToken()));
    }

    @Test
    public void registerFailsIfUsernameTaken() throws DBException {
        User validUser = new User("uname", "password");
        validUser.setEmail("email@email.com");
        validUser.setFirstName("f");
        validUser.setLastName("l");
        validUser.setGender("m");
        validUser.setPersonId("pid");
        userAccess.add(validUser);
        db.commit();
        
        User badUser = new User(validUser.getUsername(), "password2");
        badUser.setEmail("email2@email.com");
        badUser.setFirstName("f2");
        badUser.setLastName("l2");
        badUser.setGender("f");

        LoginResult result = RegisterService.register(new RegisterRequest(badUser));

        assertFalse(result.isSuccess());
    }

    @Test
    public void registerFailsIfUserDataIncomplete() {
        User badUser = new User("uname", "password");

        LoginResult result = RegisterService.register(new RegisterRequest(badUser));

        assertFalse(result.isSuccess());
    }
}