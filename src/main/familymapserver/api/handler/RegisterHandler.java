package familymapserver.api.handler;

import java.io.IOException;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;

import familymapserver.api.request.RegisterRequest;
import familymapserver.api.result.ApiResult;
import familymapserver.api.service.RegisterService;
import familymapserver.util.ObjectEncoder;

/**
 * Handler used to receive POST requests to register a new user.
 */
public class RegisterHandler extends ApiHandler {

    /**
     * Receives a POST request, creates a new user, generates data for the user,
     * logs the user in, and returns an auth token.
     * 
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O error occurs
     * @return the result of the request, or null if a response was already sent
     */
    @Override
    protected ApiResult handleRequest(HttpExchange exchange) 
        throws IOException, JsonParseException {

        RegisterRequest request = (RegisterRequest) 
            ObjectEncoder.deserialize(exchange.getRequestBody(), RegisterRequest.class);

        return RegisterService.register(request);
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
        return "/user/register".equals(uri);
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