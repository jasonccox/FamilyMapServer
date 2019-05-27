package familymapserver.util;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

/**
 * Provides methods to serialize objects and deserialize strings/streams.
 */
public class ObjectEncoder {

    public static String serialize(Object input) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        return builder.create().toJson(input);
    }

    public static Object deserialize(InputStream input, Class<?> targetClass) 
        throws JsonParseException {
            
        GsonBuilder builder = new GsonBuilder();
        builder.disableHtmlEscaping();

        return builder.create().fromJson(new InputStreamReader(input), targetClass);
    }

}