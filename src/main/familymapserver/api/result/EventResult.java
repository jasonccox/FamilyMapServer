package familymapserver.api.result;

import familymapserver.data.model.Event;

/**
 * The result of a request to the <code>/event/[eventID]</code> API route. It
 * describes the outcome of an attempt to add one event to the database.
 */
public class EventResult extends ApiResult {

    /**
     * The error message used when the requested event doesn't belong to the user associated
     * with the provided authorization token.
     */
    public static final String NOT_USERS_EVENT_ERROR = "The requested event belongs to a different user";

    private Event event;

    /**
     * Creates a new error EventResult.
     * 
     * @param message a description of the error
     */
    public EventResult(String message) {
        super(message);
    }

    /**
     * Creates a new success EventResult.
     * 
     * @param event the retrieved Event
     */
    public EventResult(Event event) {
        super(null);
        setEvent(event);
    }

    /**
     * @return the retrieved event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * @param event the retrieved event
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * @return whether the request was successfully fulfilled
     */
    @Override
    public boolean isSuccess() {
        return false;
    }

}