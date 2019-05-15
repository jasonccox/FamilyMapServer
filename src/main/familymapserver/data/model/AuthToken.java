package familymapserver.data.model;

/**
 * Represents an authorization token assigned to a user of the family map.
 */
public class AuthToken {

    private final String authToken;
    private final String userName;

    /**
     * Creates a new AuthToken.
     * 
     * @param token the token used to prove a user's identity when making requests
     * @param user the username of the user to whom this token is assigned
     */
    public AuthToken(String token, String userName) {
        this.authToken = token;
        this.userName = userName;
    }

    /**
     * @return the token used to prove a user's identity when making requests
     */
    public String getToken() {
        return authToken;
    }

    /**
     * @return the username of the user to whom this token is assigned
     */
    public String getUserName() {
        return userName;
    }

}
