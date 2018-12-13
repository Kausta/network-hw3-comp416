package group1.hw3.routing;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Interface for a distance vector update message
 */
public interface IMessage {
    /**
     * Sender id of the message
     * @return the sender id of the message
     */
    int getSenderID();

    /**
     * Receiver id of the message
     * @return the receiver id of the message
     */
    int getReceiverID();

    /**
     * New distance vector of the sender
     * @return the new distance vector of the sender
     */
    HashMap<Integer, Integer> getDistanceVectorEstimates();
}
