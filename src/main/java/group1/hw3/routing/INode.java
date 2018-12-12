package group1.hw3.routing;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Node's interface for the router simulator
 * @param <T> Message type implementing IMessage
 */
public interface INode<T extends IMessage> {
    /**
     * Get the unique client identifier for the node
     * @return Client identifier
     */
    String getClientID();

    /**
     * Update the node with the given message
     * @param message Distance vector of a neighbor node
     */
    void receiveUpdate(T message);

    /**
     * Sends updates to the neighbors if necessary
     * @return whether we sent an update
     */
    boolean sendUpdate();

    /**
     * Returns the forwarding table of the current node
     * @return the forwarding table of the current node
     */
    HashMap<String, String> getForwardingTable();

    HashMap<String, Integer> getDistanceVector();

    /**
     * Update the lint cost to the target node, for simulating dynamic links
     * @param target Target node, expected to be a neighbor
     * @param newLinkCost The new link cost
     */
    void updateLinkCostTo(String target, Integer newLinkCost);
}
