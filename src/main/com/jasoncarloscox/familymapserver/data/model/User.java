package com.jasoncarloscox.familymapserver.data.model;

/**
 * Represents a user of the family map application.
 */
public class User {
    
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String personID;

    /**
     * Creates a new User.
     * 
     * @param username the user's username - a unique identifier
     * @param password the user's password for authenticating
     */
    public User(String username, String password) {         
        setUsername(username);
        setPassword(password);
    }

    /**
     * @return the user's username - a unique identifier
     */
    public String getUsername() {
        return userName;
    }

    /**
     * @param username the user's username - a unique identifier
     */
    public void setUsername(String username) {
        this.userName = username;
    }

    /**
     * @return the user's password for authenticating
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the user's password for authenticating
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the user's email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the user's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the user's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the user's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the user's gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the id of the person representing this user in the family map
     */
    public String getPersonId() {
        return personID;
    }

    /**
     * @param personId the id of the person representing this user in the family 
     *                 map
     */
    public void setPersonId(String personId) {
        this.personID = personId;
    }

    
}