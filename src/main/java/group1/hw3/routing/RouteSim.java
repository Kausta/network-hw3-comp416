package group1.hw3.routing;

import group1.hw3.util.Pair;
import group1.hw3.util.logging.Logger;
import group1.hw3.util.logging.LoggerFactory;

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
        atLeastOneClientIsUpdated = true;
        round = 0;
        while (atLeastOneClientIsUpdated) {
            round += 1;
            logger.i("Running round " + round);
            doOneIteration();
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
     * Loads the initial distances and network topology from the input file
     * @param inputFilePath Input file  path
     */
    private void loadInitialDistances(Path inputFilePath) {
        clients = new HashMap<>();
        neighbors = new HashMap<>();
        Map<Integer, InputNode> inputNodeData = new InputParser().parseInputFile(inputFilePath);
        for (InputNode node : inputNodeData.values()) {
            int nodeId = node.getNodeId();
            Map<String, Integer> edges = node.getEdges().stream()
                    .collect(Collectors.toMap(pair -> "" + pair.getKey(), Pair::getValue));
            INode<Message> clientNode = new Node(nodeId, edges);
            clients.put(clientNode.getClientID(), clientNode);
            neighbors.put(clientNode.getClientID(), edges.keySet());
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
}
