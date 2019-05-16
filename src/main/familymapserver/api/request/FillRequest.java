package familymapserver.api.request;

/**
 * A request to the <code>/fill</code> route. It is a request to
 * generate person and event data for an existing user.
 */
public class FillRequest extends ApiRequest {

    private String username;
    private int generations;

    /**
     * Creates a new FillRequest.
     * 
     * @param username the username of the user for whom to generate the data
     * @param generations the number of generations of data to generate
     */
    public FillRequest(String username, int generations) {
        super();
        setUsername(username);
        setGenerations(generations);
    }

    /**
     * @return the username of the user for whom to generate the data
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username of the user for whom to generate the data
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the number of generations of data to generate
     */
    public int getGenerations() {
        return generations;
    }

    /**
     * @param generations the number of generations of data to generate
     */
    public void setGenerations(int generations) {
        this.generations = generations;
    }

}