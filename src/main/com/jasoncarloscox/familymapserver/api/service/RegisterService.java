package com.jasoncarloscox.familymapserver.api.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jasoncarloscox.familymapserver.api.request.FillRequest;
import com.jasoncarloscox.familymapserver.api.request.LoginRequest;
import com.jasoncarloscox.familymapserver.api.request.RegisterRequest;
import com.jasoncarloscox.familymapserver.api.result.FillResult;
import com.jasoncarloscox.familymapserver.api.result.LoginResult;
import com.jasoncarloscox.familymapserver.data.access.DBException;
import com.jasoncarloscox.familymapserver.data.access.Database;
import com.jasoncarloscox.familymapserver.data.access.PersonAccess;
import com.jasoncarloscox.familymapserver.data.access.UserAccess;
import com.jasoncarloscox.familymapserver.data.model.Person;
import com.jasoncarloscox.familymapserver.data.model.User;

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
        
        // insert user and user's person into database

        User newUser = request.getUser();
        Person userPerson = new Person(newUser);
        newUser.setPersonId(userPerson.getId());

        try (Database db = new Database()) {
            new UserAccess(db).add(newUser);
            new PersonAccess(db).add(userPerson);
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