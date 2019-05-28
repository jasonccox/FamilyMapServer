package familymapserver.api.service;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import familymapserver.api.request.ApiRequest;
import familymapserver.api.request.PersonRequest;
import familymapserver.api.result.ApiResult;
import familymapserver.api.result.PersonResult;
import familymapserver.api.result.PersonsResult;
import familymapserver.data.access.AuthTokenAccess;
import familymapserver.data.access.DBException;
import familymapserver.data.access.Database;
import familymapserver.data.access.PersonAccess;
import familymapserver.data.model.Person;

/**
 * Contains methods providing functionality of the <code>/person</code> API route.
 * It retrieves one person, or all persons associated with a specific user, from
 * the database.
 */
public class PersonService {

    private static final Logger LOG = Logger.getLogger("fms");

    /**
     * Retrieves one person from the database.
     * 
     * @param request the person request, which specifies which person to retrieve
     * @return the result of the operation
     */
    public static PersonResult getPerson(PersonRequest request) {
        
        // validate auth token

        String assocUsername;

        try (Database db = new Database()) {
            assocUsername = new AuthTokenAccess(db).getUsername(request.getAuthToken());
        } catch (DBException e) {
            LOG.log(Level.WARNING, "Fetching auth token failed.", e);
            
            return new PersonResult(ApiResult.INTERNAL_SERVER_ERROR + 
                                   ": " + e.getMessage());
        } 

        if (assocUsername == null) {
            return new PersonResult(ApiResult.INVALID_AUTH_TOKEN_ERROR);
        }

        // fetch person

        Person person;

        try (Database db = new Database()) {
            person = new PersonAccess(db).get(request.getPersonId());
        } catch (DBException e) {
            LOG.log(Level.WARNING, "Fetching person failed.", e);
            
            return new PersonResult(ApiResult.INTERNAL_SERVER_ERROR + 
                                   ": " + e.getMessage());
        } 

        if (person == null) {
            return new PersonResult(PersonResult.PERSON_NOT_FOUND_ERROR);
        }

        if (!assocUsername.equals(person.getAssociatedUsername())) {
            return new PersonResult(PersonResult.NOT_USERS_PERSON_ERROR);
        }

        return new PersonResult(person);
    }

    /**
     * Retrieves all persons associated with a specific user from the database.
     * 
     * @param request a request containing a user's authorization token
     * @return the result of the operation
     */
    public static PersonsResult getPersons(ApiRequest request) {
        // validate auth token

        String assocUsername;

        try (Database db = new Database()) {
            assocUsername = new AuthTokenAccess(db).getUsername(request.getAuthToken());
        } catch (DBException e) {
            LOG.log(Level.WARNING, "Fetching auth token failed.", e);
            
            return new PersonsResult(ApiResult.INTERNAL_SERVER_ERROR + 
                                    ": " + e.getMessage());
        } 

        if (assocUsername == null) {
            return new PersonsResult(ApiResult.INVALID_AUTH_TOKEN_ERROR);
        }

        // fetch persons

        try (Database db = new Database()) {
            Collection<Person> persons = new PersonAccess(db).getAll(assocUsername);

            return new PersonsResult(persons);

        } catch (DBException e) {
            LOG.log(Level.WARNING, "Fetching person failed.", e);
            
            return new PersonsResult(ApiResult.INTERNAL_SERVER_ERROR + 
                                   ": " + e.getMessage());
        } 
    }

}