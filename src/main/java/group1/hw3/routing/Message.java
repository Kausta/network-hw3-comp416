package group1.hw3.routing;

import java.util.HashMap;

/**
 * Simple implementation of the {@link IMessage}
 */
public class Message implements IMessage {
    /**
     * Sender id of the message
     */
    private final int senderID;
    /**
     * Receiver id of the message
     */
    private final int receiverID;
    /**
     * New distance vector of the sender
     */
    private final HashMap<Integer, Integer> distanceVectorEstimates = new HashMap<>();

    /**
     * Constructor for the distance vector message
     *
     * @param senderID                Sender of the message
     * @param receiverID              Receiver of the message
     * @param distanceVectorEstimates Distance vector of the sender
     */
    public Message(int senderID, int receiverID, HashMap<Integer, Integer> distanceVectorEstimates) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.distanceVectorEstimates.putAll(distanceVectorEstimates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSenderID() {
        return senderID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getReceiverID() {
        return receiverID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashMap<Integer, Integer> getDistanceVectorEstimates() {
        return distanceVectorEstimates;
    }
}
