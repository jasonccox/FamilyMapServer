package com.jasoncarloscox.familymapserver.api.handler;

import java.io.IOException;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;

import com.jasoncarloscox.familymapserver.api.request.ApiRequest;
import com.jasoncarloscox.familymapserver.api.request.EventRequest;
import com.jasoncarloscox.familymapserver.api.result.ApiResult;
import com.jasoncarloscox.familymapserver.api.service.EventService;

/**
 * Handler used to receive GET requests to get Event data from the database.
 */
public class EventHandler extends ApiHandler {

    /**
     * Receives a GET request and sends back the desired eveht(s).
     * 
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O error occurs
     * @return the result of the request, or null if a response was already sent
     */
    @Override
    protected ApiResult handleRequest(HttpExchange exchange) 
        throws IOException, JsonParseException {

        String reqURI = exchange.getRequestURI().toString();
        String authToken = getAuthToken(exchange);

        if (shouldFetchAllEvents(reqURI)) {
            // get all events
            ApiRequest req = new ApiRequest(authToken);
            return EventService.getEvents(req);
        } else {
            // get single person
            EventRequest req = new EventRequest(authToken,
                                                parseEventId(reqURI));
            return EventService.getEvent(req);
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
               "event".equals(uriPieces[1]);
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
     * @return whether to fetch all the user's events
     */
    private static boolean shouldFetchAllEvents(String uri) {
        return "/event".equals(uri);
    }

    /**
     * @param uri the request URI
     * @return the id of the event to fetch
     */
    private static String parseEventId(String uri) {
        return uri.substring(uri.lastIndexOf("/") + 1);
    }
    
}