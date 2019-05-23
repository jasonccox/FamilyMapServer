package familymapserver.api.service;

import familymapserver.api.result.ClearResult;

/**
 * Contains methods providing functionality of the <code>/clear</code> API route. 
 * It deletes all data from the database, including user accounts, authorization 
 * tokens, persons, and events.
 */
public class ClearService {

    /**
     * Deletes all data from the database, including user accounts, authorization
     * tokens, persons, and events.
     * 
     * @return the result of the operation
     */
    public static ClearResult clear() {
        return null;
    }

}