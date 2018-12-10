package group1.hw3.routing;

import group1.hw3.routing.topology.Edge;
import group1.hw3.util.logging.Logger;
import group1.hw3.util.logging.LoggerFactory;

import java.nio.file.Path;
import java.util.Hashtable;
import java.util.List;

public class RouteSim {
    private Logger logger = LoggerFactory.createLogger(getClass());
    private Hashtable<String, INode<IMessage>> clients;
    private Hashtable<String, List<Edge<String, Integer>>> topology;
    private boolean atLeastOneClientIsUpdated = true;

    /**
     * Initializes the route simulator using the given input file
     * @param inputFilePath Input file path, expected to exist, may throw otherwise
     */
    public RouteSim(Path inputFilePath) {
        this.loadInitialDistances(inputFilePath);
        this.printInitialData();
    }

    public void run() {
        while (atLeastOneClientIsUpdated) {
            atLeastOneClientIsUpdated = false;
            doOneIteration();
        }
    }

    private void doOneIteration() {
        for(String clientId: clients.keySet()) {
            INode<IMessage> client = clients.get(clientId);
            if (!client.sendUpdate()) {
                continue;
            }

        }
    }

    private void loadInitialDistances(Path inputFilePath) {
        clients = new Hashtable<>();
        topology = new Hashtable<>();
        // TODO: Use inputFilePath to load clients
    }

    private void printInitialData() {
        for(String clientId: clients.keySet()) {
            INode<IMessage> client = clients.get(clientId);
            logger.i("Client " + clientId + " loaded.");
            Hashtable<String, String> forwardingTable = client.getForwardingTable();
            logger.d("Client's initial forwarding table: ");
            for(String destination: forwardingTable.keySet()) {
                String target = forwardingTable.get(destination);
                logger.d("Destination: " + destination + ", Forwarding Target: " + target);
            }
        }
    }
}