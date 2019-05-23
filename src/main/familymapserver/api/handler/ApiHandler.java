package familymapserver.api.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * A default handler for web API requests.
 */
public abstract class ApiHandler implements HttpHandler {

    protected enum RequestMethod {
        GET, POST
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
            System.out.printf("\n%s %s\n", exchange.getRequestMethod(), 
                               exchange.getRequestURI());
            handleRequest(exchange);
        } catch (IOException e) {
            sendResponse(HttpURLConnection.HTTP_INTERNAL_ERROR, exchange);
        }
    }   

    /**
     * Receives a request and returns a response
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O error occurs
     */
    public abstract void handleRequest(HttpExchange exchange) throws IOException;

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

        exchange.sendResponseHeaders(status, 0);

        body.transferTo(exchange.getResponseBody());
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