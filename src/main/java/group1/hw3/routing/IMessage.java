package group1.hw3.routing;

import java.util.Hashtable;

public interface IMessage {
    String getSenderID();

    String getReceiverID();

    Hashtable<String, Integer> getDistanceVectorEstimates();
}
