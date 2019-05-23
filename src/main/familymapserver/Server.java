package familymapserver;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import familymapserver.api.handler.DefaultHandler;

/**
 * Main class of the server used to set up routes and listen.
 */
public class Server {

    private static final int MAX_WAITING_CONNECTIONS = 12;

    private HttpServer server;

    /**
     * Initializes the server and starts it listening.
     * 
     * @param port the port number on which the server should listen
     */
    private void run(int port) {
        System.out.print("Initializing server...");

        try {
            server = HttpServer.create(new InetSocketAddress(port),
                                       MAX_WAITING_CONNECTIONS);
		} catch (IOException e) {
            System.err.println("ERROR: Could not create server.");
			e.printStackTrace(System.err);
			return;
		}

        server.setExecutor(null);

        System.out.println("done.");

        System.out.print("Creating contexts...");

        server.createContext("/", new DefaultHandler());

        System.out.println("done.");

        System.out.print("Starting server...");

        server.start();

        System.out.printf("now listening on port %d.\n", port);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            printUsage();
            System.exit(1);
        }

        try {
            (new Server()).run(Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
            printUsage();
            System.exit(1);
        }
    }

    private static void printUsage() {
        System.out.println("USAGE: java familymapserver.Server [portNumber]");
    }
}