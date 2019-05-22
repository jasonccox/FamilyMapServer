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

    private Database d;
    private Person p = new Person("id", "uname", "fname", "lname", "m", "fid", "mid", "sid");

    @Before
    public void setup() throws DBException {
        File testDB = new File(DatabaseTest.TEST_DB);
        if (testDB.exists()) {
            testDB.delete();
        }
        
        d = new Database();
        d.open(DatabaseTest.TEST_DB);
    }

    @After
    public void cleanup() throws DBException, SQLException {
        if (d.getSQLConnection() != null && !d.getSQLConnection().isClosed()) {
            d.close();
        }
    }

    @Test
    public void addReturnsTrueAndAddsPersonToDB() throws SQLException, DBException {
        PersonAccess.createTable(d);

        assertTrue(PersonAccess.add(p, d));

        Connection c = d.getSQLConnection();
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
        PersonAccess.createTable(d);
        
        PersonAccess.add(p, d);

        Person p2 = new Person(p.getId(), "uname2", "fname2", "lname2", "f", "fid2", "mid2", "sid2");
        assertFalse(PersonAccess.add(p2, d));

        Connection c = d.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count() FROM person WHERE id = ?");
        ps.setString(1, p.getId());
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));
        rs.close();
        ps.close();
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfIdMissing() throws DBException {
        PersonAccess.createTable(d);

        Person p2 = new Person(null, "u", "f", "l", "m", "f", "m", "s");

        PersonAccess.add(p2, d);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfAssocUsernameMissing() throws DBException {
        PersonAccess.createTable(d);

        Person p2 = new Person("i", null, "f", "l", "m", "f", "m", "s");

        PersonAccess.add(p2, d);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfFirstNameMissing() throws DBException {
        PersonAccess.createTable(d);

        Person p2 = new Person("i", "u", null, "l", "m", "f", "m", "s");

        PersonAccess.add(p2, d);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfLastNameMissing() throws DBException {
        PersonAccess.createTable(d);

        Person p2 = new Person("i", "u", "f", null, "m", "f", "m", "s");

        PersonAccess.add(p2, d);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfGenderMissing() throws DBException {
        PersonAccess.createTable(d);

        Person p2 = new Person("i", "u", "f", "l", null, "f", "m", "s");

        PersonAccess.add(p2, d);
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfGenderInvalid() throws DBException {
        PersonAccess.createTable(d);

        Person p2 = new Person("i", "u", "f", "l", "a", "f", "m", "s");

        PersonAccess.add(p2, d);
    }

    @Test
    public void addThrowsNoExceptionIfFatherMissing() throws DBException {
        PersonAccess.createTable(d);

        Person p2 = new Person("i", "u", "f", "l", "m", null, "m", "s");

        assertTrue(PersonAccess.add(p2, d));
    }

    @Test
    public void addThrowsNoExceptionIfMotherMissing() throws DBException {
        PersonAccess.createTable(d);

        Person p2 = new Person("i", "u", "f", "l", "m", "f", null, "s");

        assertTrue(PersonAccess.add(p2, d));
    }

    @Test
    public void addThrowsNoExceptionIfSpouseMissing() throws DBException {
        PersonAccess.createTable(d);

        Person p2 = new Person("i", "u", "f", "l", "m", "f", "m", null);

        assertTrue(PersonAccess.add(p2, d));
    }

    @Test (expected = DBException.class)
    public void addThrowsExceptionIfDBClosed() throws DBException {
        d.close();

        PersonAccess.add(p, d);
    }

    @Test
    public void getReturnsPersonIfInDB() throws DBException {
        PersonAccess.createTable(d);
        
        PersonAccess.add(p, d);
        Person result = PersonAccess.get(p.getId(), d);

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
        PersonAccess.createTable(d);
        
        PersonAccess.add(p, d);
        assertNull(PersonAccess.get("doesntexist", d));
    }

    @Test (expected = DBException.class)
    public void getThrowsExceptionIfDBClosed() throws DBException {
        d.close();

        PersonAccess.get(p.getId(), d);
    }

    @Test
    public void getAllReturnsMatchingPersons() throws DBException {
        PersonAccess.createTable(d);

        Person p2 = new Person("i", "u", "f", "l", "m", "f", "m", "s");
        Person p3 = new Person("i2", p.getAssociatedUsername(), "f2", "l2", "f", "f2", "m2", "s2");
        Person p4 = new Person("i3", "u3", "f3", "l3", "m", "f3", "m3", "s3");

        PersonAccess.add(p, d);
        PersonAccess.add(p2, d);
        PersonAccess.add(p3, d);
        PersonAccess.add(p4, d);

        Collection<Person> result = PersonAccess.getAll(p.getAssociatedUsername(), d);

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
        PersonAccess.createTable(d);

        Person p2 = new Person("i", "u", "f", "l", "m", "f", "m", "s");
        Person p3 = new Person("i2", p.getAssociatedUsername(), "f2", "l2", "f", "f2", "m2", "s2");
        Person p4 = new Person("i3", "u3", "f3", "l3", "m", "f3", "m3", "s3");

        PersonAccess.add(p, d);
        PersonAccess.add(p2, d);
        PersonAccess.add(p3, d);
        PersonAccess.add(p4, d);

        Collection<Person> result = PersonAccess.getAll("nomatch", d);

        assertEquals(0, result.size());
    }

    @Test (expected = DBException.class)
    public void getAllThrowsExceptionIfDBClosed() throws DBException {
        d.close();

        PersonAccess.getAll("uname", d);
    }

    @Test
    public void clearRemovesAllPersons() throws DBException, SQLException {
        PersonAccess.createTable(d);
        
        PersonAccess.add(p, d);
        PersonAccess.clear(d);

        Connection c = d.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count() FROM person");
        ResultSet rs = ps.executeQuery();
        assertEquals(0, rs.getInt(1));
        rs.close();
        ps.close();
    }

    @Test
    public void clearDoesntThrowExceptionIfNoPersons() throws DBException {
        PersonAccess.createTable(d);
        
        PersonAccess.clear(d);
    }

    @Test (expected = DBException.class)
    public void clearThrowsExceptionIfDBClosed() throws DBException {
        PersonAccess.createTable(d);
        
        d.close();

        PersonAccess.clear(d);
    }

    @Test
    public void createTableCreatesPersonTable() throws DBException, SQLException {
        PersonAccess.createTable(d);

        Connection c = d.getSQLConnection();
        PreparedStatement ps = c.prepareStatement("SELECT count(*) FROM sqlite_master WHERE type = 'table' AND name = 'person'");
        ResultSet rs = ps.executeQuery();
        assertEquals(1, rs.getInt(1));

        rs.close();
        ps.close();
    }

    @Test (expected = DBException.class)
    public void createTableThrowsExceptionIfTableExists() throws DBException {
        PersonAccess.createTable(d);
        PersonAccess.createTable(d);
    }

    @Test (expected = DBException.class)
    public void createTableThrowsExceptionIfDBClosed() throws DBException {
        d.close();

        PersonAccess.createTable(d);
    }


}