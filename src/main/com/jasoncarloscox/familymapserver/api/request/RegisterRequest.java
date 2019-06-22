package com.jasoncarloscox.familymapserver.api.request;

import com.jasoncarloscox.familymapserver.data.model.User;

/**
 * A request to the <code>/user/register</code> route. It is a request to
 * register a new user with the server.
 */
public class RegisterRequest extends ApiRequest {

    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;

    /**
     * Creates a new RegisterRequest.
     * 
     * @param user the user to be added - note that the personId field will be 
     *             blank
     */
    public RegisterRequest(User user) {
        super();
        setUser(user);
    }

    /**
     * @return the new user - note that the personId field will be blank
     */
    public User getUser() {
        User u = new User(userName, password);
        u.setEmail(email);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        u.setGender(gender);
        
        return u;
    }

    /**
     * @param user the new user - note that the personId field will be blank
     */
    public void setUser(User user) {
        this.userName = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.gender = user.getGender();
    }

}