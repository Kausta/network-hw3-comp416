package group1.hw3.routing.topology;

import java.util.Objects;

public abstract class Edge<Key, Value> {
    private final Key from;
    private final Key to;

    public Edge(Key from, Key to) {
        this.from = from;
        this.to = to;
    }

    public Key getFrom() {
        return from;
    }

    public Key getTo() {
        return to;
    }

    public abstract Value getCost();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge<?, ?> edge = (Edge<?, ?>) o;
        return Objects.equals(from, edge.from) &&
                Objects.equals(to, edge.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
