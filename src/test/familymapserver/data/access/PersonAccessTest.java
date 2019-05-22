package familymapserver.data.access;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    private Person p = new Person("id", "uname", "fname", "lname", "m", "fid", "mid", "sid");

    @Before
    public void setup() throws DBException {
        File testDB = new File(DatabaseTest.TEST_DB);
        if (testDB.exists()) {
            testDB.delete();
        }
        
        db = new Database();
        db.open(DatabaseTest.TEST_DB);

        personAccess = new PersonAccess(db);
    }

    @After
    public void cleanup() throws DBException, SQLException {
        if (db.getSQLConnection() != null && !db.getSQLConnection().isClosed()) {
            db.close();
        }
    }

    @Test
    public void addReturnsTrueAndAddsPersonToDB() throws SQLException, DBException {
        personAccess.createTable();

        assertTrue(personAccess.add(p));

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count() FROM person");
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();
        
        ps = c.prepareStatement("SELECT id, assoc_username, first_name, last_name, gender, father, mother, spouse FROM person");
        rs = ps.executeQuery();

        assertTrue(rs.next());
        assertEquals(p.getId(), rs.getString(1));
        assertEquals(p.getAssociatedUsername(), rs.getString(2));
        assertEquals(p.getFirstName(), rs.getString(3));
        assertEquals(p.getLastName(), rs.getString(4));
        assertEquals(p.getGender(), rs.getString(5));
        assertEquals(p.getFather(), rs.getString(6));
        assertEquals(p.getMother(), rs.getString(7));
        assertEquals(p.getSpouse(), rs.getString(8));

        rs.close();
        ps.close();
    }

    @Test
    public void addReturnsFalseAndDoesNotAddPersonIfIdTaken() throws DBException, SQLException {
        personAccess.createTable();
        
        personAccess.add(p);

        Person p2 = new Person(p.getId(), "uname2", "fname2", "lname2", "f", "fid2", "mid2", "sid2");
        assertFalse(personAccess.add(p2));

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count() FROM person WHERE id = ?");
        ps.setString(1, p.getId());
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfIdMissing() throws DBException {
        personAccess.createTable();

        Person p2 = new Person(null, "u", "f", "l", "m", "f", "m", "s");

        personAccess.add(p2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfAssocUsernameMissing() throws DBException {
        personAccess.createTable();

        Person p2 = new Person("i", null, "f", "l", "m", "f", "m", "s");

        personAccess.add(p2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfFirstNameMissing() throws DBException {
        personAccess.createTable();

        Person p2 = new Person("i", "u", null, "l", "m", "f", "m", "s");

        personAccess.add(p2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfLastNameMissing() throws DBException {
        personAccess.createTable();

        Person p2 = new Person("i", "u", "f", null, "m", "f", "m", "s");

        personAccess.add(p2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfGenderMissing() throws DBException {
        personAccess.createTable();

        Person p2 = new Person("i", "u", "f", "l", null, "f", "m", "s");

        personAccess.add(p2);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfGenderInvalid() throws DBException {
        personAccess.createTable();

        Person p2 = new Person("i", "u", "f", "l", "a", "f", "m", "s");

        personAccess.add(p2);
    }

    @Test
    public void addThrowsNoExceptionIfFatherMissing() throws DBException {
        personAccess.createTable();

        Person p2 = new Person("i", "u", "f", "l", "m", null, "m", "s");

        assertTrue(personAccess.add(p2));
    }

    @Test
    public void addThrowsNoExceptionIfMotherMissing() throws DBException {
        personAccess.createTable();

        Person p2 = new Person("i", "u", "f", "l", "m", "f", null, "s");

        assertTrue(personAccess.add(p2));
    }

    @Test
    public void addThrowsNoExceptionIfSpouseMissing() throws DBException {
        personAccess.createTable();

        Person p2 = new Person("i", "u", "f", "l", "m", "f", "m", null);

        assertTrue(personAccess.add(p2));
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        personAccess.add(p);
    }

    @Test
    public void getReturnsPersonIfInDB() throws DBException {
        personAccess.createTable();
        
        personAccess.add(p);
        Person result = personAccess.get(p.getId());

        assertEquals(p.getId(), result.getId());
        assertEquals(p.getAssociatedUsername(), result.getAssociatedUsername());
        assertEquals(p.getFirstName(), result.getFirstName());
        assertEquals(p.getLastName(), result.getLastName());
        assertEquals(p.getGender(), result.getGender());
        assertEquals(p.getFather(), result.getFather());
        assertEquals(p.getMother(), result.getMother());
        assertEquals(p.getSpouse(), result.getSpouse());
    }

    @Test
    public void getReturnsNullIfNotInDB() throws DBException {
        personAccess.createTable();
        
        personAccess.add(p);
        assertNull(personAccess.get("doesntexist"));
    }

    @Test (expected = DBException.class)
    public void getThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        personAccess.get(p.getId());
    }

    @Test
    public void getAllReturnsMatchingPersons() throws DBException {
        personAccess.createTable();

        Person p2 = new Person("i", "u", "f", "l", "m", "f", "m", "s");
        Person p3 = new Person("i2", p.getAssociatedUsername(), "f2", "l2", "f", "f2", "m2", "s2");
        Person p4 = new Person("i3", "u3", "f3", "l3", "m", "f3", "m3", "s3");

        personAccess.add(p);
        personAccess.add(p2);
        personAccess.add(p3);
        personAccess.add(p4);

        Collection<Person> result = personAccess.getAll(p.getAssociatedUsername());

        assertEquals(2, result.size());
        
        Iterator<Person> i = result.iterator();
        Person r1 = i.next();
        Person r2 = i.next();

        System.out.println(r1.getId());
        System.out.println(r2.getId());
        
        if (p.getId().equals(r1.getId())) {
            assertEquals(p3.getId(), r2.getId());
        } else {
            assertEquals(p.getId(), r2.getId());
            assertEquals(p3.getId(), r1.getId());
        }
    }

    @Test
    public void getAllReturnsEmptyCollectionIfNoMatchingPersons() throws DBException {
        personAccess.createTable();

        Person p2 = new Person("i", "u", "f", "l", "m", "f", "m", "s");
        Person p3 = new Person("i2", p.getAssociatedUsername(), "f2", "l2", "f", "f2", "m2", "s2");
        Person p4 = new Person("i3", "u3", "f3", "l3", "m", "f3", "m3", "s3");

        personAccess.add(p);
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
        personAccess.createTable();
        
        personAccess.add(p);
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
        personAccess.createTable();
        
        personAccess.clear();
    }

    @Test (expected = DBException.class)
    public void clearThrowsExceptionIfDBClosed() throws DBException {
        personAccess.createTable();
        
        db.close();

        personAccess.clear();
    }

    @Test
    public void createTableCreatesPersonTable() throws DBException, SQLException {
        personAccess.createTable();

        Connection c = db.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count(*) FROM sqlite_master WHERE type = 'table' AND name = 'person'");
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));

        rs.close();
        ps.close();
    }

    @Test (expected = DBException.class)
    public void createTableThrowsExceptionIfTableExists() throws DBException {
        personAccess.createTable();
        personAccess.createTable();
    }

    @Test (expected = DBException.class)
    public void createTableThrowsExceptionIfDBClosed() throws DBException {
        db.close();

        personAccess.createTable();
    }


}