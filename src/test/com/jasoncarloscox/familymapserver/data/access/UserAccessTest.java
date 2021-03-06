package com.jasoncarloscox.familymapserver.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jasoncarloscox.familymapserver.data.access.DBException;
import com.jasoncarloscox.familymapserver.data.access.Database;
import com.jasoncarloscox.familymapserver.data.access.UserAccess;
import com.jasoncarloscox.familymapserver.data.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserAccessTest {

    private Database db;
    private UserAccess userAccess;
    private User user;

    @Before
    public void setup() throws DBException {
        File testDB = new File(DatabaseTest.TEST_DB);
        if (testDB.exists()) {
            testDB.delete();
        }
        
        Database.testDBPath = DatabaseTest.TEST_DB;
        db = new Database();

        userAccess = new UserAccess(db);

        user = new User("uname", "pw");
        user.setEmail("email");
        user.setFirstName("f");
        user.setLastName("l");
        user.setGender("m");
        user.setPersonId("pid");
    }

    @After
    public void cleanup() throws DBException, SQLException {
        if (db.getSQLConnection() != null && !db.getSQLConnection().isClosed()) {
            db.close();
        }
    }

    @Test
    public void addAddsUserToDB() throws SQLException, DBException {
        userAccess.createTableIfMissing();

        userAccess.add(user);

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count() FROM user");
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();
        
        ps = c.prepareStatement("SELECT username, password, email, first_name, " +
                                "last_name, gender, person_id FROM user");
        rs = ps.executeQuery();

        assertTrue(rs.next());
        assertEquals(user.getUsername(), rs.getString(1));
        assertEquals(user.getPassword(), rs.getString(2));
        assertEquals(user.getEmail(), rs.getString(3));
        assertEquals(user.getFirstName(), rs.getString(4));
        assertEquals(user.getLastName(), rs.getString(5));
        assertEquals(user.getGender(), rs.getString(6));
        assertEquals(user.getPersonId(), rs.getString(7));

        rs.close();
        ps.close();
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfUsernameTaken() throws DBException, SQLException {
        userAccess.createTableIfMissing();
        
        userAccess.add(user);

        User u2 = new User(user.getUsername(), "pw2");
        user.setEmail("email2");
        user.setFirstName("f2");
        user.setLastName("l2");
        user.setGender("f");
        user.setPersonId("pid2");

        userAccess.add(u2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfUsernameMissing() throws DBException {
        userAccess.createTableIfMissing();

        User u2 = new User(null, "pw2");
        user.setEmail("email2");
        user.setFirstName("f2");
        user.setLastName("l2");
        user.setGender("f");
        user.setPersonId("pid2");

        userAccess.add(u2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfPasswordMissing() throws DBException {
        userAccess.createTableIfMissing();

        User u2 = new User("uname2", null);
        user.setEmail("email2");
        user.setFirstName("f2");
        user.setLastName("l2");
        user.setGender("f");
        user.setPersonId("pid2");

        userAccess.add(u2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfEmailMissing() throws DBException {
        userAccess.createTableIfMissing();

        User u2 = new User("uname2", "pw2");
        user.setFirstName("f2");
        user.setLastName("l2");
        user.setGender("f");
        user.setPersonId("pid2");

        userAccess.add(u2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfFirstNameMissing() throws DBException {
        userAccess.createTableIfMissing();

        User u2 = new User("uname2", "pw2");
        user.setEmail("email2");
        user.setLastName("l2");
        user.setGender("f");
        user.setPersonId("pid2");

        userAccess.add(u2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfLastNameMissing() throws DBException {
        userAccess.createTableIfMissing();

        User u2 = new User("uname2", "pw2");
        user.setEmail("email2");
        user.setFirstName("f2");
        user.setGender("f");
        user.setPersonId("pid2");

        userAccess.add(u2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfGenderMissing() throws DBException {
        userAccess.createTableIfMissing();

        User u2 = new User("uname2", "pw2");
        user.setEmail("email2");
        user.setFirstName("f2");
        user.setLastName("l2");
        user.setPersonId("pid2");

        userAccess.add(u2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfPersonIdMissing() throws DBException {
        userAccess.createTableIfMissing();

        User u2 = new User("uname2", "pw2");
        user.setEmail("email2");
        user.setFirstName("f2");
        user.setLastName("l2");
        user.setGender("f");

        userAccess.add(u2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfGenderInvalid() throws DBException {
        userAccess.createTableIfMissing();

        User u2 = new User("uname2", "pw2");
        user.setEmail("email2");
        user.setFirstName("f2");
        user.setLastName("l2");
        user.setGender("a");
        user.setPersonId("pid2");

        userAccess.add(u2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        userAccess.add(user);
    }

    @Test
    public void updatePersonIdUpdatesId() throws DBException {
        userAccess.createTableIfMissing();
        userAccess.add(user);

        user.setPersonId("newID");
        userAccess.updatePersonId(user);

        User storedUser = userAccess.get(user.getUsername());

        assertEquals(user.getPersonId(), storedUser.getPersonId());
    }

    @Test (expected = DBException.class) 
    public void updatePersonIdThrowsExceptionIfDBClosed() throws DBException {
        userAccess.createTableIfMissing();
        db.commit();
        db.close();

        user.setPersonId("newID");
        userAccess.updatePersonId(user);
    }

    @Test
    public void getReturnsUserIfInDB() throws DBException {
        userAccess.createTableIfMissing();
        
        userAccess.add(user);
        User result = userAccess.get(user.getUsername());

        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getFirstName(), result.getFirstName());
        assertEquals(user.getLastName(), result.getLastName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getGender(), result.getGender());
        assertEquals(user.getPersonId(), result.getPersonId());
    }

    @Test
    public void getReturnsNullIfNotInDB() throws DBException {
        userAccess.createTableIfMissing();
        
        userAccess.add(user);
        assertNull(userAccess.get("doesntexist"));
    }

    @Test (expected = DBException.class)
    public void getThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        userAccess.get(user.getUsername());
    }

    @Test
    public void clearRemovesAllUsers() throws DBException, SQLException {
        userAccess.createTableIfMissing();
        
        userAccess.add(user);
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
        userAccess.createTableIfMissing();
        
        userAccess.clear();
    }

    @Test (expected = DBException.class)
    public void clearThrowsExceptionIfDBClosed() throws DBException {
        userAccess.createTableIfMissing();
        
        db.close();

        userAccess.clear();
    }

    @Test
    public void createTableCreatesUserTable() throws DBException, SQLException {
        userAccess.createTableIfMissing();

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count(*) FROM sqlite_master " +
                                                  "WHERE type = 'table' AND name = 'user'");
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));

        rs.close();
        ps.close();
    }

    @Test
    public void createTableThrowsNoExceptionIfTableExists() throws DBException {
        userAccess.createTableIfMissing();
        userAccess.createTableIfMissing();
    }

    @Test (expected = DBException.class)
    public void createTableThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        userAccess.createTableIfMissing();
    }
}