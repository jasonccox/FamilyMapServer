package familymapserver.api.service;

import familymapserver.api.request.LoginRequest;
import familymapserver.api.result.LoginResult;

/**
 * Contains methods providing functionality of the <code>/user/login</code> API
 * route. It logs a user into the server.
 */
public class LoginService {

    /**
     * Logs a user into the server and creates an authorization token.
     * 
     * @param request the login request, which specifies the user to login and 
     *                his/her password
     * @return the result of the operation
     */
    public static LoginResult login(LoginRequest request) {
        return null;
    }

}