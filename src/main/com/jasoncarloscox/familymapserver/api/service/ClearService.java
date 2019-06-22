package com.jasoncarloscox.familymapserver.api.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jasoncarloscox.familymapserver.api.result.ApiResult;
import com.jasoncarloscox.familymapserver.api.result.ClearResult;
import com.jasoncarloscox.familymapserver.data.access.DBException;
import com.jasoncarloscox.familymapserver.data.access.Database;

/**
 * Contains methods providing functionality of the <code>/clear</code> API route. 
 * It deletes all data from the database, including user accounts, authorization 
 * tokens, persons, and events.
 */
public class ClearService {

    private static final Logger LOG = Logger.getLogger("fms");

    /**
     * Deletes all data from the database, including user accounts, authorization
     * tokens, persons, and events.
     * 
     * @return the result of the operation
     */
    public static ClearResult clear() {
        try (Database db = new Database()) {
            db.clear();
            db.commit();
        } catch (DBException e) {
            LOG.log(Level.WARNING, "Clear failed.", e);
            
            return new ClearResult(ApiResult.INTERNAL_SERVER_ERROR + 
                                   ": " + e.getMessage() + 
                                   " No data was deleted from the database.");
        }

        return new ClearResult();
    }

}