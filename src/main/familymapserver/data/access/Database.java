package familymapserver.data.access;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Stores a connection to the database. By wrapping this connection in a class,
 * the rest of the program can just pass around a DBConnection rather than
 * worrying what kind of connection it is (SQL, something else, etc.).
 */
public class Database implements Closeable {

    private static final Logger LOG = Logger.getLogger("fms");

    /**
     * Creates a new database (if needed) and adds any necessary tables.
     * 
     * @throws DBException if a database error occurs
     */
    public static void init() throws DBException {
        try (Database db = new Database()) {
            db.createTablesIfMissing();
            db.commit();
        }
    }
    
    /**
     * The path to the database, relative to the project's top directory
     */
    private static final String DB_PATH = "db/db.sqlite";

    private static final String DRIVER = "org.sqlite.JDBC";

    private Connection connection;

    /**
     * Creates and opens a new Database object.
     * 
     * @throws DBException if a database error occurs 
     */
    public Database() throws DBException {
        this(DB_PATH);
    }

    /**
     * Creates and opens a new Database object. 
     * 
     * @param dbPath the path to the database
     * @throws DBException if a database error occurs
     */
    protected Database(String dbPath) throws DBException {
        try {
            Class.forName(DRIVER);
            open(dbPath);
        } catch (ClassNotFoundException cnfe) {
            DBException dbe = new DBException("Database driver could not be found.\n" + 
                                              cnfe.getMessage());
            LOG.throwing("Database", "constructor", dbe);
            throw dbe;
        } 
    }

    /**
     * Closes the connection to the database. Any uncommitted changes are rolled 
     * back.
     * 
     * @throws DBException if the database is already closed, or if another 
     *                     database error occurs
     */
    @Override
    public void close() throws DBException {

        this.rollback();

        try {
            connection.close();
            connection = null;
        } catch (SQLException sqle) {
            DBException dbe = new DBException("Database connection could not be " +
                                              "closed.\n" + sqle.getMessage());
            LOG.throwing("Database", "close", dbe);
            throw dbe;
        }
    }

    /**
     * Commits all changes to the database since the last commit.
     * 
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    public void commit() throws DBException {
        if (connection == null) {
            throw new DBException("Cannot commit to the database when it is closed.");
        }

        try {
            connection.commit();
        } catch (SQLException sqle) {
            DBException dbe = new DBException("Database transaction could not " + 
                                              "be committed.\n" + sqle.getMessage());
            LOG.throwing("Database", "commit", dbe);
            throw dbe;
        }
    }

    /**
     * Rolls back all changes to the database since the last commit.
     * 
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    public void rollback() throws DBException {
        if (connection == null) {
            DBException dbe = new DBException("Cannot rollback the database" + 
                                              "when it is closed.");
            LOG.throwing("Database", "rollback", dbe);
            throw dbe;
        }

        try {
            connection.rollback();
        } catch (SQLException sqle) {
            DBException dbe = new DBException("Database transaction could not " + 
                                              "be rolled back.\n" + sqle.getMessage());
            LOG.throwing("Database", "rollback", dbe);
            throw dbe;
        }
    }

    /**
     * Removes all data from the database.
     * 
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    public void clear() throws DBException {
        if (connection == null) {
            DBException dbe = new DBException("Cannot clear the database when " + 
                                              "it is closed.");
            LOG.throwing("Database", "clear", dbe);
            throw dbe;
        }

        (new UserAccess(this)).clear();
        (new AuthTokenAccess(this)).clear();
        (new PersonAccess(this)).clear();
        (new EventAccess(this)).clear();
    }

    /**
     * Creates user, auth token, person, and event tables if they are not already
     * present in the database.
     * 
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    protected void createTablesIfMissing() throws DBException {
        (new UserAccess(this)).createTableIfMissing();
        (new AuthTokenAccess(this)).createTableIfMissing();
        (new PersonAccess(this)).createTableIfMissing();
        (new EventAccess(this)).createTableIfMissing();
    }

    /**
     * @return the connection to the database, or null if their is no open 
     *         connection
     */
    protected Connection getSQLConnection() {
        return connection;
    }

    /**
     * Opens the connection to the database. If the database does not already 
     * exist on disk, a new one will be created.
     * 
     * @param dbPath the path to the database
     * @throws DBException if a database error occurs
     */
    private void open(String dbPath) throws DBException {
        try {

            if (connection != null) {
                DBException dbe = new DBException("Cannot open the database " + 
                                                  "when it is already open.");
                LOG.throwing("Database", "open", dbe);
                throw dbe;
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            connection.setAutoCommit(false);
        } catch (SQLException sqle) {
            DBException dbe = new DBException("Database connection could not " + 
                                              "be opened.\n" + sqle.getMessage());
            LOG.throwing("Database", "open", dbe);
            throw dbe;
        }
    }
}