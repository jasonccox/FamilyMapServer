package com.jasoncarloscox.familymapserver.util;

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

/**
 * Provides methods to serialize objects and deserialize strings/streams.
 */
public class ObjectEncoder {

    /**
     * Serializes an object into a String.
     * 
     * @param input the object to be serialized
     * @return a String representing the object
     */
    public static String serialize(Object input) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        return builder.create().toJson(input);
    }

    /**
     * Deserializes an input stream into an object.
     * 
     * @param input the input stream containing the data representing the object
     * @param targetClass the class of the object to be created
     * @return an object of class targetClass
     * @throws JsonParseException if input contains bad JSON
     */
    public static Object deserialize(InputStream input, Class<?> targetClass) 
        throws JsonParseException {
            
        GsonBuilder builder = new GsonBuilder();
        builder.disableHtmlEscaping();

        InputStreamReader reader = new InputStreamReader(input);

        return builder.create().fromJson(reader, targetClass);
    }

}