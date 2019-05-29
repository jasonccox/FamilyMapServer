package familymapserver.data.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import familymapserver.data.model.Event;

/**
 * Contains methods for accessing event data in the database.
 */
public class EventAccess extends Access {

    private static final Logger LOG = Logger.getLogger("fms");

    private static final String CREATE_STMT = 
        "CREATE TABLE IF NOT EXISTS event (" + 
            "id              VARCHAR(255) NOT NULL PRIMARY KEY, " +
            "assoc_username  VARCHAR(255) NOT NULL, " +
            "person_id       VARCHAR(255) NOT NULL, " +
            "latitude        FLOAT NOT NULL, " + 
            "longitude       FLOAT NOT NULL, " +
            "country         VARCHAR(255) NOT NULL, " +
            "city            VARCHAR(255) NOT NULL, " +
            "type            VARCHAR(255) NOT NULL, " +
            "year            INTEGER NOT NULL, " +
            "FOREIGN KEY (assoc_username) REFERENCES user(username), " + 
            "FOREIGN KEY (person_id) REFERENCES person(id)" + 
        ")";

    /**
     * Creates a new EventAccess object.
     * 
     * @param db the database on which to operate
     */
    public EventAccess(Database db) {
        super(db);
    }

    /**
     * Adds a new event to the database.
     * 
     * @param event the event to be added to the database
     * @throws DBException if a field is missing or invalid, if the database is 
     *                     not open, or if another database error occurs
     */
    public void add(Event event) throws DBException {
        int numAdded = 0;

        Connection conn = getOpenConnection();

        String sql = "INSERT INTO event (id, assoc_username, person_id, latitude, " +
                                        "longitude, country, city, type, year) " +
                     "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ? " +
                     "WHERE NOT EXISTS (SELECT 1 FROM event WHERE id = ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, event.getId());
            ps.setString(2, event.getAssociatedUsername());
            ps.setString(3, event.getPersonId());
            ps.setFloat(4, event.getLatitude());
            ps.setFloat(5, event.getLongitude());
            ps.setString(6, event.getCountry());
            ps.setString(7, event.getCity());
            ps.setString(8, event.getType());
            ps.setInt(9, event.getYear());
            ps.setString(10, event.getId());

            numAdded = ps.executeUpdate();
        } catch (SQLException sqle) {
            DBException dbe = new DBException(sqle);
            LOG.throwing("EventAccess", "add", dbe);
            throw dbe;
        } 

        if (numAdded < 1) {
            DBException dbe = new DBException("The event id '" + event.getId() + 
                                              "' is already in use.");
            LOG.throwing("EventAccess", "add", dbe);
            throw dbe;
        }
    }

     /**
     * Gets a event from the database.
     * 
     * @param eventId the id of the event to be found
     * @return an object containing the event's data, or null if no event was 
     *         found with the given id
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    public Event get(String eventId) throws DBException {
        Event event = null;

        Connection conn = getOpenConnection();

        String sql = "SELECT id, assoc_username, person_id, latitude, longitude, " + 
                            "country, city, type, year " +
                     "FROM event " +
                     "WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, eventId);

            // using a nested try with resources because ResultSet can throw an
            // exception on close
            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) { // no results - event doesn't exist
                    return null;
                }

                event = new Event(rs.getString(1), rs.getString(2));
                event.setPersonId(rs.getString(3));
                event.setLatitude(rs.getFloat(4));
                event.setLongitude(rs.getFloat(5));
                event.setCountry(rs.getString(6));
                event.setCity(rs.getString(7));
                event.setType(rs.getString(8));
                event.setYear(rs.getInt(9));
            }

        } catch (SQLException sqle) {
            DBException dbe = new DBException(sqle);
            LOG.throwing("EventAccess", "get", dbe);
            throw dbe;
        }

        return event;
    }

    /**
     * Gets all events associated with a specific user from the database.
     * 
     * @param username the username of the user whose events should be retrieved
     * @return objects containing the events' data
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    public Collection<Event> getAll(String username) throws DBException {
        ArrayList<Event> events = new ArrayList<>();

        Connection conn = getOpenConnection();

        String sql = "SELECT id, assoc_username, person_id, latitude, longitude, " +
                            "country, city, type, year " +
                     "FROM event " +
                     "WHERE assoc_username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            // using a nested try with resources because ResultSet can throw an
            // exception on close
            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Event event = new Event(rs.getString(1), rs.getString(2));
                    event.setPersonId(rs.getString(3));
                    event.setLatitude(rs.getFloat(4));
                    event.setLongitude(rs.getFloat(5));
                    event.setCountry(rs.getString(6));
                    event.setCity(rs.getString(7));
                    event.setType(rs.getString(8));
                    event.setYear(rs.getInt(9));

                    events.add(event);
                }
            }

        } catch (SQLException sqle) {
            DBException dbe = new DBException(sqle);
            LOG.throwing("EventAccess", "getAll", dbe);
            throw dbe;
        }

        return events;
    }

    /**
     * Removes all events from the database.
     * 
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    public void clear() throws DBException {
        executeUpdate("DELETE FROM event");
    }

    /**
     * Removes all events associated with a given user.
     * 
     * @param username the user's username
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    public void clearAll(String username) throws DBException {
        Connection conn = getOpenConnection();

        String sql = "DELETE FROM event WHERE assoc_username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) { 
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            DBException dbe = new DBException(sqle);
            LOG.throwing("EventAccess", "clearAll", dbe);
            throw dbe;
        }

    }

    /**
     * Creates a new table to hold events if it doesn't already exist in the 
     * database.
     * 
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    protected void createTableIfMissing() throws DBException {
        executeUpdate(CREATE_STMT);
    }
    
}