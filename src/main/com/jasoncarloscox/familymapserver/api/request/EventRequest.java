package com.jasoncarloscox.familymapserver.api.request;

/**
 * A request to the <code>/event/[eventId]</code> route. It is a request to
 * retrieve one event from the database.
 */
public class EventRequest extends ApiRequest {

    private String eventId;

    /**
     * Creates a new PersonRequest.
     * 
     * @param authToken the authorization token sent with the request
     * @param eventId the id of the event to retrieve from the database
     */
    public EventRequest(String authToken, String eventId) {
        super(authToken);
        setEventId(eventId);
    }

    /**
     * @return the id of the event to retrieve from the database
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * @param eventId the id of the event to retrieve from the database
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    
}