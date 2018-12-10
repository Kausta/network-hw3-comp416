package group1.hw3.routing.topology;

public class DynamicEdge<Key, Value> extends Edge<Key, Value> {
    private Value cost;

    public DynamicEdge(Key from, Key to, Value cost) {
        super(from, to);
        this.cost = cost;
    }

    @Override
    public Value getCost() {
        return null;
    }

    public void setCost(Value cost) {
        this.cost = cost;
    }
}
