package familymapserver.data.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import familymapserver.data.model.AuthToken;

/**
 * Contains methods for accessing authorization token data in the database.
 */
public class AuthTokenAccess extends Access {

    private static final String CREATE_STMT = 
        "CREATE TABLE auth_token (" +
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
     * @return true if the authorization token was added, false if an 
     *         authorization token with the same token value already existed, 
     *         thus preventing this one from being added
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    public boolean add(AuthToken authToken) throws DBException {
        boolean added = false;

        Connection conn = getOpenConnection();

        String sql = "INSERT INTO auth_token (token, username) " +
                     "SELECT ?, ?" +
                     "WHERE NOT EXISTS (SELECT 1 FROM auth_token WHERE token = ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, authToken.getToken());
            ps.setString(2, authToken.getUsername());
            ps.setString(3, authToken.getToken());

            added = (ps.executeUpdate() == 1);
        } catch (SQLException e) {
            throw new DBException(e);
        } 


        return added;
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

        } catch (SQLException e) {
            throw new DBException(e);
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
     * Creates a new table to hold authorization tokens.
     * 
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    protected void createTable() throws DBException {
        executeUpdate(CREATE_STMT);
    }
}