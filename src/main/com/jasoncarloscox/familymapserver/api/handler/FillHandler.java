package com.jasoncarloscox.familymapserver.api.handler;

import java.io.IOException;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;

import com.jasoncarloscox.familymapserver.api.request.FillRequest;
import com.jasoncarloscox.familymapserver.api.result.ApiResult;
import com.jasoncarloscox.familymapserver.api.service.FillService;

/**
 * Handler used to receive POST requests to generate data for a user.
 */
public class FillHandler extends ApiHandler {

    /**
     * Receives a POST request, generated data for the user, and sends a response.
     * 
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O error occurs
     * @return the result of the request, or null if a response was already sent
     */
    @Override
    protected ApiResult handleRequest(HttpExchange exchange) 
        throws IOException, JsonParseException {

        String reqURI = exchange.getRequestURI().toString();

        FillRequest request = new FillRequest(parseUsername(reqURI), parseNumGenerations(reqURI));

        return FillService.fill(request);
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
        String[] uriPieces = uri.split("/");

        if (uriPieces.length < 2 || uriPieces.length > 4) {
            return false;
        }

        if (!"fill".equals(uriPieces[1])) {
            return false;
        }

        if (uriPieces.length > 3 && !isNumber(uriPieces[3])) {
            return false;
        }

        return true;
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

    /**
     * @param uri the request URI
     * @return the username of the user for whom to generate data
     */
    private String parseUsername(String uri) {
        String[] uriPieces = uri.split("/");

        return uriPieces[2];
    }

    /**
     * @param uri the request URI
     * @return the number of generations of data to generate
     */
    private int parseNumGenerations(String uri) {

        final int DEFAULT_NUM_GENERATIONS = 4;

        String[] uriPieces = uri.split("/");

        if (uriPieces.length < 4) {
            return DEFAULT_NUM_GENERATIONS;
        }

        return Integer.parseInt(uriPieces[3]);
    }

    /**
     * @param s a String
     * @return whether s is a number
     */
    private boolean isNumber(String s) {
        return s.matches("-?\\d+");
    }

}
