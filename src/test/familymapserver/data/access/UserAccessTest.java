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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import familymapserver.data.model.User;

public class UserAccessTest {

    private Database db;
    private UserAccess userAccess;
    private User u = new User("uname", "pw", "uname@email.com", "fname", "lname", "m", "pid");

    @Before
    public void setup() throws DBException {
        File testDB = new File(DatabaseTest.TEST_DB);
        if (testDB.exists()) {
            testDB.delete();
        }
        
        db = new Database();
        db.open(DatabaseTest.TEST_DB);

        userAccess = new UserAccess(db);
    }

    @After
    public void cleanup() throws DBException, SQLException {
        if (db.getSQLConnection() != null && !db.getSQLConnection().isClosed()) {
            db.close();
        }
    }

    @Test
    public void addReturnsTrueAndAddsUserToDB() throws SQLException, DBException {
        userAccess.createTable();

        assertTrue(userAccess.add(u));

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count() FROM user");
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();
        
        ps = c.prepareStatement("SELECT username, password, email, first_name, last_name, gender, person_id FROM user");
        rs = ps.executeQuery();

        assertTrue(rs.next());
        assertEquals(u.getUsername(), rs.getString(1));
        assertEquals(u.getPassword(), rs.getString(2));
        assertEquals(u.getEmail(), rs.getString(3));
        assertEquals(u.getFirstName(), rs.getString(4));
        assertEquals(u.getLastName(), rs.getString(5));
        assertEquals(u.getGender(), rs.getString(6));
        assertEquals(u.getPersonId(), rs.getString(7));

        rs.close();
        ps.close();
    }

    @Test
    public void addReturnsFalseAndDoesNotAddUserIfUsernameTaken() throws DBException, SQLException {
        userAccess.createTable();
        
        userAccess.add(u);

        User u2 = new User(u.getUsername(), "pw2", "uname2@email.com", "fname2", "lname2", "f", "pid2");
        assertFalse(userAccess.add(u2));

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count() FROM user WHERE username = ?");
        ps.setString(1, u.getUsername());
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfUsernameMissing() throws DBException {
        userAccess.createTable();

        User u2 = new User(null, "p", "e", "f", "l", "m", "p");

        userAccess.add(u2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfPasswordMissing() throws DBException {
        userAccess.createTable();

        User u2 = new User("u", null, "e", "f", "l", "m", "p");

        userAccess.add(u2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfEmailMissing() throws DBException {
        userAccess.createTable();

        User u2 = new User("u", "p", null, "f", "l", "m", "p");

        userAccess.add(u2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfFirstNameMissing() throws DBException {
        userAccess.createTable();

        User u2 = new User("u", "p", "e", null, "l", "m", "p");

        userAccess.add(u2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfLastNameMissing() throws DBException {
        userAccess.createTable();

        User u2 = new User("u", "p", "e", "f", null, "m", "p");

        userAccess.add(u2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfGenderMissing() throws DBException {
        userAccess.createTable();

        User u2 = new User("u", "p", "e", "f", "l", null, "p");

        userAccess.add(u2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfPersonIdMissing() throws DBException {
        userAccess.createTable();

        User u2 = new User("u", "p", "e", "f", "l", "m", null);

        userAccess.add(u2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfGenderInvalid() throws DBException {
        userAccess.createTable();

        User u2 = new User("u", "p", "e", "f", "l", "g", "p");

        userAccess.add(u2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        userAccess.add(u);
    }

    @Test
    public void getReturnsUserIfInDB() throws DBException {
        userAccess.createTable();
        
        userAccess.add(u);
        User result = userAccess.get(u.getUsername());

        assertEquals(u.getUsername(), result.getUsername());
        assertEquals(u.getPassword(), result.getPassword());
        assertEquals(u.getFirstName(), result.getFirstName());
        assertEquals(u.getLastName(), result.getLastName());
        assertEquals(u.getEmail(), result.getEmail());
        assertEquals(u.getGender(), result.getGender());
        assertEquals(u.getPersonId(), result.getPersonId());
    }

    @Test
    public void getReturnsNullIfNotInDB() throws DBException {
        userAccess.createTable();
        
        userAccess.add(u);
        assertNull(userAccess.get("doesntexist"));
    }

    @Test (expected = DBException.class)
    public void getThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        userAccess.get(u.getUsername());
    }

    @Test
    public void clearRemovesAllUsers() throws DBException, SQLException {
        userAccess.createTable();
        
        userAccess.add(u);
        userAccess.clear();

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count() FROM user");
        ResultSet rs = ps.executeQuery();
        assertEquals(0, rs.getInt(1));
        rs.close();
        ps.close();
    }

    @Test
    public void clearDoesntThrowExceptionIfNoUsers() throws DBException {
        userAccess.createTable();
        
        userAccess.clear();
    }

    @Test (expected = DBException.class)
    public void clearThrowsExceptionIfDBClosed() throws DBException {
        userAccess.createTable();
        
        db.close();

        userAccess.clear();
    }

    @Test
    public void createTableCreatesUserTable() throws DBException, SQLException {
        userAccess.createTable();

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count(*) FROM sqlite_master WHERE type = 'table' AND name = 'user'");
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));

        rs.close();
        ps.close();
    }

    @Test (expected = DBException.class)
    public void createTableThrowsExceptionIfTableExists() throws DBException {
        userAccess.createTable();
        userAccess.createTable();
    }

    @Test (expected = DBException.class)
    public void createTableThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        userAccess.createTable();
    }
}