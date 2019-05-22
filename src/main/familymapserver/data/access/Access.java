package familymapserver.data.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Contains generic methods for accessing data in the database.
 */
public abstract class Access {

    /**
     * Gets an open connection from the database, if it has one.
     * 
     * @param db the database from which to get the connection
     * @return an open connection to the database
     * @throws DBException if the database is not open
     */
    protected static Connection getOpenConnection(Database db) throws DBException {
        Connection c = db.getSQLConnection();
        if (c == null) {
            throw new DBException("The database is closed.");
        }

        return c;
    }

    /**
     * Executes an update SQL statement on a database.
     * 
     * @param db the database on which to execute the statement
     * @param sql the statement to be executed
     * @throws DBException if the database is not open, or if another database error occurs
     */
    protected static void executeUpdate(Database db, String sql) throws DBException {
        Connection c = getOpenConnection(db);
        
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

}