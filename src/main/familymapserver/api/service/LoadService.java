package familymapserver.api.service;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import familymapserver.api.request.LoadRequest;
import familymapserver.api.result.ApiResult;
import familymapserver.api.result.LoadResult;
import familymapserver.data.access.DBException;
import familymapserver.data.access.Database;
import familymapserver.data.access.EventAccess;
import familymapserver.data.access.PersonAccess;
import familymapserver.data.access.UserAccess;
import familymapserver.data.model.Event;
import familymapserver.data.model.Person;
import familymapserver.data.model.User;

/**
 * Contains methods providing functionality of the <code>/load</code> API route.
 * It replaces all data with user-specified data.
 */
public class LoadService {

    private static final Logger LOG = Logger.getLogger("fms");

    /**
     * Clears all data from the database and then loads the provided user, person, 
     * and event data into the database.
     * 
     * @param request the load request, which contains the data to be loaded
     * @return the result of the operation
     */
    public static LoadResult load(LoadRequest request) {
        Collection<User> users = request.getUsers();
        Collection<Person> persons = request.getPersons();
        Collection<Event> events = request.getEvents();

        int numUsersAdded, numPersonsAdded, numEventsAdded;

        try (Database db = new Database()) {

            db.clear();

            numUsersAdded = loadUsers(users, db);
            numPersonsAdded = loadPersons(persons, db);
            numEventsAdded = loadEvents(events, db);

            db.commit();

        } catch (DBException e) {
            LOG.log(Level.WARNING, "Load failed.", e);
            
            return new LoadResult(ApiResult.INVALID_REQUEST_DATA_ERROR + 
                                  ": " + e.getMessage() + 
                                  " No data was loaded into the database.");
        }

        return new LoadResult(numUsersAdded, numPersonsAdded, numEventsAdded);
    }

    /**
     * Adds users to the database.
     * 
     * @param users the users to be added
     * @param db the database to which to add the users
     * @throws DBException if a database error occurs
     * @return the number of users successfully added to the database
     */
    private static int loadUsers(Collection<User> users, Database db) 
        throws DBException {
        
        if (users == null) {
            return 0;
        }

        UserAccess userAccess = new UserAccess(db);

        for (User u : users) {
            userAccess.add(u);
        }

        return users.size();
    }

    /**
     * Adds persons to the database.
     * 
     * @param persons the persons to be added
     * @param db the database to which to add the persons
     * @throws DBException if a database error occurs
     * @return the number of persons successfully added to the database
     */
    private static int loadPersons(Collection<Person> persons, Database db) 
        throws DBException {
        
        if (persons == null) {
            return 0;
        }

        PersonAccess personAccess = new PersonAccess(db);

        for (Person p : persons) {
            personAccess.add(p);
        }

        return persons.size();
    }

    /**
     * Adds events to the database.
     * 
     * @param events the events to be added
     * @param db the database to which to add the events
     * @throws DBException if a database error occurs
     * @return the number of events successfully added to the database
     */
    private static int loadEvents(Collection<Event> events, Database db) 
        throws DBException {

        if (events == null) {
            return 0;
        }

        EventAccess eventAccess = new EventAccess(db);

        for (Event e : events) {
            eventAccess.add(e);
        }

        return events.size();
    }

}