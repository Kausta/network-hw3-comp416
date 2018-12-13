package group1.hw3.visualization;

import group1.hw3.util.logging.Logger;
import group1.hw3.util.logging.LoggerFactory;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class GraphStreamGraph implements ViewerListener {
    private final Logger logger = LoggerFactory.createLogger(getClass());
    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private SingleGraph graphImpl;
    private Viewer viewer = null;
    private boolean viewOpen = false;

    public GraphStreamGraph() {
        this.init();
    }

    private void init() {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        logger.i("Using advanced graph stream renderer");

        graphImpl = new SingleGraph("Distance Vector Graph");

        String filePath = Objects.requireNonNull(
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResource("stylesheet.css"))
                .toExternalForm();
        graphImpl.addAttribute("ui.stylesheet", "url(" + filePath + ")");
        graphImpl.addAttribute("ui.antialias", true);
        graphImpl.addAttribute("ui.quality", true);
    }

    public void addNode(String nodeName) {
        if (graphImpl.getNode(nodeName) != null) {
            return;
        }
        Node node = graphImpl.addNode(nodeName);
        node.addAttribute("ui.label", nodeName);
    }

    public void addEdge(String edgeFrom, String edgeTo, int initialCost) {
        if (graphImpl.getEdge(edgeFrom + "|" + edgeTo) != null || graphImpl.getEdge(edgeTo + "|" + edgeFrom) != null) {
            return;
        }
        Edge edge = graphImpl.addEdge(edgeFrom + "|" + edgeTo, edgeFrom, edgeTo);
        edge.addAttribute("ui.label", initialCost);
    }

    private Edge getEdge(String edgeFrom, String edgeTo) {
        Edge edge = graphImpl.getEdge(edgeFrom + "|" + edgeTo);
        if (edge == null) {
            edge = graphImpl.getEdge(edgeTo + "|" + edgeFrom);
        }
        if (edge == null) {
            throw new RuntimeException("Please add the edge first to retrieve it: " + edgeFrom + " -> " + edgeTo);
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

    public void removeAllMarks() {
        graphImpl.getEachEdge()
                .forEach(edge -> edge.removeAttribute("ui.class"));
    }

    public void display() {
        viewer = graphImpl.display();
        viewOpen = true;
        this.addListener();
    }

    private void addListener() {
        if (viewer == null) {
            return;
        }

        final ViewerPipe pipe = viewer.newViewerPipe();
        pipe.addViewerListener(this);
        pipe.addSink(graphImpl);
        executor.execute(() -> {
            while (viewOpen) {
                try {
                    pipe.blockingPump();
                } catch (InterruptedException ignored) {
                }
            }
        });
    }

    public void pause() {
        if (viewer == null) {
            return;
        }
        viewer.disableAutoLayout();
    }

    public void resume() {
        if (viewer == null) {
            return;
        }
        viewer.enableAutoLayout();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.support.removePropertyChangeListener(listener);
    }

    @Override
    public void viewClosed(String viewName) {
        viewOpen = false;
    }

    @Override
    public void buttonPushed(String id) {
        this.support.firePropertyChange("mouseClick", "", id);
    }

    @Override
    public void buttonReleased(String id) {
    }
}
