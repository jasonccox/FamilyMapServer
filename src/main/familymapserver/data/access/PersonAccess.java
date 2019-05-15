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
     * @throws DBException
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
     * @throws DBException
     */
    public static Person get(String personId, Database db) throws DBException {
        return null;
    }

    /**
     * Gets all persons from the database.
     * 
     * @param db the database in which to find the persons
     * @return objects containing the persons' data
     * @throws DBException
     */
    public static Collection<Person> getAll(Database db) throws DBException {
        return null;
    }
}