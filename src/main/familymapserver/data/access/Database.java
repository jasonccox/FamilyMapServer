package familymapserver.data.access;

import java.sql.Connection;

/**
 * Stores a connection to the database. By wrapping this connection in a class,
 * the rest of the program can just pass around a DBConnection rather than
 * worrying what kind of connection it is (SQL, something else, etc.).
 */
public class Database {
    
    private Connection connection;

    /**
     * Creates a new Database object. If the database does not already exist on disk,
     * a new one will be created.
     */
    public Database() {

    }

    /**
     * Opens the connection to the database.
     * 
     * @throws DBException
     */
    public void open() throws DBException {

    }

    /**
     * Closes the connection to the database. Any uncommitted changes are rolled back.
     * 
     * @throws DBException
     */
    public void close() throws DBException {

    }

    /**
     * Commits all changes to the database since the last commit.
     * 
     * @throws DBException
     */
    public void commit() throws DBException {

    }

    /**
     * Rolls back all changes to the database since the last commit.
     * 
     * @throws DBException
     */
    public void rollback() throws DBException {

    }

    /**
     * Removes all data from the database.
     * 
     * @throws DBException
     */
    public void clear() throws DBException {

    }

    /**
     * @return the connection to the database, or null if their is no open connection
     */
    protected Connection getSQLConnection() {
        return connection;
    }
}