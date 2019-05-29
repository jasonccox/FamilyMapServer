package familymapserver.api.handler;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import familymapserver.api.result.ApiResult;
import familymapserver.api.service.ClearService;

/**
 * Handler used to receive POST requests to clear all data from the database.
 */
public class ClearHandler extends ApiHandler {

    /**
     * Receives a POST request, clears the database of all data, and sends a 
     * response.
     * 
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected ApiResult handleRequest(HttpExchange exchange) throws IOException {
        return ClearService.clear();
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
    @Override
    protected boolean isValidURI(String uri) {
        return "/clear".equals(uri);
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