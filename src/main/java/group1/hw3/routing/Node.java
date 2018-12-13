package group1.hw3.routing;

import group1.hw3.util.logging.Logger;
import group1.hw3.util.logging.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for simulating a router node
 */
public class Node implements INode<Message> {
    private static final int INFINITY = 999;
    /**
     * Logger for the node
     */
    private final Logger logger = LoggerFactory.createLogger(getClass());
    /**
     * Unique identifier of the node
     */
    private final int nodeID;
    /**
     * Link cost of the node to the immediate neighbors
     */
    private final HashMap<Integer, Integer> linkCost = new HashMap<>();
    /**
     * Distance vector of the node
     */
    private final HashMap<Integer, HashMap<Integer, Integer>> distanceTable = new HashMap<>();
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
    public Node(int nodeID, Map<Integer, Integer> linkCost) {
        this.nodeID = nodeID;
        this.linkCost.putAll(linkCost);
        HashMap<Integer, Integer> currentDistanceVector = new HashMap<>();
        currentDistanceVector.put(nodeID, 0);
        for (Integer target : linkCost.keySet()) {
            currentDistanceVector.put(target, linkCost.get(target));
            HashMap<Integer, Integer> targetDistanceVector = new HashMap<>();
            targetDistanceVector.put(nodeID, INFINITY);
            for (Integer target2 : linkCost.keySet()) {
                targetDistanceVector.put(target2, INFINITY);
            }
            this.distanceTable.put(target, targetDistanceVector);
            this.forwardingTable.put("" + target, "" + target);
        }
        this.distanceTable.put(nodeID, currentDistanceVector);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNodeId() {
        return nodeID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receiveUpdate(Message message) {
        int senderID = message.getSenderID();
        HashMap<Integer, Integer> senderDistanceVector = message.getDistanceVectorEstimates();

        logger.i("Message sent by " + senderID + " received by " + getNodeId() + ": " + senderDistanceVector);

        this.distanceTable.put(senderID, senderDistanceVector);
        this.addMissingNodesFrom(senderID);
        this.recalculateDistanceTable();
        logger.i(distanceVectorToString());
        if (shouldUpdate) {
            logger.i("Distance vector updated");
        } else {
            logger.i("Distance vector not updated");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean sendUpdate() {
        if (shouldUpdate) {
            for (Integer neighbor : linkCost.keySet()) {
                Message message = new Message(nodeID, neighbor, distanceTable.get(nodeID));
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

    @Override
    public HashMap<Integer, Integer> getDistanceVector() {
        return distanceTable.get(nodeID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateLinkCostTo(Integer target, Integer newLinkCost) {
        if (!linkCost.containsKey(target)) {
            throw new RuntimeException("Cannot change the link cost to a non-neighbor node");
        }
        int prevCost = linkCost.get(target);
        if (prevCost == newLinkCost) {
            logger.w("Link cost didn't change, skipping");
        }
        linkCost.put(target, newLinkCost);
        recalculateDistanceTable();
        logger.i("Updated " + nodeID + " by link change: " + distanceVectorToString());
        logger.d("shouldUpdate="+shouldUpdate);
    }

    private void recalculateDistanceTable() {
        for (Integer target : distanceTable.get(nodeID).keySet()) {
            if(target == nodeID) {
                continue;
            }
            int currentCost = distanceTable.get(nodeID).get(target);
            String currentNode = forwardingTable.get("" + target);
            int newMin = INFINITY;
            int newMinNode = INFINITY;
            for (int neighbor : linkCost.keySet()) {
                if (neighbor == nodeID) {
                    continue;
                }
                int c = linkCost.get(neighbor);
                int D = distanceTable.get(neighbor).getOrDefault(target, INFINITY);
                if (c + D < newMin) {
                    newMin = c + D;
                    newMinNode = neighbor;
                }
            }
            if(newMin != currentCost) {
                distanceTable.get(nodeID).put(target, newMin);
                shouldUpdate = true;
            }
            String newForwardNode = "" + newMinNode;
            if(!newForwardNode.equals(currentNode))  {
                forwardingTable.put("" + target, newForwardNode);
            }
        }
    }

    private void addMissingNodesFrom(int senderID) {
        for(int target: distanceTable.get(senderID).keySet()) {
            for(int entry: distanceTable.keySet()) {
                if(target == entry) {
                    continue;
                }
                distanceTable.get(entry).putIfAbsent(target, INFINITY);
            }
        }
    }

    @Override
    public String toString() {
        return "Node " + this.nodeID + "\n" + distanceVectorToString() + "\n";
    }

    private String distanceVectorToString() {
        StringBuilder builder = new StringBuilder();
        for (Integer target : distanceTable.get(nodeID).keySet()) {
            builder.append("[")
                    .append(target)
                    .append(" -> ")
                    .append(distanceTable.get(nodeID).get(target))
                    .append("] ");
        }
        return builder.toString();
    }
}
