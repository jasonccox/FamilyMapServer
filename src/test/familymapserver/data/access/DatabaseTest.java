package familymapserver.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import familymapserver.data.model.AuthToken;
import familymapserver.data.model.Event;
import familymapserver.data.model.Person;
import familymapserver.data.model.User;

public class DatabaseTest {

    public static final String TEST_DB = "db/test.sqlite";

    private Database db;

    @Before
    public void setup() throws DBException {
        File dbFile = new File(TEST_DB);
        if (dbFile.exists()) {
            dbFile.delete();
        }

        Database.testDBPath = TEST_DB;
        db = new Database();
    }

    @After
    public void cleanup() throws DBException, SQLException {
        if (db == null) {
            return;
        }

        Connection c = db.getSQLConnection();

        if (c != null && !c.isClosed()) {
            db.close();
        }

        db = null;
    }

    @Test
    public void constructorRunsNoException() throws DBException, SQLException {
        Database db2 = new Database();
        assertNotNull(db2.getSQLConnection());
        assertFalse(db2.getSQLConnection().isClosed());
        db2.close();
    }

    @Test
    public void closeSetsConnectionToNull() throws DBException, SQLException {
        db.close();
        assertNull(db.getSQLConnection());
    }

    @Test (expected = DBException.class)
    public void closeThrowsExceptionIfClosed() throws DBException {
        db.close();
        db.close();
    }

    @Test
    public void closeRollsBackChanges() throws DBException, SQLException {        
        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("CREATE TABLE test (test_field INTEGER)");
        ps.executeUpdate();

        db.close();

        db = new Database();

        c = db.getSQLConnection();
        ps = c.prepareStatement("SELECT count(*) FROM sqlite_master WHERE type = 'table'");
        ResultSet rs = ps.executeQuery();
        assertEquals(0, rs.getInt(1));

        rs.close();
        ps.close();
        db.close();
    }

    @Test
    public void commitSavesChanges() throws DBException, SQLException {
        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("CREATE TABLE test (test_field INTEGER)");
        ps.executeUpdate();
        ps.close();

        db.commit();
        db.close();

        db = new Database();

        c = db.getSQLConnection();
        ps = c.prepareStatement("SELECT count(*) FROM sqlite_master WHERE type = 'table'");
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));

        rs.close();
        ps.close();
        db.close();
    }

    @Test (expected = DBException.class)
    public void commitThrowsExceptionIfClosed() throws DBException {
        db.close();
        db.commit();
    }

    @Test
    public void rollbackRollsBackChanges() throws DBException, SQLException {
        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("CREATE TABLE test (test_field INTEGER)");
        ps.executeUpdate();
        ps.close();

        db.rollback();
        db.close();

        db = new Database();

        c = db.getSQLConnection();
        ps = c.prepareStatement("SELECT count(*) FROM sqlite_master WHERE type = 'table'");
        ResultSet rs = ps.executeQuery();
        assertEquals(0, rs.getInt(1));

        rs.close();
        ps.close();
        db.close();
    }

    @Test (expected = DBException.class)
    public void rollbackThrowsExceptionIfClosed() throws DBException {
        db.close();
        db.rollback();
    }

    @Test
    public void clearRemovesAllDataFromTables() throws DBException, SQLException {
        UserAccess userAccess = new UserAccess(db);
        AuthTokenAccess authTokenAccess = new AuthTokenAccess(db);
        PersonAccess personAccess = new PersonAccess(db);
        EventAccess eventAccess = new EventAccess(db);

        User user = new User("uname", "pw");
        user.setEmail("email");
        user.setFirstName("f");
        user.setLastName("l");
        user.setGender("m");
        user.setPersonId("pid");

        userAccess.createTableIfMissing();
        userAccess.add(user);

        authTokenAccess.createTableIfMissing();
        authTokenAccess.add(new AuthToken("a", "b"));

        Person person = new Person("id", "uname");
        person.setFirstName("f");
        person.setLastName("l");
        person.setGender("m");
        person.setFather("dad");
        person.setMother("mom");
        person.setSpouse("spouse");

        personAccess.createTableIfMissing();
        personAccess.add(person);

        Event event = new Event("id", "uname");
        event.setPersonId("pid");
        event.setLatitude(1);
        event.setLongitude(1);
        event.setCountry("USA");
        event.setCity("Provo");
        event.setType("birth");
        event.setYear(2000);

        eventAccess.createTableIfMissing();
        eventAccess.add(event);

        db.clear();

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count() FROM user");
        ResultSet rs = ps.executeQuery();
        assertEquals(0, rs.getInt(1));
        rs.close();
        ps.close();

        ps = c.prepareStatement("SELECT count() FROM auth_token");
        rs = ps.executeQuery();
        assertEquals(0, rs.getInt(1));
        rs.close();
        ps.close();

        ps = c.prepareStatement("SELECT count() FROM person");
        rs = ps.executeQuery();
        assertEquals(0, rs.getInt(1));
        rs.close();
        ps.close();

        ps = c.prepareStatement("SELECT count() FROM event");
        rs = ps.executeQuery();
        assertEquals(0, rs.getInt(1));
        rs.close();
        ps.close();

        db.close();
    }

    @Test (expected = DBException.class)
    public void clearThrowsExceptionIfClosed() throws DBException {
        db.close();
        db.clear();
    }

    @Test
    public void createTableCreatesTables() throws DBException, SQLException {
        db.createTablesIfMissing();

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count(*) FROM sqlite_master " +
                                                  "WHERE type = 'table' AND name = 'user'");
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();

        ps = c.prepareStatement("SELECT count(*) FROM sqlite_master " +
                                "WHERE type = 'table' AND name = 'auth_token'");
        rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();

        ps = c.prepareStatement("SELECT count(*) FROM sqlite_master " +
                                "WHERE type = 'table' AND name = 'person'");
        rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();

        ps = c.prepareStatement("SELECT count(*) FROM sqlite_master " +
                                "WHERE type = 'table' AND name = 'event'");
        rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();
    }

    @Test
    public void createTablesThrowsNoExceptionIfTablesExist() throws DBException {
        db.createTablesIfMissing();
        db.createTablesIfMissing();
    }

    @Test (expected = DBException.class)
    public void createTablesThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        db.createTablesIfMissing();
    }

    @Test
    public void initCreatesTablesIfMissingAndCommits() throws DBException, SQLException {
        db.init();
        db.close();

        db = new Database();

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count(*) FROM sqlite_master " +
                                                  "WHERE type = 'table' AND name = 'user'");
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();

        ps = c.prepareStatement("SELECT count(*) FROM sqlite_master " +
                                "WHERE type = 'table' AND name = 'auth_token'");
        rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();

        ps = c.prepareStatement("SELECT count(*) FROM sqlite_master " +
                                "WHERE type = 'table' AND name = 'person'");
        rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();

        ps = c.prepareStatement("SELECT count(*) FROM sqlite_master " +
                                "WHERE type = 'table' AND name = 'event'");
        rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();
    }

    @Test
    public void initThrowsNoExceptionIfTablesExist() throws DBException {
        db.createTablesIfMissing();
        db.init();
    }

    @Test (expected = DBException.class)
    public void initThrowsExceptionIfDBClosed() throws DBException {
        db.close();
        db.init();
    }

}