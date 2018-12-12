package group1.hw3.routing.io;

import java.security.SecureRandom;
import java.util.Random;

public class DynamicLink implements LinkCost {
    private static final Random random = new SecureRandom();
    private static final int MIN_COST = 1;
    private static final int MAX_COST = 10;
    private static final boolean COST_SHOULD_CHANGE = true;

    private int cost;

    public DynamicLink() {
        this.updateLinkCostRandomly();
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Override
    public Integer getCost() {
        return cost;
    }

    public boolean willLinkCostChange() {
        return random.nextBoolean();
    }

    public void updateLinkCostRandomly() {
        int newCost = generateNewCost();
        if(COST_SHOULD_CHANGE) {
            while (newCost == cost) {
                newCost = generateNewCost();
            }
        }
        this.cost = newCost;
    }

    private static int generateNewCost() {
        return random.nextInt(MAX_COST - MIN_COST + 1) + MIN_COST;
    }
}
