package familymapserver.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import familymapserver.api.request.FillRequest;
import familymapserver.api.result.FillResult;
import familymapserver.data.access.DBException;
import familymapserver.data.access.Database;
import familymapserver.data.access.DatabaseTest;
import familymapserver.data.access.EventAccess;
import familymapserver.data.access.PersonAccess;
import familymapserver.data.access.UserAccess;
import familymapserver.data.model.Event;
import familymapserver.data.model.Person;
import familymapserver.data.model.User;

public class FillServiceTest {

    private Database db;
    private UserAccess userAccess;
    private PersonAccess personAccess;
    private EventAccess eventAccess;
    private User user;

    @Before
    public void setup() throws DBException {
        Database.testDBPath = DatabaseTest.TEST_DB;
        db = new Database();
        db.init();
        db.clear();

        userAccess = new UserAccess(db);
        personAccess = new PersonAccess(db);
        eventAccess = new EventAccess(db);

        user = new User("uname", "password");
        user.setEmail("email");
        user.setFirstName("firstName");
        user.setLastName("lastName");
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
    public void fillSucceedsIfUsernameValid() throws DBException {
        FillResult result = FillService.fill(new FillRequest(user.getUsername(), 2));

        assertTrue(result.getMessage(), result.isSuccess());

        // the user should now have a person representing him/her

        user = userAccess.get(user.getUsername());
        assertNotNull(user.getPersonId());

        Person userPerson = personAccess.get(user.getPersonId());
        assertNotNull(userPerson);
        assertNotNull(userPerson.getFather());
        assertNotNull(userPerson.getMother());

        // the userPerson should have married parents

        Person dad = personAccess.get(userPerson.getFather());
        Person mom = personAccess.get(userPerson.getMother());
        assertNotNull(dad);
        assertNotNull(mom);
        assertEquals(dad.getId(), mom.getSpouse());
        assertEquals(mom.getId(), dad.getSpouse());

        // the parents should have married grandparents who have no parents

        ArrayList<Person> parents = new ArrayList<>();
        parents.add(dad);
        parents.add(mom);

        for (Person p : parents) {
            assertNotNull(p.getFather());
            assertNotNull(p.getMother());

            Person grandpa = personAccess.get(p.getFather());
            Person grandma = personAccess.get(p.getMother());
            assertNotNull(grandpa);
            assertNotNull(grandma);
            assertEquals(grandpa.getId(), grandma.getSpouse());
            assertEquals(grandma.getId(), grandpa.getSpouse());
            assertNull(grandpa.getFather());
            assertNull(grandpa.getMother());
            assertNull(grandma.getFather());
            assertNull(grandma.getMother());
        }

        // each created person should have at least three events

        Collection<Person> persons = personAccess.getAll(user.getUsername());

        assertEquals(7, persons.size());
        
        Collection<Event> events = eventAccess.getAll(user.getUsername());
        assertTrue(events.size() >= 7*3);

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
    }

    @Test
    public void fillOverwritesPreviousDataExceptUsersPersonId() throws DBException {
        // generate data

        FillResult result = FillService.fill(new FillRequest(user.getUsername(), 2));
        assertTrue(result.getMessage(), result.isSuccess());

        String oldPersonId = userAccess.get(user.getUsername()).getPersonId();

        Collection<Person> persons = personAccess.getAll(user.getUsername());
        Set<String> personIds = new HashSet<>();
        for (Person p : persons) {
             personIds.add(p.getId());
        }

        Collection<Event> events = eventAccess.getAll(user.getUsername());
        Set<String> eventIds = new HashSet<>();
        for (Event e : events) {
            eventIds.add(e.getId());
        }

        db.commit();

        // overwrite data

        result = FillService.fill(new FillRequest(user.getUsername(), 2));
        assertTrue(result.getMessage(), result.isSuccess());

        String newPersonId = userAccess.get(user.getUsername()).getPersonId();

        assertEquals(oldPersonId, newPersonId);

        Collection<Person> newPersons = personAccess.getAll(user.getUsername());
        for (Person p : newPersons) {
            if (oldPersonId.equals(p.getId())) { // skip the person representing the user
                continue;
            }

            assertFalse(personIds.contains(p.getId()));
        }

        Collection<Event> newEvents = eventAccess.getAll(user.getUsername());
        for (Event e : newEvents) {
            assertFalse(eventIds.contains(e.getId()));
        }
    }

    @Test
    public void fillFailsIfUsernameInvalid() {
        FillResult result = FillService.fill(new FillRequest("invalid", 2));

        assertFalse(result.isSuccess());
    }
}