package familymapserver.api.result;

import java.util.ArrayList;
import java.util.Collection;

import familymapserver.data.model.Event;

/**
 * The result of a request to the <code>/event</code> route. It describes the 
 * outcome of an attempt to retrieve multiple events from the database.
 */
public class EventsResult extends ApiResult {

    private Collection<EventResult> data;

    /**
     * Creates a new error EventsResult.
     * 
     * @param message a description of the error
     */
    public EventsResult(String message) {
        super(false, message);
    }

    /**
     * Creates a new success EventsResult.
     * 
     * @param data the retrieved events
     */
    public EventsResult(Collection<Event> data) {
        super(true, null);
        this.data = new ArrayList<>();
        for (Event e : data) {
            this.data.add(new EventResult(e));
        }
    }

    /**
     * @return the retrieved events
     */
    public Collection<EventResult> getData() {
        return data;
    }

    /**
     * @param data the retrieved events
     */
    public void setData(Collection<EventResult> data) {
        this.data = data;
    }

}