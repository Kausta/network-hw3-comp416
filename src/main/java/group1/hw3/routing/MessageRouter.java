package group1.hw3.routing;

import group1.hw3.util.logging.Logger;
import group1.hw3.util.logging.LoggerFactory;

public class MessageRouter {
    private static volatile MessageRouter _instace = null;
    private final Logger logger = LoggerFactory.createLogger(getClass());
    private RouteSim routeSim;

    private MessageRouter() {

    }

    public static MessageRouter getInstance() {
        if(_instace == null) {
            synchronized (MessageRouter.class) {
                if(_instace == null) {
                    _instace = new MessageRouter();
                }
            }
        }
        return _instace;
    }

    public void init(RouteSim routeSim) {
        this.routeSim = routeSim;
    }

    public void routeMessage(Message message) {
        if(this.routeSim == null) {
            logger.e("Dont call route message before initialing");
            return;
        }
        this.routeSim.routeMessage(message);
    }
}
