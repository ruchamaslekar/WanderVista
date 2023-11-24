package server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

/** This class uses Jetty & servlets to implement server responding to Http GET requests */
public class JettyServer {
    // FILL IN CODE
    private static final int PORT = 8080;
    private final Server jettyServer;  // Jetty server
    private final ServletContextHandler handlers = new ServletContextHandler(ServletContextHandler.SESSIONS); //handler for the jetty server

    // FILL IN CODE: add other variables and methods as needed:
    // store a resource/resources you need to pass to the servlets
    // (such as ThreadSafeHotelData, but you want to store resources as objects to make the code more general).
    public JettyServer() {
        jettyServer = new Server(PORT);
        jettyServer.setHandler(handlers);
    }
    /**
     * Maps a given URL path/endpoint to the name of the servlet class that will handle requests coming at this endpoint
     * @param path String
     * @param className Class
     */
    public void addMapping(String path, Class className) {
        // FILL IN CODE: call the method on the handler that adds the mapping between the path and the servlet
         handlers.addServlet(className, path);
    }
    /**
     * Maps a given resourceName to the object of map
     * @param resourceName String
     * @param resource Object
     */
    // FILL IN CODE: add other methods as needed
    public void setResourceAttribute(String resourceName, Object resource) {
        handlers.setAttribute(resourceName, resource);
    }

    /**
     * Function that starts the server
     * @throws Exception throws exception if access failed
     */
    public  void start() throws Exception {
        // FILL IN CODE: run the jetty server
        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
