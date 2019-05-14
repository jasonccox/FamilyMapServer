package familymapserver.data.model;

/**
 * Represents an authorization token assigned to a user of the family map.
 */
public class AuthToken {

    private final String token;
    private final User user;

    /**
     * Creates a new AuthToken.
     * 
     * @param token the token used to prove a user's identity when making requests
     * @param user the User to whom this token is assigned
     */
    public AuthToken(String token, User user) {
        this.token = token;
        this.user = user;
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
    public User getUser() {
        return user;
    }

}
