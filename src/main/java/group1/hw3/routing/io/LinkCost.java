package group1.hw3.routing.io;

public interface LinkCost {
    /**
     * Return whether the link cost is static or dynamic
     * @return whether the link cost is static
     */
    boolean isStatic();

    /**
     * Get the cost of the link
     * @return the current link cost value
     */
    Integer getCost();
}
