package familymapserver.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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

    protected static final String TEST_DB = "db/test.sqlite";

    private Database db;

    @Before
    public void setup() throws DBException {
        File dbFile = new File(TEST_DB);
        if (dbFile.exists()) {
            dbFile.delete();
        }

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
    public void constructorRunsNoException() throws DBException {
        Database db = new Database();
        assertNull(db.getSQLConnection());
    }

    @Test
    public void openOpensConnection() throws DBException, SQLException {
        db.open();
        assertFalse(db.getSQLConnection().isClosed());
    }

    @Test (expected = DBException.class)
    public void openThrowsExceptionIfOpen() throws DBException {
        db.open();
        db.open();
    }

    @Test
    public void closeClosesConnection() throws DBException, SQLException {
        db.open();
        db.close();
        assertNull(db.getSQLConnection());
    }

    @Test (expected = DBException.class)
    public void closeThrowsExceptionIfClosed() throws DBException {
        db.close();
    }

    @Test
    public void closeRollsBackChanges() throws DBException, SQLException {
        db.open(TEST_DB);
        
        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("CREATE TABLE test (test_field INTEGER)");
        ps.executeUpdate();

        db.close();

        db.open(TEST_DB);

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
        db.open(TEST_DB);

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("CREATE TABLE test (test_field INTEGER)");
        ps.executeUpdate();
        ps.close();

        db.commit();
        db.close();

        db.open(TEST_DB);

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
        db.commit();
    }

    @Test
    public void rollbackRollsBackChanges() throws DBException, SQLException {
        db.open(TEST_DB);

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("CREATE TABLE test (test_field INTEGER)");
        ps.executeUpdate();
        ps.close();

        db.rollback();
        db.close();

        db.open(TEST_DB);

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
        db.rollback();
    }

    @Test
    public void clearRemovesAllDataFromTables() throws DBException, SQLException {
        db.open(TEST_DB);

        UserAccess userAccess = new UserAccess(db);
        PersonAccess personAccess = new PersonAccess(db);

        userAccess.createTable();
        userAccess.add(new User("test", "pw", "a", "b", "c", "d", "e"));

        AuthTokenAccess.createTable(db);
        AuthTokenAccess.add(new AuthToken("a", "b"), db);

        personAccess.createTable();
        personAccess.add(new Person("a", "b", "c", "d", "e", "f", "g", "h"));

        EventAccess.createTable(db);
        EventAccess.add(new Event("a", "b", "c", 0, 1, "d", "e", "f", 2), db);

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
        db.clear();
    }

}