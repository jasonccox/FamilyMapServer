package familymapserver.api.result;

/**
 * The result of a request to the <code>/fill</code> route. It describes the 
 * outcome of an attempt to add auto-generated data to the database.
 */
public class FillResult extends ApiResult {

    /**
     * The format (to be used with 
     * {@link java.lang.String#format(String, Object...) String.format()}) of 
     * the message to be used if the request succeeds.
     */
    private static final String SUCCESS_MSG_FORMAT = 
        "Successfully added %d persons and %d events to the database.";

    /**
     * Creates a new success FillResult.
     * 
     * @param personsCreated the number of persons created as a result of this 
     *                       fill request
     * @param eventsCreated the number of events created as a result of this 
     *                      fill request
     */
    public FillResult(int personsCreated, int eventsCreated) {
        super(String.format(SUCCESS_MSG_FORMAT, personsCreated, eventsCreated));
    }

    /**
     * Creates a new error FillResult.
     * 
     * @param error the error message
     */
    public FillResult(String error) {
        super(error);
    }

    /**
     * @return whether the request was successfully fulfilled
     */
    @Override
    public boolean isSuccess() {
        return false;
    }

}