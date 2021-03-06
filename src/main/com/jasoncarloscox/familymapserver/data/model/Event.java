package com.jasoncarloscox.familymapserver.data.model;

import java.util.UUID;

/**
 * Represents an event occurring in one person's life.
 */
public class Event {

    private final String eventID;
    private String associatedUsername;
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
     * @param id a unique identifier for this event
     * @param associatedUsername the username of the user in whose family map 
     *                           this event is found
     */
    public Event(String id, String associatedUsername) {
        this.eventID = id;
        setAssociatedUsername(associatedUsername);
    }

    /**
     * Creates a new Event with an auto-generated id.
     * 
     * @param associatedUsername the username of the user in whose family map 
     *                           this event is found
     */
    public Event(String associatedUsername) {
        this.eventID = UUID.randomUUID().toString();
        setAssociatedUsername(associatedUsername);
    }

    /**
     * @return a unique identifier for this event
     */
    public String getId() {
        return eventID;
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
     * Creates a copy of this event with a different id.
     * 
     * @return the copy
     */
    public Event duplicate() {
        Event copy = new Event(associatedUsername);
        copy.setLatitude(latitude);
        copy.setLongitude(longitude);
        copy.setCountry(country);
        copy.setCity(city);
        copy.setType(eventType);
        copy.setYear(year);

        return copy;
    }

}