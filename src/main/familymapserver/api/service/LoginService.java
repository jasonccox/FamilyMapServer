package familymapserver.api.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import familymapserver.api.request.LoginRequest;
import familymapserver.api.result.LoginResult;
import familymapserver.data.access.AuthTokenAccess;
import familymapserver.data.access.DBException;
import familymapserver.data.access.Database;
import familymapserver.data.access.UserAccess;
import familymapserver.data.model.AuthToken;
import familymapserver.data.model.User;

/**
 * Contains methods providing functionality of the <code>/user/login</code> API
 * route. It logs a user into the server.
 */
public class LoginService {

    private static final Logger LOG = Logger.getLogger("fms");

    /**
     * Logs a user into the server and creates an authorization token.
     * 
     * @param request the login request, which specifies the user to login and 
     *                his/her password
     * @return the result of the operation
     */
    public static LoginResult login(LoginRequest request) {

        // get the user from the database and make sure password matches

        User storedUser;
        try (Database db = new Database()) {
            storedUser = new UserAccess(db).get(request.getUserName());
        } catch (DBException e) {
            LOG.log(Level.WARNING, "Getting user failed.", e);
            
            return new LoginResult(LoginResult.INVALID_REQUEST_DATA_ERROR + 
                                   ": " + e.getMessage());
        }

        if (storedUser == null) {
            return new LoginResult(LoginResult.USER_NOT_FOUND);
        }

        if (!request.getPassword().equals(storedUser.getPassword())) {
            return new LoginResult(LoginResult.WRONG_PASSWORD_ERROR);
        }

        // create a new auth token
        AuthToken at = new AuthToken(request.getUserName());

        try (Database db = new Database()) {
            new AuthTokenAccess(db).add(at);
            db.commit();
        } catch (DBException e) {
            LOG.log(Level.WARNING, "Adding auth token failed.", e);
            
            return new LoginResult(LoginResult.INTERNAL_SERVER_ERROR + 
                                   ": " + e.getMessage());
        }

        return new LoginResult(at.getToken(), request.getUserName(), 
                               storedUser.getPersonId());
    }

}