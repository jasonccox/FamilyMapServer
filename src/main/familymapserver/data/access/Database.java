package familymapserver.data.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Stores a connection to the database. By wrapping this connection in a class,
 * the rest of the program can just pass around a DBConnection rather than
 * worrying what kind of connection it is (SQL, something else, etc.).
 */
public class Database {
    
    /**
     * The path to the database, relative to the project's top directory
     */
    private static final String DB_PATH = "db/db.sqlite";

    private static final String DRIVER = "org.sqlite.JDBC";

    private Connection connection;

    /**
     * Creates a new Database object. 
     */
    public Database() throws DBException {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            throw new DBException("Database driver could not be found.\n" + e.getMessage());
        } 
    }

    /**
     * Opens the connection to the database. If the database does not already exist on disk,
     * a new one will be created.
     * 
     * @throws DBException if the database is already open, or if another database error 
     * occurs
     */
    public void open() throws DBException {
        open(DB_PATH);
    }

     /**
     * Opens the connection to the database. If the database does not already exist on disk,
     * a new one will be created.
     * 
     * @param dbPath the path to the database, relative to the project's top directory
     * @throws DBException if the database is already open, or if another database error 
     * occurs
     */
    protected void open(String dbPath) throws DBException {
        try {

            if (connection != null) {
                throw new DBException("Cannot open the database when it is already open.");
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DBException("Database connection could not be opened.\n" + e.getMessage());
        }
    }

    /**
     * Closes the connection to the database. Any uncommitted changes are rolled back.
     * 
     * @throws DBException if the database is already closed, or if another database error 
     * occurs
     */
    public void close() throws DBException {

        this.rollback();

        try {
            connection.close();
            connection = null;
        } catch (SQLException e) {
            throw new DBException("Database connection could not be closed.\n" + e.getMessage());
        }
    }

    /**
     * Commits all changes to the database since the last commit.
     * 
     * @throws DBException if the database is not open, or if another database error 
     * occurs
     */
    public void commit() throws DBException {
        if (connection == null) {
            throw new DBException("Cannot commit to the database when it is closed.");
        }

        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DBException("Database transaction could not be committed.\n" + e.getMessage());
        }
    }

    /**
     * Rolls back all changes to the database since the last commit.
     * 
     * @throws DBException if the database is not open, or if another database error 
     * occurs
     */
    public void rollback() throws DBException {
        if (connection == null) {
            throw new DBException("Cannot rollback the database when it is closed.");
        }

        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new DBException("Database transaction could not be rolled back.\n" + e.getMessage());
        }
    }

    /**
     * Removes all data from the database.
     * 
     * @throws DBException if the database is not open, or if another database error 
     * occurs
     */
    public void clear() throws DBException {
        if (connection == null) {
            throw new DBException("Cannot clear the database when it is closed.");
        }

        /* UserAccess.clear(this);
        AuthTokenAccess.clear(this);
        PersonAccess.clear(this);
        EventAccess.clear(this); */
    }

    /**
     * @return the connection to the database, or null if their is no open connection
     */
    protected Connection getSQLConnection() {
        return connection;
    }
}