package familymapserver.data.model;

/**
 * Represents a person and his/her relationships in the family map.
 */
public class Person {

    private final String id;
    private User descendant;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Person father;
    private Person mother;
    private Person spouse;

    /**
     * Creates a new Person.
     * 
     * @param id a unique identifier for this person
     * @param descendant the User in whose family map this person is found
     * @param firstName the person's first name
     * @param lastName the person's last name
     * @param gender the person's gender
     * @param father the Person object representing this person's father
     * @param mother the Person object representing this person's mother
     * @param spouse the Person object representing this person's spouse
     */
    public Person(String id, User descendant, String firstName, String lastName,
                  Gender gender, Person father, Person mother, Person spouse) {
        this.id = id;
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
    public String getId() {
        return id;
    }

    /**
     * @return the User in whose family map this person is found
     */
    public User getDescendant() {
        return descendant;
    }

    /**
     * @param descendant the User in whose family map this person is found
     */
    public void setDescendant(User descendant) {
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
     * @return the person's gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * @param gender the person's gender
     */
    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /**
     * @return the Person object representing this person's father
     */
    public Person getFather() {
        return father;
    }

    /**
     * @param father the Person object representing this person's father
     */
    public void setFather(Person father) {
        this.father = father;
    }

    /**
     * @return the Person object representing this person's mother
     */
    public Person getMother() {
        return mother;
    }

    /**
     * @param mother the Person object representing this person's mother
     */
    public void setMother(Person mother) {
        this.mother = mother;
    }

    /**
     * @return the Person object representing this person's spouse
     */
    public Person getSpouse() {
        return spouse;
    }

    /**
     * @param spouse the Person object representing this person's spouse
     */
    public void setSpouse(Person spouse) {
        this.spouse = spouse;
    }

    
}