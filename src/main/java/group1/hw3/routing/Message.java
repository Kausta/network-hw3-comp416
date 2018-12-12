package group1.hw3.routing;

import java.util.HashMap;

/**
 * Simple implementation of the {@link IMessage}
 */
public class Message implements IMessage {
    /**
     * Sender id of the message
     */
    private final String senderID;
    /**
     * Receiver id of the message
     */
    private final String receiverID;
    /**
     * New distance vector of the sender
     */
    private final HashMap<String, Integer> distanceVectorEstimates = new HashMap<>();

    /**
     * Constructor for the distance vector message
     *
     * @param senderID                Sender of the message
     * @param receiverID              Receiver of the message
     * @param distanceVectorEstimates Distance vector of the sender
     */
    public Message(String senderID, String receiverID, HashMap<String, Integer> distanceVectorEstimates) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.distanceVectorEstimates.putAll(distanceVectorEstimates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSenderID() {
        return senderID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReceiverID() {
        return receiverID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashMap<String, Integer> getDistanceVectorEstimates() {
        return distanceVectorEstimates;
    }
}
