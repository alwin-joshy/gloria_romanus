package unsw.gloriaromanus;

public class Mine extends WealthGenerationBuilding {
    private Faction f;
    private double multiplier;

    public Mine(Faction f) {
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
