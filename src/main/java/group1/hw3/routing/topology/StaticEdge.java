package group1.hw3.routing.topology;

public class StaticEdge<Key, Value> extends Edge<Key, Value> {
    private final Value cost;

    public StaticEdge(Key from, Key to, Value cost) {
        super(from, to);
        this.cost = cost;
    }

    @Override
    public Value getCost() {
        return cost;
    }
}
