package familymapserver.api.request;

import familymapserver.data.model.User;

/**
 * A request to the <code>/user/register</code> route. It is a request to
 * register a new user with the server.
 */
public class RegisterRequest extends ApiRequest {

    private User user;

    /**
     * Creates a new RegisterRequest.
     * 
     * @param user the user to be added - note that the personId field will be blank
     */
    public RegisterRequest(User user) {
        super();
        setUser(user);
    }

    /**
     * @return the new user - note that the personId field will be blank
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the new user - note that the personId field will be blank
     */
    public void setUser(User user) {
        this.user = user;
    }

}