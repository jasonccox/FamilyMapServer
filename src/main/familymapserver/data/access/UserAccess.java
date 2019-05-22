package familymapserver.data.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import familymapserver.data.model.User;

/**
 * Contains methods for accessing user data in the database.
 */
public class UserAccess extends Access {

    private static final String CREATE_STMT = "CREATE TABLE user (" + 
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
     * @return true if the user was added, false if a user with the same username
     * already existed, thus preventing this one from being added
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public boolean add(User user) throws DBException {
        boolean added = false;

        Connection conn = getOpenConnection();

        String sql = "INSERT INTO user (username, password, email, first_name, last_name, gender, person_id) " +
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

            added = (ps.executeUpdate() == 1);
        } catch (SQLException e) {
            throw new DBException(e);
        } 


        return added;
    }

    /**
     * Gets a user from the database.
     * 
     * @param username the username of the user to be found
     * @return an object containing the user's data, or null if no user was found with
     * the given username
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public User get(String username) throws DBException {
        User user = null;

        Connection conn = getOpenConnection();

        String sql = "SELECT username, password, email, first_name, last_name, gender, person_id " +
                     "FROM user " +
                     "WHERE username = ?";

        ResultSet rs = null;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);

            rs = ps.executeQuery();

            if (!rs.next()) { // no results - user doesn't exist
                return null;
            }

            user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                         rs.getString(5), rs.getString(6), rs.getString(7));
        } catch (SQLException e) {
            throw new DBException(e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DBException(e);
                }
            }
        }

        return user;
    }

    /**
     * Removes all users from the database.
     * 
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public void clear() throws DBException {
        executeUpdate("DELETE FROM user");
    }

    /**
     * Creates a new table to hold users.
     * 
     * @throws DBException if the database is not open, or if another database error occurs
     */
    protected void createTable() throws DBException {
        executeUpdate(CREATE_STMT);
    }

}