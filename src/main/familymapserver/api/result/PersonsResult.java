package familymapserver.api.result;

import java.util.Collection;

import familymapserver.data.model.Person;

/**
 * The result of a request to the <code>/person</code> API route. It describes the 
 * outcome of an attempt to retrieve multiple persons from the database.
 */
public class PersonsResult extends ApiResult {

    private Collection<Person> data;

    /**
     * Creates a new error PersonsResult.
     * 
     * @param message a description of the error
     */
    public PersonsResult(String message) {
        super(message);
    }

    /**
     * Creates a new success PersonsResult.
     * 
     * @param data the retrieved persons
     */
    public PersonsResult(Collection<Person> data) {
        super(null);
        setData(data);
    }

    /**
     * @return the retrieved persons
     */
    public Collection<Person> getData() {
        return data;
    }

    /**
     * @param data the retrieved persons
     */
    public void setData(Collection<Person> data) {
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