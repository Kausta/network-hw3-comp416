package group1.hw3.routing;

import group1.hw3.util.logging.Logger;
import group1.hw3.util.logging.LoggerFactory;

/**
 * Singleton helper for the nodes to send messages to their neighbors
 */
public class MessageRouter {
    /**
     * Singleton instance
     */
    private static volatile MessageRouter _instance = null;
    /**
     * Logger for the message router
     */
    private final Logger logger = LoggerFactory.createLogger(getClass());
    /**
     * Route simulator instance
     */
    private RouteSim routeSim;

    private MessageRouter() {

    }

    /**
     * Returns or instantiates the singleton message router instance
     * @return the message router instance
     */
    public static MessageRouter getInstance() {
        if(_instance == null) {
            synchronized (MessageRouter.class) {
                if(_instance == null) {
                    _instance = new MessageRouter();
                }
            }
        }
        return _instance;
    }

    /**
     * Initializes the message user with the given router simulator
     * @param routeSim Current router simulator
     */
    public void init(RouteSim routeSim) {
        this.routeSim = routeSim;
    }

    /**
     * Routes a message through the router simulator
     * @param message Distance vector message to route
     * @throws RuntimeException if called before init
     */
    public void routeMessage(Message message) {
        if(this.routeSim == null) {
            logger.e("Dont call route message before initialing");
            return;
        }
        this.routeSim.routeMessage(message);
    }
}
