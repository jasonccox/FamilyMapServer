package familymapserver.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import familymapserver.data.model.Event;

public class EventAccessTest {

    private Database db;
    private EventAccess eventAccess;
    private Event event = new Event("id", "uname", "pid", 1, -2, "USA", "Chicago", "birth", 2000);

    @Before
    public void setup() throws DBException {
        File testDB = new File(DatabaseTest.TEST_DB);
        if (testDB.exists()) {
            testDB.delete();
        }
        
        db = new Database();
        db.open(DatabaseTest.TEST_DB);

        eventAccess = new EventAccess(db);
    }

    @After
    public void cleanup() throws DBException, SQLException {
        if (db.getSQLConnection() != null && !db.getSQLConnection().isClosed()) {
            db.close();
        }
    }

    @Test
    public void addReturnsTrueAndAddsEventToDB() throws SQLException, DBException {
        eventAccess.createTable();

        assertTrue(eventAccess.add(event));

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count() FROM event");
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();
        
        ps = c.prepareStatement("SELECT id, assoc_username, person_id, latitude, longitude, country, city, type, year FROM event");
        rs = ps.executeQuery();

        assertTrue(rs.next());
        assertEquals(event.getId(), rs.getString(1));
        assertEquals(event.getAssociatedUsername(), rs.getString(2));
        assertEquals(event.getPersonId(), rs.getString(3));
        assertEquals(event.getLatitude(), rs.getFloat(4), 0);
        assertEquals(event.getLongitude(), rs.getFloat(5), 0);
        assertEquals(event.getCountry(), rs.getString(6));
        assertEquals(event.getCity(), rs.getString(7));
        assertEquals(event.getType(), rs.getString(8));
        assertEquals(event.getYear(), rs.getInt(9));

        rs.close();
        ps.close();
    }

    @Test
    public void addReturnsFalseAndDoesNotAddEventIfIdTaken() throws DBException, SQLException {
        eventAccess.createTable();
        
        eventAccess.add(event);

        Event e2 = new Event(event.getId(), "uname2", "pid2", 2, -1, "Mexico", "Jalisco", "death", 2010);
        assertFalse(eventAccess.add(e2));

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count() FROM event WHERE id = ?");
        ps.setString(1, event.getId());
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfIdMissing() throws DBException {
        eventAccess.createTable();

        Event e2 = new Event(null, "u", "p", 1, 1, "c", "c", "t", 1);

        eventAccess.add(e2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfAssocUsernameMissing() throws DBException {
        eventAccess.createTable();

        Event e2 = new Event("i", null, "p", 1, 1, "c", "c", "t", 1);

        eventAccess.add(e2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfPersonIdMissing() throws DBException {
        eventAccess.createTable();

        Event e2 = new Event("i", "u", null, 1, 1, "c", "c", "t", 1);

        eventAccess.add(e2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfCountryMissing() throws DBException {
        eventAccess.createTable();

        Event e2 = new Event("i", "u", "p", 1, 1, null, "c", "t", 1);

        eventAccess.add(e2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfCityMissing() throws DBException {
        eventAccess.createTable();

        Event e2 = new Event("i", "u", "p", 1, 1, "c", null, "t", 1);

        eventAccess.add(e2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfTypeMissing() throws DBException {
        eventAccess.createTable();

        Event e2 = new Event("i", "u", "p", 1, 1, "c", "c", null, 1);

        eventAccess.add(e2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        eventAccess.add(event);
    }

    @Test
    public void getReturnsEventIfInDB() throws DBException {
        eventAccess.createTable();
        
        eventAccess.add(event);
        Event result = eventAccess.get(event.getId());

        assertEquals(event.getId(), result.getId());
        assertEquals(event.getAssociatedUsername(), result.getAssociatedUsername());
        assertEquals(event.getPersonId(), result.getPersonId());
        assertEquals(event.getLatitude(), result.getLatitude(), 0);
        assertEquals(event.getLongitude(), result.getLongitude(), 0);
        assertEquals(event.getCountry(), result.getCountry());
        assertEquals(event.getCity(), result.getCity());
        assertEquals(event.getType(), result.getType());
        assertEquals(event.getYear(), result.getYear());
    }

    @Test
    public void getReturnsNullIfNotInDB() throws DBException {
        eventAccess.createTable();
        
        eventAccess.add(event);
        assertNull(eventAccess.get("doesntexist"));
    }

    @Test (expected = DBException.class)
    public void getThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        eventAccess.get(event.getId());
    }

    @Test
    public void getAllReturnsMatchingEvents() throws DBException {
        eventAccess.createTable();

        Event e2 = new Event("i2", "u2", "p2", 2, 2, "c2", "c2", "t2", 2);
        Event e3 = new Event("i3", event.getAssociatedUsername(), "p3", 3, 3, "c3", "c3", "t3", 3);
        Event e4 = new Event("i4", "u4", "p4", 4, 4, "c4", "c4", "t4", 4);

        eventAccess.add(event);
        eventAccess.add(e2);
        eventAccess.add(e3);
        eventAccess.add(e4);

        Collection<Event> result = eventAccess.getAll(event.getAssociatedUsername());

        assertEquals(2, result.size());
        
        Iterator<Event> i = result.iterator();
        Event r1 = i.next();
        Event r2 = i.next();

        System.out.println(r1.getId());
        System.out.println(r2.getId());
        
        if (event.getId().equals(r1.getId())) {
            assertEquals(e3.getId(), r2.getId());
        } else {
            assertEquals(event.getId(), r2.getId());
            assertEquals(e3.getId(), r1.getId());
        }
    }

    @Test
    public void getAllReturnsEmptyCollectionIfNoMatchingEvents() throws DBException {
        eventAccess.createTable();

        Event e2 = new Event("i2", "u2", "p2", 2, 2, "c2", "c2", "t2", 2);
        Event e3 = new Event("i3", event.getAssociatedUsername(), "p3", 3, 3, "c3", "c3", "t3", 3);
        Event e4 = new Event("i4", "u4", "p4", 4, 4, "c4", "c4", "t4", 4);

        eventAccess.add(event);
        eventAccess.add(e2);
        eventAccess.add(e3);
        eventAccess.add(e4);

        Collection<Event> result = eventAccess.getAll("nomatch");

        assertEquals(0, result.size());
    }

    @Test (expected = DBException.class)
    public void getAllThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        eventAccess.getAll("uname");
    }

    @Test
    public void clearRemovesAllEvents() throws DBException, SQLException {
        eventAccess.createTable();
        
        eventAccess.add(event);
        eventAccess.clear();

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count() FROM event");
        ResultSet rs = ps.executeQuery();
        assertEquals(0, rs.getInt(1));
        rs.close();
        ps.close();
    }

    @Test
    public void clearDoesntThrowExceptionIfNoEvents() throws DBException {
        eventAccess.createTable();
        
        eventAccess.clear();
    }

    @Test (expected = DBException.class)
    public void clearThrowsExceptionIfDBClosed() throws DBException {
        eventAccess.createTable();
        
        db.close();

        eventAccess.clear();
    }

    @Test
    public void createTableCreatesEventTable() throws DBException, SQLException {
        eventAccess.createTable();

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count(*) FROM sqlite_master WHERE type = 'table' AND name = 'event'");
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));

        rs.close();
        ps.close();
    }

    @Test (expected = DBException.class)
    public void createTableThrowsExceptionIfTableExists() throws DBException {
        eventAccess.createTable();
        eventAccess.createTable();
    }

    @Test (expected = DBException.class)
    public void createTableThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        eventAccess.createTable();
    }

}