package familymapserver.data.access;

import java.util.Collection;

import familymapserver.data.model.Person;

/**
 * Contains methods for accessing person data in the database.
 */
public class PersonAccess {

    /**
     * Adds a new person to the database.
     * 
     * @param person the person to be added to the database
     * @param db the database to which the person should be added
     * @return true if the person was added, false if a person with the same id
     * already existed, thus preventing this one from being added
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public static boolean add(Person person, Database db) throws DBException {
        return false;
    }

     /**
     * Gets a person from the database.
     * 
     * @param personId the id of the person to be found
     * @param db the database in which to find the person
     * @return an object containing the person's data, or null if no person was found with
     * the given id
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public static Person get(String personId, Database db) throws DBException {
        return null;
    }

    /**
     * Gets all persons associated with a specific user from the database.
     * 
     * @param username the username of the user whose persons should be retrieved
     * @param db the database in which to find the persons
     * @return objects containing the persons' data
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public static Collection<Person> getAll(String username, Database db) throws DBException {
        return null;
    }

    /**
     * Removes all persons from the database.
     * 
     * @param db the database from which to remove the persons
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public static void clear(Database db) throws DBException {

    }

    /**
     * Creates a new table to hold persons.
     * 
     * @param db the database in which to create the table
     * @throws DBException if the table already exists, if the database is not open, or if 
     * another database error occurs
     */
    protected static void createTable(Database db) throws DBException {

    }
}