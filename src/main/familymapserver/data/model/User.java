package familymapserver.data.model;

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
     * @param userName the user's username - a unique identifier
     * @param password the user's password for authenticating
     * @param email the user's email address
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param gender the user's gender ({@link familymapserver.data.model.Person#MALE Person.MALE} or {@link familymapserver.data.model.Person#FEMALE Person.FEMALE})
     * @param personID the id of the person representing this user in the family map
     */
    public User(String userName, String password, String email, String firstName,
                String lastName, String gender, String personID) {
        setUserName(userName);
        setPassword(password);
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setPersonID(personID);
    }

    /**
     * @return the user's username - a unique identifier
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the user's username - a unique identifier
     */
    public void setUserName(String userName) {
        this.userName = userName;
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
     * @return the user's gender ({@link familymapserver.data.model.Person#MALE Person.MALE} or {@link familymapserver.data.model.Person#FEMALE Person.FEMALE})
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the user's gender ({@link familymapserver.data.model.Person#MALE Person.MALE} or {@link familymapserver.data.model.Person#FEMALE Person.FEMALE})
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the id of the person representing this user in the family map
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * @param personID the Person object representing this user in the family map
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    
}