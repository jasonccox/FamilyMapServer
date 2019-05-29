package familymapserver.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonParseException;

/**
 * Provides methods to generate random locations.
 */
public class LocationGenerator {

    private static final Logger LOG = Logger.getLogger("fms");

    private static ArrayList<Location> locations;

    private static Random random = new Random();

    static {
        try {
            loadLocations();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Could not load location file.", e);
            System.out.println(e.getMessage());
        }
    }

    /**
     * @return a random location
     */
    public static Location getLocation() {
        return locations.get(random.nextInt(locations.size()));
    }

    /**
     * Loads locations from files into an ArrayList
     * 
     * @throws IOException if an I/O error occurs
     */
    private static void loadLocations() throws IOException {

        final File LOCATION_FILE = new File("resources/locations.json");

        try (InputStream locationStream = new FileInputStream(LOCATION_FILE)) {
            Locations locs = 
                (Locations) ObjectEncoder.deserialize(locationStream, Locations.class);

            locations = locs.data;
        } catch (JsonParseException e) {
            LOG.log(Level.SEVERE, "Could not parse locations file.", e);
        }
    }

    /**
     * Stores a location.
     */
    public class Location {
    
        private float longitude;
        private float latitude;
        private String country;
        private String city;
    
        /**
         * @return the location's longitude
         */
        public float getLongitude() {
            return longitude;
        }
    
        /**
         * @return the location's latitude
         */
        public float getLatitude() {
            return latitude;
        }
    
        /**
         * @return the location's country
         */
        public String getCountry() {
            return country;
        }
    
        /**
         * @return the location's city
         */
        public String getCity() {
            return city;
        }
    
    }

    /**
     * Stores a list of locations - used to parse locations file.
     */
    private class Locations {
        private ArrayList<Location> data;
    }
}