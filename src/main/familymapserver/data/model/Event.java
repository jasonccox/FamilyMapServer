package familymapserver.data.model;

/**
 * Represents an event occurring in one person's life.
 */
public class Event {

    private final String id;
    private User descendant;
    private Person person;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String type;
    private int year;

    /**
     * Creates a new Event.
     * 
     * @param id a unique identifier for this event
     * @param descendant the User in whose family map this event is found
     * @param person the Person in whose life this event occurred
     * @param latitude the latitude at which the event occurred
     * @param longitude the longitude at which the event occurred
     * @param country the country in which the event occurred
     * @param city the city in which the event occurred
     * @param type the event's type (e.g., birth, baptism, marriage, etc.)
     * @param year the year in which the event occurred
     */
    public Event(String id, User descendant, Person person, float latitude, float longitude, 
                 String country, String city, String type, int year) {
        this.id = id;
        setDescendant(descendant);
        setPerson(person);
        setLatitude(latitude);
        setLongitude(longitude);
        setCountry(country);
        setCity(city);
        setType(type);
        setYear(year);
    }

    /**
     * @return a unique identifier for this event
     */
    public String getId() {
        return id;
    }

    /**
     * @return the User in whose family map this event is found
     */
    public User getDescendant() {
        return descendant;
    }

    /**
     * @param descendant the User in whose family map this event is found
     */
    public void setDescendant(User descendant) {
        this.descendant = descendant;
    }

    /**
     * @return the Person in whose life this event occurred
     */
    public Person getPerson() {
        return person;
    }

    /**
     * @param person the Person in whose life this event occurred
     */
    public void setPerson(Person person) {
        this.person = person;
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
        return type;
    }

    /**
     * @param type the event's type 
     */
    public void setType(String type) {
        this.type = type;
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