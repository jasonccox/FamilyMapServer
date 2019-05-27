package familymapserver.data.access;

import static org.junit.Assert.assertEquals;
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

import familymapserver.data.model.AuthToken;

public class AuthTokenAccessTest {

    private Database db;
    private AuthTokenAccess authTokenAccess;
    private AuthToken authToken = new AuthToken("token", "uname");

    @Before
    public void setup() throws DBException {
        File testDB = new File(DatabaseTest.TEST_DB);
        if (testDB.exists()) {
            testDB.delete();
        }
        
        db = new Database(DatabaseTest.TEST_DB);

        authTokenAccess = new AuthTokenAccess(db);
    }

    @After
    public void cleanup() throws DBException, SQLException {
        if (db.getSQLConnection() != null && !db.getSQLConnection().isClosed()) {
            db.close();
        }
    }

    @Test
    public void addAddsAuthTokenToDB() throws SQLException, DBException {
        authTokenAccess.createTableIfMissing();

        authTokenAccess.add(authToken);

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count() FROM auth_token");
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();
        
        ps = c.prepareStatement("SELECT token, username FROM auth_token");
        rs = ps.executeQuery();

        assertTrue(rs.next());
        assertEquals(authToken.getToken(), rs.getString(1));
        assertEquals(authToken.getUsername(), rs.getString(2));

        rs.close();
        ps.close();
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfTokenTaken() throws DBException, SQLException {
        authTokenAccess.createTableIfMissing();
        
        authTokenAccess.add(authToken);

        AuthToken at2 = new AuthToken(authToken.getToken(), "uname2");
        authTokenAccess.add(at2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfTokenMissing() throws DBException {
        authTokenAccess.createTableIfMissing();

        AuthToken at2 = new AuthToken(null, "uname2");

        authTokenAccess.add(at2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfUsernameMissing() throws DBException {
        authTokenAccess.createTableIfMissing();

        AuthToken at2 = new AuthToken("token", null);

        authTokenAccess.add(at2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        authTokenAccess.add(authToken);
    }

    @Test
    public void getUsernameReturnsUsernameIfTokenInDB() throws DBException {
        authTokenAccess.createTableIfMissing();
        
        authTokenAccess.add(authToken);
        assertEquals(authToken.getUsername(), authTokenAccess.getUsername(authToken.getToken()));
    }

    @Test
    public void getUsernameReturnsNullIfTokenNotInDB() throws DBException {
        authTokenAccess.createTableIfMissing();
        
        authTokenAccess.add(authToken);
        assertNull(authTokenAccess.getUsername("doesntexist"));
    }

    @Test (expected = DBException.class)
    public void getUsernameThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        authTokenAccess.getUsername(authToken.getToken());
    }

    @Test
    public void clearRemovesAllAuthTokens() throws DBException, SQLException {
        authTokenAccess.createTableIfMissing();
        
        authTokenAccess.add(authToken);
        authTokenAccess.clear();

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count() FROM auth_token");
        ResultSet rs = ps.executeQuery();
        assertEquals(0, rs.getInt(1));
        rs.close();
        ps.close();
    }

    @Test
    public void clearDoesntThrowExceptionIfNoAuthTokens() throws DBException {
        authTokenAccess.createTableIfMissing();
        
        authTokenAccess.clear();
    }

    @Test (expected = DBException.class)
    public void clearThrowsExceptionIfDBClosed() throws DBException {
        authTokenAccess.createTableIfMissing();
        
        db.close();

        authTokenAccess.clear();
    }

    @Test
    public void createTableCreatesAuthTokenTable() throws DBException, SQLException {
        authTokenAccess.createTableIfMissing();

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count(*) FROM sqlite_master " +
                                                  "WHERE type = 'table' AND name = 'auth_token'");
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));

        rs.close();
        ps.close();
    }

    @Test
    public void createTableThrowsNoExceptionIfTableExists() throws DBException {
        authTokenAccess.createTableIfMissing();
        authTokenAccess.createTableIfMissing();
    }

    @Test (expected = DBException.class)
    public void createTableThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        authTokenAccess.createTableIfMissing();
    }
}