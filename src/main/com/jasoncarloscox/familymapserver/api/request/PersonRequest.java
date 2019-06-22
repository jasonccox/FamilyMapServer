package com.jasoncarloscox.familymapserver.api.request;

/**
 * A request to the <code>/person/[personId]</code> route. It is a request to
 * retrieve one person from the database.
 */
public class PersonRequest extends ApiRequest {

    private String personId;

    /**
     * Creates a new PersonRequest.
     * 
     * @param authToken the authorization token sent with the request
     * @param personId the id of the person to retrieve from the database
     */
    public PersonRequest(String authToken, String personId) {
        super(authToken);
        setPersonId(personId);
    }

    /**
     * @return the id of the person to retrieve from the database
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * @param personId the id of the person to retrieve from the database
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
}