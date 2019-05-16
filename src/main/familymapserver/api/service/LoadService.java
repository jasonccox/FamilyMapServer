package familymapserver.api.service;

import familymapserver.api.request.LoadRequest;
import familymapserver.api.result.LoadResult;

/**
 * Contains methods providing functionality of the <code>/load</code> API route. It
 * replaces all data with user-specified data.
 */
public class LoadService {

    /**
     * Clears all data from the database and then loads the provided user, person, 
     * and event data into the database.
     * 
     * @param request the load request, which contains the data to be loaded
     * @return the result of the operation
     */
    public static LoadResult load(LoadRequest request) {
        return null;
    }

}