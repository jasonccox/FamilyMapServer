package familymapserver.api.service;

import familymapserver.api.request.ApiRequest;
import familymapserver.api.request.PersonRequest;
import familymapserver.api.result.PersonResult;
import familymapserver.api.result.PersonsResult;

/**
 * Contains methods providing functionality of the <code>/person</code> API route.
 * It retrieves one person, or all persons associated with a specific user, from
 * the database.
 */
public class PersonService {

    /**
     * Retrieves one person from the database.
     * 
     * @param request the person request, which specifies which person to retrieve
     * @return the result of the operation
     */
    public static PersonResult getPerson(PersonRequest request) {
        return null;
    }

    /**
     * Retrieves all persons associated with a specific user from the database.
     * 
     * @param request a request containing a user's authorization token
     * @return the result of the operation
     */
    public static PersonsResult getPersons(ApiRequest request) {
        return null;
    }

}