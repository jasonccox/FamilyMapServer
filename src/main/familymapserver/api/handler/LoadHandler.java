package familymapserver.api.handler;

import java.io.IOException;
import java.net.HttpURLConnection;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.HttpExchange;

import familymapserver.api.request.LoadRequest;
import familymapserver.api.result.LoadResult;
import familymapserver.api.service.LoadService;
import familymapserver.util.ObjectEncoder;

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
     */
    @Override
    protected void handleRequest(HttpExchange exchange) 
        throws IOException, JsonParseException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            sendResponse(HttpURLConnection.HTTP_BAD_REQUEST, exchange);
            return;
        }

        LoadRequest req = (LoadRequest) ObjectEncoder.deserialize(exchange.getRequestBody(), 
                                                                  LoadRequest.class);

        exchange.getRequestBody().close();

        LoadResult res = LoadService.load(req);

        String resBody = ObjectEncoder.serialize(res);

        if (res.isSuccess()) {
            sendResponse(HttpURLConnection.HTTP_OK, resBody, exchange);
        } else {
            sendResponse(HttpURLConnection.HTTP_BAD_REQUEST, resBody, exchange);
        }
    }

    
}