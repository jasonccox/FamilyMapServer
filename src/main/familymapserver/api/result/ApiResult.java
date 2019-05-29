package familymapserver.api.result;

/**
 * A generic result of an API request.
 */
public class ApiResult {

    /**
     * The error message used when the provided authorization token is invalid.
     */
    public static final String INVALID_AUTH_TOKEN_ERROR = "Invalid auth token";

    /**
     * The error message used when required data is not included with the request 
     * or is invalid.
     */
    public static final String INVALID_REQUEST_DATA_ERROR = 
        "Request contains invalid/incomplete data";
    
    /**
     * The error message used when the server has an error.
     */
    public static final String INTERNAL_SERVER_ERROR = "Internal server error";

    /**
     * The error message used when no user has the provided username.
     */
    public static final String USER_NOT_FOUND = 
        "No user exists with that username";

    private String message;

    private transient boolean success;

    /**
     * Creates a new ApiResult.
     * 
     * @param message a message describing the result of the request
     * @param success whether the request was successfully fulfilled
     */
    public ApiResult(boolean success, String message) {
        setSuccess(success);
        setMessage(message);
    }

    /**
     * @return a message describing the result of the request
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message a message describing the result of the request
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return whether the request was successfully fulfilled
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success whether the request was successfully fulfilled
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
}