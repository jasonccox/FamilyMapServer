package familymapserver.data.model;

/**
 * Represents an authorization token assigned to a user of the family map.
 */
public class AuthToken {

    private final String token;
    private final String username;

    /**
     * Creates a new AuthToken.
     * 
     * @param token the token used to prove a user's identity when making requests
     * @param username the username of the user to whom this token is assigned
     */
    public AuthToken(String token, String username) {
        this.token = token;
        this.username = username;
    }

    /**
     * @return the token used to prove a user's identity when making requests
     */
    public String getToken() {
        return token;
    }

    /**
     * @return the User to whom this token is assigned
     */
    public String getUser() {
        return username;
    }

}
