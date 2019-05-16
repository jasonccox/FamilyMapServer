package familymapserver.api.result;

/**
 * The result of a request to the <code>/user/login</code> or <code>/user/register</code> 
 * route. It describes the outcome of an attempt to log a user into or register a new user 
 * with the server.
 */
public class LoginResult extends ApiResult {

    /**
     * The error message used when a username has already been taken by another user.
     */
    public static final String USERNAME_TAKEN_ERROR = "Username already taken by another user";

    private String authToken;
    private String userName;
    private String personID;

    /**
     * Cretaes a new error LoginResult.
     * 
     * @param error the error message
     */
    public LoginResult(String error) {
        super(error);
    }

    /**
     * Creates a new success LoginResult.
     * 
     * @param authToken the authorization token for the logged in user
     * @param userName the username of the logged in user
     * @param personID the id of the person representing the logged in user in the 
     * family map
     */
    public LoginResult(String authToken, String userName, String personID) {
        super(null);
        setAuthToken(authToken);
        setUserName(userName);
        setPersonID(personID);
    }

    /**
     * @return the authorization token for the logged in user
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * @param authToken the authorization token for the logged in user
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * @return the username of the logged in user
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the username of the logged in user
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the id of the person representing the logged in user in the family map
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * @param personID the id of the person representing the logged in user in the 
     * family map
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

     /**
     * @return whether the request was successfully fulfilled
     */
    @Override
    public boolean isSuccess() {
        return false;
    }

}