package familymapserver.data.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import familymapserver.data.model.Person;

/**
 * Contains methods for accessing person data in the database.
 */
public class PersonAccess extends Access {

    private static final String CREATE_STMT = "CREATE TABLE person (" + 
                                                "id              VARCHAR(255) NOT NULL PRIMARY KEY, " +
                                                "assoc_username  VARCHAR(255) NOT NULL, " +
                                                "first_name      VARCHAR(255) NOT NULL, " +
                                                "last_name       VARCHAR(255) NOT NULL, " +
                                                "gender          CHAR(1) NOT NULL, " +
                                                "father          VARCHAR(255), " +
                                                "mother          VARCHAR(255), " +
                                                "spouse          VARCHAR(255), " +
                                                "CHECK (gender IN ('f', 'm')), " +
                                                "FOREIGN KEY (assoc_username) REFERENCES user(username)," + 
                                                "FOREIGN KEY (father) REFERENCES person(id)," + 
                                                "FOREIGN KEY (mother) REFERENCES person(id)," + 
                                                "FOREIGN KEY (spouse) REFERENCES person(id)" + 
                                              ")";

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
        boolean added = false;

        Connection c = getOpenConnection(db);

        String sql = "INSERT INTO person (id, assoc_username, first_name, last_name, gender, father, mother, spouse) " +
                     "SELECT ?, ?, ?, ?, ?, ?, ?, ? " +
                     "WHERE NOT EXISTS (SELECT 1 FROM person WHERE id = ?)";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, person.getId());
            ps.setString(2, person.getAssociatedUsername());
            ps.setString(3, person.getFirstName());
            ps.setString(4, person.getLastName());
            ps.setString(5, person.getGender());
            ps.setString(6, person.getFather());
            ps.setString(7, person.getMother());
            ps.setString(8, person.getSpouse());
            ps.setString(9, person.getId());

            added = (ps.executeUpdate() == 1);
        } catch (SQLException e) {
            throw new DBException(e);
        } 


        return added;
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
        Person p = null;

        Connection c = getOpenConnection(db);

        String sql = "SELECT id, assoc_username, first_name, last_name, gender, father, mother, spouse " +
                     "FROM person " +
                     "WHERE id = ?";

        ResultSet rs = null;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, personId);

            rs = ps.executeQuery();

            if (!rs.next()) { // no results - person doesn't exist
                return null;
            }

            p = new Person(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                         rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
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

        return p;
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
        ArrayList<Person> p = new ArrayList<>();

        Connection c = getOpenConnection(db);

        String sql = "SELECT id, assoc_username, first_name, last_name, gender, father, mother, spouse " +
                     "FROM person " +
                     "WHERE assoc_username = ?";

        ResultSet rs = null;
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            
            rs = ps.executeQuery();

            while (rs.next()) {
                p.add(new Person(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                      rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
            }
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

        return p;
    }

    /**
     * Removes all persons from the database.
     * 
     * @param db the database from which to remove the persons
     * @throws DBException if the database is not open, or if another database error occurs
     */
    public static void clear(Database db) throws DBException {
        executeUpdate(db, "DELETE FROM person");
    }

    /**
     * Creates a new table to hold persons.
     * 
     * @param db the database in which to create the table
     * @throws DBException if the table already exists, if the database is not open, or if 
     * another database error occurs
     */
    protected static void createTable(Database db) throws DBException {
        executeUpdate(db, CREATE_STMT);
    }
}