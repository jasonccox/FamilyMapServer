package familymapserver.api.request;

import java.util.Collection;

import familymapserver.data.model.Event;
import familymapserver.data.model.Person;
import familymapserver.data.model.User;

/**
 * A request to the <code>/load</code> route. It is a request to
 * clear all data from the database and load in new users, persons,
 * and events.
 */
public class LoadRequest extends ApiRequest {

    private Collection<User> users;
    private Collection<Person> persons;
    private Collection<Event> events;

    /**
     * Creates a new LoadRequest.
     * 
     * @param users the users to load into the database
     * @param persons the persons to load into the database
     * @param events the events to load into the database
     */
    public LoadRequest(Collection<User> users, Collection<Person> persons, 
                       Collection<Event> events) {
        super();
    }

    /**
     * @return the users to load into the database
     */
    public Collection<User> getUsers() {
        return users;
    }

    /**
     * @param users the users to load into the database
     */
    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    /**
     * @return the persons to load into the database
     */
    public Collection<Person> getPersons() {
        return persons;
    }

    /**
     * @param persons the persons to load into the database
     */
    public void setPersons(Collection<Person> persons) {
        this.persons = persons;
    }

    /**
     * @return the events to load into the database
     */
    public Collection<Event> getEvents() {
        return events;
    }

    /**
     * @param events the events to load into the database
     */
    public void setEvents(Collection<Event> events) {
        this.events = events;
    }
    
}