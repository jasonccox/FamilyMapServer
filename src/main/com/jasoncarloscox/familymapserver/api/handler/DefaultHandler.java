package com.jasoncarloscox.familymapserver.api.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;

import com.jasoncarloscox.familymapserver.api.result.ApiResult;

/**
 * Handler used to serve up a static web site for interacting with the server.
 */
public class DefaultHandler extends ApiHandler {
    
    /**
     * Receives a GET request and returns the desired file in the response
     * 
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O error occurs
     * @return the result of the request, or null if a response was already sent
     */
    @Override
    public ApiResult handleRequest(HttpExchange exchange) throws IOException {

        File requestedFile = getRequestedFile(exchange.getRequestURI().toString());

        sendResponse(HttpURLConnection.HTTP_OK, new FileInputStream(requestedFile), 
                     exchange);

        return null;
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
        return getRequestedFile(uri).exists();
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
     * Gets the file described by the request URI.
     * 
     * @param uri the request URI
     * @return the file described by the request URI
     */
    private File getRequestedFile(String uri) {
        final String WEB_FILES_DIR = "web";

        if ("/".equals(uri)) {
            uri = "/index.html";
        }

        String filePath = WEB_FILES_DIR + uri;

        return new File(filePath);
    }
}