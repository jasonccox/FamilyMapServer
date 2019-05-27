package familymapserver.api.result;

import familymapserver.data.model.Event;

/**
 * The result of a request to the <code>/event/[eventID]</code> API route. It
 * describes the outcome of an attempt to add one event to the database.
 */
public class EventResult extends ApiResult {

    /**
     * The error message used when the requested event doesn't belong to the 
     * user associated with the provided authorization token.
     */
    public static final String NOT_USERS_EVENT_ERROR = 
        "The requested event belongs to a different user";

    private String eventID;
    private String associatedUsername;
    private String personID;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;

    /**
     * Creates a new error EventResult.
     * 
     * @param message a description of the error
     */
    public EventResult(String message) {
        super(false, message);
    }

    /**
     * Creates a new success EventResult.
     * 
     * @param event the retrieved Event
     */
    public EventResult(Event event) {
        super(true, null);

        if (event == null) {
            return;
        }

        setId(event.getId());
        setAssociatedUsername(event.getAssociatedUsername());
        setPersonId(event.getPersonId());
        setLatitude(event.getLatitude());
        setLongitude(event.getLongitude());
        setCountry(event.getCountry());
        setCity(event.getCity());
        setType(event.getType());
        setYear(event.getYear());
    }

    /**
     * @return a unique identifier for this event
     */
    public String getId() {
        return eventID;
    }

    /**
     * @param id a unique identifier for this event
     */
    public void setId(String id) {
        this.eventID = id;
    }

    /**
     * @return the username of the user in whose family map this event is found
     */
    public String getAssociatedUsername() {
        return associatedUsername;
    }

    /**
     * @param associatedUsername the username of the user in whose family map 
     *                           this event is found
     */
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    /**
     * @return the id of the person in whose life this event occurred
     */
    public String getPersonId() {
        return personID;
    }

    /**
     * @param personID the id of the person in whose life this event occurred
     */
    public void setPersonId(String personID) {
        this.personID = personID;
    }

    /**
     * @return the latitude at which the event occurred
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude at which the event occurred
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude at which the event occurred
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude at which the event occurred
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the country in which the event occurred
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country in which the event occurred
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the city in which the event occurred
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city in which the event occurred
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the event's type
     */
    public String getType() {
        return eventType;
    }

    /**
     * @param type the event's type 
     */
    public void setType(String type) {
        this.eventType = type;
    }

    /**
     * @return the year in which the event occurred
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year in which the event occurred
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return whether the request was successfully fulfilled
     */
    @Override
    public boolean isSuccess() {
        return false;
    }

}