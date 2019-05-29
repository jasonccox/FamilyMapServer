package familymapserver.api.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import familymapserver.api.request.FillRequest;
import familymapserver.api.result.FillResult;
import familymapserver.data.access.DBException;
import familymapserver.data.access.Database;
import familymapserver.data.access.EventAccess;
import familymapserver.data.access.PersonAccess;
import familymapserver.data.access.UserAccess;
import familymapserver.data.model.Event;
import familymapserver.data.model.Gender;
import familymapserver.data.model.Person;
import familymapserver.data.model.User;
import familymapserver.util.LocationGenerator;
import familymapserver.util.LocationGenerator.Location;
import familymapserver.util.NameGenerator;

/**
 * Contains methods providing functionality of the <code>/fill</code> API route.
 * It populates the database with generated data for a specific user.
 */
public class FillService {

    private static final Logger LOG = Logger.getLogger("fms");
    private static final Random random = new Random();
    private static final int ADULT_AGE = 18;
    private static final int MAX_AGE = 100;
    private static final int CURRENT_YEAR = 
        Calendar.getInstance().get(Calendar.YEAR) - 1900;

    /**
     * Populates the server's database with generated data for a user.
     * 
     * @param request the fill request, which specifies the user for whom to
     * generate data and how many generations of data to generate
     * @return the result of the operation
     */
    public static FillResult fill(FillRequest request) {
        
        // make sure it's a valid user

        User user = null;
        try (Database db = new Database()) {
            user = new UserAccess(db).get(request.getUsername());
        } catch (DBException e) {
            LOG.log(Level.WARNING, "Verifying user failed.", e);
            
            return new FillResult(FillResult.INTERNAL_SERVER_ERROR + 
                                   ": " + e.getMessage());
        }

        if (user == null) {
            return new FillResult("The specified user could not be found.");
        }

        // only accept non-negative numbers of generations 

        if (request.getGenerations() < 0) {
            return new FillResult("The number of generations must be non-negative.");
        }

        // create a person representing the user

        Person userPerson = new Person(user);
        user.setPersonId(userPerson.getId());

        int maxBirthYear = CURRENT_YEAR - random.nextInt(MAX_AGE - ADULT_AGE);
        Event userBirth = generateBirth(userPerson, maxBirthYear);

        Collection<Event> userEvents = 
            generateNonMarriageEvents(userPerson, userBirth.getYear(), CURRENT_YEAR + 1);

        // create ancestor data

        Collection<Object> ancestorData = 
            generateAncestorData(userPerson, userBirth.getYear(), request.getGenerations());

        // add data to database

        int personsCreated = 1; // userPerson
        int eventsCreated = userEvents.size();

        try (Database db = new Database()) {
            PersonAccess personAccess = new PersonAccess(db);
            EventAccess eventAccess = new EventAccess(db);

            // first clear existing data in database
            personAccess.clearAll(request.getUsername());
            eventAccess.clearAll(request.getUsername());

            // update the user to have the new personId
            new UserAccess(db).updatePersonId(user);

            personAccess.add(userPerson);

            for (Event e : userEvents) {
                eventAccess.add(e);
            }

            for (Object o : ancestorData) {
                if (o instanceof Person) {
                    personsCreated++;
                    personAccess.add((Person) o);
                } else if (o instanceof Event) {
                    eventsCreated++;
                    eventAccess.add((Event) o);
                }
            }

            db.commit();
            
        } catch (DBException e) {
            LOG.log(Level.SEVERE, "Adding data failed.", e);
            
            return new FillResult(FillResult.INTERNAL_SERVER_ERROR + 
                                   ": " + e.getMessage());
        }

        return new FillResult(personsCreated, eventsCreated);

    }

    /**
     * Calculates an appropriate birth year for a person based on his/her year
     * of marriage.
     * 
     * @param marriageYear the person's year of marriage
     * @return an appropriate birth year for the person
     */
    private static int calcBirthYear(int marriageYear) {
        return marriageYear - ADULT_AGE - random.nextInt(10);
    }

    /**
     * Creates ancestor data for a given person. This includes Person objects
     * representing direct ancestors (parents, grandparents, etc.) as well as at
     * least three Event objects associated with each Person.
     * 
     * @param person the person for whom to create ancestor data
     * @param personBirthYear the birth year of person
     * @param numGenerations the number of generations of ancestor data to be
     *                       created
     * @return the Person and Event objects
     */
    private static Collection<Object> generateAncestorData(Person person, 
                                                           int personBirthYear, 
                                                           int numGenerations) {

        Collection<Object> ancestorData = new ArrayList<>();

        if (numGenerations == 0) {
            return ancestorData;
        }

        // create parents and link to each other and child

        Person dad = generatePerson(person.getAssociatedUsername(), Gender.MALE, 
                                    person.getLastName());
        Person mom = generatePerson(person.getAssociatedUsername(), Gender.FEMALE, 
                                    null);

        dad.setSpouse(mom.getId());
        mom.setSpouse(dad.getId());

        person.setFather(dad.getId());
        person.setMother(mom.getId());

        ancestorData.add(dad);
        ancestorData.add(mom);

        // create marriage events

        Event dadMarriage = generateMarriage(dad, mom, personBirthYear);
        dadMarriage.setPersonId(dad.getId());
        ancestorData.add(dadMarriage);

        Event momMarriage = dadMarriage.duplicate();        
        momMarriage.setPersonId(mom.getId());
        ancestorData.add(momMarriage);

        int marriageYear = dadMarriage.getYear();

        // create other events
    
        int dadBirthYear = calcBirthYear(marriageYear);
        ancestorData.addAll(generateNonMarriageEvents(dad, dadBirthYear, 
                                                      marriageYear));

        int momBirthYear = calcBirthYear(marriageYear);
        ancestorData.addAll(generateNonMarriageEvents(mom, momBirthYear, 
                                                      marriageYear));

        // recursively add father's and mother's ancestors' data

        ancestorData.addAll(generateAncestorData(dad, dadBirthYear, 
                                                 numGenerations - 1));

        ancestorData.addAll(generateAncestorData(mom, momBirthYear, 
                                                 numGenerations - 1));
        
        return ancestorData;
    }

    /**
     * Creates a new person with random data.
     * 
     * @param assocUsername the username of the user with whom this person is
     *                      associated
     * @param gender the gender of the person
     * @param surname the surname of the person. If this value is null, a random
     *                surname will be generated.
     * @return a new person
     */
    private static Person generatePerson(String assocUsername, String gender,
                                         String surname) {

        Person p = new Person(assocUsername);
        p.setGender(gender);
        
        if (Gender.MALE.equals(gender)) {
            p.setFirstName(NameGenerator.getMaleName());
        } else if (Gender.FEMALE.equals(gender)) {
            p.setFirstName(NameGenerator.getFemaleName());
        }

        if (surname == null) {
            p.setLastName(NameGenerator.getSurname());
        } else {
            p.setLastName(surname);
        }

        return p;
    }

    /**
     * Creates birth, baptism, graduation, and (maybe) death events associated 
     * with a given person.
     * 
     * @param person the person in whose life the events occurred
     * @param birthYear the person's birth year
     * @param minDeathYear the earliest year in which the person could have died.
     *                     If this is in the future, no death event will be
     *                     created.
     * @return the events
     */
    private static Collection<Event> generateNonMarriageEvents(Person person, 
                                                               int birthYear, 
                                                               int minDeathYear) {

        Collection<Event> events = new ArrayList<>();

        Event birth = generateBirth(person, birthYear);
        events.add(birth);

        Event baptism = generateBaptism(person, birth.getYear(), CURRENT_YEAR);
        events.add(baptism);

        Event graduation = generateGraduation(person, birth.getYear());
        events.add(graduation);

        if (minDeathYear <= CURRENT_YEAR) {
            Event death = generateDeath(person, minDeathYear);

            if (death.getYear() <= CURRENT_YEAR) {
                events.add(death);
            }
        }

        return events;
    }

    /**
     * Creates a marriage event.
     * 
     * @param husband the husband
     * @param wife the wife
     * @param maxYear the latest year in which the marriage could have occurred
     * @return a marriage event
     */
    private static Event generateMarriage(Person husband, Person wife, int maxYear) {
        Event marriage = new Event(husband.getAssociatedUsername());
        marriage.setType("Marriage");
        marriage.setYear(maxYear - random.nextInt(5));
        
        Location location = LocationGenerator.getLocation();
        marriage.setLatitude(location.getLatitude());
        marriage.setLongitude(location.getLongitude());
        marriage.setCountry(location.getCountry());
        marriage.setCity(location.getCity());

        return marriage;
    }

    /**
     * Creates a birth event.
     * 
     * @param person the person whose birth this event represents
     * @param year the year of the birth
     * @return a birth event
     */
    private static Event generateBirth(Person person, int year) {
        Event birth = new Event(person.getAssociatedUsername());
        birth.setType("Birth");
        birth.setYear(year);
        birth.setPersonId(person.getId());

        Location location = LocationGenerator.getLocation();
        birth.setLatitude(location.getLatitude());
        birth.setLongitude(location.getLongitude());
        birth.setCountry(location.getCountry());
        birth.setCity(location.getCity());

        return birth;
    }

    /**
     * Creates a death event.
     * 
     * @param person the person whose death this event represents
     * @param minYear the earliest year in which the person could have died
     * @return a death event
     */
    private static Event generateDeath(Person person, int minYear) {
        Event death = new Event(person.getAssociatedUsername());
        death.setType("Death");
        death.setYear(minYear + random.nextInt(MAX_AGE - ADULT_AGE));
        death.setPersonId(person.getId());

        Location location = LocationGenerator.getLocation();
        death.setLatitude(location.getLatitude());
        death.setLongitude(location.getLongitude());
        death.setCountry(location.getCountry());
        death.setCity(location.getCity());

        return death;
    }

    /**
     * Creates a baptism event.
     * 
     * @param person the person whose baptism this event represents
     * @param minYear the earliest year in which the baptism could have occurred
     * @param maxYear the latest year in which the baptism could have occurred
     * @return a baptism event
     */
    private static Event generateBaptism(Person person, int minYear, int maxYear) {
        Event baptism = new Event(person.getAssociatedUsername());
        baptism.setType("Baptism");
        baptism.setYear(minYear + random.nextInt(maxYear - minYear));
        baptism.setPersonId(person.getId());

        Location location = LocationGenerator.getLocation();
        baptism.setLatitude(location.getLatitude());
        baptism.setLongitude(location.getLongitude());
        baptism.setCountry(location.getCountry());
        baptism.setCity(location.getCity());

        return baptism;
    }

    /**
     * Creates a graduation event.
     * 
     * @param person the person whose graduation this event represents
     * @param birthYear the person's year of birth
     * @return a graduation event
     */
    private static Event generateGraduation(Person person, int birthYear) {
        Event graduation = new Event(person.getAssociatedUsername());
        graduation.setType("Graduation");
        graduation.setYear(birthYear + ADULT_AGE - random.nextInt(2));
        graduation.setPersonId(person.getId());

        Location location = LocationGenerator.getLocation();
        graduation.setLatitude(location.getLatitude());
        graduation.setLongitude(location.getLongitude());
        graduation.setCountry(location.getCountry());
        graduation.setCity(location.getCity());

        return graduation;
    }

}