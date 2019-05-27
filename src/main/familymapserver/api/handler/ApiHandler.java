package familymapserver.api.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import familymapserver.api.result.ApiResult;
import familymapserver.util.ObjectEncoder;

/**
 * A default handler for web API requests.
 */
public abstract class ApiHandler implements HttpHandler {

    private static final Logger LOG = Logger.getLogger("fms");

    /**
     * Creates a string representing an HTTP request
     * 
     * @param exchange the HttpExchange containing the request
     * @return a string representung the request
     */
    private static String requestToString(HttpExchange exchange) {
        StringBuilder sb = new StringBuilder();

        sb.append(exchange.getRequestMethod() + " ");
        sb.append(exchange.getRequestURI() + "\n");

        Headers headers = exchange.getRequestHeaders();
        for (String header : headers.keySet()) {
            sb.append("\t" + header + ": ");
            sb.append(headers.get(header).toString() + "\n");
        }

        return sb.toString();
    }

    /**
     * Receives a request and returns a response.
     * 
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            LOG.info(ApiHandler.requestToString(exchange));
            handleRequest(exchange);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Failed to handle request.", e);
            sendResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, exchange);
        } catch (JsonParseException e) {
            ApiResult result = new ApiResult(false, "Incorrectly formatted request body: " + 
                                                    e.getMessage());

            sendResponse(HttpURLConnection.HTTP_BAD_REQUEST, 
                         ObjectEncoder.serialize(result), exchange);
        }
    }   

    /**
     * Receives a request and returns a response
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O error occurs
     */
    public abstract void handleRequest(HttpExchange exchange) 
        throws IOException, JsonParseException;

    /**
     * Sends a response.
     * 
     * @param status the desired status code of the response
     * @param body the desired body of the response
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O error occurs
     */
    protected void sendResponse(int status, InputStream body, HttpExchange exchange) 
        throws IOException {

        LOG.info("Sending response: " + String.valueOf(status));
        
        exchange.sendResponseHeaders(status, 0);

        body.transferTo(exchange.getResponseBody());
        body.close();

        exchange.getResponseBody().close();
    }

    /**
     * Sends a response.
     * 
     * @param status the desired status code of the response
     * @param body the desired body of the response
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O error occurs
     */
    protected void sendResponse(int status, String body, HttpExchange exchange) 
        throws IOException {

        LOG.info("Sending response: " + String.valueOf(status));

        exchange.sendResponseHeaders(status, 0);

        writeToStream(body, exchange.getResponseBody());
        exchange.getResponseBody().close();
    }
    
    /**
     * Sends a response.
     * 
     * @param status the desired status code of the response
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O error occurs
     */
    protected void sendResponse(int status, HttpExchange exchange) 
        throws IOException {

        LOG.info("Sending response: " + String.valueOf(status));

        exchange.sendResponseHeaders(status, 0);
        exchange.getResponseBody().close();
    }

    /**
     * Writes a string to an OutputStream.
     * 
     * @param string the String to be written
     * @param stream the OutputStream to which to write
     * @throws IOException if an I/O error occurs
     */
    protected void writeToStream(String string, OutputStream stream) 
        throws IOException {
        
        OutputStreamWriter writer = new OutputStreamWriter(stream);
        writer.write(string);
        writer.flush();
    }
    
}