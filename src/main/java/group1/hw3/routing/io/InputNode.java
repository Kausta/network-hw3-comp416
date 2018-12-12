package group1.hw3.routing.io;

import group1.hw3.util.Pair;

import java.util.List;

public class InputNode {
    private final int nodeId;
    private final List<Pair<Integer, LinkCost>> edges;

    public InputNode(int nodeId, List<Pair<Integer, LinkCost>> edges) {
        this.nodeId = nodeId;
        this.edges = edges;
    }

    public int getNodeId() {
        return nodeId;
    }

    public List<Pair<Integer, LinkCost>> getEdges() {
        return edges;
    }
}
