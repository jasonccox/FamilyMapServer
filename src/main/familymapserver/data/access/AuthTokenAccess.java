package familymapserver.data.access;

import familymapserver.data.model.AuthToken;

/**
 * Contains methods for accessing authorization token data in the database.
 */
public class AuthTokenAccess extends Access {

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
     * @return true if the authorization token was added, false if an authorization 
     * token with the same token value already existed, thus preventing this one from 
     * being added
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public boolean add(AuthToken authToken) throws DBException {
        return false;
    }

    /**
     * Gets the username of the user with whom the token is associated.
     * 
     * @param token an authorization token value
     * @return the username of the user with whom the token is associated, or null if there
     * is no authorization token with the given token value
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public String getUsername(String token) throws DBException {
        return null;
    }

    /**
     * Removes all authorization tokens from the database.
     * 
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public void clear() throws DBException {

    }

    /**
     * Creates a new table to hold authorization tokens.
     * 
     * @throws DBException if the database is not open, or if another database error occurs
     */
    protected void createTable() throws DBException {

    }
}