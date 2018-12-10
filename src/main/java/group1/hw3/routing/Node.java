package group1.hw3.routing;

import group1.hw3.util.error.NotImplementedException;

import java.util.Hashtable;

public class Node implements INode<Message> {
    public Node(int nodeID, Hashtable<String, String> linkCost) {
        throw new NotImplementedException();
    }

    @Override
    public String getClientID() {
        throw new NotImplementedException();
    }

    @Override
    public void receiveUpdate(Message message) {
        throw new NotImplementedException();
    }

    @Override
    public boolean selfUpdate() {
        throw new NotImplementedException();
    }

    @Override
    public Hashtable<String, String> getForwardingTable() {
        throw new NotImplementedException();
    }
}
