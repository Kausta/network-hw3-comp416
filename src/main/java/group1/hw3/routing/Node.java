package group1.hw3.routing;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for simulating a router node
 */
public class Node implements INode<Message> {
    /**
     * Unique identifier of the node
     */
    private final String nodeID;
    /**
     * Link cost of the node to the immediate neighbors
     */
    private final HashMap<String, Integer> linkCost = new HashMap<>();
    /**
     * Distance vector of the node
     */
    private final HashMap<String, Integer> distanceTable = new HashMap<>();
    /**
     * Current forwarding table of the node
     */
    private final HashMap<String, String> forwardingTable = new HashMap<>();
    /**
     * Whether we should send an updates in the next iteration
     */
    private boolean shouldUpdate = true;

    /**
     * Node class containing node id and and each edge going from this node
     *
     * @param nodeID   Node id
     * @param linkCost Link costs, in the form of Key: To, Value: Distance
     */
    public Node(int nodeID, Map<String, Integer> linkCost) {
        this.nodeID = "" + nodeID;
        this.linkCost.putAll(linkCost);
        this.distanceTable.putAll(this.linkCost);
        this.distanceTable.put(this.nodeID, 0);
        for (String key : distanceTable.keySet()) {
            this.forwardingTable.put(key, key);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClientID() {
        return nodeID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receiveUpdate(Message message) {
        String senderID = message.getSenderID();
        HashMap<String, Integer> senderDistanceTable = message.getDistanceVectorEstimates();
        int distanceToSender = distanceTable.get(senderID);

        for (String target : senderDistanceTable.keySet()) {
            int newDistance = senderDistanceTable.get(target);
            if (!distanceTable.containsKey(target)) {
                updateDistanceTo(target, newDistance, senderID);
                continue;
            }
            int currDistance = distanceTable.get(target);
            if (newDistance + distanceToSender < currDistance) {
                updateDistanceTo(target, newDistance + distanceToSender, senderID);
            }
        }
    }

    /**
     * Updates distance to the given node using the new distance and a new routing target
     * <br />
     * Also sets the should update flag
     *
     * @param target    Target node we need to send a packet to
     * @param distance  New distance to the target node
     * @param routeFrom The node that we used to obtain the new smallest distance
     */
    private void updateDistanceTo(String target, int distance, String routeFrom) {
        distanceTable.put(target, distance);
        forwardingTable.put(target, routeFrom);
        shouldUpdate = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean sendUpdate() {
        if (shouldUpdate) {
            for (String neighbor : linkCost.keySet()) {
                Message message = new Message(nodeID, neighbor, distanceTable);
                MessageRouter.getInstance().routeMessage(message);
            }
            shouldUpdate = false;
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashMap<String, String> getForwardingTable() {
        return forwardingTable;
    }
}
