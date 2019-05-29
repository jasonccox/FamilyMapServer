package familymapserver.api.handler;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import familymapserver.api.request.ApiRequest;
import familymapserver.api.request.PersonRequest;
import familymapserver.api.result.ApiResult;
import familymapserver.api.service.PersonService;

/**
 * Handler used to receive GET requests to get Person data from the database.
 */
public class PersonHandler extends ApiHandler {

    /**
     * Receives a GET request and sends back the desired person(s).
     * 
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O error occurs
     * @return the result of the request, or null if a response was already sent
     */
    @Override
    protected ApiResult handleRequest(HttpExchange exchange) throws IOException {

        String reqURI = exchange.getRequestURI().toString();
        String authToken = getAuthToken(exchange);

        if (shouldFetchAllPersons(reqURI)) {
            // get all persons
            ApiRequest req = new ApiRequest(authToken);
            return PersonService.getPersons(req);
        } else {
            // get single person
            PersonRequest req = new PersonRequest(authToken,
                                                  parsePersonId(reqURI));
            return PersonService.getPerson(req);
        }
    }

    /**
     * Determines if the request method is valid.
     * 
     * @param requestMethod the request method, in all caps
     * @return whether the request method is valid
     */
    @Override
    protected boolean isValidMethod(String requestMethod) {
        return "GET".equals(requestMethod);
    }

    /**
     * Determines if the request URI is valid.
     * 
     * @param uri the request URI
     * @return whether the URI is valid
     */
    @Override
    protected boolean isValidURI(String uri) {
        String[] uriPieces = uri.split("/");

        return uriPieces.length > 1 && 
               uriPieces.length <= 3 &&
               "person".equals(uriPieces[1]);
    }

    /**
     * Determines if the request requires an auth token.
     * 
     * @return whether the request requires an auth token
     */
    @Override
    protected boolean requiresAuthToken() {
        return true;
    }

    /**
     * @param uri the request URI
     * @return whether to fetch all the user's persons
     */
    private static boolean shouldFetchAllPersons(String uri) {
        return "/person".equals(uri);
    }

    /**
     * @param uri the request URI
     * @return the id of the person to fetch
     */
    private static String parsePersonId(String uri) {
        return uri.substring(uri.lastIndexOf("/") + 1);
    }
    
}