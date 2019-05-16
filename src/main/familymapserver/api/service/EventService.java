package familymapserver.api.service;

import familymapserver.api.request.ApiRequest;
import familymapserver.api.request.EventRequest;
import familymapserver.api.result.EventResult;
import familymapserver.api.result.EventsResult;

/**
 * Contains methods providing functionality of the <code>/event</code> API route. It
 * retrieves one event, or all events associated with a specific user, from the 
 * database.
 */
public class EventService {

    /**
     * Retrieves one event from the database.
     * 
     * @param request the event request, which specifies the event to retrieve
     * @return the result of the operation
     */
    public static EventResult getEvent(EventRequest request) {
        return null;
    }

    /**
     * Retrieves all of the events from the database.
     * 
     * @param request a request containing a user's authorization token
     * @return the result of the operation
     */
    public static EventsResult getEvents(ApiRequest request) {
        return null;
    }

}
