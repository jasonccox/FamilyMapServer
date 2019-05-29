package familymapserver.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import familymapserver.api.request.LoadRequest;
import familymapserver.api.result.LoadResult;
import familymapserver.data.access.DBException;
import familymapserver.data.access.Database;
import familymapserver.data.access.DatabaseTest;
import familymapserver.data.access.EventAccess;
import familymapserver.data.access.PersonAccess;
import familymapserver.data.access.UserAccess;
import familymapserver.data.model.Event;
import familymapserver.data.model.Person;
import familymapserver.data.model.User;

public class LoadServiceTest {

    private Database db;
    private UserAccess userAccess;
    private PersonAccess personAccess;
    private EventAccess eventAccess;

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
    }

    @After
    public void cleanup() throws DBException {
        db.close();
    }

    @Test
    public void loadAddsUsersIfValid() throws DBException {
        ArrayList<User> validUsers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User u = new User("uname" + i, "password");
            u.setEmail("email@email.com");
            u.setFirstName("f");
            u.setLastName("l");
            u.setGender("m");
            u.setPersonId("pid"  + i);

            validUsers.add(u);
        }

        LoadResult result = LoadService.load(new LoadRequest(validUsers, null, 
                                                             null));

        assertTrue(result.isSuccess());

        for (User inputUser : validUsers) {
            User outputUser = userAccess.get(inputUser.getUsername());

            assertEquals(inputUser.getUsername(), outputUser.getUsername());
            assertEquals(inputUser.getPassword(), outputUser.getPassword());
            assertEquals(inputUser.getFirstName(), outputUser.getFirstName());
            assertEquals(inputUser.getLastName(), outputUser.getLastName());
            assertEquals(inputUser.getGender(), outputUser.getGender());
            assertEquals(inputUser.getEmail(), outputUser.getEmail());
            assertEquals(inputUser.getPersonId(), outputUser.getPersonId());
        }
    }

    @Test
    public void loadAddsPersonsIfValid() throws DBException {
        ArrayList<Person> validPersons = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Person p = new Person("pid" + i, "uname" + i);
            p.setFirstName("f");
            p.setLastName("l");
            p.setGender("m");
            p.setFather("fid" + i);
            p.setMother("mid" + i);
            p.setSpouse("sid" + i);

            validPersons.add(p);
        }

        LoadResult result = LoadService.load(new LoadRequest(null, validPersons, 
                                                             null));

        assertTrue(result.isSuccess());

        for (Person inputPerson : validPersons) {
            Person outputPerson = personAccess.get(inputPerson.getId());

            assertEquals(inputPerson.getId(), outputPerson.getId());
            assertEquals(inputPerson.getAssociatedUsername(), 
                         outputPerson.getAssociatedUsername());
            assertEquals(inputPerson.getFirstName(), outputPerson.getFirstName());
            assertEquals(inputPerson.getLastName(), outputPerson.getLastName());
            assertEquals(inputPerson.getGender(), outputPerson.getGender());
            assertEquals(inputPerson.getFather(), outputPerson.getFather());
            assertEquals(inputPerson.getMother(), outputPerson.getMother());
            assertEquals(inputPerson.getSpouse(), outputPerson.getSpouse());
        }
    }

    @Test
    public void loadAddsEventsIfValid() throws DBException {
        ArrayList<Event> validEvents = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Event e = new Event("eid" + i, "uname" + i);
            e.setPersonId("pid" + i);
            e.setLatitude(i);
            e.setLongitude(i + 1);
            e.setCountry("country");
            e.setCity("city");
            e.setType("type");
            e.setYear(2000 + i);

            validEvents.add(e);
        }

        LoadResult result = LoadService.load(new LoadRequest(null, null, 
                                                             validEvents));

        assertTrue(result.isSuccess());

        for (Event inputEvent : validEvents) {
            Event outputEvent = eventAccess.get(inputEvent.getId());

            assertEquals(inputEvent.getId(), outputEvent.getId());
            assertEquals(inputEvent.getAssociatedUsername(), 
                         outputEvent.getAssociatedUsername());
            assertEquals(inputEvent.getPersonId(), outputEvent.getPersonId());
            assertEquals(inputEvent.getLatitude(), outputEvent.getLatitude(), 0);
            assertEquals(inputEvent.getLongitude(), outputEvent.getLongitude(), 0);
            assertEquals(inputEvent.getCountry(), outputEvent.getCountry());
            assertEquals(inputEvent.getCity(), outputEvent.getCity());
            assertEquals(inputEvent.getType(), outputEvent.getType());
            assertEquals(inputEvent.getYear(), outputEvent.getYear());
        }
    }

    @Test
    public void loadAddsAllIfValid() throws DBException {
        ArrayList<User> validUsers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            User u = new User("uname" + i, "password");
            u.setEmail("email@email.com");
            u.setFirstName("f");
            u.setLastName("l");
            u.setGender("m");
            u.setPersonId("pid"  + i);

            validUsers.add(u);
        }

        ArrayList<Person> validPersons = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Person p = new Person("pid" + i, "uname" + i);
            p.setFirstName("f");
            p.setLastName("l");
            p.setGender("m");
            p.setFather("fid" + i);
            p.setMother("mid" + i);
            p.setSpouse("sid" + i);

            validPersons.add(p);
        }

        ArrayList<Event> validEvents = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Event e = new Event("eid" + i, "uname" + i);
            e.setPersonId("pid"  + i);
            e.setLatitude(i);
            e.setLongitude(i + 1);
            e.setCountry("country");
            e.setCity("city");
            e.setType("type");
            e.setYear(2000 + i);

            validEvents.add(e);
        }

        LoadResult result = LoadService.load(new LoadRequest(validUsers, 
                                                             validPersons, 
                                                             validEvents));

        assertTrue(result.isSuccess());

        for (User inputUser : validUsers) {
            User outputUser = userAccess.get(inputUser.getUsername());

            assertEquals(inputUser.getUsername(), outputUser.getUsername());
            assertEquals(inputUser.getPassword(), outputUser.getPassword());
            assertEquals(inputUser.getFirstName(), outputUser.getFirstName());
            assertEquals(inputUser.getLastName(), outputUser.getLastName());
            assertEquals(inputUser.getGender(), outputUser.getGender());
            assertEquals(inputUser.getEmail(), outputUser.getEmail());
            assertEquals(inputUser.getPersonId(), outputUser.getPersonId());
        }

        for (Person inputPerson : validPersons) {
            Person outputPerson = personAccess.get(inputPerson.getId());

            assertEquals(inputPerson.getId(), outputPerson.getId());
            assertEquals(inputPerson.getAssociatedUsername(), 
                         outputPerson.getAssociatedUsername());
            assertEquals(inputPerson.getFirstName(), outputPerson.getFirstName());
            assertEquals(inputPerson.getLastName(), outputPerson.getLastName());
            assertEquals(inputPerson.getGender(), outputPerson.getGender());
            assertEquals(inputPerson.getFather(), outputPerson.getFather());
            assertEquals(inputPerson.getMother(), outputPerson.getMother());
            assertEquals(inputPerson.getSpouse(), outputPerson.getSpouse());
        }

        for (Event inputEvent : validEvents) {
            Event outputEvent = eventAccess.get(inputEvent.getId());

            assertEquals(inputEvent.getId(), outputEvent.getId());
            assertEquals(inputEvent.getAssociatedUsername(), 
                         outputEvent.getAssociatedUsername());
            assertEquals(inputEvent.getPersonId(), outputEvent.getPersonId());
            assertEquals(inputEvent.getLatitude(), outputEvent.getLatitude(), 0);
            assertEquals(inputEvent.getLongitude(), outputEvent.getLongitude(), 0);
            assertEquals(inputEvent.getCountry(), outputEvent.getCountry());
            assertEquals(inputEvent.getCity(), outputEvent.getCity());
            assertEquals(inputEvent.getType(), outputEvent.getType());
            assertEquals(inputEvent.getYear(), outputEvent.getYear());
        }
    }

    @Test
    public void loadFailsAndAddsNoDataIfUsernameNotUnique() throws DBException {
        User u1 = new User("uname", "password");
        u1.setEmail("email");
        u1.setFirstName("firstName");
        u1.setLastName("lastName");
        u1.setGender("m");
        u1.setPersonId("personId");

        userAccess.add(u1);

        User u2 = new User(u1.getUsername(), "password2");
        u2.setEmail("email2");
        u2.setFirstName("firstName2");
        u2.setLastName("lastName2");
        u2.setGender("f");
        u2.setPersonId("personId2");

        ArrayList<User> users = new ArrayList<>();
        users.add(u2);

        LoadResult result = LoadService.load(new LoadRequest(users, null, null));

        assertFalse(result.isSuccess());

        User outputUser = userAccess.get(u1.getUsername());
        assertEquals(u1.getUsername(), outputUser.getUsername());
        assertEquals(u1.getPassword(), outputUser.getPassword());
        assertEquals(u1.getFirstName(), outputUser.getFirstName());
        assertEquals(u1.getLastName(), outputUser.getLastName());
        assertEquals(u1.getGender(), outputUser.getGender());
        assertEquals(u1.getEmail(), outputUser.getEmail());
        assertEquals(u1.getPersonId(), outputUser.getPersonId());
    }

    @Test
    public void loadFailsAndAddsNoDataIfPersonIdNotUnique() throws DBException {
        Person p1 = new Person("pid", "uname");
        p1.setFirstName("f");
        p1.setLastName("l");
        p1.setGender("m");
        p1.setFather("fid");
        p1.setMother("mid");
        p1.setSpouse("sid");

        personAccess.add(p1);

        Person p2 = new Person(p1.getId(), "uname2");
        p2.setFirstName("f2");
        p2.setLastName("l2");
        p2.setGender("f");
        p2.setFather("fid2");
        p2.setMother("mid2");
        p2.setSpouse("sid2");

        ArrayList<Person> persons = new ArrayList<>();
        persons.add(p2);

        LoadResult result = LoadService.load(new LoadRequest(null, persons, null));

        assertFalse(result.isSuccess());

        Person outputPerson = personAccess.get(p1.getId());
        assertEquals(p1.getId(), outputPerson.getId());
        assertEquals(p1.getAssociatedUsername(), 
                        outputPerson.getAssociatedUsername());
        assertEquals(p1.getFirstName(), outputPerson.getFirstName());
        assertEquals(p1.getLastName(), outputPerson.getLastName());
        assertEquals(p1.getGender(), outputPerson.getGender());
        assertEquals(p1.getFather(), outputPerson.getFather());
        assertEquals(p1.getMother(), outputPerson.getMother());
        assertEquals(p1.getSpouse(), outputPerson.getSpouse());
    }

    @Test
    public void loadFailsAndAddsNoDataIfEventIdNotUnique() throws DBException {
        Event e1 = new Event("eid", "uname");
        e1.setPersonId("pid");
        e1.setLatitude(1);
        e1.setLongitude(1);
        e1.setCountry("country");
        e1.setCity("city");
        e1.setType("type");
        e1.setYear(2000);

        eventAccess.add(e1);

        Event e2 = new Event(e1.getId(), "uname2");
        e2.setPersonId("pid2");
        e2.setLatitude(2);
        e2.setLongitude(2);
        e2.setCountry("country2");
        e2.setCity("city2");
        e2.setType("type2");
        e2.setYear(2002);

        ArrayList<Event> events = new ArrayList<>();
        events.add(e2);

        LoadResult result = LoadService.load(new LoadRequest(null, null, events));

        assertFalse(result.isSuccess());

        Event outputEvent = eventAccess.get(e1.getId());
        assertEquals(e1.getId(), outputEvent.getId());
        assertEquals(e1.getAssociatedUsername(), 
                        outputEvent.getAssociatedUsername());
        assertEquals(e1.getPersonId(), outputEvent.getPersonId());
        assertEquals(e1.getLatitude(), outputEvent.getLatitude(), 0);
        assertEquals(e1.getLongitude(), outputEvent.getLongitude(), 0);
        assertEquals(e1.getCountry(), outputEvent.getCountry());
        assertEquals(e1.getCity(), outputEvent.getCity());
        assertEquals(e1.getType(), outputEvent.getType());
        assertEquals(e1.getYear(), outputEvent.getYear());
    }

    @Test
    public void loadFailsAndAddsNoDataIfUserMissingFields() throws DBException {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("uname", null));

        LoadResult result = LoadService.load(new LoadRequest(users, null, null));

        assertFalse(result.isSuccess());

        assertNull(userAccess.get(users.get(0).getUsername()));
    }

    @Test
    public void loadFailsAndAddsNoDataIfPersonMissingFields() throws DBException {
        ArrayList<Person> persons = new ArrayList<>();
        persons.add(new Person("id", null));

        LoadResult result = LoadService.load(new LoadRequest(null, persons, null));

        assertFalse(result.isSuccess());

        assertNull(personAccess.get(persons.get(0).getId()));
    }

    @Test
    public void loadFailsAndAddsNoDataIfEventMissingFields() throws DBException {
        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("id", null));

        LoadResult result = LoadService.load(new LoadRequest(null, null, events));

        assertFalse(result.isSuccess());

        assertNull(eventAccess.get(events.get(0).getId()));
    }
}