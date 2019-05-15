package familymapserver.data.model;

/**
 * Represents a person and his/her relationships in the family map.
 */
public class Person {

    /**
     * The string used to indicate male gender.
     */
    public static final String MALE = "m";

    /**
     * The string used to indicate female gender.
     */
    public static final String FEMALE = "f";

    private final String personID;
    private String descendant;
    private String firstName;
    private String lastName;
    private String gender;
    private String father;
    private String mother;
    private String spouse;

    /**
     * Creates a new Person.
     * 
     * @param personID a unique identifier for this person
     * @param descendant the username of the user in whose family map this person is found
     * @param firstName the person's first name
     * @param lastName the person's last name
     * @param gender the person's gender ({@link familymapserver.data.model.Person#MALE Person.MALE} or {@link familymapserver.data.model.Person#FEMALE Person.FEMALE})
     * @param father the id of the person representing this person's father
     * @param mother the id of the person representing this person's mother
     * @param spouse the id of the person representing this person's spouse
     */
    public Person(String personID, String descendant, String firstName, String lastName,
                  String gender, String father, String mother, String spouse) {
        this.personID = personID;
        setDescendant(descendant);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setFather(father);
        setMother(mother);
        setSpouse(spouse);
    }

    /**
     * @return a unique identifier for this person
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * @return the username of the user in whose family map this person is found
     */
    public String getDescendant() {
        return descendant;
    }

    /**
     * @param descendant the username of the user in whose family map this person is found
     */
    public void setDescendant(String descendant) {
        this.descendant = descendant;
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
     * @return the person's gender ({@link familymapserver.data.model.Person#MALE Person.MALE} or {@link familymapserver.data.model.Person#FEMALE Person.FEMALE})
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the person's gender ({@link familymapserver.data.model.Person#MALE Person.MALE} or {@link familymapserver.data.model.Person#FEMALE Person.FEMALE})
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the id of the person representing this person's father
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