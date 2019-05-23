package familymapserver.api.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;

/**
 * Handler used to serve up a static web site for interacting with the server.
 */
public class DefaultHandler extends ApiHandler {

    private static final String NOT_FOUND_HTML = "web/HTML/404.html";
    
    /**
     * Receives a request and returns a response
     * 
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void handleRequest(HttpExchange exchange) throws IOException {
        
        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            sendResponse(HttpURLConnection.HTTP_BAD_REQUEST, exchange);
            return;
        }

        String filePath = "web" + exchange.getRequestURI();

        if (filePath.equals("web/")) {
            filePath += "index.html";
        }

        File file = new File(filePath);

        if (!file.exists()) {
            sendResponse(HttpURLConnection.HTTP_NOT_FOUND, 
                         new FileInputStream(new File(NOT_FOUND_HTML)), exchange);
            return;
        }

        sendResponse(HttpURLConnection.HTTP_OK, new FileInputStream(file), exchange);
    }

}