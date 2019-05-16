package familymapserver.api.result;

import java.util.Collection;

import familymapserver.data.model.Event;

/**
 * The result of a request to the <code>/event</code> route. It describes the 
 * outcome of an attempt to retrieve multiple events from the database.
 */
public class EventsResult extends ApiResult {

    private Collection<Event> data;

    /**
     * Creates a new error EventsResult.
     * 
     * @param message a description of the error
     */
    public EventsResult(String message) {
        super(message);
    }

    /**
     * Creates a new success EventsResult.
     * 
     * @param data the retrieved events
     */
    public EventsResult(Collection<Event> data) {
        super(null);
        setData(data);
    }

    /**
     * @return the retrieved events
     */
    public Collection<Event> getData() {
        return data;
    }

    /**
     * @param data the retrieved events
     */
    public void setData(Collection<Event> data) {
        this.data = data;
    }

    /**
     * @return whether the request was successfully fulfilled
     */
    @Override
    public boolean isSuccess() {
        return false;
    }

}