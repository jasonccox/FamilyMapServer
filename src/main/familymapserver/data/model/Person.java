package familymapserver.data.model;

/**
 * Represents a person and his/her relationships in the family map.
 */
public class Person {

    private final String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private String gender;
    private String father;
    private String mother;
    private String spouse;

    /**
     * Creates a new Person.
     * 
     * @param id a unique identifier for this person
     * @param associatedUsername the username of the user in whose family map this person is found
     */
    public Person(String id, String associatedUsername) {
        this.personID = id;
        setAssociatedUsername(associatedUsername);
    }

    /**
     * Creates a new Person with an auto-generated id.
     * 
     * @param associatedUsername the username of the user in whose family map 
     *                           this person is found
     */
    public Person(String associatedUsername) {
        this.personID = "PLACEHOLDER"; // TODO: change this!
        setAssociatedUsername(associatedUsername);
    }

    /**
     * @return a unique identifier for this person
     */
    public String getId() {
        return personID;
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
        return father;
    }

    /**
     * @param father the id of the person representing this person's father
     */
    public void setFather(String father) {
        this.father = father;
    }

    /**
     * @return the id of the person representing this person's mother
     */
    public String getMother() {
        return mother;
    }

    /**
     * @param mother the id of the person representing this person's mother
     */
    public void setMother(String mother) {
        this.mother = mother;
    }

    /**
     * @return the id of the person representing this person's spouse
     */
    public String getSpouse() {
        return spouse;
    }

    /**
     * @param spouse the id of the person representing this person's spouse
     */
    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    
}