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
        try (Database db = new Database()) {

            loadUsers(request.getUsers(), db);
            loadPersons(request.getPersons(), db);
            loadEvents(request.getEvents(), db);

            db.commit();

        } catch (DBException e) {
            LOG.log(Level.WARNING, "Load failed.", e);
            
            return new LoadResult(ApiResult.INVALID_REQUEST_DATA_ERROR + 
                                  ": " + e.getMessage() + 
                                  " No data was loaded into the database.");
        }

        return new LoadResult(request.getUsers().size(), 
                              request.getPersons().size(), 
                              request.getEvents().size());
    }

    /**
     * Adds users to the database.
     * 
     * @param users the users to be added
     * @param db the database to which to add the users
     * @throws DBException if a daortabase error occurs
     */
    private static void loadUsers(Collection<User> users, Database db) 
        throws DBException {

        UserAccess userAccess = new UserAccess(db);

        for (User u : users) {
            userAccess.add(u);
        }
    }

    /**
     * Adds persons to the database.
     * 
     * @param persons the persons to be added
     * @param db the database to which to add the persons
     * @throws DBException if a daortabase error occurs
     */
    private static void loadPersons(Collection<Person> persons, Database db) 
        throws DBException {

        PersonAccess personAccess = new PersonAccess(db);

        for (Person p : persons) {
            personAccess.add(p);
        }
    }

    /**
     * Adds events to the database.
     * 
     * @param events the events to be added
     * @param db the database to which to add the events
     * @throws DBException if a daortabase error occurs
     */
    private static void loadEvents(Collection<Event> events, Database db) 
        throws DBException {

        EventAccess eventAccess = new EventAccess(db);

        for (Event e : events) {
            eventAccess.add(e);
        }
    }

}