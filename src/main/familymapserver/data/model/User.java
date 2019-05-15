package familymapserver.data.model;

/**
 * Represents a user of the family map application.
 */
public class User {
    
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String personId;

    /**
     * Creates a new User.
     * 
     * @param username the user's username - a unique identifier
     * @param password the user's password for authenticating
     * @param email the user's email address
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param gender the user's gender
     * @param personId the id of the person representing this user in the family map
     */
    public User(String username, String password, String email, String firstName,
                String lastName, Gender gender, String personId) {
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setPersonId(personId);
    }

    /**
     * @return the user's username - a unique identifier
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the user's username - a unique identifier
     */
    public void setUsername(String username) {
        this.username = username;
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
    public Gender getGender() {
        return gender;
    }

    /**
     * @param gender the user's gender
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * @return the id of the person representing this user in the family map
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * @param personId the id of the person representing this user in the family map
     */
    public void setPersonId(String personId) {
        this.personId = personId;
    }

    
}