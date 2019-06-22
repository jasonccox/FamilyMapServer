package com.jasoncarloscox.familymapserver.data.access;

import java.io.IOException;
import java.sql.SQLException;

/**
 * An exception caused by database operations.
 */
public class DBException extends IOException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new DBException.
     * 
     * @param message a brief explanation of why the exception occurred
     */
    public DBException(String message) {
        super(message);
    }

    /**
     * Creates a new DBException to wrap a SQL Exception.
     * 
     * @param e the SQLException that was thrown
     */
    protected DBException(SQLException e) {
        this("A database error occurred. " + e.getMessage());
    }
}