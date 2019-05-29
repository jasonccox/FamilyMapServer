package familymapserver.api.service;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import familymapserver.api.request.ApiRequest;
import familymapserver.api.request.EventRequest;
import familymapserver.api.result.ApiResult;
import familymapserver.api.result.EventResult;
import familymapserver.api.result.EventsResult;
import familymapserver.data.access.AuthTokenAccess;
import familymapserver.data.access.DBException;
import familymapserver.data.access.Database;
import familymapserver.data.access.EventAccess;
import familymapserver.data.model.Event;

/**
 * Contains methods providing functionality of the <code>/event</code> API route.
 * It retrieves one event, or all events associated with a specific user, from 
 * the database.
 */
public class EventService {

    private static final Logger LOG = Logger.getLogger("fms");

    /**
     * Retrieves one event from the database.
     * 
     * @param request the event request, which specifies the event to retrieve
     * @return the result of the operation
     */
    public static ApiResult getEvent(EventRequest request) {

        // validate auth token

        String assocUsername;

        try (Database db = new Database()) {
            assocUsername = new AuthTokenAccess(db).getUsername(request.getAuthToken());
        } catch (DBException e) {
            LOG.log(Level.WARNING, "Fetching auth token failed.", e);
            
            return new ApiResult(false, ApiResult.INTERNAL_SERVER_ERROR + 
                                        ": " + e.getMessage());
        } 

        if (assocUsername == null) {
            return new ApiResult(false, ApiResult.INVALID_AUTH_TOKEN_ERROR);
        }

        // fetch event

        Event event;

        try (Database db = new Database()) {
            event = new EventAccess(db).get(request.getEventId());
        } catch (DBException e) {
            LOG.log(Level.WARNING, "Fetching event failed.", e);
            
            return new ApiResult(false, ApiResult.INTERNAL_SERVER_ERROR + 
                                        ": " + e.getMessage());
        } 

        if (event == null) {
            return new ApiResult(false, EventResult.EVENT_NOT_FOUND_ERROR);
        }

        if (!assocUsername.equals(event.getAssociatedUsername())) {
            return new ApiResult(false, EventResult.NOT_USERS_EVENT_ERROR);
        }

        return new EventResult(event);
    }

    /**
     * Retrieves all of the events from the database.
     * 
     * @param request a request containing a user's authorization token
     * @return the result of the operation
     */
    public static EventsResult getEvents(ApiRequest request) {
        // validate auth token

        String assocUsername;

        try (Database db = new Database()) {
            assocUsername = new AuthTokenAccess(db).getUsername(request.getAuthToken());
        } catch (DBException e) {
            LOG.log(Level.WARNING, "Fetching auth token failed.", e);
            
            return new EventsResult(ApiResult.INTERNAL_SERVER_ERROR + 
                                   ": " + e.getMessage());
        } 

        if (assocUsername == null) {
            return new EventsResult(ApiResult.INVALID_AUTH_TOKEN_ERROR);
        }

        // fetch persons

        try (Database db = new Database()) {
            Collection<Event> events = new EventAccess(db).getAll(assocUsername);

            return new EventsResult(events);

        } catch (DBException e) {
            LOG.log(Level.WARNING, "Fetching person failed.", e);
            
            return new EventsResult(ApiResult.INTERNAL_SERVER_ERROR + 
                                   ": " + e.getMessage());
        } 
    }

}
