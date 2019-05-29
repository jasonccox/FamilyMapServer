package familymapserver.data.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import familymapserver.data.model.Person;

/**
 * Contains methods for accessing person data in the database.
 */
public class PersonAccess extends Access {

    private static final Logger LOG = Logger.getLogger("fms");

    private static final String CREATE_STMT = 
        "CREATE TABLE IF NOT EXISTS person (" + 
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
     * Creates a new PersonAccess object.
     * 
     * @param db the database on which to operate
     */
    public PersonAccess(Database db) {
        super(db);
    }

    /**
     * Adds a new person to the database.
     * 
     * @param person the person to be added to the database
     * @throws DBException if a field is missing or invalid, if the database is 
     *                     not open, or if another database error occurs
     */
    public void add(Person person) throws DBException {
        int numAdded = 0;

        Connection conn = getOpenConnection();

        String sql = "INSERT INTO person (id, assoc_username, first_name, last_name, " +
                                         "gender, father, mother, spouse) " +
                     "SELECT ?, ?, ?, ?, ?, ?, ?, ? " +
                     "WHERE NOT EXISTS (SELECT 1 FROM person WHERE id = ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, person.getId());
            ps.setString(2, person.getAssociatedUsername());
            ps.setString(3, person.getFirstName());
            ps.setString(4, person.getLastName());
            ps.setString(5, person.getGender());
            ps.setString(6, person.getFather());
            ps.setString(7, person.getMother());
            ps.setString(8, person.getSpouse());
            ps.setString(9, person.getId());

            numAdded = ps.executeUpdate();
        } catch (SQLException sqle) {
            DBException dbe = new DBException(sqle);
            LOG.throwing("PersonAccess", "add", dbe);
            throw dbe;
        } 

        if (numAdded < 1) {
            DBException dbe = new DBException("The person id '" + person.getId() + 
                                              "' is already in use.");
            LOG.throwing("PersonAccess", "add", dbe);
            throw dbe;
        }
    }

     /**
     * Gets a person from the database.
     * 
     * @param personId the id of the person to be found
     * @return an object containing the person's data, or null if no person was 
     *         found with the given id
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    public Person get(String personId) throws DBException {
        Person person = null;

        Connection conn = getOpenConnection();

        String sql = "SELECT id, assoc_username, first_name, last_name, gender, " + 
                            "father, mother, spouse " +
                     "FROM person " +
                     "WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, personId);

            // using a nested try with resources because ResultSet can throw an
            // exception on close
            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) { // no results - person doesn't exist
                    return null;
                }

                person = new Person(rs.getString(1), rs.getString(2));
                person.setFirstName(rs.getString(3));
                person.setLastName(rs.getString(4));
                person.setGender(rs.getString(5));
                person.setFather(rs.getString(6));
                person.setMother(rs.getString(7));
                person.setSpouse(rs.getString(8));
            }

        } catch (SQLException sqle) {
            DBException dbe = new DBException(sqle);
            LOG.throwing("PersonAccess", "get", dbe);
            throw dbe;
        }

        return person;
    }

    /**
     * Gets all persons associated with a specific user from the database.
     * 
     * @param username the username of the user whose persons should be retrieved
     * @return objects containing the persons' data
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    public Collection<Person> getAll(String username) throws DBException {
        ArrayList<Person> persons = new ArrayList<>();

        Connection conn = getOpenConnection();

        String sql = "SELECT id, assoc_username, first_name, last_name, gender, " + 
                            "father, mother, spouse " +
                     "FROM person " +
                     "WHERE assoc_username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);

            // using a nested try with resources because ResultSet can throw an
            // exception on close
            try (ResultSet rs = ps.executeQuery()) {
            
                while (rs.next()) {
                    Person person = new Person(rs.getString(1), rs.getString(2));
                    person.setFirstName(rs.getString(3));
                    person.setLastName(rs.getString(4));
                    person.setGender(rs.getString(5));
                    person.setFather(rs.getString(6));
                    person.setMother(rs.getString(7));
                    person.setSpouse(rs.getString(8));

                    persons.add(person);
                }
            }

        } catch (SQLException sqle) {
            DBException dbe = new DBException(sqle);
            LOG.throwing("PersonAccess", "getAll", dbe);
            throw dbe;
        }

        return persons;
    }

    /**
     * Removes all persons from the database.
     * 
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    public void clear() throws DBException {
        executeUpdate("DELETE FROM person");
    }

    /**
     * Removes all persons associated with a given user.
     * 
     * @param username the user's username
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    public void clearAll(String username) throws DBException {
        Connection conn = getOpenConnection();

        String sql = "DELETE FROM person WHERE assoc_username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) { 
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            DBException dbe = new DBException(sqle);
            LOG.throwing("PersonAccess", "clearAll", dbe);
            throw dbe;
        }

    }

    /**
     * Creates a new table to hold persons if it doesn't already exist in the 
     * database.
     * 
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    protected void createTableIfMissing() throws DBException {
        executeUpdate(CREATE_STMT);
    }
}