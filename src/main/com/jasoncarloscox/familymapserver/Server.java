package com.jasoncarloscox.familymapserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.jasoncarloscox.familymapserver.api.handler.ClearHandler;
import com.jasoncarloscox.familymapserver.api.handler.DefaultHandler;
import com.jasoncarloscox.familymapserver.api.handler.EventHandler;
import com.jasoncarloscox.familymapserver.api.handler.FillHandler;
import com.jasoncarloscox.familymapserver.api.handler.LoadHandler;
import com.jasoncarloscox.familymapserver.api.handler.LoginHandler;
import com.jasoncarloscox.familymapserver.api.handler.PersonHandler;
import com.jasoncarloscox.familymapserver.api.handler.RegisterHandler;
import com.jasoncarloscox.familymapserver.data.access.DBException;
import com.jasoncarloscox.familymapserver.data.access.Database;
import com.sun.net.httpserver.HttpServer;

/**
 * Main class of the server used to set up routes and listen.
 */
public class Server {

    // ---------------------------- STATIC MEMBERS ----------------------------

    private static final int MAX_WAITING_CONNECTIONS = 12;

    private static final Logger LOG = Logger.getLogger("fms");
    private static final Level LOG_LEVEL = Level.FINEST;

    static {
        try {
            initLog();
        } catch (IOException e) {
            System.err.println("Could not initialize log: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            printUsage();
            System.exit(1);
        }

        initDatabase();

        try {
            (new Server()).run(Integer.parseInt(args[0]));
        } catch (NumberFormatException e) {
            printUsage();
            System.exit(1);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Server failed.", e);
            System.exit(1);
        }
    }

    /**
     * Prints a usage statement to stdout.
     */
    private static void printUsage() {
        System.out.println("USAGE: java familymapserver.Server [portNumber]");
    }

    /**
     * Sets up the logger.
     * @throws IOException if an I/O error occurs
     */
    private static void initLog() throws IOException {
        LOG.setLevel(LOG_LEVEL);
        LOG.setUseParentHandlers(false);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(LOG_LEVEL);
        consoleHandler.setFormatter(new SimpleFormatter());
        LOG.addHandler(consoleHandler);

        FileHandler fileHandler = new FileHandler(getLogFilePath(), false);
        fileHandler.setLevel(LOG_LEVEL);
        fileHandler.setFormatter(new SimpleFormatter());
        LOG.addHandler(fileHandler);
    }

    /**
     * @return the path to the log file
     */
    private static String getLogFilePath() {
        StringBuilder sb = new StringBuilder("log/");
        sb.append("log_");
        sb.append(new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()));
        sb.append(".txt");
        return sb.toString();
    }

    /**
     * Creates the database (if needed) and adds necessary tables (if needed).
     */
    private static void initDatabase() {
        LOG.info("Initializing database...");

        try (Database db = new Database()) {
            db.init();
        } catch (DBException e) {
            LOG.log(Level.SEVERE, "Failed to initialize database.", e);
        }

        LOG.info("Finished initializing database.");
    }
    
    // -------------------------- NON-STATIC MEMBERS --------------------------

    private HttpServer server;

    /**
     * Initializes the server and starts it listening.
     * 
     * @param port the port number on which the server should listen
     */
    private void run(int port) {
        LOG.info("Initializing server...");

        try {
            server = HttpServer.create(new InetSocketAddress(port), MAX_WAITING_CONNECTIONS);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Could not create server.", e);
            return;
        }

        server.setExecutor(null);

        LOG.info("Finished initializing server.");

        LOG.info("Creating contexts...");

        server.createContext("/", new DefaultHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/fill", new FillHandler());

        LOG.info("Finished creating contexts.");

        LOG.info("Starting server...");

        server.start();

        LOG.info(String.format("Server now listening on port %d.\n", port));
    }
}