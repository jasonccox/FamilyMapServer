package com.jasoncarloscox.familymapserver.api.result;

import com.jasoncarloscox.familymapserver.data.model.Person;

/**
 * The result of a request to the <code>/person/[personID]</code> API route. It 
 * describes the outcome of an attempt to retrieve one person from the database.
 */
public class PersonResult extends ApiResult {

    /**
     * The error message used when the requested person doesn't belong to the 
     * user associated with the provided authorization token.
     */
    public static final String NOT_USERS_PERSON_ERROR = 
        "The requested person belongs to a different user";

    /**
     * The error message used when the requested person isn't found.
     */
    public static final String PERSON_NOT_FOUND_ERROR = 
        "The requested person could not be found";

    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;

    /**
     * Creates a new error PersonResult.
     * 
     * @param message a description of the error
     */
    public PersonResult(String message) {
        super(false, message);
    }

    /**
     * Creates a new success PersonResult.
     * 
     * @param person the retrieved person
     */
    public PersonResult(Person person) {
        super(true, null);
        setId(person.getId());
        setAssociatedUsername(person.getAssociatedUsername());
        setFirstName(person.getFirstName());
        setLastName(person.getLastName());
        setGender(person.getGender());
        setFather(person.getFather());
        setMother(person.getMother());
        setSpouse(person.getSpouse());
    }

    /**
     * @return a unique identifier for this person
     */
    public String getId() {
        return personID;
    }

    /**
     * @param id a unique identifier for this person
     */
    public void setId(String id) {
        this.personID = id;
    }

    /**
     * @return the username of the user in whose family map this person is found
     */
    public String getAssociatedUsername() {
        return associatedUsername;
    }

    /**
     * @param associatedUsername the username of the user in whose family map 
     *                           this person is found
     */
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    /**
     * @return the person's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the person's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the person's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the person's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the person's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the person's gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the Pid of the person representing this person's father
     */
    public String getFather() {
        return fatherID;
    }

    /**
     * @param father the id of the person representing this person's father
     */
    public void setFather(String father) {
        this.fatherID = father;
    }

    /**
     * @return the id of the person representing this person's mother
     */
    public String getMother() {
        return motherID;
    }

    /**
     * @param mother the id of the person representing this person's mother
     */
    public void setMother(String mother) {
        this.motherID = mother;
    }

    /**
     * @return the id of the person representing this person's spouse
     */
    public String getSpouse() {
        return spouseID;
    }

    /**
     * @param spouse the id of the person representing this person's spouse
     */
    public void setSpouse(String spouse) {
        this.spouseID = spouse;
    }
    
}