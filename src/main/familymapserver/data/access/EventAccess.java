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
     * @throws DBException
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
     * @throws DBException
     */
    public static Event get(String eventId, Database db) throws DBException {
        return null;
    }

    /**
     * Gets all events from the database.
     * 
     * @param db the database in which to find the events
     * @return objects containing the events' data
     * @throws DBException
     */
    public static Collection<Event> getAll(Database db) throws DBException {
        return null;
    }

    /**
     * Creates a new table to hold events.
     * 
     * @param db the database in which to create the table
     * @throws DBException
     */
    protected static void createTable(Database db) throws DBException {

    }
    
}