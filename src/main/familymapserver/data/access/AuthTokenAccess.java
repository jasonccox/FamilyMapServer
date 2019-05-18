package familymapserver.data.access;

import familymapserver.data.model.AuthToken;

/**
 * Contains methods for accessing authorization token data in the database.
 */
public class AuthTokenAccess {

    /**
     * Adds a new authorization token to the database.
     * 
     * @param authToken the authorization token to be added to the database
     * @param db the database to which the authorization token should be added
     * @return true if the authorization token was added, false if an authorization 
     * token with the same token value already existed, thus preventing this one from 
     * being added
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public static boolean add(AuthToken authToken, Database db) throws DBException {
        return false;
    }

    /**
     * Gets the username of the user with whom the token is associated.
     * 
     * @param token an authorization token value
     * @param db the database in which to find the authorization token
     * @return the username of the user with whom the token is associated, or null if there
     * is no authorization token with the given token value
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public static String getUsername(String token, Database db) throws DBException {
        return null;
    }

    /**
     * Removes all authorization tokens from the database.
     * 
     * @param db the database from which to remove the authorization tokens
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public static void clear(Database db) throws DBException {

    }

    /**
     * Creates a new table to hold authorization tokens.
     * 
     * @param db the database in which to create the table
     * @throws DBException if the database is not open, or if another database error occurs
     */
    protected static void createTable(Database db) throws DBException {

    }
}