package familymapserver.data.access;

import java.sql.SQLException;

/**
 * An exception caused by database operations.
 */
public class DBException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new DBException.
     * 
     * @param message a brief explanation of why the exception occurred
     */
    public DBException(String message) {

    }

    /**
     * Creates a new DBException to wrap a SQL Exception.
     * 
     * @param e the SQLException that was thrown
     */
    protected DBException(SQLException e) {
        this("A database error occurred: \n" + e.getMessage());
    }
}