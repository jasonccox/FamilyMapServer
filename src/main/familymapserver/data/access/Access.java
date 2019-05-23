package familymapserver.data.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Contains generic methods for accessing data in the database.
 */
public abstract class Access {

    private Database db;

    protected Access(Database db) {
        assert db != null;

        this.db = db;
    }

    /**
     * Gets an open connection from the database, if it has one.
     * 
     * @return an open connection to the database
     * @throws DBException if the database is not open
     */
    protected Connection getOpenConnection() throws DBException {
        Connection conn = db.getSQLConnection();
        if (conn == null) {
            throw new DBException("The database is closed.");
        }

        return conn;
    }

    /**
     * Executes an update SQL statement on a database.
     * 
     * @param sql the statement to be executed
     * @throws DBException if the database is not open, or if another database 
     *                     error occurs
     */
    protected void executeUpdate(String sql) throws DBException {
        assert sql != null;

        Connection conn = getOpenConnection();
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

}