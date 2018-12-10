package group1.hw3.routing;

import group1.hw3.util.error.NotImplementedException;

import java.util.Hashtable;

public class Message implements IMessage {
    @Override
    public String getSenderID() {
        throw new NotImplementedException();
    }

    @Override
    public String getReceiverID() {
        throw new NotImplementedException();
    }

    @Override
    public Hashtable<String, Integer> getDistanceVectorEstimates() {
        throw new NotImplementedException();
    }
}
