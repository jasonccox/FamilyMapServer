package familymapserver.data.access;

import familymapserver.data.model.User;

/**
 * Contains methods for accessing user data in the database.
 */
public class UserAccess {

    /**
     * Adds a new user to the database.
     * 
     * @param user the user to be added to the database
     * @param db the database to which the user should be added
     * @return true if the user was added, false if a user with the same username
     * already existed, thus preventing this one from being added
     * @throws DBException
     */
    public static boolean add(User user, Database db) throws DBException {
        return false;
    }

    /**
     * Gets a user from the database.
     * 
     * @param username the username of the user to be found
     * @param db the database in which to find the user
     * @return an object containing the user's data, or null if no user was found with
     * the given username
     * @throws DBException
     */
    public static User get(String username, Database db) throws DBException {
        return null;
    }

}