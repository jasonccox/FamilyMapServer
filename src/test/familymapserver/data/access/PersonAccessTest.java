package familymapserver.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import familymapserver.data.model.Person;

public class PersonAccessTest {

    private Database db;
    private PersonAccess personAccess;
    private Person person;

    @Before
    public void setup() throws DBException {
        File testDB = new File(DatabaseTest.TEST_DB);
        if (testDB.exists()) {
            testDB.delete();
        }
        
        Database.testDBPath = DatabaseTest.TEST_DB;
        db = new Database();

        personAccess = new PersonAccess(db);

        person = new Person("id", "uname");
        person.setFirstName("f");
        person.setLastName("l");
        person.setGender("m");
        person.setFather("dad");
        person.setMother("mom");
        person.setSpouse("spouse");
    }

    @After
    public void cleanup() throws DBException, SQLException {
        if (db.getSQLConnection() != null && !db.getSQLConnection().isClosed()) {
            db.close();
        }
    }

    @Test
    public void addAddsPersonToDB() throws SQLException, DBException {
        personAccess.createTableIfMissing();

        personAccess.add(person);

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count() FROM person");
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();
        
        ps = c.prepareStatement("SELECT id, assoc_username, first_name, last_name, " +
                                "gender, father, mother, spouse FROM person");
        rs = ps.executeQuery();

        assertTrue(rs.next());
        assertEquals(person.getId(), rs.getString(1));
        assertEquals(person.getAssociatedUsername(), rs.getString(2));
        assertEquals(person.getFirstName(), rs.getString(3));
        assertEquals(person.getLastName(), rs.getString(4));
        assertEquals(person.getGender(), rs.getString(5));
        assertEquals(person.getFather(), rs.getString(6));
        assertEquals(person.getMother(), rs.getString(7));
        assertEquals(person.getSpouse(), rs.getString(8));

        rs.close();
        ps.close();
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfIdTaken() throws DBException, SQLException {
        personAccess.createTableIfMissing();
        
        personAccess.add(person);

        Person p2 = new Person(person.getId(), "uname2");
        p2.setFirstName("f2");
        p2.setLastName("l2");
        p2.setGender("f");
        p2.setFather("dad2");
        p2.setMother("mom2");
        p2.setSpouse("spouse2");

        personAccess.add(p2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfIdMissing() throws DBException {
        personAccess.createTableIfMissing();

        Person p2 = new Person(null, "uname2");
        p2.setFirstName("f2");
        p2.setLastName("l2");
        p2.setGender("f");
        p2.setFather("dad2");
        p2.setMother("mom2");
        p2.setSpouse("spouse2");

        personAccess.add(p2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfAssocUsernameMissing() throws DBException {
        personAccess.createTableIfMissing();

        Person p2 = new Person("id2", null);
        p2.setFirstName("f2");
        p2.setLastName("l2");
        p2.setGender("f");
        p2.setFather("dad2");
        p2.setMother("mom2");
        p2.setSpouse("spouse2");

        personAccess.add(p2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfFirstNameMissing() throws DBException {
        personAccess.createTableIfMissing();

        Person p2 = new Person("id2", "uname2");
        p2.setLastName("l2");
        p2.setGender("f");
        p2.setFather("dad2");
        p2.setMother("mom2");
        p2.setSpouse("spouse2");

        personAccess.add(p2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfLastNameMissing() throws DBException {
        personAccess.createTableIfMissing();

        Person p2 = new Person("id2", "uname2");
        p2.setFirstName("f2");
        p2.setGender("f");
        p2.setFather("dad2");
        p2.setMother("mom2");
        p2.setSpouse("spouse2");

        personAccess.add(p2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfGenderMissing() throws DBException {
        personAccess.createTableIfMissing();

        Person p2 = new Person("id2", "uname2");
        p2.setFirstName("f2");
        p2.setLastName("l2");
        p2.setFather("dad2");
        p2.setMother("mom2");
        p2.setSpouse("spouse2");

        personAccess.add(p2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfGenderInvalid() throws DBException {
        personAccess.createTableIfMissing();

        Person p2 = new Person("id2", "uname2");
        p2.setFirstName("f2");
        p2.setLastName("l2");
        p2.setGender("z");
        p2.setFather("dad2");
        p2.setMother("mom2");
        p2.setSpouse("spouse2");

        personAccess.add(p2);
    }

    @Test
    public void addThrowsNoExceptionIfFatherMissing() throws DBException {
        personAccess.createTableIfMissing();

        Person p2 = new Person("id2", "uname2");
        p2.setFirstName("f2");
        p2.setLastName("l2");
        p2.setGender("f");
        p2.setMother("mom2");
        p2.setSpouse("spouse2");

        personAccess.add(p2);
    }

    @Test
    public void addThrowsNoExceptionIfMotherMissing() throws DBException {
        personAccess.createTableIfMissing();

        Person p2 = new Person("id2", "uname2");
        p2.setFirstName("f2");
        p2.setLastName("l2");
        p2.setGender("f");
        p2.setFather("dad2");
        p2.setSpouse("spouse2");

        personAccess.add(p2);
    }

    @Test
    public void addThrowsNoExceptionIfSpouseMissing() throws DBException {
        personAccess.createTableIfMissing();

        Person p2 = new Person("id2", "uname2");
        p2.setFirstName("f2");
        p2.setLastName("l2");
        p2.setGender("f");
        p2.setFather("dad2");
        p2.setMother("mom2");

        personAccess.add(p2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        personAccess.add(person);
    }

    @Test
    public void getReturnsPersonIfInDB() throws DBException {
        personAccess.createTableIfMissing();
        
        personAccess.add(person);
        Person result = personAccess.get(person.getId());

        assertEquals(person.getId(), result.getId());
        assertEquals(person.getAssociatedUsername(), result.getAssociatedUsername());
        assertEquals(person.getFirstName(), result.getFirstName());
        assertEquals(person.getLastName(), result.getLastName());
        assertEquals(person.getGender(), result.getGender());
        assertEquals(person.getFather(), result.getFather());
        assertEquals(person.getMother(), result.getMother());
        assertEquals(person.getSpouse(), result.getSpouse());
    }

    @Test
    public void getReturnsNullIfNotInDB() throws DBException {
        personAccess.createTableIfMissing();
        
        personAccess.add(person);
        assertNull(personAccess.get("doesntexist"));
    }

    @Test (expected = DBException.class)
    public void getThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        personAccess.get(person.getId());
    }

    @Test
    public void getAllReturnsMatchingPersons() throws DBException {
        personAccess.createTableIfMissing();

        Person p2 = new Person("id2", "uname2");
        p2.setFirstName("f2");
        p2.setLastName("l2");
        p2.setGender("f");

        Person p3 = new Person("id3", person.getAssociatedUsername());
        p3.setFirstName("f3");
        p3.setLastName("l3");
        p3.setGender("m");

        Person p4 = new Person("id4", "uname4");
        p4.setFirstName("f4");
        p4.setLastName("l4");
        p4.setGender("f");

        personAccess.add(person);
        personAccess.add(p2);
        personAccess.add(p3);
        personAccess.add(p4);

        Collection<Person> result = personAccess.getAll(person.getAssociatedUsername());

        assertEquals(2, result.size());
        
        Iterator<Person> i = result.iterator();
        Person r1 = i.next();
        Person r2 = i.next();

        System.out.println(r1.getId());
        System.out.println(r2.getId());
        
        if (person.getId().equals(r1.getId())) {
            assertEquals(p3.getId(), r2.getId());
        } else {
            assertEquals(person.getId(), r2.getId());
            assertEquals(p3.getId(), r1.getId());
        }
    }

    @Test
    public void getAllReturnsEmptyCollectionIfNoMatchingPersons() throws DBException {
        personAccess.createTableIfMissing();

        Person p2 = new Person("id2", "uname2");
        p2.setFirstName("f2");
        p2.setLastName("l2");
        p2.setGender("f");

        Person p3 = new Person("id3", person.getAssociatedUsername());
        p3.setFirstName("f3");
        p3.setLastName("l3");
        p3.setGender("m");

        Person p4 = new Person("id4", "uname4");
        p4.setFirstName("f4");
        p4.setLastName("l4");
        p4.setGender("f");

        personAccess.add(person);
        personAccess.add(p2);
        personAccess.add(p3);
        personAccess.add(p4);

        Collection<Person> result = personAccess.getAll("nomatch");

        assertEquals(0, result.size());
    }

    @Test (expected = DBException.class)
    public void getAllThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        personAccess.getAll("uname");
    }

    @Test
    public void clearRemovesAllPersons() throws DBException, SQLException {
        personAccess.createTableIfMissing();
        
        personAccess.add(person);
        personAccess.clear();

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count() FROM person");
        ResultSet rs = ps.executeQuery();
        assertEquals(0, rs.getInt(1));
        rs.close();
        ps.close();
    }

    @Test
    public void clearDoesntThrowExceptionIfNoPersons() throws DBException {
        personAccess.createTableIfMissing();
        
        personAccess.clear();
    }

    @Test (expected = DBException.class)
    public void clearThrowsExceptionIfDBClosed() throws DBException {
        personAccess.createTableIfMissing();
        
        db.close();

        personAccess.clear();
    }

    @Test
    public void clearAllRemovesAllUsersPersons() throws DBException {
        personAccess.createTableIfMissing();

        personAccess.add(person);
        personAccess.clearAll(person.getAssociatedUsername());

        assertNull(personAccess.get(person.getId()));
        assertEquals(0, personAccess.getAll(person.getAssociatedUsername()).size());
    }

    @Test (expected = DBException.class)
    public void clearAllThrowsExceptionIfDBClosed() throws DBException {
        personAccess.createTableIfMissing();
        personAccess.add(person);
        
        db.close();

        personAccess.clearAll(person.getAssociatedUsername());
    }

    @Test
    public void createTableCreatesPersonTable() throws DBException, SQLException {
        personAccess.createTableIfMissing();

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count(*) FROM sqlite_master " +
                                                  "WHERE type = 'table' AND name = 'person'");
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));

        rs.close();
        ps.close();
    }

    @Test
    public void createTableThrowsNoExceptionIfTableExists() throws DBException {
        personAccess.createTableIfMissing();
        personAccess.createTableIfMissing();
    }

    @Test (expected = DBException.class)
    public void createTableThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        personAccess.createTableIfMissing();
    }


}