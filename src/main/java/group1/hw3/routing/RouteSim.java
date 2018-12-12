package group1.hw3.routing;

import group1.hw3.routing.io.DynamicLink;
import group1.hw3.routing.io.InputNode;
import group1.hw3.routing.io.InputParser;
import group1.hw3.routing.io.LinkCost;
import group1.hw3.util.Pair;
import group1.hw3.util.logging.Logger;
import group1.hw3.util.logging.LoggerFactory;
import group1.hw3.visualization.UpdateLinkFunction;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Router simulator class
 */
public class RouteSim {
    /**
     * Logger for the router simulator
     */
    private final Logger logger = LoggerFactory.createLogger(getClass());
    /**
     * Nodes contained in the network
     */
    private HashMap<String, INode<Message>> clients;
    /**
     * Immediate neighbors of each node in the network
     */
    private HashMap<String, Set<String>> neighbors;
    /**
     * Dynamic cost links in the topology
     */
    private HashMap<Pair<String, String>, DynamicLink> dynamicLinks;
    /**
     * Whether we had updated a node in the previous iteration
     */
    private boolean atLeastOneClientIsUpdated = true;
    /**
     * How many iteration rounds we had
     */
    private int round = 0;

    /**
     * Initializes the route simulator using the given input file
     *
     * @param inputFilePath Input file path, expected to exist, may throw otherwise
     */
    public RouteSim(Path inputFilePath) {
        this.loadInitialDistances(inputFilePath);
        this.printInitialData();
    }

    /**
     * Runs the algorithm loop
     */
    public void run() {
        // Run without callbacks
        run(() -> {
        }, () -> {
        }, (from, to, edge) -> {
        });
    }

    public void run(Runnable preIterationCallback, Runnable postIterationCallback, UpdateLinkFunction updateLinkCallback) {
        atLeastOneClientIsUpdated = true;
        round = 0;
        while (atLeastOneClientIsUpdated) {
            round += 1;
            logger.i("Running round " + round);
            preIterationCallback.run();
            updateDynamicLinks(updateLinkCallback);
            doOneIteration();
            postIterationCallback.run();
        }
        logger.i("Distance Vector Routing Algorithm converged in " + round + " rounds.");
    }

    /**
     * Performs one iteration of the algorithm
     */
    private void doOneIteration() {
        atLeastOneClientIsUpdated = false;
        for (String clientId : clients.keySet()) {
            INode<Message> client = clients.get(clientId);
            if (client.sendUpdate()) {
                atLeastOneClientIsUpdated = true;
            }
        }
    }

    /**
     * Updates the dynamic links in the topology
     */
    private void updateDynamicLinks(UpdateLinkFunction updateLinkCallback) {
        for (Pair<String, String> dynamicEdge : dynamicLinks.keySet()) {
            DynamicLink link = dynamicLinks.get(dynamicEdge);
            if (link.willLinkCostChange()) {
                link.updateLinkCostRandomly();
                logger.d("Updating dynamic link between "
                        + dynamicEdge.getKey() + " - " + dynamicEdge.getValue()
                        + ", new cost is " + link.getCost());
                INode<Message> client1 = clients.get(dynamicEdge.getKey());
                INode<Message> client2 = clients.get(dynamicEdge.getValue());
                client1.updateLinkCostTo(client2.getClientID(), link.getCost());
                client2.updateLinkCostTo(client1.getClientID(), link.getCost());
                updateLinkCallback.run(client1.getClientID(), client2.getClientID(), link.getCost());
            }
        }
    }

    /**
     * Loads the initial distances and network topology from the input file
     *
     * @param inputFilePath Input file  path
     */
    private void loadInitialDistances(Path inputFilePath) {
        clients = new HashMap<>();
        neighbors = new HashMap<>();
        dynamicLinks = new HashMap<>();
        Map<Integer, InputNode> inputNodeData = new InputParser().parseInputFile(inputFilePath);
        for (InputNode node : inputNodeData.values()) {
            int nodeId = node.getNodeId();
            Map<String, Integer> edges = node.getEdges().stream()
                    .collect(Collectors.toMap(pair -> "" + pair.getKey(), pair -> pair.getValue().getCost()));
            INode<Message> clientNode = new Node(nodeId, edges);
            clients.put(clientNode.getClientID(), clientNode);
            neighbors.put(clientNode.getClientID(), edges.keySet());
            for (Pair<Integer, LinkCost> edge : node.getEdges()) {
                LinkCost link = edge.getValue();
                if (!link.isStatic() && link instanceof DynamicLink) {
                    String clientId = clientNode.getClientID();
                    String targetId = "" + edge.getKey();
                    if (dynamicLinks.containsKey(new Pair<>(targetId, clientId))) {
                        // Skip, since we already set the cost to random on the other direction and graph is symmetric
                        continue;
                    }
                    dynamicLinks.put(new Pair<>(clientId, targetId), (DynamicLink) link);
                }
            }
        }
    }

    /**
     * Prints the initial forwarding table
     */
    private void printInitialData() {
        for (String clientId : clients.keySet()) {
            INode<Message> client = clients.get(clientId);
            logger.i("Client " + clientId + " loaded.");
            HashMap<String, String> forwardingTable = client.getForwardingTable();
            logger.d("Client's initial forwarding table: ");
            for (String destination : forwardingTable.keySet()) {
                String target = forwardingTable.get(destination);
                logger.d("Destination: " + destination + ", Forwarding Target: " + target);
            }
        }
    }

    public void routeMessage(Message message) {
        String senderNodeID = message.getSenderID();
        String targetNodeID = message.getReceiverID();
        if (!neighbors.get(senderNodeID).contains(targetNodeID)) {
            throw new RuntimeException("Cannot send vector to non neighbor router");
        }
        INode<Message> targetNode = clients.get(targetNodeID);
        targetNode.receiveUpdate(message);
    }

    public HashMap<String, INode<Message>> getClients() {
        return clients;
    }

    public HashMap<String, Set<String>> getNeighbors() {
        return neighbors;
    }

    public HashMap<Pair<String, String>, DynamicLink> getDynamicLinks() {
        return dynamicLinks;
    }

    public int getRound() {
        return round;
    }
}
