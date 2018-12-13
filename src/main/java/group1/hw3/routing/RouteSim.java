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
    private HashMap<Integer, INode<Message>> clients;
    /**
     * Immediate neighbors of each node in the network
     */
    private HashMap<Integer, Set<Integer>> neighbors;
    /**
     * Dynamic cost links in the topology
     */
    private HashMap<Pair<Integer, Integer>, DynamicLink> dynamicLinks;
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
        round = 0;
        while (true) {
            round += 1;
            logger.i("Running round " + round);
            preIterationCallback.run();
            updateDynamicLinks(updateLinkCallback);
            boolean updated = doOneIteration();
            postIterationCallback.run();
            if(!updated) {
                break;
            }
        }
        logger.i("Distance Vector Routing Algorithm converged in " + round + " rounds.");
        for (INode<Message> node : clients.values()) {
            logger.i(node.toString());
        }
    }

    /**
     * Performs one iteration of the algorithm
     */
    private boolean doOneIteration() {
        boolean updated = false;
        for (int clientId : clients.keySet()) {
            INode<Message> client = clients.get(clientId);
            if (client.sendUpdate()) {
                updated = true;
                logger.d("Updated  " + clientId);
            } else {
                logger.d("Not updated " + clientId);
            }
        }
        return updated;
    }

    /**
     * Updates the dynamic links in the topology
     */
    private void updateDynamicLinks(UpdateLinkFunction updateLinkCallback) {
        for (Pair<Integer, Integer> dynamicEdge : dynamicLinks.keySet()) {
            DynamicLink link = dynamicLinks.get(dynamicEdge);
            if (link.willLinkCostChange()) {
                link.updateLinkCostRandomly();
                logger.d("Updating dynamic link between "
                        + dynamicEdge.getKey() + " - " + dynamicEdge.getValue()
                        + ", new cost is " + link.getCost());
                INode<Message> client1 = clients.get(dynamicEdge.getKey());
                INode<Message> client2 = clients.get(dynamicEdge.getValue());
                client1.updateLinkCostTo(client2.getNodeId(), link.getCost());
                client2.updateLinkCostTo(client1.getNodeId(), link.getCost());
                updateLinkCallback.run("" + client1.getNodeId(), "" + client2.getNodeId(), link.getCost());
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
            Map<Integer, Integer> edges = node.getEdges().stream()
                    .collect(Collectors.toMap(Pair::getKey, pair -> pair.getValue().getCost()));
            INode<Message> clientNode = new Node(nodeId, edges);
            clients.put(clientNode.getNodeId(), clientNode);
            neighbors.put(clientNode.getNodeId(), edges.keySet());
            for (Pair<Integer, LinkCost> edge : node.getEdges()) {
                LinkCost link = edge.getValue();
                if (!link.isStatic() && link instanceof DynamicLink) {
                    int clientId = clientNode.getNodeId();
                    int targetId = edge.getKey();
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
        for (int clientId : clients.keySet()) {
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
        int senderNodeID = message.getSenderID();
        int targetNodeID = message.getReceiverID();
        if (!neighbors.get(senderNodeID).contains(targetNodeID)) {
            throw new RuntimeException("Cannot send vector to non neighbor router");
        }
        INode<Message> targetNode = clients.get(targetNodeID);
        targetNode.receiveUpdate(message);
    }

    public HashMap<Integer, INode<Message>> getClients() {
        return clients;
    }

    public HashMap<Integer, Set<Integer>> getNeighbors() {
        return neighbors;
    }

    public HashMap<Pair<Integer, Integer>, DynamicLink> getDynamicLinks() {
        return dynamicLinks;
    }

    public int getRound() {
        return round;
    }
}
