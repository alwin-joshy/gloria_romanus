package unsw.gloriaromanus;

public class Market extends WealthGenerationBuilding {
    private Faction f;
    private double multiplier;

    public Market (Faction f) {
        this.f = f;
        multiplier = 0.99;
        setBaseCost(40);
        setBaseContructionTime(2);
    }

    public void levelUp() {
        updateCosts();
        multiplier -= 0.01;
    }

    public double getMultiplier() {
        return multiplier;
    }

}
