package group1.hw3.visualization;

import group1.hw3.routing.INode;
import group1.hw3.routing.Message;
import group1.hw3.util.logging.Logger;
import group1.hw3.util.logging.LoggerFactory;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Objects;

public class ForwardingTableVisualizer implements PropertyChangeListener {
    private final Logger logger = LoggerFactory.createLogger(getClass());
    private final RouteSimVisualizer visualizer;
    private final GraphStreamGraph graph;
    private String node1 = null;
    private String node2 = null;

    public ForwardingTableVisualizer(RouteSimVisualizer visualizer, GraphStreamGraph graph) {
        this.visualizer = visualizer;
        this.graph = graph;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String nodeId = (String) evt.getNewValue();
        addValue(nodeId);
    }

    public void updateGraph() {
        if(node1 != null && node2 != null) {
            updateGraphImpl();
        }
    }

    private void addValue(String value) {
        if(node1 == null || node2 == null) {
            node1 = node2;
            node2 = value;
        } else {
            this.graph.removeAllMarks();

            node1 = null;
            node2 = value;
        }
        updateGraph();
    }

    private void updateGraphImpl() {
        this.graph.removeAllMarks();

        HashMap<Integer, INode<Message>> nodes = visualizer.getSimulator().getClients();
        INode<Message> from = nodes.get(Integer.parseInt(node1));
        INode<Message> to = nodes.get(Integer.parseInt(node2));
        while (from != null && !Objects.equals(from.getNodeId(), to.getNodeId())) {
            String nextNode = from.getForwardingTable().get("" + to.getNodeId());
            graph.markEdge("" + from.getNodeId(), nextNode);
            logger.d("Marking " + from.getNodeId() + " -> " + nextNode);
            from = nodes.get(Integer.parseInt(nextNode));
        }
    }
}
