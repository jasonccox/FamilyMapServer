package familymapserver.api.service;

import familymapserver.api.request.RegisterRequest;
import familymapserver.api.result.LoginResult;

/**
 * Contains methods providing functionality of the <code>/user/register</code> API route.
 * It registers a new user with the server.
 */
public class RegisterService {

    /**
     * Creates a new user, generates four generations of ancestor data for the user, 
     * logs the user in, and creates an authorization token.
     * 
     * @param request the register request, which specifies the user's information
     * @return the result of the operation
     */
    public static LoginResult register(RegisterRequest request) {
        return null;
    }

}