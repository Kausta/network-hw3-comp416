package group1.hw3.routing;

import group1.hw3.util.error.NotImplementedException;

import java.util.Hashtable;
import java.util.Map;

public class Node implements INode<Message> {
    private final String nodeID;

    /**
     * Node class containing node id and and each edge going from this node
     *
     * @param nodeID   Node id
     * @param linkCost Link costs, in the form of Key: To, Value: Distance
     */
    public Node(int nodeID, Map<Integer, Integer> linkCost) {
        this.nodeID = "" + nodeID;
    }

    @Override
    public String getClientID() {
        return nodeID;
    }

    @Override
    public void receiveUpdate(Message message) {
        throw new NotImplementedException();
    }

    @Override
    public boolean sendUpdate() {
        throw new NotImplementedException();
    }

    @Override
    public Hashtable<String, String> getForwardingTable() {
        throw new NotImplementedException();
    }
}
