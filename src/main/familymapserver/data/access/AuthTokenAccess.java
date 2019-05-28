package familymapserver.data.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import familymapserver.data.model.AuthToken;

/**
 * Contains methods for accessing authorization token data in the database.
 */
public class AuthTokenAccess extends Access {

    private static final Logger LOG = Logger.getLogger("fms");

    private static final String CREATE_STMT = 
        "CREATE TABLE IF NOT EXISTS auth_token (" +
            "token      VARCHAR(255) NOT NULL PRIMARY KEY, " +
            "username   VARCHAR(255) NOT NULL, " +
            "FOREIGN KEY (username) REFERENCES user(username)" +
        ")";

    /**
     * Creates a new AuthTokenAccess object.
     * 
     * @param db the database on which to operate
     */
    public AuthTokenAccess(Database db) {
        super(db);
    }

    /**
     * Adds a new authorization token to the database.
     * 
     * @param authToken the authorization token to be added to the database
     * @throws DBException if a field is missing or invalid, if the database is 
     *                     not open, or if another database error occurs
     */
    public void add(AuthToken authToken) throws DBException {
        int numAdded = 0;

        Connection conn = getOpenConnection();

        String sql = "INSERT INTO auth_token (token, username) " +
                     "SELECT ?, ?" +
                     "WHERE NOT EXISTS (SELECT 1 FROM auth_token WHERE token = ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, authToken.getToken());
            ps.setString(2, authToken.getUsername());
            ps.setString(3, authToken.getToken());

            numAdded = ps.executeUpdate();
        } catch (SQLException sqle) {
            DBException dbe = new DBException(sqle);
            LOG.throwing("AuthTokenAccess", "add", dbe);
            throw dbe;
        } 

        if (numAdded < 1) {
            DBException dbe = new DBException("The token '" + authToken.getToken() + 
                                              "' is already in use.");
            LOG.throwing("AuthTokenAccess", "add", dbe);
            throw dbe;
        }
    }

    /**
     * Gets the username of the user with whom the token is associated.
     * 
     * @param token an authorization token value
     * @return the username of the user with whom the token is associated, or 
     *         null if there is no authorization token with the given token value
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    public String getUsername(String token) throws DBException {

        Connection conn = getOpenConnection();

        String sql = "SELECT username FROM auth_token WHERE token = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, token);

            // using a nested try with resources because ResultSet can throw an
            // exception on close
            try (ResultSet rs = ps.executeQuery() ) {

                if (!rs.next()) { // no results - authtoken doesn't exist
                    return null;
                }

                return rs.getString(1);
            } 

        } catch (SQLException sqle) {
            DBException dbe = new DBException(sqle);
            LOG.throwing("AuthTokenAccess", "getUsername", dbe);
            throw dbe;
        }
    }

    /**
     * Removes all authorization tokens from the database.
     * 
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    public void clear() throws DBException {
        executeUpdate("DELETE FROM auth_token");
    }

    /**
     * Creates a new table to hold authorization tokens if it doesn't already
     * exist in the database.
     * 
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    protected void createTableIfMissing() throws DBException {
        executeUpdate(CREATE_STMT);
    }
}