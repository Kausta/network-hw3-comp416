package group1.hw3.routing;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

public class Node implements INode<Message> {
    private String nodeID;
    private Map<String, Integer> linkCost = new HashMap<>();
    private Hashtable<String, Integer> distanceTable = new Hashtable<>();
    private Hashtable<String, String> forwardingTable = new Hashtable<>();
    private boolean shouldUpdate = true;

    /**
     * Node class containing node id and and each edge going from this node
     *
     * @param nodeID   Node id
     * @param linkCost Link costs, in the form of Key: To, Value: Distance
     */
    public Node(int nodeID, Map<Integer, Integer> linkCost) {
        this.nodeID = "" + nodeID;
        this.linkCost.putAll(linkCost.keySet()
                .stream()
                .collect(Collectors.toMap(key -> "" + key, linkCost::get)));
        this.distanceTable.putAll(this.linkCost);
        this.distanceTable.put(this.nodeID, 0);
        for (String key : distanceTable.keySet()) {
            this.forwardingTable.put(key, key);
        }
    }

    @Override
    public String getClientID() {
        return nodeID;
    }

    @Override
    public void receiveUpdate(Message message) {
        String senderID = message.getSenderID();
        Hashtable<String, Integer> senderDistanceTable = message.getDistanceVectorEstimates();
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

    private void updateDistanceTo(String target, int distance, String routeFrom) {
        distanceTable.put(target, distance);
        forwardingTable.put(target, routeFrom);
        shouldUpdate = true;
    }

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

    @Override
    public Hashtable<String, String> getForwardingTable() {
        return forwardingTable;
    }
}
