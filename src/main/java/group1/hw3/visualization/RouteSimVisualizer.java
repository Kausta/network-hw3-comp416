package group1.hw3.visualization;

import group1.hw3.routing.INode;
import group1.hw3.routing.Message;
import group1.hw3.routing.RouteSim;

import java.util.HashMap;

public class RouteSimVisualizer {
    private final RouteSim simulator;
    private final GraphStreamGraph graph;

    public RouteSimVisualizer(RouteSim routeSim) {
        this.simulator = routeSim;
        this.graph = new GraphStreamGraph();
        this.initializeGraph();
    }

    private void initializeGraph() {
        HashMap<String, INode<Message>> nodes = simulator.getClients();
        for (String node : nodes.keySet()) {
            graph.addNode(node);
            INode<Message> nodeImpl = nodes.get(node);
            HashMap<String, Integer> connections = nodeImpl.getDistanceVector();
            for (String target : connections.keySet()) {
                if (node.equals(target)) {
                    continue;
                }
                int cost = connections.get(target);
                graph.addNode(target);
                graph.addEdge(node, target, cost);
            }
        }
        graph.display();
    }

    public void preIterationCallback() {
        // graph.pause();
    }

    public void postIterationCallback() {
        // graph.resume();
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {

        }
    }

    public void performLinkCostChange(String from, String to, int newCost) {
        graph.updateEdge(from, to, newCost);
    }

    public void algorithmFinished() {
        HashMap<String, INode<Message>> nodes = simulator.getClients();
        for (String node : nodes.keySet()) {
            INode<Message> nodeImpl = nodes.get(node);
            HashMap<String, String> forwardingTable = nodeImpl.getForwardingTable();
            for (String target : forwardingTable.keySet()) {
                if (node.equals(target)) {
                    continue;
                }
                String forwardBy = forwardingTable.get(target);
                graph.markEdge(node, forwardBy);
            }
        }
    }
}
