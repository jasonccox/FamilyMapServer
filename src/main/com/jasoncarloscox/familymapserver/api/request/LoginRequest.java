package com.jasoncarloscox.familymapserver.api.request;

/**
 * A request to the <code>/user/login</code> route. It is a request to
 * log a user into the server.
 */
public class LoginRequest {

    private String userName;
    private String password;

    /**
     * Creates a new LoginRequest.
     * 
     * @param userName the user's username
     * @param password the user's password
     */
    public LoginRequest(String userName, String password) {
        super();
        setUserName(userName);
        setPassword(password);
    }

    /**
     * @return the user's username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the user's username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

}