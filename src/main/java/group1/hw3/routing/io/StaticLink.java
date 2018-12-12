package group1.hw3.routing.io;

public class StaticLink implements LinkCost {
    private final int cost;

    public StaticLink(int cost) {
        this.cost = cost;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public Integer getCost() {
        return cost;
    }
}
