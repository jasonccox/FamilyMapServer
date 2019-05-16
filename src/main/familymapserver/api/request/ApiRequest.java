package familymapserver.api.request;

/**
 * A generic API request.
 */
public class ApiRequest {

    private String authToken; 

    /**
     * Creates a new ApiRequest without an authorization token.
     */
    public ApiRequest() {}

    /**
     * Creates a new ApiRequest that includes an authorization token.
     * 
     * @param authToken the authorization token sent with the request
     */
    public ApiRequest(String authToken) {
        setAuthToken(authToken);
    }

    /**
     * @return the authorization token sent with the request
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * @param authToken the authorization token sent with the request
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}