package familymapserver.api.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import familymapserver.api.request.FillRequest;
import familymapserver.api.request.LoginRequest;
import familymapserver.api.request.RegisterRequest;
import familymapserver.api.result.FillResult;
import familymapserver.api.result.LoginResult;
import familymapserver.data.access.DBException;
import familymapserver.data.access.Database;
import familymapserver.data.access.UserAccess;
import familymapserver.data.model.User;

/**
 * Contains methods providing functionality of the <code>/user/register</code> 
 * API route. It registers a new user with the server.
 */
public class RegisterService {

    private static final Logger LOG = Logger.getLogger("fms");

    /**
     * Creates a new user, generates four generations of ancestor data for the  
     * user, logs the user in, and creates an authorization token.
     * 
     * @param request the register request, which specifies the user's information
     * @return the result of the operation
     */
    public static LoginResult register(RegisterRequest request) {
        
        // insert user into database

        User newUser = request.getUser();
        
        // set the person id to something so that the user can be added to the
        // database -  this will be overridden by FillService
        newUser.setPersonId("filler");

        try (Database db = new Database()) {
            new UserAccess(db).add(newUser);
            db.commit();
        } catch (DBException e) {
            LOG.log(Level.WARNING, "Adding user failed.", e);
            
            return new LoginResult(LoginResult.INVALID_REQUEST_DATA_ERROR + 
                                   ": " + e.getMessage());
        }

        // generate four generations of data

        FillResult fillResult = 
            FillService.fill(new FillRequest(newUser.getUsername(), 4));

        if (!fillResult.isSuccess()) {
            return new LoginResult(fillResult.getMessage());
        }

        // log the user in and return the result

        return LoginService.login(new LoginRequest(newUser.getUsername(), 
                                                   newUser.getPassword()));
    }

}