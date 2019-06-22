package com.jasoncarloscox.familymapserver.data.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Contains generic methods for accessing data in the database.
 */
public abstract class Access {

    private static final Logger LOG = Logger.getLogger("fms");

    private Database db;

    /**
     * Creates a new Access object.
     * 
     * @param db the database to be accessed
     */
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
            DBException e = new DBException("The database is closed.");
            LOG.throwing("Access", "getOpenConnection", e);
            throw e;
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
        } catch (SQLException sqle) {
            DBException dbe = new DBException(sqle);
            LOG.throwing("Access", "executeUpdate", dbe);
            throw dbe;
        }
    }

}