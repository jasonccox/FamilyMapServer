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

    private Database d;

    @Before
    public void setup() throws DBException {
        File dbFile = new File(TEST_DB);
        if (dbFile.exists()) {
            dbFile.delete();
        }

        d = new Database();
    }

    @After
    public void cleanup() throws DBException, SQLException {
        if (d == null) {
            return;
        }

        Connection c = d.getSQLConnection();

        if (c != null && !c.isClosed()) {
            d.close();
        }

        d = null;
    }

    @Test
    public void constructorRunsNoException() throws DBException {
        Database db = new Database();
        assertNull(db.getSQLConnection());
    }

    @Test
    public void openOpensConnection() throws DBException, SQLException {
        d.open();
        assertFalse(d.getSQLConnection().isClosed());
    }

    @Test (expected = DBException.class)
    public void openThrowsExceptionIfOpen() throws DBException {
        d.open();
        d.open();
    }

    @Test
    public void closeClosesConnection() throws DBException, SQLException {
        d.open();
        d.close();
        assertNull(d.getSQLConnection());
    }

    @Test (expected = DBException.class)
    public void closeThrowsExceptionIfClosed() throws DBException {
        d.close();
    }

    @Test
    public void closeRollsBackChanges() throws DBException, SQLException {
        d.open(TEST_DB);
        
        Connection c = d.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("CREATE TABLE test (test_field INTEGER)");
        ps.executeUpdate();

        d.close();

        d.open(TEST_DB);

        c = d.getSQLConnection();
        ps = c.prepareStatement("SELECT count(*) FROM sqlite_master WHERE type = 'table'");
        ResultSet rs = ps.executeQuery();
        assertEquals(0, rs.getInt(1));

        rs.close();
        ps.close();
        d.close();
    }

    @Test
    public void commitSavesChanges() throws DBException, SQLException {
        d.open(TEST_DB);

        Connection c = d.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("CREATE TABLE test (test_field INTEGER)");
        ps.executeUpdate();
        ps.close();

        d.commit();
        d.close();

        d.open(TEST_DB);

        c = d.getSQLConnection();
        ps = c.prepareStatement("SELECT count(*) FROM sqlite_master WHERE type = 'table'");
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));

        rs.close();
        ps.close();
        d.close();
    }

    @Test (expected = DBException.class)
    public void commitThrowsExceptionIfClosed() throws DBException {
        d.commit();
    }

    @Test
    public void rollbackRollsBackChanges() throws DBException, SQLException {
        d.open(TEST_DB);

        Connection c = d.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("CREATE TABLE test (test_field INTEGER)");
        ps.executeUpdate();
        ps.close();

        d.rollback();
        d.close();

        d.open(TEST_DB);

        c = d.getSQLConnection();
        ps = c.prepareStatement("SELECT count(*) FROM sqlite_master WHERE type = 'table'");
        ResultSet rs = ps.executeQuery();
        assertEquals(0, rs.getInt(1));

        rs.close();
        ps.close();
        d.close();
    }

    @Test (expected = DBException.class)
    public void rollbackThrowsExceptionIfClosed() throws DBException {
        d.rollback();
    }

    @Test
    public void clearRemovesAllDataFromTables() throws DBException, SQLException {
        d.open(TEST_DB);

        UserAccess.createTable(d);
        UserAccess.add(new User("test", "pw", "a", "b", "c", "d", "e"), d);

        AuthTokenAccess.createTable(d);
        AuthTokenAccess.add(new AuthToken("a", "b"), d);

        PersonAccess.createTable(d);
        PersonAccess.add(new Person("a", "b", "c", "d", "e", "f", "g", "h"), d);

        EventAccess.createTable(d);
        EventAccess.add(new Event("a", "b", "c", 0, 1, "d", "e", "f", 2), d);

        d.clear();

        Connection c = d.getSQLConnection();
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

        d.close();
    }

    @Test (expected = DBException.class)
    public void clearThrowsExceptionIfClosed() throws DBException {
        d.clear();
    }

}