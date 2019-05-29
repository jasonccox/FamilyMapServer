package familymapserver.api.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonParseException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import familymapserver.api.result.ApiResult;
import familymapserver.api.result.EventResult;
import familymapserver.api.result.LoginResult;
import familymapserver.api.result.PersonResult;
import familymapserver.util.ObjectEncoder;

/**
 * A default handler for web API requests.
 */
public abstract class ApiHandler implements HttpHandler {

    private static final Logger LOG = Logger.getLogger("fms");

    private static final String NOT_FOUND_HTML_PATH = "web/HTML/404.html";

    protected static final String AUTH_HEADER = "Authorization";


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
        LOG.info(ApiHandler.requestToString(exchange));

        if (!isValidURI(exchange.getRequestURI().toString())) {
            sendResponse(HttpURLConnection.HTTP_NOT_FOUND, 
                         new FileInputStream(new File(NOT_FOUND_HTML_PATH)), 
                         exchange);
            return;
        }

        if (!isValidMethod(exchange.getRequestMethod().toUpperCase())) {
            sendResponse(HttpURLConnection.HTTP_BAD_REQUEST, exchange);
            return;
        }

        if (!hasRequiredAuthToken(exchange)) {
            ApiResult result = new ApiResult(false, ApiResult.INVALID_AUTH_TOKEN_ERROR);
            sendResponse(HttpURLConnection.HTTP_UNAUTHORIZED,
                         ObjectEncoder.serialize(result), exchange);
            return;
        }
        
        ApiResult result = null; 
        
        try {
            result = handleRequest(exchange);
        } catch (JsonParseException e) {
            result = new ApiResult(false, ApiResult.INVALID_REQUEST_DATA_ERROR+ 
                                          ": " + e.getMessage());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Failed to handle request.", e);
            result = new ApiResult(false, ApiResult.INTERNAL_SERVER_ERROR + 
                                          ": " + e.getMessage());
        }

        sendResponse(result, exchange);
    }   

    /**
     * Receives a request and returns a response
     * @param exchange the HttpExchange object for the request
     * @throws IOException if an I/O error occurs
     * @throws JsonParseException if the request body contains invalid JSON
     * @return the result of the request, or null if a response was already sent
     */
    protected abstract ApiResult handleRequest(HttpExchange exchange) 
        throws IOException, JsonParseException;

    /**
     * Determines if the request method is valid.
     * 
     * @param requestMethod the request method, in all caps
     * @return whether the request method is valid
     */
    protected abstract boolean isValidMethod(String requestMethod);

    /**
     * Determines if the request URI is valid.
     * 
     * @param uri the request URI
     * @return whether the URI is valid
     */
    protected abstract boolean isValidURI(String uri);

    /**
     * Determines if the request requires an auth token.
     * 
     * @return whether the request requires an auth token
     */
    protected abstract boolean requiresAuthToken();

    /**
     * Sends a response based on an ApiResult.
     * 
     * @param result the result of the request, or null if a response was
     *               already sent
     * @param exchange the HttpExchange object for this request
     * @throws IOException if an I/O error occurs
     */
    private void sendResponse(ApiResult result, HttpExchange exchange) 
        throws IOException {
            
        if (result == null) { // response already sent
            return;
        }
        
        String message = result.getMessage();

        int status = HttpURLConnection.HTTP_INTERNAL_ERROR;

        if (result.isSuccess()) {

            status = HttpURLConnection.HTTP_OK;

        } else if (message.startsWith(ApiResult.INVALID_REQUEST_DATA_ERROR) ||
                   message.startsWith(PersonResult.NOT_USERS_PERSON_ERROR) ||
                   message.startsWith(EventResult.NOT_USERS_EVENT_ERROR) ||
                   message.startsWith(LoginResult.USERNAME_TAKEN_ERROR)) {

            status = HttpURLConnection.HTTP_BAD_REQUEST;

        } else if (message.startsWith(ApiResult.INTERNAL_SERVER_ERROR)) {

            status = HttpURLConnection.HTTP_INTERNAL_ERROR;

        } else if (message.startsWith(ApiResult.INVALID_AUTH_TOKEN_ERROR) || 
                   message.startsWith(LoginResult.WRONG_PASSWORD_ERROR)) {
            
            status = HttpURLConnection.HTTP_UNAUTHORIZED;

        } else if (message.startsWith(PersonResult.PERSON_NOT_FOUND_ERROR) ||
                   message.startsWith(EventResult.EVENT_NOT_FOUND_ERROR) ||
                   message.startsWith(ApiResult.USER_NOT_FOUND)) {

            status = HttpURLConnection.HTTP_NOT_FOUND;

        }

        sendResponse(status, ObjectEncoder.serialize(result), exchange);
    }

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

    /**
     * Determines if the request is authorized.
     * 
     * @param exchange the HttpExchange object for the request
     * @return whether the request is authorized
     */
    protected boolean hasRequiredAuthToken(HttpExchange exchange) {
        return !requiresAuthToken() || getAuthToken(exchange) != null;
    }

    /**
     * Gets the auth token sent with the request.
     * 
     * @param exchange the HttpExchange object for the request
     * @return the auth token
     */
    protected String getAuthToken(HttpExchange exchange) {
        List<String> authHeaders = exchange.getRequestHeaders().get(AUTH_HEADER);

        if (authHeaders == null || authHeaders.isEmpty()) {
            return null;
        }

        return authHeaders.get(0);
    }
}