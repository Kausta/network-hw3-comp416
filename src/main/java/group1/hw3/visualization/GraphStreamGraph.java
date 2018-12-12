package group1.hw3.visualization;

import group1.hw3.util.logging.Logger;
import group1.hw3.util.logging.LoggerFactory;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;

public class GraphStreamGraph {
    private final Logger logger = LoggerFactory.createLogger(getClass());
    private SingleGraph graphImpl;
    private Viewer viewer = null;

    public GraphStreamGraph() {
        this.init();
    }

    private void init() {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        logger.i("Using advanced graph stream renderer");

        graphImpl = new SingleGraph("Distance Vector Graph");

        String filePath = Thread.currentThread().getContextClassLoader().getResource("stylesheet.css").toExternalForm();
        graphImpl.addAttribute("ui.stylesheet", "url(" + filePath + ")");
        graphImpl.addAttribute("ui.antialias", true);
        graphImpl.addAttribute("ui.quality", true);
    }

    public void addNode(String nodeName) {
        if(graphImpl.getNode(nodeName) != null) {
            return;
        }
        Node node = graphImpl.addNode(nodeName);
        node.addAttribute("ui.label", nodeName);
    }

    public void addEdge(String edgeFrom, String edgeTo, int initialCost) {
        if(graphImpl.getEdge(edgeFrom + "|" + edgeTo) != null || graphImpl.getEdge(edgeTo + "|" + edgeFrom) != null) {
            return;
        }
        Edge edge = graphImpl.addEdge(edgeFrom + "|" + edgeTo, edgeFrom, edgeTo);
        edge.addAttribute("ui.label", initialCost);
    }

    private Edge getEdge(String edgeFrom, String edgeTo) {
        Edge edge = graphImpl.getEdge(edgeFrom + "|" + edgeTo);
        if(edge == null) {
            edge = graphImpl.getEdge(edgeTo + "|" + edgeFrom);
        }
        if(edge == null) {
            throw new RuntimeException("Please add the edge first to retrieve it");
        }
        return edge;
    }

    public void updateEdge(String edgeFrom, String edgeTo, int newCost) {
        Edge edge = getEdge(edgeFrom, edgeTo);
        edge.setAttribute("ui.label", newCost);
    }

    public void markEdge(String edgeFrom, String edgeTo) {
        Edge edge = getEdge(edgeFrom, edgeTo);
        edge.setAttribute("ui.class", "marked");
    }

    public void display() {
        viewer = graphImpl.display();
    }

    public void pause() {
        if(viewer == null) {
            return;
        }
        viewer.disableAutoLayout();
    }

    public void resume() {
        if(viewer == null) {
            return;
        }
        viewer.enableAutoLayout();
    }
}
