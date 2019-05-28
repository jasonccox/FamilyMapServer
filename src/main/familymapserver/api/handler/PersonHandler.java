package familymapserver.api.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;

import familymapserver.api.request.ApiRequest;
import familymapserver.api.request.PersonRequest;
import familymapserver.api.result.ApiResult;
import familymapserver.api.service.PersonService;
import familymapserver.util.ObjectEncoder;

/**
 * Handler used to receive GET requests to get Person data from the database.
 */
public class PersonHandler extends ApiHandler {

    /**
     * Receives a GET request and sends back the desired person(s).
     * 
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            sendResponse(HttpURLConnection.HTTP_BAD_REQUEST, exchange);
            return;
        }

        if (!isValidURI(exchange.getRequestURI().toString())) {
            sendResponse(HttpURLConnection.HTTP_NOT_FOUND, exchange);
            return;
        }

        ApiResult res = getResult(exchange);

        String resBody = ObjectEncoder.serialize(res);

        if (res.isSuccess()) {
            sendResponse(HttpURLConnection.HTTP_OK, resBody, exchange);
        } else {

            if (ApiResult.INVALID_AUTH_TOKEN_ERROR.equals(res.getMessage())) {
                sendResponse(HttpURLConnection.HTTP_UNAUTHORIZED, resBody, exchange);
            } else {
                sendResponse(HttpURLConnection.HTTP_BAD_REQUEST, resBody, exchange);
            }
            
        }
    }

    private boolean isValidURI(String uri) {
        String[] uriPieces = uri.split("/");

        return uriPieces.length > 0 && 
               uriPieces.length <= 3 &&
               "person".equals(uriPieces[1]);
    }

    private ApiResult getResult(HttpExchange exchange) {
        List<String> authHeaders = exchange.getResponseHeaders().get(AUTH_HEADER);

        if (authHeaders == null || authHeaders.size() < 1) {
            return new ApiResult(false, ApiResult.INVALID_AUTH_TOKEN_ERROR);
        } 
        
        String reqURI = exchange.getRequestURI().toString();

        if ("/person".equals(reqURI)) {
            // get all persons
            ApiRequest req = new ApiRequest(authHeaders.get(0));
            return PersonService.getPersons(req);
        } 

        // get single person
        String personId = reqURI.substring(reqURI.lastIndexOf("/") + 1);
        return PersonService.getPerson(new PersonRequest(authHeaders.get(0),
                                                         personId));
    }
    
}