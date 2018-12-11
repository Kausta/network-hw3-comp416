package group1.hw3.routing;

import java.util.Hashtable;

public class Message implements IMessage {
    private String senderID;
    private String receiverID;
    private Hashtable<String, Integer> distanceVectorEstimates;

    public Message(String senderID, String receiverID, Hashtable<String, Integer> distanceVectorEstimates) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.distanceVectorEstimates = distanceVectorEstimates;
    }

    @Override
    public String getSenderID() {
        return senderID;
    }

    @Override
    public String getReceiverID() {
        return receiverID;
    }

    @Override
    public Hashtable<String, Integer> getDistanceVectorEstimates() {
        return distanceVectorEstimates;
    }
}
