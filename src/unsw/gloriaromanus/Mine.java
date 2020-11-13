package unsw.gloriaromanus;

public class Mine extends WealthGenerationBuilding {
    private double multiplier;

    public Mine() {
        multiplier = 0.99;
        setBaseCost(40);
        setBaseContructionTime(2);
        setName("Mine");
    }

    public void levelUp() {
        updateCosts();
        multiplier -= 0.01;
    }

    public double getMultiplier() {
        return multiplier;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        Mine m = (Mine) obj;
        return super.equals(obj) && multiplier == m.getMultiplier();
    }
}
