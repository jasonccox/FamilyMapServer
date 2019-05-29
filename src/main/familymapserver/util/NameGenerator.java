package familymapserver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides methods to generate random names.
 */
public class NameGenerator {

    private static final Logger LOG = Logger.getLogger("fms");

    private static ArrayList<String> maleNames;
    private static ArrayList<String> femaleNames;
    private static ArrayList<String> surnames;

    private static Random random = new Random();

    static {
        try {
            loadNames();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Could not load name files.", e);
        }
    }

    /**
     * @return a random male name
     */
    public static String getMaleName() {
        return maleNames.get(random.nextInt(maleNames.size()));
    }

    /**
     * @return a random female name
     */
    public static String getFemaleName() {
        return femaleNames.get(random.nextInt(femaleNames.size()));
    }

    /**
     * @return a random surname
     */
    public static String getSurname() {
        return surnames.get(random.nextInt(surnames.size()));
    }

    /**
     * Loads names from files into ArrayLists
     * 
     * @throws IOException if an I/O error occurs
     */
    private static void loadNames() throws IOException {

        final File MALE_NAME_FILE = new File("resources/names_male.json");
        final File FEMALE_NAME_FILE = new File("resources/names_female.json");
        final File SURNAME_FILE = new File("resources/surnames.json");

        try (FileInputStream stream = new FileInputStream(MALE_NAME_FILE)) {
            maleNames = 
                ((Names) ObjectEncoder.deserialize(stream, Names.class)).data;
        }

        try (FileInputStream stream = new FileInputStream(FEMALE_NAME_FILE)) {
            femaleNames = 
                ((Names) ObjectEncoder.deserialize(stream, Names.class)).data;
        }

        try (FileInputStream stream = new FileInputStream(SURNAME_FILE)) {
            surnames = 
                ((Names) ObjectEncoder.deserialize(stream, Names.class)).data;
        }
    }

    /**
     * Stores a list of names - used to parse names files.
     */
    private class Names {
        private ArrayList<String> data;
    }

}