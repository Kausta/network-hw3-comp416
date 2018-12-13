package group1.hw3.visualization;

import group1.hw3.routing.INode;
import group1.hw3.routing.Message;
import group1.hw3.routing.RouteSim;

import java.util.HashMap;

public class RouteSimVisualizer {
    private final RouteSim simulator;
    private final GraphStreamGraph graph;
    private final ForwardingTableVisualizer forwardingTableVisualizer;

    public RouteSimVisualizer(RouteSim routeSim) {
        this.simulator = routeSim;
        this.graph = new GraphStreamGraph();
        this.forwardingTableVisualizer = new ForwardingTableVisualizer(this, graph);
        this.initializeGraph();
    }

    private void initializeGraph() {
        HashMap<Integer, INode<Message>> nodes = simulator.getClients();
        for (int node : nodes.keySet()) {
            graph.addNode("" + node);
            INode<Message> nodeImpl = nodes.get(node);
            HashMap<Integer, Integer> connections = nodeImpl.getDistanceVector();
            for (int target : connections.keySet()) {
                if (node == target) {
                    continue;
                }
                int cost = connections.get(target);
                graph.addNode("" + target);
                graph.addEdge("" + node, "" + target, cost);
            }
        }
        graph.display();
        graph.addPropertyChangeListener(forwardingTableVisualizer);
    }

    public void preIterationCallback() {
        // graph.pause();
    }

    public void postIterationCallback() {
        // graph.resume();
        try {
            forwardingTableVisualizer.updateGraph();
            Thread.sleep(1);
        } catch (InterruptedException ignored) {

        }
    }

    public void performLinkCostChange(String from, String to, int newCost) {
        graph.updateEdge(from, to, newCost);
    }

    public void algorithmFinished() {

    }

    public RouteSim getSimulator() {
        return simulator;
    }
}
