package com.jasoncarloscox.familymapserver.api.handler;

import java.io.IOException;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;

import com.jasoncarloscox.familymapserver.api.request.LoadRequest;
import com.jasoncarloscox.familymapserver.api.result.ApiResult;
import com.jasoncarloscox.familymapserver.api.service.LoadService;
import com.jasoncarloscox.familymapserver.util.ObjectEncoder;

/**
 * Handler used to receive POST requests to load data into the database.
 */
public class LoadHandler extends ApiHandler {

    /**
     * Receives a POST request, populates the database with the provided data, 
     * and sends a response.
     * 
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O error occurs
     * @throws JsonParseException if the request body contains invalid JSON
     * @return the result of the request, or null if a response was already sent
     */
    @Override
    protected ApiResult handleRequest(HttpExchange exchange) 
        throws IOException, JsonParseException {

        LoadRequest req = (LoadRequest) ObjectEncoder.deserialize(exchange.getRequestBody(), 
                                                                  LoadRequest.class);

        exchange.getRequestBody().close();

        return LoadService.load(req);
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
        return "/load".equals(uri);
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