package familymapserver.data.access;

import java.util.Collection;

import familymapserver.data.model.Event;

/**
 * Contains methods for accessing event data in the database.
 */
public class EventAccess {

    /**
     * Adds a new event to the database.
     * 
     * @param event the event to be added to the database
     * @param db the database to which the event should be added
     * @return true if the event was added, false if an event with the same id
     * already existed, thus preventing this one from being added
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public static boolean add(Event event, Database db) throws DBException {
        return false;
    }

     /**
     * Gets a event from the database.
     * 
     * @param eventId the id of the event to be found
     * @param db the database in which to find the event
     * @return an object containing the event's data, or null if no event was found with
     * the given id
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public static Event get(String eventId, Database db) throws DBException {
        return null;
    }

    /**
     * Gets all events associated with a specific user from the database.
     * 
     * @param username the username of the user whose events should be retrieved
     * @param db the database in which to find the events
     * @return objects containing the events' data
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public static Collection<Event> getAll(String username, Database db) throws DBException {
        return null;
    }

    /**
     * Removes all events from the database.
     * 
     * @param db the database from which to remove the events
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public static void clear(Database db) throws DBException {

    }

    /**
     * Creates a new table to hold events.
     * 
     * @param db the database in which to create the table
     * @throws DBException if the database is not open, or if another database error occurs
     */
    protected static void createTable(Database db) throws DBException {

    }
    
}