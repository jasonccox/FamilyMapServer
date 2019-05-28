package familymapserver.api.result;

/**
 * The result of a request to the <code>/clear</code> API route. It describes the 
 * outcome of an attempt to clear all data from the database.
 */
public class ClearResult extends ApiResult {

    /**
     * The message to be used if the clear request succeeds.
     */
    private static final String SUCCESS_MSG = "Clear succeeded.";

    /**
     * Creates a new error ClearResult.
     * 
     * @param error a description of the error
     */
    public ClearResult(String error) {
        super(false, error);
    }

    /**
     * Creates a new success ClearResult.
     */
    public ClearResult(){
        super(true, SUCCESS_MSG);
    }
    
}