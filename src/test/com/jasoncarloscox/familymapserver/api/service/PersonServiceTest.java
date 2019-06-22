package com.jasoncarloscox.familymapserver.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import com.jasoncarloscox.familymapserver.api.request.ApiRequest;
import com.jasoncarloscox.familymapserver.api.request.PersonRequest;
import com.jasoncarloscox.familymapserver.api.result.PersonResult;
import com.jasoncarloscox.familymapserver.api.result.PersonsResult;
import com.jasoncarloscox.familymapserver.api.service.PersonService;
import com.jasoncarloscox.familymapserver.data.access.AuthTokenAccess;
import com.jasoncarloscox.familymapserver.data.access.DBException;
import com.jasoncarloscox.familymapserver.data.access.Database;
import com.jasoncarloscox.familymapserver.data.access.PersonAccess;
import com.jasoncarloscox.familymapserver.data.model.AuthToken;
import com.jasoncarloscox.familymapserver.data.model.Person;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.jasoncarloscox.familymapserver.data.access.DatabaseTest;

public class PersonServiceTest {

    private Database db;
    private AuthTokenAccess authTokenAccess;
    private PersonAccess personAccess;

    private AuthToken at;
    private ArrayList<Person> persons;

    @Before
    public void setup() throws DBException {
        Database.testDBPath = DatabaseTest.TEST_DB;
        db = new Database();
        db.init();
        db.clear();
        db.commit();

        authTokenAccess = new AuthTokenAccess(db);
        personAccess = new PersonAccess(db);

        at = new AuthToken("token", "uname");
        authTokenAccess.add(at);

        persons = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Person p = new Person("pid" + i, at.getUsername());
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
    }

    @After
    public void cleanup() throws DBException {
        db.close();
    }

    @Test
    public void getPersonSucceedsIfIdFoundAndAuthTokenValid() {
        for (Person p : persons) {
            PersonRequest req = new PersonRequest(at.getToken(), p.getId());

            PersonResult result = PersonService.getPerson(req);

            assertTrue(result.getMessage(), result.isSuccess());

            assertEquals(p.getId(), result.getId());
            assertEquals(p.getAssociatedUsername(), result.getAssociatedUsername());
            assertEquals(p.getFirstName(), result.getFirstName());
            assertEquals(p.getLastName(), result.getLastName());
            assertEquals(p.getGender(), result.getGender());
            assertEquals(p.getFather(), result.getFather());
            assertEquals(p.getMother(), result.getMother());
            assertEquals(p.getSpouse(), result.getSpouse());
        }
    }

    @Test
    public void getPersonFailsIfIdNotFound() {
        PersonRequest req = new PersonRequest(at.getToken(), "invalid");

        PersonResult result = PersonService.getPerson(req);

        assertFalse(result.getMessage(), result.isSuccess());
    }

    @Test
    public void getPersonFailsIfAuthTokenNotValid() {
        PersonRequest req = new PersonRequest("invalid", persons.get(0).getId());

        PersonResult result = PersonService.getPerson(req);

        assertFalse(result.getMessage(), result.isSuccess());
    }

    @Test
    public void getPersonsSucceedsIfAuthTokenValid() {
        ApiRequest req = new ApiRequest(at.getToken());

        PersonsResult result = PersonService.getPersons(req);

        assertTrue(result.getMessage(), result.isSuccess());

        assertEquals(persons.size(), result.getData().size());
    }

    @Test
    public void getPersonsFailsIfAuthTokenNotValid() {
        ApiRequest req = new ApiRequest("invalid");

        PersonsResult result = PersonService.getPersons(req);

        assertFalse(result.getMessage(), result.isSuccess());
    }
}