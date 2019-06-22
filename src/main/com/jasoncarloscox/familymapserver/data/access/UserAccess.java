package com.jasoncarloscox.familymapserver.data.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.jasoncarloscox.familymapserver.data.model.User;

/**
 * Contains methods for accessing user data in the database.
 */
public class UserAccess extends Access {

    private static final Logger LOG = Logger.getLogger("fms");

    private static final String CREATE_STMT = 
        "CREATE TABLE IF NOT EXISTS user (" + 
            "username        VARCHAR(255) NOT NULL PRIMARY KEY, " +
            "password        VARCHAR(255) NOT NULL, " +
            "email           VARCHAR(255) NOT NULL, " +
            "first_name      VARCHAR(255) NOT NULL, " +
            "last_name       VARCHAR(255) NOT NULL, " +
            "gender          CHAR(1) NOT NULL, " +
            "person_id       VARCHAR(255) NOT NULL, " +
            "CHECK (gender IN ('f', 'm')), " +
            "FOREIGN KEY (person_id) REFERENCES person(id)" + 
        ")";

    /**
     * Creates a new UserAccess object.
     * 
     * @param db the database on which to operate
     */
    public UserAccess(Database db) {
        super(db);
    }
    
    /**
     * Adds a new user to the database.
     * 
     * @param user the user to be added to the database
     * @throws DBException if a field is missing or invalid, if the database is 
     *                     not open, or if another database error occurs
     */
    public void add(User user) throws DBException {
        int numAdded = 0;

        Connection conn = getOpenConnection();

        String sql = "INSERT INTO user (username, password, email, first_name, " +
                                       "last_name, gender, person_id) " +
                     "SELECT ?, ?, ?, ?, ?, ?, ? " +
                     "WHERE NOT EXISTS (SELECT 1 FROM user WHERE username = ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getFirstName());
            ps.setString(5, user.getLastName());
            ps.setString(6, user.getGender());
            ps.setString(7, user.getPersonId());
            ps.setString(8, user.getUsername());

            numAdded = ps.executeUpdate();
        } catch (SQLException sqle) {
            DBException dbe = new DBException(sqle);
            LOG.throwing("UserAccess", "add", dbe);
            throw dbe;
        } 

        if (numAdded < 1) {
            DBException dbe = new DBException("The username '" + user.getUsername() + 
                                              "' is already in use.");
            LOG.throwing("UserAccess", "add", dbe);
            throw dbe;
        }
    }

    /**
     * Updates the person id for a user.
     * 
     * @param user the user to update, with the person id set to the new value
     * @throws DBException if the database is not open, or if another database
     *                     error occurs
     */
    public void updatePersonId(User user) throws DBException {
        Connection conn = getOpenConnection();

        String sql = "UPDATE user " +
                     "SET person_id = ? " +
                     "WHERE username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getPersonId());
            ps.setString(2, user.getUsername());

            ps.executeUpdate();
        } catch (SQLException sqle) {
            DBException dbe = new DBException(sqle);
            LOG.throwing("UserAccess", "updatePersonId", dbe);
            throw dbe;
        } 
    }

    /**
     * Gets a user from the database.
     * 
     * @param username the username of the user to be found
     * @return an object containing the user's data, or null if no user was found 
     *         with the given username
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    public User get(String username) throws DBException {
        User user = null;

        Connection conn = getOpenConnection();

        String sql = "SELECT username, password, email, first_name, last_name, " +
                            "gender, person_id " +
                     "FROM user " +
                     "WHERE username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);

            // using a nested try with resources because ResultSet can throw an 
            // exception on close
            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) { // no results - user doesn't exist
                    return null;
                }

                user = new User(rs.getString(1), rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setFirstName(rs.getString(4));
                user.setLastName(rs.getString(5));
                user.setGender(rs.getString(6));
                user.setPersonId(rs.getString(7));
            }

        } catch (SQLException sqle) {
            DBException dbe = new DBException(sqle);
            LOG.throwing("UserAccess", "get", dbe);
            throw dbe;
        }

        return user;
    }

    /**
     * Removes all users from the database.
     * 
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    public void clear() throws DBException {
        executeUpdate("DELETE FROM user");
    }

    /**
     * Creates a new table to hold users if it doesn't already exist in the 
     * database.
     * 
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    protected void createTableIfMissing() throws DBException {
        executeUpdate(CREATE_STMT);
    }

}