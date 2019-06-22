package com.jasoncarloscox.familymapserver.api.result;

import java.util.ArrayList;
import java.util.Collection;

import com.jasoncarloscox.familymapserver.data.model.Person;

/**
 * The result of a request to the <code>/person</code> API route. It describes 
 * the outcome of an attempt to retrieve multiple persons from the database.
 */
public class PersonsResult extends ApiResult {

    private Collection<PersonResult> data;

    /**
     * Creates a new error PersonsResult.
     * 
     * @param message a description of the error
     */
    public PersonsResult(String message) {
        super(false, message);
    }

    /**
     * Creates a new success PersonsResult.
     * 
     * @param data the retrieved persons
     */
    public PersonsResult(Collection<Person> data) {
        super(true, null);
        this.data = new ArrayList<>();
        for (Person p : data) {
            this.data.add(new PersonResult(p));
        }
    }

    /**
     * @return the retrieved persons
     */
    public Collection<PersonResult> getData() {
        return data;
    }

    /**
     * @param data the retrieved persons
     */
    public void setData(Collection<PersonResult> data) {
        this.data = data;
    }

}