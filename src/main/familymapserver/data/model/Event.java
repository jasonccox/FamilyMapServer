package familymapserver.data.model;

/**
 * Represents an event occurring in one person's life.
 */
public class Event {

    private final String eventID;
    private String descendant;
    private String personID;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;

    /**
     * Creates a new Event.
     * 
     * @param eventID a unique identifier for this event
     * @param descendant the username of the user in whose family map this event is found
     * @param personID the id of the person in whose life this event occurred
     * @param latitude the latitude at which the event occurred
     * @param longitude the longitude at which the event occurred
     * @param country the country in which the event occurred
     * @param city the city in which the event occurred
     * @param eventType the event's type (e.g., birth, baptism, marriage, etc.)
     * @param year the year in which the event occurred
     */
    public Event(String eventID, String descendant, String personID, float latitude, float longitude, 
                 String country, String city, String eventType, int year) {
        this.eventID = eventID;
        setDescendant(descendant);
        setPersonID(personID);
        setLatitude(latitude);
        setLongitude(longitude);
        setCountry(country);
        setCity(city);
        setEventType(eventType);
        setYear(year);
    }

    /**
     * @return a unique identifier for this event
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * @return the username of the user in whose family map this event is found
     */
    public String getDescendant() {
        return descendant;
    }

    /**
     * @param descendant the username of the user in whose family map this event is found
     */
    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    /**
     * @return the id of the person in whose life this event occurred
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * @param personID the id of the person in whose life this event occurred
     */
    public void setPersonID(String personID) {
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
     * @return the event's type (e.g., birth, baptism, marriage, etc.)
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * @param eventType the event's type (e.g., birth, baptism, marriage, etc.)
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
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

    
}