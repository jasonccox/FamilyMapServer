package familymapserver.api.service;

import familymapserver.api.request.FillRequest;
import familymapserver.api.result.FillResult;

/**
 * Contains methods providing functionality of the <code>/fill</code> API route.
 * It populates the database with generated data for a specific user.
 */
public class FillService {

    /**
     * Populates the server's database with generated data for a user.
     * 
     * @param request the fill request, which specifies the user for whom to
     * generate data and how many generations of data to generate
     * @return the result of the operation
     */
    public static FillResult fill(FillRequest request) {
        return null;
    }

}