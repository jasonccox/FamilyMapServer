package familymapserver.api.result;

import familymapserver.data.model.Person;

/**
 * The result of a request to the <code>/person/[personID]</code> API route. It describes the 
 * outcome of an attempt to retrieve one person from the database.
 */
public class PersonResult extends ApiResult {

    /**
     * The error message used when the requested person doesn't belong to the user associated
     * with the provided authorization token.
     */
    public static final String NOT_USERS_PERSON_ERROR = "The requested person belongs to a different user";

    private Person person;

    /**
     * Creates a new error PersonResult.
     * 
     * @param message a description of the error
     */
    public PersonResult(String message) {
        super(message);
    }

    /**
     * Creates a new success PersonResult.
     * 
     * @param person the retrieved person
     */
    public PersonResult(Person person) {
        super(null);
        setPerson(person);
    }

    /**
     * @return the retrieved personn
     */
    public Person getPerson() {
        return person;
    }

    /**
     * @param person the retrieved person
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * @return whether the request was successfully fulfilled
     */
    @Override
    public boolean isSuccess() {
        return false;
    }
    
}