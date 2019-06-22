package com.jasoncarloscox.familymapserver.data.model;

import java.util.UUID;

/**
 * Represents an authorization token assigned to a user of the family map.
 */
public class AuthToken {

    private final String token;
    private final String username;

    /**
     * Creates a new AuthToken.
     * 
     * @param token the token used to prove a user's identity
     * @param username the username of the user to whom this token is assigned
     */
    public AuthToken(String token, String username) {
        this.token = token;
        this.username = username;
    }

    /**
     * Creates a new AuthToken with an auto-generated token value.
     * 
     * @param username the username of the user to whom this token is assigned
     */
    public AuthToken(String username) {
        this.token = UUID.randomUUID().toString();
        this.username = username;
    }

    /**
     * @return the token used to prove a user's identity
     */
    public String getToken() {
        return token;
    }

    /**
     * @return the username of the user to whom this token is assigned
     */
    public String getUsername() {
        return username;
    }

}
