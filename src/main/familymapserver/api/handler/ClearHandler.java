package familymapserver.api.handler;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;

import familymapserver.api.result.ClearResult;
import familymapserver.api.service.ClearService;
import familymapserver.util.ObjectEncoder;

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
    protected void handleRequest(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendResponse(HttpURLConnection.HTTP_BAD_REQUEST, exchange);
            return;
        }

        ClearResult res = ClearService.clear();

        String resBody = ObjectEncoder.serialize(res);

        if (res.isSuccess()) {
            sendResponse(HttpURLConnection.HTTP_OK, resBody, exchange);
        } else {
            sendResponse(HttpURLConnection.HTTP_BAD_REQUEST, resBody, exchange);
        }
    }

    
}