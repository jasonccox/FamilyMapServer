package familymapserver.api.result;

/**
 * The result of a request to the <code>/load</code> route. It describes the 
 * outcome of an attempt to load user-specified data into the database.
 */
public class LoadResult extends ApiResult {

    /**
     * The format (to be used with 
     * {@link java.lang.String#format(String, Object...) String.format()}) of
     * the message to be used if the request succeeds.
     */
    private static final String SUCCESS_MSG_FORMAT = 
        "Successfully added %d users, %d persons, and %d events to the database.";

    /**
     * Creates a new success LoadResult.
     * 
     * @param usersCreated the number of users created as a result of the load 
     *                     request
     * @param personsCreated the number of persons created as a result of the 
     *                       load request
     * @param eventsCreated the number of events created as a result of the load 
     *                      request
     */
    public LoadResult(int usersCreated, int personsCreated, int eventsCreated) {
        super(String.format(SUCCESS_MSG_FORMAT, usersCreated, personsCreated, 
                            eventsCreated));
    }

    /**
     * Creates a new error LoadResult.
     * 
     * @param error the error message
     */
    public LoadResult(String error) {
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