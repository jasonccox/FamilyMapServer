package familymapserver.api.handler;

import java.io.IOException;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;

import familymapserver.api.request.LoginRequest;
import familymapserver.api.result.ApiResult;
import familymapserver.api.service.LoginService;
import familymapserver.util.ObjectEncoder;

/**
 * Handler used to receive POST requests to log a user in.
 */
public class LoginHandler extends ApiHandler {

    /**
     * Receives a POST request, validates the username/password combo, and sends
     * back an auth token.
     * 
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O error occurs
     * @return the result of the request, or null if a response was already sent
     */
    @Override
    protected ApiResult handleRequest(HttpExchange exchange) 
        throws IOException, JsonParseException {

        LoginRequest request = (LoginRequest) 
            ObjectEncoder.deserialize(exchange.getRequestBody(), LoginRequest.class);

        return LoginService.login(request);
    }

    /**
     * Determines if the request method is valid.
     * 
     * @param requestMethod the request method, in all caps
     * @return whether the request method is valid
     */
    @Override
    protected boolean isValidMethod(String requestMethod) {
        return "POST".equals(requestMethod);
    }

    /**
     * Determines if the request URI is valid.
     * 
     * @param uri the request URI
     * @return whether the URI is valid
     */
    protected boolean isValidURI(String uri) {
        return "/user/login".equals(uri);
    }

    /**
     * Determines if the request requires an auth token.
     * 
     * @return whether the request requires an auth token
     */
    @Override
    protected boolean requiresAuthToken() {
        return false;
    }
    
}